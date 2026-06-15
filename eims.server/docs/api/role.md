# Role API

`RoleController` — 角色 CRUD 與 menu / perm 關聯。

| Method | Path | 說明 |
|---|---|---|
| GET | `/roles` | 角色清單 |
| POST | `/roles` | 新增角色 |
| PUT | `/roles` | 修改角色 |
| GET | `/roles/{roleId}` | 取單筆 |
| DELETE | `/roles/{roleId}` | 刪除 |
| GET | `/roles/{roleId}/menus` | 角色對應之選單清單 |
| GET | `/roles/{roleId}/menupopups` | popup 用選單清單 |
| PUT | `/roles/{roleId}/menus` | 設定角色之選單 |
| DELETE | `/roles/{roleId}/menus/{menuId}` | 移除單一選單 |
| GET | `/roles/{roleId}/perms` | 角色對應之權限清單 |
| GET | `/roles/{roleId}/permpopups` | popup 用權限清單 |
| PUT | `/roles/{roleId}/perms` | 設定角色之權限 |
| DELETE | `/roles/{roleId}/perms/{permId}` | 移除單一權限 |

詳見 [`openapi.yaml`](./openapi.yaml) tag `Role`。
