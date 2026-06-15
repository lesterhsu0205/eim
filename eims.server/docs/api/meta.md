# Meta API

`MetaController` — Meta CRUD、影響分析、同步。

| Method | Path | 說明 |
|---|---|---|
| GET | `/metas` | 清單 |
| POST | `/metas` | 新增 |
| PUT | `/metas` | 修改 |
| GET | `/metas/{metaEngNm}` | 取單筆 |
| DELETE | `/metas/{metaEngNm}` | 刪除 |
| GET | `/metas/effects` | Meta 變更影響分析 |
| GET | `/metas/syncs` | Meta 同步 |

詳見 [`openapi.yaml`](./openapi.yaml) tag `Meta`。
