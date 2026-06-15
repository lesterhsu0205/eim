## 1. 前置調查

- [x] 1.1 讀 `eims.web.dto` 之 `IntrfcDeploy`、`IntrfcDeployResponse`、`IntrfcDeployResponseList`、`IntrfcDeployResponseResult`、`IntrfcDeployTerminal`、`IntrfcFileImportErrInfo`、`IntrfcInfo`、`IntrfcInfoExportDto`、`IntrfccombsDetail`、`IntrfcdeployInfoDto`、`GenLayoutDetailDto`、`AdminLayoutInfo`
- [x] 1.2 讀 `eims.web.dto.table` 之 Intrfc 系列 18 個 DTO
- [x] 1.3 讀 Msg 系列：`MsglayoutbsDto`、`MsglayoutbsDtoMapping`、`MsglayoutbsFileUploadDto`、`MsglayoutbsListDto`、`MsglayoutdtDto`、`MsgIdCreateDto`、`MsgInsertDto`、`MsgLayoutEffectDto`
- [x] 1.4 讀 `eims.web.dto.ui` 之 `UiIntrfc*` 系列與 `UiMsglayout*` 系列共 11 個 DTO
- [x] 1.5 確認 `IntrfccombsDetail` 是否含 `detailType`（或類似）欄位作為 discriminator；若無，改用 description 列舉
- [x] 1.6 比對 `IntrfcdeployInfoDto` 與 `UiIntrfcdeployhisthsOut` 欄位重疊度
- [x] 1.7 比對 `MsglayoutbsDtoMapping` 與 `IntrfccombsMappingDto` 欄位重疊度

## 2. Schema 新增（dto 共用）

- [x] 2.1 `IntrfcDeploy`、`IntrfcDeployResponse`、`IntrfcDeployResponseList`、`IntrfcDeployResponseResult`（依 D9 巢狀展開）
- [x] 2.2 `IntrfcDeployTerminal`（內部用，description 註明）
- [x] 2.3 `IntrfcFileImportErrInfo`、`IntrfcInfo`、`IntrfcInfoExportDto`
- [x] 2.4 `IntrfccombsDetail`（oneOf + discriminator，依 D8）
- [x] 2.5 `IntrfcdeployInfoDto`、`GenLayoutDetailDto`、`AdminLayoutInfo`（若 main-auth 已加則略過）

## 3. Schema 新增（table - Intrfc 系列）

- [x] 3.1 `IntrfccombsDto`、`IntrfccombsListDto`
- [x] 3.2 `IntrfccombsDetailCCDto`、`IntrfccombsDetailEAIDto`、`IntrfccombsDetailFEPDto`、`IntrfccombsDetailMCIDto`
- [x] 3.3 `IntrfccombsMappingDto`、`IntrfccombsRawDataDto`、`IntrfccombsFileUploadDto`
- [x] 3.4 `IntrfccomdtDto`、`IntrfcdeployhisthsDto`、`IntrfcdeploysysdtDto`
- [x] 3.5 `IntrfcmsglayoutdtDto`、`IntrfcroutinfodtDto`、`IntrfcsrsysdtDto`
- [x] 3.6 `IntrfcFileUploadDto`、`IntrfcIdCreateDto`、`IntrfcMsgFieldEncodingDto`

## 4. Schema 新增（table - Msg 系列）

- [x] 4.1 `MsglayoutbsDto`、`MsglayoutbsListDto`、`MsglayoutdtDto`
- [x] 4.2 `MsglayoutbsDtoMapping`、`MsglayoutbsFileUploadDto`
- [x] 4.3 `MsgIdCreateDto`、`MsgInsertDto`、`MsgLayoutEffectDto`

## 5. Schema 新增（ui）

