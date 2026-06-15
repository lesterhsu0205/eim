# Msglayout API

`MsglayoutController` — 訊息版面 CRUD、暫存、條件查詢、影響分析、Excel 匯出、檔案上傳、外部訊息、msgId 產生。

| Method | Path | 說明 | Content-Type |
|---|---|---|---|
| GET | `/msglayouts` | 清單 | json |
| POST | `/msglayouts` | 新增 | json |
| PUT | `/msglayouts` | 修改 | json |
| GET | `/msglayouts/{msgLayoutId}` | 取單筆 | json |
| DELETE | `/msglayouts/{msgLayoutId}` | 刪除 | json |
| GET | `/msglayouts/{msgLayoutId}/effects` | 變更影響分析 | json |
| POST | `/msglayoutstemp` | 暫存 | json |
| PUT | `/msglayoutstemp` | 更新暫存 | json |
| POST | `/msglayoutslist` | 條件查詢清單 | json |
| POST | `/msglayouts/excelexport` | Excel 匯出 | **application/octet-stream** |
| POST | `/msglayouts/fileuploads` | 檔案上傳 | **multipart/form-data** |
| GET | `/msglayouts/extrnlMsgs` | 外部訊息清單 | json |
| POST | `/msglayouts/msgidcreate` | 產生 msg id | json |

詳見 [`openapi.yaml`](./openapi.yaml) tag `Msglayout`。
