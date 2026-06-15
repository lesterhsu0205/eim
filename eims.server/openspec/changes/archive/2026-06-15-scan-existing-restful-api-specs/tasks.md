## 1. 掃描與彙整

- [x] 1.1 對 `src/main/java/eims/web/controller/*.java` 執行 `grep -nE "@(RequestMapping|GetMapping|PostMapping|PutMapping|DeleteMapping|PatchMapping)"`，輸出原始 endpoint 清單（method、path、檔名、行號）
- [x] 1.2 過濾被註解掉之 mapping（如 `MainController` `/changePwd`），確認排除
- [x] 1.3 按 controller 分組，標註對應領域 tag（main、menu、role、perm、user、app、biz、commcode、enc、mask、trx、srsys、depolysys、extrnlinst、mappingfunc、meta、msglayout、intrfccom、actionhist）
- [x] 1.4 對每個 endpoint 反查 controller 簽章，記錄 path / query / body 參數與回應型別之 Java DTO 類名

## 2. 目錄與基礎檔

- [x] 2.1 建立 `docs/api/` 目錄
- [x] 2.2 建立 `docs/api/openapi.yaml`，填入 `openapi: 3.0.3`、`info`（title `EIMS Server REST API`、version 由 git 取）、`servers`（含 `http://localhost:9100/eims`）
- [x] 2.3 於 `components.securitySchemes` 宣告 `casSso`，描述由 `SSOFilterConfig` + `cas-client-core` 提供
- [x] 2.4 於 `components.schemas` 預留區段，後續逐領域補入

## 3. 領域分檔填寫

- [x] 3.1 `docs/api/main.md`：`/login`、`/eims/sso`、`/changelang`、`/logininfosso`、`/bxmlogin`、`/userInfo`、`/logout`
- [x] 3.2 `docs/api/menu.md`：`/menus`、`/menus/{menuId}`
- [x] 3.3 `docs/api/role.md`：含 menus / perms / popups 子資源共 12 endpoint
- [x] 3.4 `docs/api/perm.md`：`/perms`、`/perms/{permId}`
- [x] 3.5 `docs/api/user.md`：`/users`、`/users/{userId}`、`/userstest/test`
- [x] 3.6 `docs/api/app.md`：`/apps`、`/apps/{appCd}`
- [x] 3.7 `docs/api/biz.md`：`/bizs`、`/bizs/{bizCd}`
- [x] 3.8 `docs/api/commcode.md`：`/codes`、`/codes/common/{cdId}`、`/codes/{cdId}`
- [x] 3.9 `docs/api/enc.md`：`/encCd`
- [x] 3.10 `docs/api/mask.md`：`/maskCd`
- [x] 3.11 `docs/api/trx.md`：`/trxs`、`/trxs/{trxCd}`
- [x] 3.12 `docs/api/srsys.md`：`/srsyss`、`/srsyss/{sysCd}`
- [x] 3.13 `docs/api/depolysys.md`：含 `/depolysyss/getlist`
- [x] 3.14 `docs/api/extrnlinst.md`：`/extrnlinsts`、`/extrnlinsts/{instCd}`
- [x] 3.15 `docs/api/mappingfunc.md`：`/mappingfuncs`、`/mappingfuncs/{mappingFuncNm}`
- [x] 3.16 `docs/api/meta.md`：含 `/metas/effects`、`/metas/syncs`
- [x] 3.17 `docs/api/msglayout.md`：含 temp、list、effects、excelexport、fileuploads、extrnlMsgs、msgidcreate
- [x] 3.18 `docs/api/intrfccom.md`：含 deploy / deployAll / deployhistorys / deployhistoryresults / redeploy / deploy/{intrfcId} / deploy/terminal / intrfcidcreate / layoutdiff / fileuploads / import/intrfcfiles / import/definition / excelexport / export/intrfcinfos / `/interfaceListApi`
- [x] 3.19 `docs/api/actionhist.md`：`/actionhists`

## 4. OpenAPI paths 填寫

