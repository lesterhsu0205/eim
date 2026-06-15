## Why

`scan-existing-restful-api-specs` 完成後，`docs/api/openapi.yaml` 之 `components.schemas` 只放了 `Generic` 與 `ErrorResponse` 兩個佔位 schema，所有 endpoint 之 request body 與 response 都 `$ref` 到 `Generic`，呼叫端與整合方仍無法從規格知道實際欄位、型別、必填性。需要把 DTO 反查為 OpenAPI schema。為避免單一 change 過大，依領域拆三批處理；本 change 處理「認證 + ACL」批次：Main/Menu/Role/Perm/User/App/Biz。

## What Changes

- 於 `docs/api/openapi.yaml` `components.schemas` 加入下列 schema（規則：3 層嵌套；超過以 description 指向 Java class）：
  - **dto（共用）**：`LoginUserInfo`、`SessionInfo`、`UserInfo`、`CommonResponse`、`AdminLayoutInfo`
  - **dto.table**：`MenuDto`、`RoleDto`、`PermDto`、`UserDto`、`AppcdDto`、`BizcdDto`、`MenuRoleRelDto`、`RolePermRelDto`、`ChangePwdInfo`
  - **dto.ui**：`UiMenuOut`、`UiMenuTreeInfo`、`UiRoleOut`、`UiPermOut`、`UiUserOut`、`UiAppcdOut`、`UiBizcdOut`
- 將下列 tag 之 path operation 中 `$ref: "#/components/schemas/Generic"` 改為對應 schema：
  `Main`、`Menu`、`Role`、`Perm`、`User`、`App`、`Biz`。
- 每筆 schema 標 `description: Java class FQN`（例 `eims.web.dto.table.UserDto`）以便回溯。
- 不變更 controller / DTO / 任何 runtime code。

## Capabilities

### New Capabilities
（無）

### Modified Capabilities
- `rest-api-catalog`: 「Request / Response Schema 揭露」要求中之 `Generic` 佔位需被本批次涵蓋之 DTO schema 取代。

## Impact

- 影響檔案：`docs/api/openapi.yaml`（schemas + 受影響 paths 之 `$ref`）。
- `docs/api/<domain>.md` 不動（速查表不展 schema）。
- 檢查腳本 `scripts/check-api-catalog.sh` 不需更新——它只比對 (method, path) 集合，不檢查 schema。
- 後續 change：`flesh-out-schemas-data`（資料 CRUD 領域）、`flesh-out-schemas-intrfc`（介面/訊息版面領域）。
