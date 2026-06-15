# Trx API

`TrxController` — 交易 CRUD。

| Method | Path | 說明 | Interceptor |
|---|---|---|---|
| GET | `/trxs` | 清單 | ⛔ excluded |
| POST | `/trxs` | 新增 | ⛔ excluded |
| PUT | `/trxs` | 修改 | ⛔ excluded |
| GET | `/trxs/{trxCd}` | 取單筆 | ⛔ excluded（`/trxs/**`） |
| DELETE | `/trxs/{trxCd}` | 刪除 | ⛔ excluded（`/trxs/**`） |

詳見 [`openapi.yaml`](./openapi.yaml) tag `Trx`。
