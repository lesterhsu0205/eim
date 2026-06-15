## 1. 前置調查

- [x] 1.1 讀 `eims.web.dto.table` 之 `CommCodeDto`、`CodeInterfaceDto`、`EnccdDto`、`MaskcdDto`、`TrxcdDto`、`SrsysbsDto`、`DepolysysbsDto`、`ExtrnlinstcdDto`、`MappingfuncbsDto`
- [x] 1.2 讀 `MetabsDto`、`BcMetaDto`、`BcMetaAppCdDto`、`BcMetaSysCdDto`、`MetaEffectDto`、`MetaInterfaceDto`、`ActionhisthsDto`
- [x] 1.3 讀 `DomainCORSDto`、`DomainSSLDto`、`FieldEncodingDto`，grep usage 確認是否與本批次 endpoint 連動
- [x] 1.4 讀 `eims.web.dto.ui` 之 `UiCommCodeOut`、`UiEncOut`、`UiMaskOut`、`UiTrxcdOut`、`UiSrsysbsOut`、`UiDepolysysbsOut`、`UiExtrnlinstcdOut`、`UiMappingfuncbsOut`、`UiMetabsOut`、`UiActionhisthsOut`
- [x] 1.5 比對 `FieldEncodingDto` 與 `IntrfcMsgFieldEncodingDto`（intrfc 批次）欄位；決定是否合併

## 2. Schema 新增（table）

- [x] 2.1 `CommCodeDto`、`CodeInterfaceDto`
- [x] 2.2 `EnccdDto`、`MaskcdDto`、`FieldEncodingDto`
- [x] 2.3 `TrxcdDto`、`SrsysbsDto`、`DepolysysbsDto`、`ExtrnlinstcdDto`、`MappingfuncbsDto`
- [x] 2.4 `MetabsDto`、`BcMetaDto`、`BcMetaAppCdDto`、`BcMetaSysCdDto`（含 `資料來源：JNDI / spring.meta.datasource.*` 註明）
- [x] 2.5 `MetaEffectDto`、`MetaInterfaceDto`、`ActionhisthsDto`
- [x] 2.6 `DomainCORSDto`、`DomainSSLDto`（僅納入 schemas，不切 path 若無對應 endpoint）

## 3. Schema 新增（ui）

- [x] 3.1 `UiCommCodeOut`、`UiEncOut`、`UiMaskOut`
- [x] 3.2 `UiTrxcdOut`、`UiSrsysbsOut`、`UiDepolysysbsOut`、`UiExtrnlinstcdOut`、`UiMappingfuncbsOut`
- [x] 3.3 `UiMetabsOut`、`UiActionhisthsOut`

## 4. Path operation $ref 切換

- [x] 4.1 `CommCode` tag：POST/PUT body → `CommCodeDto`；GET → `UiCommCodeOut`；`/codes/common/{cdId}` 視 controller 簽章決定
- [x] 4.2 `Enc` tag：`/encCd` GET → `UiEncOut`（list）
- [x] 4.3 `Mask` tag：`/maskCd` GET → `UiMaskOut`（list）
- [x] 4.4 `Trx` tag：POST/PUT body → `TrxcdDto`；GET → `UiTrxcdOut`
- [x] 4.5 `Srsys` tag：POST/PUT body → `SrsysbsDto`；GET → `UiSrsysbsOut`
- [x] 4.6 `Depolysys` tag：POST/PUT body → `DepolysysbsDto`；GET → `UiDepolysysbsOut`；`/depolysyss/getlist` body 視 controller 反查
- [x] 4.7 `Extrnlinst` tag：POST/PUT body → `ExtrnlinstcdDto`；GET → `UiExtrnlinstcdOut`
- [x] 4.8 `Mappingfunc` tag：POST/PUT body → `MappingfuncbsDto`；GET → `UiMappingfuncbsOut`
- [x] 4.9 `Meta` tag：POST/PUT body → `MetabsDto`；GET → `UiMetabsOut`；`/metas/effects` → `MetaEffectDto` array；`/metas/syncs` 視 controller 反查（可能 `Generic`）
- [x] 4.10 `Actionhist` tag：POST body → `ActionhisthsDto`；GET → `UiActionhisthsOut`

## 5. 驗證

- [x] 5.1 跑 `./scripts/check-api-catalog.sh`
- [x] 5.2 yaml lint 通過
- [x] 5.3 grep `Generic` 用量；確認本批次涵蓋之 path 已不再用 `Generic`（測試／檔案上傳例外）