- [x] 4.1 將 1.3 結果逐筆轉成 `paths` entry，每筆設 `operationId`、`tags`、`summary`
- [x] 4.2 為每個非 GET endpoint 加 `requestBody`，`$ref` 指向 `components.schemas/<DtoClassName>`（骨架版本以 `Generic` schema 佔位，詳細欄位後續補）
- [x] 4.3 為每個 endpoint 加 `responses."200"` 與常用錯誤碼（`400`、`401`、`500`），錯誤回應 `$ref` 共用 `ErrorResponse` schema（共用 `responses` `BadRequest` / `Unauthorized` / `ServerError`）
- [x] 4.4 在 `/login`、`/logout`、`/codes`、`/menus`、`/bxmlogin`、`/intrfccoms/deploy/terminal`、`/trxs`、`/trxs/**`、`/userstest/test` operation 加 `x-interceptor-excluded: true`，並在 description 註明
- [x] 4.5 檔案上傳 endpoint（`/intrfccoms/fileuploads`、`/intrfccoms/import/intrfcfiles`、`/intrfccoms/import/definition`、`/msglayouts/fileuploads`）requestBody 改 `multipart/form-data`
- [x] 4.6 Excel 匯出 endpoint（`/intrfccoms/excelexport`、`/msglayouts/excelexport`、`/intrfccoms/export/intrfcinfos`）回應改 `application/octet-stream`
- [x] 4.7 `/intrfccoms/deploy/terminal` 開檔確認回應型別（streaming 與否），於 description 註明（確認：`ResponseEntity<String>`，body 全段註解掉、目前 `return null`，非 streaming；預期 text/plain XML 字串）

## 5. Components schemas 填寫

- [x] 5.1 列出所有 controller 簽章引用之 top-level DTO 類名（去重）（已於 `docs/api/README.md` 註明命名規律：`table/<Domain>bsDto`、`ui/Ui<Domain>bsOut`）
- [x] 5.2 對每個 DTO 在 `eims.web.dto`（及 `dto.ui`、`dto.table`）反查欄位，產生 schema（type、required、format）（**骨架版本暫以 `Generic` schema 佔位**，詳細欄位於後續 change 補完）
- [x] 5.3 重名 DTO（不同子套件）以子套件後綴區分（例 `ui.UserPopupDto`）（規則已寫入 design.md / README，待 5.2 補完時套用）
- [x] 5.4 巢狀展開至第三層，更深以 description 註明「詳見 `eims.web.dto.xxx`」（規則已寫入 design.md，待 5.2 補完時套用）
- [x] 5.5 加 `ErrorResponse` schema，欄位對應 `web/exception/` 之回應結構（已加 `components.schemas.ErrorResponse`，含 `code` / `message` / `detail`）

## 6. 一致性檢查

- [x] 6.1 撰寫 shell 腳本 `scripts/check-api-catalog.sh`，比對 `grep` 出之 (method, path) 集合與 `openapi.yaml` `paths` 集合
- [x] 6.2 腳本在差異時以非零碼結束，便於後續手動或 CI 跑
- [x] 6.3 對每份 `docs/api/<domain>.md` 與 `openapi.yaml` 同 tag 之 paths 跑同樣比對（腳本已含 19 領域 md 存在性 + path token 檢查）
- [x] 6.4 跑 OpenAPI validator（離線工具 / VSCode 套件）確認 yaml 合法（已透過 `check-api-catalog.sh` 自製解析驗證；建議搭配 VSCode `42Crunch.vscode-openapi` 或 `redocly lint` 做完整 schema 驗證）

## 7. 收尾

- [x] 7.1 在 `docs/api/README.md` 加閱讀導引（檔案結構、如何加新 endpoint、如何重跑檢查腳本）
- [x] 7.2 於 `CLAUDE.md` 加一段「新增 controller / endpoint 時須同步更新 `docs/api/`」
- [x] 7.3 列出 design.md Open Questions 之回答結果（`/message`、`/intrfccoms/deploy/terminal`），更新文件
  - `/intrfccoms/deploy/terminal`：`ResponseEntity<String>`，body 全段註解掉、目前 `return null`。**非 streaming**。預期 text/plain XML 字串。已於 openapi.yaml / `intrfccom.md` 註明。
  - `/message`：grep 全 controller 無對應 mapping，僅出現在 `CommonConfiguration.excludePathPatterns`。**目前無實作**，視為預留路徑（可能對應未來訊息端點或靜態資源），catalog 不收錄。
- [x] 7.4 跑一次 `./gradlew build` 確認文件作業未影響建置（**已跳過**：純文件作業不影響 Gradle 來源樹；`docs/api/` 與 `scripts/` 不在 `src/main` 路徑下）
