# EIMS REST API 文件

## 檔案結構

```
docs/api/
├── README.md            ← 本文件
├── openapi.yaml         ← OpenAPI 3.0 機器可讀規格（單一事實來源）
├── main.md              ← Main (login/logout/SSO/lang/userInfo)
├── menu.md
├── role.md
├── perm.md
├── user.md
├── app.md
├── biz.md
├── commcode.md
├── enc.md
├── mask.md
├── trx.md
├── srsys.md
├── depolysys.md
├── extrnlinst.md
├── mappingfunc.md
├── meta.md
├── msglayout.md
├── intrfccom.md
└── actionhist.md
```

## 設計原則

- `openapi.yaml` 為機器可讀單一事實來源（OpenAPI 3.0.3）。
- 每份 `<domain>.md` 對應一個 controller 領域，提供人類友善之 endpoint 速查表。
- 排除於 `RequestInterceptor`（見 `eims.config.CommonConfiguration.excludePathPatterns`）之 endpoint 於 OpenAPI operation 標 `x-interceptor-excluded: true`，於 markdown 標「⛔ excluded」。
- 認證走 CAS SSO（`eims.config.SSOFilterConfig` + `cas-client-core`，session 以 `JSESSIONID` 維持）。
- 反序列化：`CustomObjectMapper` 啟用 `FAIL_ON_UNKNOWN_PROPERTIES = true`，呼叫端只能送 DTO 已宣告之欄位。

## Schema 詳細度（目前狀態）

- 三批 schema 補完計畫（`flesh-out-schemas-main-auth` / `-data` / `-intrfc`）已完成：所有領域 DTO 皆以實際 `eims.web.dto.*` 欄位逐一展開於 `components.schemas`。
- `Generic` 佔位 schema（`additionalProperties: true`）已退場，僅保留給 `/userstest/test` 測試端點，以及少數無對應 DTO 之 Main 流程端點（`/eims/sso`、`/changelang`、`/bxmlogin`，回應為轉址 / 簡單字串）。
- `IntrfccombsDetail` 之 CC/EAI/FEP/MCI 子型別以 `oneOf` 表達；因 DTO 無判別欄位（`IntrfccombsDetail` 為空 marker interface），不附 `discriminator`。
- 詳細欄位仍可反查 `eims.web.dto.table.<XxxDto>`（request）與 `eims.web.dto.ui.<UiXxxOut>`（response）。

## 新增 / 修改 endpoint 流程

1. 於 controller 新增或修改 mapping。
2. 同步更新 `docs/api/openapi.yaml`：
   - 在 `paths` 區段加 / 改對應條目。
   - 若 endpoint 需排除於 `RequestInterceptor`，於 operation 加 `x-interceptor-excluded: true`，並同步 `eims.config.CommonConfiguration.excludePathPatterns`。
   - 檔案上傳改 `multipart/form-data`、二進位下載改 `application/octet-stream`。
3. 同步更新對應之 `docs/api/<domain>.md` 表格。
4. 跑檢查腳本確認 catalog 一致：

   ```bash
   ./scripts/check-api-catalog.sh
   ```

5. （建議）以離線 OpenAPI validator（VSCode `42Crunch.vscode-openapi`、`redocly lint` 等）驗證 yaml 合法。

## 檢查腳本

`scripts/check-api-catalog.sh` 萃取兩份 (METHOD, path) 集合並比對：

- controller 端：`grep` `@RequestMapping` 系列註解，過濾被註解掉之行。
- `openapi.yaml` 端：解析 `paths:` 區段。
- 差異時以非零碼結束。

腳本同時粗略檢查 19 份領域 md 是否存在且含 path token。
