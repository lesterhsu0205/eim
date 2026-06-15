## Context

EIMS 為 Spring Boot 1.4.x WAR，封裝為 `ROOT.war`，context path `/eims`、port `9100`。後端 REST API 散落於 `eims.web.controller` 19 個 controller，共約 110 個 endpoint，命名慣例為 `XxxController` ↔ `XxxService` ↔ `XxxDao`。目前無任何 OpenAPI / swagger 註解，前端 AngularJS SPA 與外部對接方需直接讀 controller 原始碼。本 change 為純文件化作業，不引入新相依、不變動執行路徑。

主要限制：

- Java 8、`sourceCompatibility = 1.8`。
- 相依為 `lib/` 扁平 JAR，不走 Maven Central；任何要引入的工具必須能離線運作或產出靜態檔。
- Spring Boot 1.4.x 與 springdoc/springfox 新版本不相容，導入 swagger 註解風險高，本次不做。

## Goals / Non-Goals

**Goals:**

- 一次性掃描現有 controllers，產生完整 OpenAPI 3.0 規格 `docs/api/openapi.yaml`。
- 提供領域分檔人類可讀 markdown，便於前端／整合方查閱。
- 揭露 CAS SSO 認證與 `RequestInterceptor` 排除清單，避免對接者誤判認證行為。
- 文件可以手動維護，未來新增 endpoint 須同步更新 catalog（由 tasks 訂出檢查機制）。

**Non-Goals:**

- 不引入 springdoc、springfox、swagger-ui 等 runtime jar。
- 不重構 controller、不調整 DTO、不改 `CommonConfiguration` 排除清單。
- 不產出 client SDK（JS / Java）。
- 不涵蓋前端 AngularJS 靜態資源路徑（`/app/**`、`/assets/**`）。
- 不涵蓋 SPA 用 `HomeController` 之 wildcard 轉發（屬非 REST 行為）。

## Decisions

### D1：產出格式選擇 OpenAPI 3.0 + per-domain markdown

採用 `openapi.yaml` 作為機器可讀單一事實來源，並由人手維護 per-domain `.md` 作為閱讀視圖。原因：

- OpenAPI 為業界標準，未來若改用 swagger-ui、Stoplight、Redocly 等都可直接接。
- markdown 對前端與業務同仁友善，YAML 對工程與工具友善，兩者互補。
- per-domain 拆檔可降低 merge conflict 風險（19 領域分檔）。

**替代方案**：

- 只寫 markdown — 否決，缺乏機器可讀格式，未來自動化困難。
- 直接加 swagger 註解 — 否決，Spring Boot 1.4 + Java 8 + `lib/` 扁平相依環境風險高，且本次明確列為 non-goal。

### D2：手動掃描，不導入產生器

以 grep 掃 `@RequestMapping` / `@GetMapping` 等註解，逐筆人工填入 OpenAPI。原因：

- 一次性作業，掃描樣本固定（~110 endpoints）。
- 自動產生需要在 Java 8 / Spring Boot 1.4 環境執行 reflection-based scanner，整合成本高於人工輸入。
- DTO schema 透過 IDE 反查 controller 簽章與 `eims.web.dto` 套件補上即可。

### D3：路徑前綴處理

`application.properties` 已設 `server.context-path=/eims`。OpenAPI `servers[].url` 結尾為 `:9100/eims`，`paths` 區段使用 controller 內宣告之相對路徑（例如 `/roles`），不再重複前綴 `/eims`。

### D4：interceptor 排除以自訂 extension 標註

OpenAPI 無原生欄位描述 Spring MVC interceptor 排除清單。採用自訂 extension `x-interceptor-excluded: true` 標註在 operation level，並在 description 補充說明。原因：

- 機器可讀，便於未來自動驗證 catalog 與 `CommonConfiguration` 同步。
- `x-` 開頭符合 OpenAPI 規範，不破壞 validator。

### D5：DTO schema 命名以 Java class simple name

`components.schemas` key 直接用 `eims.web.dto` 中之 simple class name（例：`UserDto`、`RoleDto`）。不做命名空間扁平化以避免 `dto.ui` / `dto.table` 子套件衝突——若有重名，在 schema key 加上子套件後綴（例：`ui.UserPopupDto`）。

## Risks / Trade-offs

- **規格與程式碼漂移**：手動維護 OpenAPI，新增／修改 endpoint 後可能忘記更新文件。
  - 緩解：在 tasks 中加上 PR checklist 與一次性 CI 對照 script（grep controller mapping vs openapi paths 集合）。
- **DTO 結構深度過大**：部分 DTO（如 `dto.table.*`）巢狀深，全部展開會讓 yaml 過長。
  - 緩解：只展開 controller 直接簽章對到的 top-level DTO；深層欄位以 `$ref` 串接，無限展開停在第三層，其餘以 `description` 註明「詳見 `eims.web.dto.xxx`」。
- **Excel 匯出與檔案上傳 endpoint**：`/intrfccoms/excelexport`、`/msglayouts/fileuploads` 等回應為 binary／multipart。
  - 緩解：以 `application/octet-stream` 與 `multipart/form-data` 標註，並在 description 註明 content-type。
- **JNDI 模式差異不影響 API 表面**：`BxConstants.IS_SERVER` 切換僅影響資料來源綁定，不影響 REST 行為，不需在 catalog 揭露。

## Migration Plan

不涉及 runtime 變更，無 rollback 議題。文件可隨時刪除或重生。

## Open Questions

- 是否要在後續另開 change 導入 springdoc（需評估 Spring Boot 1.4 相容版本）？暫不在本次處理。
- `/intrfccoms/deploy/terminal` 是否回傳 streaming（SSE / chunked）？需開檔確認後在規格中註明。
- `/message` 出現在 `excludePathPatterns` 但未掃到對應 controller mapping，是否為靜態資源或預留？需與 owner 確認。
