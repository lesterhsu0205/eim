# Menu API

`MenuController` — 選單 CRUD。

| Method | Path | 說明 | Interceptor |
|---|---|---|---|
| GET | `/menus` | 選單清單 | ⛔ excluded |
| POST | `/menus` | 新增選單 | ⛔ excluded |
| PUT | `/menus` | 修改選單 | ⛔ excluded |
| GET | `/menus/{menuId}` | 取單筆選單 | applied |
| DELETE | `/menus/{menuId}` | 刪除選單 | applied |

詳見 [`openapi.yaml`](./openapi.yaml) tag `Menu`。
