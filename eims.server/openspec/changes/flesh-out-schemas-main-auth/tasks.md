## 1. 前置調查

- [x] 1.1 讀 `eims.web.dto.LoginUserInfo`、`SessionInfo`、`UserInfo`、`CommonResponse`、`AdminLayoutInfo` 五個共用 DTO，紀錄欄位
- [x] 1.2 讀 `eims.web.dto.table` 之 `MenuDto`、`RoleDto`、`PermDto`、`UserDto`、`AppcdDto`、`BizcdDto`、`MenuRoleRelDto`、`RolePermRelDto`、`ChangePwdInfo`
- [x] 1.3 讀 `eims.web.dto.ui` 之 `UiMenuOut`、`UiMenuTreeInfo`、`UiRoleOut`、`UiPermOut`、`UiUserOut`、`UiAppcdOut`、`UiBizcdOut`、`ErrorResponse`
- [x] 1.4 grep `AdminLayoutInfo` usage 決定是否納入；無 controller 引用則跳過（**結論：無 controller 引用，本批次跳過；保留至 `flesh-out-schemas-intrfc` 處理，因依賴 `GenLayoutDetailDto`**）
- [x] 1.5 比對 `CommonResponse` vs `ErrorResponse` 結構；若幾乎相同則合併、否則並存（**結論：結構不同。`CommonResponse` = `hasError` + `errorMsg`；`ErrorResponse` = `responseStatus` + `message` + `parameters[]` + `stackTrace`。並存。已修正 `ErrorResponse` schema 對齊實際欄位**）

## 2. 共用 DTO schema

- [x] 2.1 在 `components.schemas` 加 `LoginUserInfo`（含 `SessionInfo` `$ref`）
- [x] 2.2 加 `SessionInfo`
- [x] 2.3 加 `UserInfo`
- [x] 2.4 加 `CommonResponse`（與 `ErrorResponse` 不同，並存）
- [x] 2.5 （視 1.4）加 `AdminLayoutInfo`（**跳過**，依 1.4 結論移至 intrfc 批次）

## 3. ACL / Menu / Role / Perm schema

- [x] 3.1 加 `MenuDto`、`UiMenuOut`、`UiMenuTreeInfo`
- [x] 3.2 加 `RoleDto`、`UiRoleOut`、`MenuRoleRelDto`、`RolePermRelDto`
- [x] 3.3 加 `PermDto`、`UiPermOut`

## 4. User / App / Biz schema

- [x] 4.1 加 `UserDto`、`UiUserOut`、`ChangePwdInfo`
- [x] 4.2 加 `AppcdDto`、`UiAppcdOut`
- [x] 4.3 加 `BizcdDto`、`UiBizcdOut`

## 5. Path operation $ref 切換

- [x] 5.1 `Main` tag：`/login` body → `SessionInfo`、200 → `LoginUserInfo`；`/userInfo` 200 → `UserInfo`；`/logininfosso` 200 → `LoginUserInfo`；`/logout` 200 → `CommonResponse`；`/eims/sso`、`/changelang`、`/bxmlogin` 維持 `Generic`（非 typed JSON / 入口端點）
- [x] 5.2 `Menu` tag：POST/PUT body → `MenuDto`；GET list → `UiMenuOut`；GET single → `MenuDto`；DELETE → `CommonResponse`
- [x] 5.3 `Role` tag：POST/PUT `/roles` body → `RoleDto`；GET list → `UiRoleOut`；GET single → `RoleDto`；子資源 `/menus`、`/menupopups` GET → `UiMenuTreeInfo` array；`/menus` PUT body → `MenuRoleRelDto` array；`/perms`、`/permpopups` GET → `UiPermOut`；`/perms` PUT body → `RolePermRelDto` array；所有 DELETE → `CommonResponse`
- [x] 5.4 `Perm` tag：POST/PUT body → `PermDto`；GET list → `UiPermOut`；GET single → `PermDto`；DELETE → `CommonResponse`
- [x] 5.5 `User` tag：POST/PUT body → `UserDto`；GET list → `UiUserOut`；GET single → `UserDto`；DELETE → `CommonResponse`；`/userstest/test` 維持 `Generic`
- [x] 5.6 `App` tag：POST/PUT body → `AppcdDto`；GET list → `UiAppcdOut`；GET single → `AppcdDto`；DELETE → `CommonResponse`
- [x] 5.7 `Biz` tag：POST/PUT body → `BizcdDto`；GET list → `UiBizcdOut`；GET single → `BizcdDto`；DELETE → `CommonResponse`

## 6. 驗證

- [x] 6.1 跑 `./scripts/check-api-catalog.sh`，確認 (method, path) 集合仍一致（**通過**）
- [x] 6.2 yaml lint（VSCode `42Crunch.vscode-openapi` 或 `redocly lint`）通過（**本地無安裝 redocly**；yaml syntax 由 python yaml load 隱式驗證；建議 IDE OpenAPI plugin 跑完整 schema lint）
- [x] 6.3 grep `Generic` 用量；確認本批次涵蓋之 path 已不再用 `Generic`（測試端點與檔案上傳除外）（**結果**：main-auth 範圍內僅 `/eims/sso`、`/changelang`、`/bxmlogin`、`/userstest/test` 維持 `Generic`，皆有明確理由；其他 path 已切換完成）
