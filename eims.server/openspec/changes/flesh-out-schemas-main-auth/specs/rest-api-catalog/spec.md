## MODIFIED Requirements

### Requirement: Request / Response Schema 揭露

每個非 GET 之 endpoint 規格 SHALL 揭露其 request body 之 DTO 類別名（取自 controller 簽章），以及回應主體之 DTO 類別名。Main / Menu / Role / Perm / User / App / Biz 領域之 schema MUST 以實際 `eims.web.dto.*` 類別欄位反查而成，不得再使用 `Generic` 佔位 schema。

#### Scenario: POST/PUT body schema 對應 DTO

- **WHEN** 檢視 Main / Menu / Role / Perm / User / App / Biz 領域中任一 `POST` / `PUT` operation 之 `requestBody.content."application/json".schema`
- **THEN** schema `$ref` 指向 `components.schemas/<DtoClassName>`，且 `<DtoClassName>` 對應實際 `eims.web.dto.table` 套件中之類別（例如 `UserDto`、`RoleDto`、`MenuDto`）。

#### Scenario: GET response schema 對應 Ui DTO

- **WHEN** 檢視 Menu / Role / Perm / User / App / Biz 領域中任一 `GET` operation 之 `responses."200".content."application/json".schema`
- **THEN** schema `$ref` 指向對應之 `Ui<Domain>Out` schema（例如 `UiUserOut`、`UiRoleOut`），且該 schema 已於 `components.schemas` 定義；若 controller 簽章無對應 `Ui*Out`，可保留 `Generic` 並於 description 註明原因。

#### Scenario: Login / userInfo 回應結構

- **WHEN** 檢視 `/login` POST 與 `/userInfo` GET 之 200 回應 schema
- **THEN** `/login` schema 指向 `LoginUserInfo`（內含 `SessionInfo`），`/userInfo` schema 指向 `UserInfo` 或 `LoginUserInfo`，視 controller 簽章決定。

#### Scenario: Schema 必含 FQN description

- **WHEN** 檢視本批次新增之任一 schema
- **THEN** 該 schema 之頂層 `description` SHALL 以 `對應 eims.web.dto.<sub-package>.<SimpleName>` 起始，便於回溯來源 Java 類別。

#### Scenario: 嵌套展開深度限制

- **WHEN** 檢視任一新增 schema
- **THEN** 嵌套展開最多到第 3 層；超過第 3 層之欄位 SHALL 以 `type: object`（或 array of object）+ `description: 詳見 eims.web.dto.<sub-package>.<SimpleName>` 表示。

#### Scenario: 未知 property 政策

- **WHEN** 規格描述全域反序列化行為
- **THEN** 規格 SHALL 註明 `CustomObjectMapper` 設定 `FAIL_ON_UNKNOWN_PROPERTIES = true`，呼叫端必須只送 DTO 已宣告之欄位。
