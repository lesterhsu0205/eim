## Context

`flesh-out-schemas-main-auth` 已建立 schema 反查方法論（D1–D5）與必填政策。本 change 為三批計畫之第 2 批（data：資料 CRUD 領域）。完全套用 main-auth 之 Decisions（field 反查規則、必填寬鬆、嵌套 3 層、FQN description、path $ref 切換策略），不重述。

## Goals / Non-Goals

**Goals:**

- 將 CommCode/Enc/Mask/Trx/Srsys/Depolysys/Extrnlinst/Mappingfunc/Meta/Actionhist 領域之 DTO 反查為 schema。
- 涵蓋 Meta 領域的二次資料來源（`eims.web.meta.dao` / `BcMetaDao` 相關 DTO），確認其結構與主資料來源 DTO 區分清楚。

**Non-Goals:**

- 不涵蓋 intrfc 批次之 DTO。
- 不重述 main-auth 之 D1–D5 規則。
- `DomainCORSDto` / `DomainSSLDto` / `FieldEncodingDto` 等若無對應 endpoint，僅納入 schemas 供其他批次 `$ref` 用、不切換任何 path。

## Decisions

### D1：沿用 main-auth Decisions

直接套用 `flesh-out-schemas-main-auth/design.md` 之 D1（field 反查）、D2（必填）、D3（重名）、D4（FQN description）、D5（path $ref 切換策略）。

### D6：BcMeta 系列分組

`BcMetaDto`、`BcMetaAppCdDto`、`BcMetaSysCdDto` 對應 `eims.web.meta.dao.BcMetaDao` 之 meta 資料來源；schema description 註明 `（資料來源：JNDI / spring.meta.datasource.*）` 以提示對接方。

### D7：Code / Enc / Mask 之單欄端點

`/encCd`、`/maskCd`、`/codes/common/{cdId}` 等回應結構若為「代碼 list」之 wrapper（無對應 `Ui*Out`），保留 `Generic` 並於 description 註明，留待後續確認。`UiEncOut`、`UiMaskOut`、`UiCommCodeOut` 若實際對應這些 endpoint，直接切換。

## Risks / Trade-offs

- **Meta 影響分析端點**：`/metas/effects`、`/metas/syncs` 之回應可能為自訂結構，需 controller 簽章反查決定 schema。風險：可能僅是 `Map<String, Object>` → 退回 `Generic` + description。
- **`MetaEffectDto` / `MetaInterfaceDto` 命名相近**：注意 schema key 不要混淆，description 中標清楚用途。

## Migration Plan

純文件作業；無 runtime 影響。

## Open Questions

- `DomainCORSDto` / `DomainSSLDto` 用途？grep 後確認是否與 SSO 或 SSL 相關設定 endpoint 連動；若無 endpoint，仍納入 schemas 供未來使用。
- `FieldEncodingDto` 是否與 `IntrfcMsgFieldEncodingDto`（intrfc 批次）有重疊？需比對欄位後決定是否合併。