- [x] 5.1 `UiIntrfccombsOut`、`UiIntrfccomdtOut`
- [x] 5.2 `UiIntrfcdeployhisthsOut`、`UiIntrfcdeploysysdtOut`、`UiIntrfcDeployResponse`、`UiIntrfcIdOut`
- [x] 5.3 `UiIntrfcmsglayoutdtOut`、`UiIntrfcroutinfodtOut`、`UiIntrfcsrsysdtOut`
- [x] 5.4 `UiMsglayoutbsOut`、`UiMsgLayoutIdOut`

## 6. Path operation $ref 切換 — Intrfccom

- [x] 6.1 `/intrfccoms` POST/PUT body → `IntrfccombsDto`；GET → `UiIntrfccombsOut`
- [x] 6.2 `/intrfccoms/{intrfcId}` GET → `UiIntrfccombsOut`；DELETE → `CommonResponse`
- [x] 6.3 `/interfaceListApi` GET → `UiIntrfccombsOut` list
- [x] 6.4 `/intrfccoms/deploy`、`/intrfccoms/deployAll` POST body → `IntrfcDeploy`；回應 → `UiIntrfcDeployResponse`
- [x] 6.5 `/intrfccoms/deployhistorys`、`/intrfccoms/deployhistoryresults` GET → `UiIntrfcdeployhisthsOut`
- [x] 6.6 `/intrfccoms/redeploy` POST body → `IntrfcDeploy`；回應 → `UiIntrfcDeployResponse`
- [x] 6.7 `/intrfccoms/deploy/{intrfcId}` GET → `UiIntrfcdeployhisthsOut`
- [x] 6.8 `/intrfccoms/deploy/terminal` GET 回應確認為 `text/plain` + `type: string`（依 D10）
- [x] 6.9 `/intrfccoms/intrfcidcreate` POST body → `IntrfcIdCreateDto`；回應 → `UiIntrfcIdOut`
- [x] 6.10 `/intrfccoms/layoutdiff` GET 回應依 controller 反查（可能 `GenLayoutDetailDto`）
- [x] 6.11 `/intrfccoms/fileuploads`、`/intrfccoms/import/intrfcfiles`、`/intrfccoms/import/definition` multipart：依 D11 展開 metadata 欄位
- [x] 6.12 `/intrfccoms/excelexport` POST body → `IntrfccombsListDto`（查詢條件）；response `application/octet-stream`
- [x] 6.13 `/intrfccoms/export/intrfcinfos` GET response `application/octet-stream`

## 7. Path operation $ref 切換 — Msglayout

- [x] 7.1 `/msglayouts` POST/PUT body → `MsglayoutbsDto`；GET → `UiMsglayoutbsOut`
- [x] 7.2 `/msglayouts/{msgLayoutId}` GET → `UiMsglayoutbsOut`；DELETE → `CommonResponse`
- [x] 7.3 `/msglayouts/{msgLayoutId}/effects` GET → `MsgLayoutEffectDto` array
- [x] 7.4 `/msglayoutstemp` POST/PUT body → `MsglayoutbsDto`（暫存版本）
- [x] 7.5 `/msglayoutslist` POST body → `MsglayoutbsListDto`；回應 → `UiMsglayoutbsOut` list
- [x] 7.6 `/msglayouts/excelexport` POST body → `MsglayoutbsListDto`；response `application/octet-stream`
- [x] 7.7 `/msglayouts/fileuploads` multipart：依 D11 展開
- [x] 7.8 `/msglayouts/extrnlMsgs` GET 回應依 controller 反查
- [x] 7.9 `/msglayouts/msgidcreate` POST body → `MsgIdCreateDto`；回應 → `UiMsgLayoutIdOut`

## 8. 驗證

- [x] 8.1 跑 `./scripts/check-api-catalog.sh`
- [x] 8.2 yaml lint 通過（含 `discriminator` / `oneOf` 結構）
- [x] 8.3 grep `"$ref": "#/components/schemas/Generic"`，確認僅出現在 `/userstest/test` 或預留 endpoint
- [x] 8.4 更新 `docs/api/README.md` 之「Schema 詳細度」段落，標註本批次完成、`Generic` 退場
