## Context

三批 schema 補完計畫之第 3 批（最後一批）。Intrfccom / Msglayout 領域 DTO 數量最多、結構最複雜：含 4 種 detail 子型別（CC/EAI/FEP/MCI）、部署結果巢狀 list、多種檔案上傳變體。沿用 main-auth 與 data 批次之 Decisions（D1–D7），新增 discriminator 處理。

## Goals / Non-Goals

**Goals:**

- 涵蓋 Intrfccom 全部 16 個 endpoint 與 Msglayout 全部 13 個 endpoint 之 schema。
- 對 `IntrfccombsDetailCC/EAI/FEP/MCI` 4 子型別以 OpenAPI `oneOf` + `discriminator` 表達。
- 釐清 `/intrfccoms/deploy/terminal` 之 text/plain 回應與 `IntrfcDeployTerminal` DTO 之關係（DTO 在 controller 端構造但回應僅輸出 `String` body）。

**Non-Goals:**

- 不涵蓋 main-auth 與 data 之 DTO（已於前兩 change 處理）。
- 不引入 OpenAPI 3.1 特性（堅持 3.0.3）。
- 不重整 controller signature。

## Decisions

### D1：沿用 main-auth / data Decisions

直接套用 `flesh-out-schemas-main-auth/design.md` 之 D1–D5，以及 `flesh-out-schemas-data/design.md` 之 D6–D7。

### D8：Detail 子型別之 oneOf + discriminator

`IntrfccombsDetailCCDto`、`IntrfccombsDetailEAIDto`、`IntrfccombsDetailFEPDto`、`IntrfccombsDetailMCIDto` 共用 `IntrfccombsDetail` 基底。以下列方式表達：

```yaml
IntrfccombsDetail:
  oneOf:
    - $ref: "#/components/schemas/IntrfccombsDetailCCDto"
    - $ref: "#/components/schemas/IntrfccombsDetailEAIDto"
    - $ref: "#/components/schemas/IntrfccombsDetailFEPDto"
    - $ref: "#/components/schemas/IntrfccombsDetailMCIDto"
  discriminator:
    propertyName: detailType
    mapping:
      CC: "#/components/schemas/IntrfccombsDetailCCDto"
      EAI: "#/components/schemas/IntrfccombsDetailEAIDto"
      FEP: "#/components/schemas/IntrfccombsDetailFEPDto"
      MCI: "#/components/schemas/IntrfccombsDetailMCIDto"
```

若 controller 簽章直接使用 `IntrfccombsDetail` 基底而非具體子型別，path operation `$ref` 指向基底。

### D9：Deploy 巢狀結構

`IntrfcDeployResponse` 內含 `IntrfcDeployResponseList`（為多個 item）、每筆 item 內含 `IntrfcDeployResponseResult`。第 3 層後若還有巢狀，依 D5（main-auth 規則）以 description 指向 FQN。

### D10：Terminal 回應處理

`/intrfccoms/deploy/terminal` controller 簽章為 `ResponseEntity<String>`、回應 content-type `text/plain`，**path operation 不引用 `IntrfcDeployTerminal` schema**。`IntrfcDeployTerminal` schema 仍納入 `components.schemas` 標 `description: 內部用途，controller 構造後序列化為 text/plain`。

### D11：檔案上傳之 metadata 欄位

`IntrfccombsFileUploadDto`、`IntrfcFileUploadDto`、`MsglayoutbsFileUploadDto`：若除 `file` 外尚有 metadata 欄位（如 `intrfcId`、`version`），於 multipart schema 一併展開：

```yaml
content:
  multipart/form-data:
    schema:
      type: object
      properties:
        file: { type: string, format: binary }
        intrfcId: { type: string }
        version: { type: string }
```

## Risks / Trade-offs

- **`detailType` discriminator 欄位**：若實際 DTO 無此欄位，需以 controller 簽章中的路徑或 query 參數判別子型別；可能改用 `description` 列舉而非 `discriminator`。
- **DTO 命名與 endpoint 對映模糊**：例如 `IntrfccombsDto` 為基本欄位、`IntrfccombsListDto` 為清單變體；POST/PUT 用哪個需 controller 反查。
- **單元測試缺**：本批次完成後 yaml 變大，可能影響 IDE 載入效能；可考慮拆分 `components.schemas` 至獨立檔案（OpenAPI `$ref` external file）— 列為 Open Question。

## Migration Plan

純文件作業；無 runtime 影響。

## Open Questions

- 是否將 `components.schemas` 拆分為 `docs/api/schemas/*.yaml` 多檔，以 `$ref: "./schemas/Xxx.yaml"` 引用？需評估工具相容性（VSCode OpenAPI plugin 支援 external $ref，但 Redocly lint 預設需 bundle）。建議：本批次先單檔，必要時再開另一 change 重構。
- `IntrfcdeployInfoDto` 與 `UiIntrfcdeployhisthsOut` 之關係？欄位是否重疊到可合併？需開檔確認。
- `MsglayoutbsDtoMapping` 與 `IntrfccombsMappingDto` 是否共享欄位？若是，是否抽出共用 schema？
