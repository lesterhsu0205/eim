## Context

`scan-existing-restful-api-specs` 完成後 OpenAPI 之 schemas 僅 `Generic` + `ErrorResponse`。本 change 為三批 schema 補完計畫之第 1 批（main-auth：認證 + ACL）。後續批次：`flesh-out-schemas-data`（資料 CRUD 領域）、`flesh-out-schemas-intrfc`（介面 / 訊息版面）。三批共用同樣作業手法。

## Goals / Non-Goals

**Goals:**

- 將 main-auth 領域之 DTO 反查為 OpenAPI 3.0 schema，取代對應 path operation 之 `Generic` `$ref`。
- 保留 `Generic` schema，未涵蓋之 path 仍可繼續指向；批次完成後 `Generic` 不刪、待全部三批做完再決議。
- 維持 `scripts/check-api-catalog.sh` 為綠燈。

**Non-Goals:**

- 不動 controller / DTO / service 原始碼。
- 不引入 springdoc 等 runtime 工具。
- 不涵蓋 data / intrfc 批次之 DTO。
- 不展開超過 3 層嵌套（依 `scan-existing-restful-api-specs` design.md D5 規則）。

## Decisions

### D1：欄位反查以 Java field 為準

反查每個 DTO `public` / Lombok `@Data` / private+getter 之 field：

- 基本型別 → `type` + `format`：`String → string`；`Integer/int → integer (int32)`；`Long/long → integer (int64)`；`Double/double / BigDecimal → number`；`Boolean/boolean → boolean`；`Date / java.sql.Timestamp / LocalDateTime → string (date-time)`。
- 集合：`List<X> / X[]` → `type: array, items: $ref or primitive`。
- 巢狀 DTO → `$ref: "#/components/schemas/<DtoSimpleName>"`，並把該 DTO 也加入 schemas（若已在批次清單中）。
- 巢狀 DTO 超過第 3 層 → 改為 `type: object, description: "詳見 eims.web.dto.table.XxxDto"`。

### D2：必填性

Java 端無 `@NotNull` / Bean Validation 標註慣例。預設**全部欄位 optional**（不設 `required`）。若 controller signature 直接綁定為 path / query 參數則該欄位視為必填，但這部分已由 path-level `parameters` 處理，schema 仍維持寬鬆。

### D3：重名處理

main-auth 批次目前無跨子套件重名 DTO。若 data / intrfc 批次出現（例如 `UserDto` 與 `UiUserOut`），schema key 直接使用 simple class name：`UserDto`、`UiUserOut`（已天然區分）。若真有重名，加子套件前綴：`ui.UserPopupDto`。

### D4：每筆 schema 加 description 指向 FQN

每個 schema 第一行：`description: 對應 eims.web.dto.[ui|table].<SimpleName>`。便於日後從規格回溯原始類別。

### D5：path operation $ref 切換策略

每筆 path operation 依 HTTP method 切：

- POST/PUT request body：`$ref` → `table` DTO（建立／更新用）。
- GET response：`$ref` → `ui` DTO（若有）；無 `Ui*` 對應則仍指 `Generic`。
- DELETE 回應通常為 `CommonResponse`。
- `/login` 回應走 `LoginUserInfo`（含 `SessionInfo`）。
- `/userInfo` / `/logininfosso` 回應走 `UserInfo` 或 `LoginUserInfo`，依 controller 簽章決定。

## Risks / Trade-offs

- **Lombok 解析**：若 DTO 用 `@Data` 隱藏 getter，需從 field 推；風險低，可逐檔目視確認。
- **空 DTO**：部分 `Ui*Out` 可能僅 wrap list；schema 設為 `type: object, additionalProperties: true` + description 指向 FQN。
- **schema 漂移**：未來 DTO 改欄位若沒更新 yaml，會與 controller 不一致。緩解：CLAUDE.md 已有「同步 docs/api」規範；本次無新檢查機制。

## Migration Plan

純文件作業；無 runtime 影響、無 rollback 議題。

## Open Questions

- `CommonResponse` 結構與 `ErrorResponse` 是否近似？若是，是否合併為單一 schema？需開檔比對後決定。
- `AdminLayoutInfo` 用途與哪些 endpoint 對應未明，需 grep usage 後決定是否納入。
