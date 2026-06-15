# CommCode API

`CommCodeController` — 共用代碼 CRUD 與查詢。

| Method | Path | 說明 | Interceptor |
|---|---|---|---|
| GET | `/codes` | 清單 | ⛔ excluded |
| POST | `/codes` | 新增 | ⛔ excluded |
| PUT | `/codes` | 修改 | ⛔ excluded |
| GET | `/codes/common/{cdId}` | 共用代碼 | applied |
| GET | `/codes/{cdId}` | 取單筆 | applied |
| DELETE | `/codes/{cdId}` | 刪除 | applied |

詳見 [`openapi.yaml`](./openapi.yaml) tag `CommCode`。
