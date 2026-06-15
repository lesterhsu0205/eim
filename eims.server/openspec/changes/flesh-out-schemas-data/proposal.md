## Why

承接 `flesh-out-schemas-main-auth`，本 change 處理「資料 CRUD」批次：CommCode/Enc/Mask/Trx/Srsys/Depolysys/Extrnlinst/Mappingfunc/Meta/Actionhist。把這些領域之 path operation 從 `Generic` 佔位切換為實際 DTO schema。

## What Changes

- 於 `docs/api/openapi.yaml` `components.schemas` 加入：
  - **dto.table**：`CommCodeDto`、`CodeInterfaceDto`、`EnccdDto`、`MaskcdDto`、`TrxcdDto`、`SrsysbsDto`、`DepolysysbsDto`、`ExtrnlinstcdDto`、`MappingfuncbsDto`、`MetabsDto`、`BcMetaDto`、`BcMetaAppCdDto`、`BcMetaSysCdDto`、`MetaEffectDto`、`MetaInterfaceDto`、`ActionhisthsDto`、`DomainCORSDto`、`DomainSSLDto`、`FieldEncodingDto`
  - **dto.ui**：`UiCommCodeOut`、`UiEncOut`、`UiMaskOut`、`UiTrxcdOut`、`UiSrsysbsOut`、`UiDepolysysbsOut`、`UiExtrnlinstcdOut`、`UiMappingfuncbsOut`、`UiMetabsOut`、`UiActionhisthsOut`
- 將下列 tag 之 path operation 中 `Generic` `$ref` 改為對應 schema：
  `CommCode`、`Enc`、`Mask`、`Trx`、`Srsys`、`Depolysys`、`Extrnlinst`、`Mappingfunc`、`Meta`、`Actionhist`。
- 不變更 controller / DTO / 任何 runtime code。

## Capabilities

### New Capabilities
（無）

### Modified Capabilities
- `rest-api-catalog`: 「Request / Response Schema 揭露」要求延伸至資料 CRUD 領域。

## Impact

- 影響檔案：`docs/api/openapi.yaml`（schemas + 受影響 paths 之 `$ref`）。
- `docs/api/<domain>.md` 不動。
- 檢查腳本不需更新。
- 前置依賴：`flesh-out-schemas-main-auth` 完成（共用 schema 與 `CommonResponse` 已就位）。
- 後續：`flesh-out-schemas-intrfc`（介面 / 訊息版面領域，DTO 結構最複雜）。
