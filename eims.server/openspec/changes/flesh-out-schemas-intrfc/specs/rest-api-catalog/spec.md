## MODIFIED Requirements

### Requirement: Request / Response Schema 揭露

每個非 GET 之 endpoint 規格 SHALL 揭露其 request body 之 DTO 類別名，以及回應主體之 DTO 類別名。Intrfccom 與 Msglayout 領域之 schema MUST 以實際 `eims.web.dto.*` 類別欄位反查而成。`IntrfccombsDetail` 系列 SHALL 以 `oneOf` + `discriminator` 表達 4 種子型別（CC/EAI/FEP/MCI）。完成本批次後，`Generic` schema 僅得用於 `/userstest/test` 與尚未實作之預留 endpoint。

#### Scenario: POST/PUT body schema 對應 DTO

- **WHEN** 檢視 Intrfccom / Msglayout 領域中任一 `POST` / `PUT` operation 之 `requestBody.content."application/json".schema`
- **THEN** schema `$ref` 指向 `components.schemas/<DtoClassName>`，且 `<DtoClassName>` 對應實際 `eims.web.dto.table` 或 `eims.web.dto` 套件中之類別（例如 `IntrfccombsDto`、`MsglayoutbsDto`、`IntrfcDeploy`）。

#### Scenario: GET response schema 對應 Ui DTO

- **WHEN** 檢視 Intrfccom / Msglayout 領域中任一 `GET` operation 之 `responses."200".content."application/json".schema`
- **THEN** schema `$ref` 指向對應之 `Ui<Domain>Out` 或 `UiIntrfc*Out` schema。

#### Scenario: Detail 子型別之 oneOf + discriminator

- **WHEN** 檢視 `IntrfccombsDetail` schema
- **THEN** schema SHALL 包含 `oneOf` 列出 `IntrfccombsDetailCCDto`、`IntrfccombsDetailEAIDto`、`IntrfccombsDetailFEPDto`、`IntrfccombsDetailMCIDto` 並提供 `discriminator.propertyName` 與 `mapping`。

#### Scenario: Deploy terminal text/plain 回應

- **WHEN** 檢視 `/intrfccoms/deploy/terminal` GET 之 `responses."200"`
- **THEN** content-type SHALL 為 `text/plain` 且 schema 為 `type: string`，**不** `$ref` 至 `IntrfcDeployTerminal`（該 DTO 僅內部用）。

#### Scenario: 檔案上傳 metadata 展開

- **WHEN** 檢視 `/intrfccoms/fileuploads` 或 `/msglayouts/fileuploads` 之 `requestBody.content."multipart/form-data".schema`
- **THEN** schema `properties` SHALL 包含 `file: { type: string, format: binary }` 與對應 `*FileUploadDto` 中之 metadata 欄位（如 `intrfcId`、`version`）。

#### Scenario: Generic 殘留範圍限制

- **WHEN** 三批 schema 補完 change（main-auth、data、intrfc）全部完成後，於 `docs/api/openapi.yaml` grep `"$ref": "#/components/schemas/Generic"`
- **THEN** 僅得出現於 `/userstest/test` 或標 `description` 含「預留」之 endpoint，不得出現於其他位置。
