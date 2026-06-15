## Why

目前專案沒有 REST API 規格文件，所有 endpoint 散落在 `eims.web.controller` 各 controller 中，前端、整合方、外部團隊都得直接讀原始碼才能知道介面定義。需要把既有 ~110 個 endpoint 系統性掃出並沉澱為可維護的 API 規格，作為日後變更管理與對接的單一事實來源。

## What Changes

- 新增 `docs/api/` 結構，依 controller 對應的功能領域產生 API 規格 markdown（每個領域一份）。
- 新增 `docs/api/openapi.yaml`，以 OpenAPI 3.0 描述全部既有 endpoint（method、path、path/query/body 參數型別、回應結構、認證需求）。
- 在規格中註記哪些 endpoint 被 `CommonConfiguration` 排除於 `RequestInterceptor`（`/login`、`/logout`、`/codes`、`/menus`、`/bxmlogin`、`/intrfccoms/deploy/terminal`、`/trxs`、`/trxs/**`、`/userstest/test`、`/message`）。
- 註記 context path `/eims` 與 server port `9100`。
- 不修改任何 controller / service / dao 行為——純文件化作業。

## Capabilities

### New Capabilities
- `rest-api-catalog`: 涵蓋所有 `eims.web.controller` 暴露之 REST endpoint 的對外規格與文件結構，包含路徑、方法、請求／回應 schema、認證模型、攔截器排除清單。

### Modified Capabilities
（無，現行無 spec 可改。）

## Impact

- 影響範圍：純新增文件，不動 production code。
- 產出檔案：`docs/api/openapi.yaml`、`docs/api/<domain>.md`（per controller 領域）。
- 對接系統：CAS SSO（`SSOFilterConfig`）、JNDI `java:/EIMSNXA` 雙資料來源（`EimsDatabaseConfig`、`MetaDatabaseConfig`）——僅作為背景說明，不在文件流程中變更。
- 後續：若要加 swagger-ui，需引入 springdoc/swagger jar 至 `lib/`（Java 8 相容版），屬另一個 change，本次不含。
