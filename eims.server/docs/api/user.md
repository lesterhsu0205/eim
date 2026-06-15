# User API

`UserController` — 使用者 CRUD。

| Method | Path | 說明 | Interceptor |
|---|---|---|---|
| GET | `/users` | 使用者清單 | applied |
| POST | `/users` | 新增使用者 | applied |
| PUT | `/users` | 修改使用者 | applied |
| GET | `/users/{userId}` | 取單筆 | applied |
| DELETE | `/users/{userId}` | 刪除 | applied |
| POST | `/userstest/test` | 測試端點 | ⛔ excluded |

詳見 [`openapi.yaml`](./openapi.yaml) tag `User`。
