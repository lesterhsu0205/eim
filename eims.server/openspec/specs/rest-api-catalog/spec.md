# rest-api-catalog

## Purpose

涵蓋所有 `eims.web.controller` 暴露之 REST endpoint 的對外規格與文件結構，包含路徑、方法、請求／回應 schema、認證模型、攔截器排除清單。

## Requirements

### Requirement: API Catalog 涵蓋率

系統 SHALL 在 `docs/api/` 下提供文件，涵蓋 `eims.web.controller` 套件中**每一個**以 `@RequestMapping`、`@GetMapping`、`@PostMapping`、`@PutMapping`、`@DeleteMapping`、`@PatchMapping` 註解之 endpoint。

#### Scenario: 全 controller 掃描完整
- **WHEN** 對 `src/main/java/eims/web/controller/*.java` 執行 mapping 註解掃描
- **THEN** `docs/api/openapi.yaml` 之 `paths` 區段對每筆掃描結果皆有對應條目，缺漏為 0。

#### Scenario: 不含註解掉的 endpoint
- **WHEN** controller 中存在 `// @RequestMapping(...)` 等被註解掉的 mapping（例如 `MainController` 之 `/changePwd`）
- **THEN** 該 endpoint MUST NOT 出現在 catalog 中。

### Requirement: OpenAPI 3.0 規格檔

系統 SHALL 提供 `docs/api/openapi.yaml`，符合 OpenAPI 3.0.x 規範，至少包含 `info`、`servers`、`paths`、`components.schemas`、`components.securitySchemes` 區段。

#### Scenario: server 條目正確
- **WHEN** 讀取 `openapi.yaml` `servers` 區段
- **THEN** 至少有一筆 `url` 結尾為 `:9100/eims`（對應 `application.properties` `server.port=9100` 與 `server.context-path=/eims`）。

#### Scenario: 每個 path 標註 HTTP method 與 operationId
- **WHEN** 檢視任一 path entry
- **THEN** 每個 method 皆有 `operationId`（kebab 或 camelCase 皆可，全域唯一），且 `tags` 對應其 controller 領域名（例如 `Role`、`Intrfccom`、`Msglayout`）。

### Requirement: 認證與攔截器排除清單揭露

規格 SHALL 揭露 CAS SSO 認證模型，並以可機器讀取方式列出被 `CommonConfiguration.webMvcConfigurerAdapter()` 排除於 `RequestInterceptor` 的 endpoint。

#### Scenario: securityScheme 宣告
- **WHEN** 檢視 `components.securitySchemes`
- **THEN** 至少有一個 scheme 描述 CAS SSO（例如 `casSso`，type `apiKey` 或自訂說明），文字 SHALL 註明由 `SSOFilterConfig` 與 `cas-client-core` 提供。

#### Scenario: interceptor 排除標註
- **WHEN** 檢視 `/login`、`/logout`、`/codes`、`/menus`、`/bxmlogin`、`/intrfccoms/deploy/terminal`、`/trxs`、`/trxs/**`、`/userstest/test` 對應之 path operation
- **THEN** 其 description 或自訂 `x-interceptor-excluded: true` SHALL 標示為排除於 `RequestInterceptor`。

### Requirement: 領域分檔文件

系統 SHALL 為下列每一個 controller 領域提供一份 `docs/api/<domain>.md` 文件：`main`、`menu`、`role`、`perm`、`user`、`app`、`biz`、`commcode`、`enc`、`mask`、`trx`、`srsys`、`depolysys`、`extrnlinst`、`mappingfunc`、`meta`、`msglayout`、`intrfccom`、`actionhist`。

#### Scenario: 領域文件存在且非空
- **WHEN** 列出 `docs/api/` 目錄
- **THEN** 上述 19 份 `.md` 檔皆存在，且每份至少含一個 endpoint 區段（method + path + 摘要）。

#### Scenario: 領域文件與 OpenAPI 一致
- **WHEN** 比對任一領域 md 與 `openapi.yaml` 同 tag 之 paths
- **THEN** 兩邊列出的 (method, path) 集合 MUST 相等。

### Requirement: Request / Response Schema 揭露

每個非 GET 之 endpoint 規格 SHALL 揭露其 request body 之 DTO 類別名（取自 controller 簽章），以及回應主體之 DTO 類別名。

#### Scenario: POST/PUT body schema 對應 DTO
- **WHEN** 檢視任一 `POST` / `PUT` operation 之 `requestBody.content."application/json".schema`
- **THEN** schema `$ref` 指向 `components.schemas/<DtoClassName>`，且 `<DtoClassName>` 對應實際 `eims.web.dto` 套件中之類別。

#### Scenario: 未知 property 政策
- **WHEN** 規格描述全域反序列化行為
- **THEN** 規格 SHALL 註明 `CustomObjectMapper` 設定 `FAIL_ON_UNKNOWN_PROPERTIES = true`，呼叫端必須只送 DTO 已宣告之欄位。
