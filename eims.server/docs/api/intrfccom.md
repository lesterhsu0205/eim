# Intrfccom API

`IntrfccomController` — 介面 CRUD、部署 / 重新部署 / 歷程、檔案上傳 / 匯入 / 匯出、layoutdiff、terminal、intrfcId 產生。

| Method | Path | 說明 | Content-Type | Interceptor |
|---|---|---|---|---|
| GET | `/intrfccoms` | 清單 | json | applied |
| POST | `/intrfccoms` | 新增 | json | applied |
| PUT | `/intrfccoms` | 修改 | json | applied |
| GET | `/intrfccoms/{intrfcId}` | 取單筆 | json | applied |
| DELETE | `/intrfccoms/{intrfcId}` | 刪除 | json | applied |
| GET | `/interfaceListApi` | 介面清單（外部 API） | json | applied |
| POST | `/intrfccoms/deploy` | 部署 | json | applied |
| POST | `/intrfccoms/deployAll` | 全部部署 | json | applied |
| GET | `/intrfccoms/deployhistorys` | 部署歷程 | json | applied |
| GET | `/intrfccoms/deployhistoryresults` | 部署結果 | json | applied |
| POST | `/intrfccoms/redeploy` | 重新部署 | json | applied |
| GET | `/intrfccoms/deploy/{intrfcId}` | 單筆部署狀態 | json | applied |
| GET | `/intrfccoms/deploy/terminal` | Terminal 介面查詢 | **text/plain** | ⛔ excluded |
| POST | `/intrfccoms/intrfcidcreate` | 產生 intrfc id | json | applied |
| GET | `/intrfccoms/layoutdiff` | layout 差異 | json | applied |
| POST | `/intrfccoms/fileuploads` | 檔案上傳 | **multipart/form-data** | applied |
| POST | `/intrfccoms/import/intrfcfiles` | 匯入介面檔案 | **multipart/form-data** | applied |
| POST | `/intrfccoms/import/definition` | 匯入介面定義 | **multipart/form-data** | applied |
| POST | `/intrfccoms/excelexport` | Excel 匯出 | **application/octet-stream** | applied |
| GET | `/intrfccoms/export/intrfcinfos` | 介面資訊匯出 | **application/octet-stream** | applied |

`/intrfccoms/deploy/terminal` 註記：目前 controller body 全段註解掉、`return null`，非 streaming。若回復實作預期為 text/plain XML 字串。

詳見 [`openapi.yaml`](./openapi.yaml) tag `Intrfccom`。
