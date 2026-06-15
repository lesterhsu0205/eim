## Why

承接 `flesh-out-schemas-main-auth` 與 `flesh-out-schemas-data`，本 change 處理三批計畫之最後一批：「介面 / 訊息版面」領域（Intrfccom + Msglayout）。此批為三批中 DTO 結構最複雜者：含部署相關 detail 子型別（CC/EAI/FEP/MCI）、檔案上傳 / 匯入 / 匯出回應、deploy terminal。完成後 `Generic` 佔位 schema 僅保留給「測試端點 / 未來預留 endpoint」。

## What Changes

- 於 `docs/api/openapi.yaml` `components.schemas` 加入：
  - **dto（共用）**：`IntrfcDeploy`、`IntrfcDeployResponse`、`IntrfcDeployResponseList`、`IntrfcDeployResponseResult`、`IntrfcDeployTerminal`、`IntrfcFileImportErrInfo`、`IntrfcInfo`、`IntrfcInfoExportDto`、`IntrfccombsDetail`、`IntrfcdeployInfoDto`、`GenLayoutDetailDto`、`AdminLayoutInfo`（若 main-auth 已加則略過）
  - **dto.table（Intrfc 系列）**：`IntrfccombsDto`、`IntrfccombsListDto`、`IntrfccombsMappingDto`、`IntrfccombsRawDataDto`、`IntrfccombsFileUploadDto`、`IntrfccombsDetailCCDto`、`IntrfccombsDetailEAIDto`、`IntrfccombsDetailFEPDto`、`IntrfccombsDetailMCIDto`、`IntrfccomdtDto`、`IntrfcdeployhisthsDto`、`IntrfcdeploysysdtDto`、`IntrfcmsglayoutdtDto`、`IntrfcroutinfodtDto`、`IntrfcsrsysdtDto`、`IntrfcFileUploadDto`、`IntrfcIdCreateDto`、`IntrfcMsgFieldEncodingDto`
  - **dto.table（Msg 系列）**：`MsglayoutbsDto`、`MsglayoutbsDtoMapping`、`MsglayoutbsFileUploadDto`、`MsglayoutbsListDto`、`MsglayoutdtDto`、`MsgIdCreateDto`、`MsgInsertDto`、`MsgLayoutEffectDto`
  - **dto.ui**：`UiIntrfccombsOut`、`UiIntrfccomdtOut`、`UiIntrfcdeployhisthsOut`、`UiIntrfcdeploysysdtOut`、`UiIntrfcmsglayoutdtOut`、`UiIntrfcroutinfodtOut`、`UiIntrfcsrsysdtOut`、`UiIntrfcDeployResponse`、`UiIntrfcIdOut`、`UiMsglayoutbsOut`、`UiMsgLayoutIdOut`
- 將 `Intrfccom` 與 `Msglayout` tag 之 path operation 中 `Generic` `$ref` 改為對應 schema。
- 檔案上傳 endpoint 維持 `multipart/form-data`，但 `properties.file` 之外若有伴隨 metadata 欄位，依 `*FileUploadDto` 展開。
- Excel 匯出 endpoint 維持 `application/octet-stream`，request body 改 `$ref` 對應之查詢條件 DTO。
- 不變更 controller / DTO / 任何 runtime code。

## Capabilities

### New Capabilities
（無）

### Modified Capabilities
- `rest-api-catalog`: 「Request / Response Schema 揭露」要求延伸至 Intrfccom / Msglayout 領域並補完 detail 子型別之 discriminator 處理。

## Impact

- 影響檔案：`docs/api/openapi.yaml`。
- `docs/api/intrfccom.md` / `msglayout.md` 不動。
- 檢查腳本不需更新。
- 前置依賴：`flesh-out-schemas-main-auth`、`flesh-out-schemas-data` 完成。
- 完成後：`Generic` schema 僅留給 `/userstest/test` 與未來預留 endpoint。
