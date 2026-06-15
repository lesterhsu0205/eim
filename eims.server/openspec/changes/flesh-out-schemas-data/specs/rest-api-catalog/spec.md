## MODIFIED Requirements

### Requirement: Request / Response Schema 揭露

每個非 GET 之 endpoint 規格 SHALL 揭露其 request body 之 DTO 類別名（取自 controller 簽章），以及回應主體之 DTO 類別名。CommCode / Enc / Mask / Trx / Srsys / Depolysys / Extrnlinst / Mappingfunc / Meta / Actionhist 領域之 schema MUST 以實際 `eims.web.dto.*` 類別欄位反查而成，不得再使用 `Generic` 佔位 schema。

#### Scenario: POST/PUT body schema 對應 DTO

- **WHEN** 檢視上述任一資料 CRUD 領域中之 `POST` / `PUT` operation 之 `requestBody.content."application/json".schema`
- **THEN** schema `$ref` 指向 `components.schemas/<DtoClassName>`，且 `<DtoClassName>` 對應實際 `eims.web.dto.table` 套件中之類別（例如 `CommCodeDto`、`TrxcdDto`、`MetabsDto`）。

#### Scenario: GET response schema 對應 Ui DTO

- **WHEN** 檢視上述領域中之 `GET` operation 之 `responses."200".content."application/json".schema`
- **THEN** schema `$ref` 指向對應之 `Ui<Domain>Out` schema；若 controller 簽章無對應 `Ui*Out`，可保留 `Generic` 並於 description 註明原因。

#### Scenario: Meta 二次資料來源標註

- **WHEN** 檢視 `BcMetaDto`、`BcMetaAppCdDto`、`BcMetaSysCdDto` 之 schema description
- **THEN** description SHALL 註明 `資料來源：JNDI / spring.meta.datasource.*`，以區分主資料來源 DTO。

#### Scenario: 沿用 main-auth 之 schema 規則

- **WHEN** 檢視本批次任一新增 schema
- **THEN** SHALL 遵守 `flesh-out-schemas-main-auth` 制定之 FQN description、嵌套 3 層、未知 property 政策等規則。
