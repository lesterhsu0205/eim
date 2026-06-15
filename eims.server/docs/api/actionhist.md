# Actionhist API

`ActionhistController` — 操作歷程查詢與新增。

| Method | Path | 說明 | Query 參數 |
|---|---|---|---|
| GET | `/actionhists` | 操作歷程查詢 | `hstDscd`, `itemId`, `workCttCd`, `workDttmFrom`, `workDttmTo` |
| POST | `/actionhists` | 新增操作歷程 | — |

詳見 [`openapi.yaml`](./openapi.yaml) tag `Actionhist`。
