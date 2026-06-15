## 1. 前置調查

- [ ] 1.1 讀 `eims.web.dto.LoginUserInfo`、`SessionInfo`、`UserInfo`、`CommonResponse`、`AdminLayoutInfo` 五個共用 DTO，紀錄欄位
- [ ] 1.2 讀 `eims.web.dto.table` 之 `MenuDto`、`RoleDto`、`PermDto`、`UserDto`、`AppcdDto`、`BizcdDto`、`MenuRoleRelDto`、`RolePermRelDto`、`ChangePwdInfo`
- [ ] 1.3 讀 `eims.web.dto.ui` 之 `UiMenuOut`、`UiMenuTreeInfo`、`UiRoleOut`、`UiPermOut`、`UiUserOut`、`UiAppcdOut`、`UiBizcdOut`、`ErrorResponse`
- [ ] 1.4 grep `AdminLayoutInfo` usage 決定是否納入；無 controller 引用則跳過
- [ ] 1.5 比對 `CommonResponse` vs `ErrorResponse` 結構；若幾乎相同則合併、否則並存

## 2. 共用 DTO schema

- [ ] 2.1 在 `components.schemas` 加 `LoginUserInfo`（含 `SessionInfo` `$ref`）
- [ ] 2.2 加 `SessionInfo`
- [ ] 2.3 加 `UserInfo`
- [ ] 2.4 加 `CommonResponse`（若與 `ErrorResponse` 合併則跳過、僅補 `ErrorResponse` 註解）
- [ ] 2.5 （視 1.4）加 `AdminLayoutInfo`

## 3. ACL / Menu / Role / Perm schema

- [ ] 3.1 加 `MenuDto`、`UiMenuOut`、`UiMenuTreeInfo`
- [ ] 3.2 加 `RoleDto`、`UiRoleOut`、`MenuRoleRelDto`、`RolePermRelDto`
- [ ] 3.3 加 `PermDto`、`UiPermOut`

## 4. User / App / Biz schema

- [ ] 4.1 加 `UserDto`、`UiUserOut`、`ChangePwdInfo`
- [ ] 4.2 加 `AppcdDto`、`UiAppcdOut`
- [ ] 4.3 加 `BizcdDto`、`UiBizcdOut`

## 5. Path operation $ref 切換

- [ ] 5.1 `Main` tag：`/login` 200 → `LoginUserInfo`；`/userInfo`、`/logininfosso` 200 → `UserInfo`／`LoginUserInfo`；`/logout` 200 → `CommonResponse`
- [ ] 5.2 `Menu` tag：POST/PUT body → `MenuDto`；GET list/single → `UiMenuOut`（list 用 array）
- [ ] 5.3 `Role` tag：POST/PUT `/roles` body → `RoleDto`；GET → `UiRoleOut`；子資源（`/menus`、`/perms`、`/menupopups`、`/permpopups`）回應走 `UiMenuOut`/`UiMenuTreeInfo`/`UiPermOut` array，PUT body 走 `MenuRoleRelDto`/`RolePermRelDto` array
- [ ] 5.4 `Perm` tag：POST/PUT body → `PermDto`；GET → `UiPermOut`
- [ ] 5.5 `User` tag：POST/PUT body → `UserDto`；GET → `UiUserOut`；`/userstest/test` 維持 `Generic`（測試端點）
- [ ] 5.6 `App` tag：POST/PUT body → `AppcdDto`；GET → `UiAppcdOut`
- [ ] 5.7 `Biz` tag：POST/PUT body → `BizcdDto`；GET → `UiBizcdOut`

## 6. 驗證

- [ ] 6.1 跑 `./scripts/check-api-catalog.sh`，確認 (method, path) 集合仍一致
- [ ] 6.2 yaml lint（VSCode `42Crunch.vscode-openapi` 或 `redocly lint`）通過
- [ ] 6.3 grep `Generic` 用量；確認本批次涵蓋之 path 已不再用 `Generic`（測試端點與檔案上傳除外）
