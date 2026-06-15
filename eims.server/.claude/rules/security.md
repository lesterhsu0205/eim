# 前端安全規範

針對 SPA 與瀏覽器環境的常見風險。

## 1. 不在前端儲存機敏資訊

- **不寫死任何 token / API key**（會被打包進 bundle）
- 設定值放 `.env.*`，且 `.env*` 必須在 `.gitignore`
- localStorage 僅儲存非敏感識別資訊（如 user id、token）。Token 存活時間應由後端控制

## 2. XSS 防護

### React 預設安全
- React 會自動 escape JSX 文字內容

### 高風險 API
- **避免 `dangerouslySetInnerHTML`**。若必須使用，需先用 sanitizer（如 DOMPurify）淨化
- **react-markdown** 已加 `remark-gfm`：避免直接渲染未受信任的 raw HTML

```tsx
// Bad
<div dangerouslySetInnerHTML={{ __html: userInput }} />

// Good
<ReactMarkdown remarkPlugins={[remarkGfm]}>{userInput}</ReactMarkdown>
```

### URL 注入
- `<a href={url}>` 中的 URL 若來自使用者，必須驗證 protocol（禁止 `javascript:`）

## 3. API 通訊

> API 層正本：攔截器機制、回應格式、驗證皆定義於此。

### Axios 攔截器（`src/services/ApiService.ts`）

- **請求**：自動加 `Authorization: Bearer {jwt_token}` 標頭
- **回應**：`messageCode !== '00000'` 轉 `ApiError` 拋出
- `auth:logout` 自訂事件處理認證錯誤（messageCode `9997`/`9996`/`0402` 及 HTTP 401）

### 請求參數
- URL 不傳遞密碼、token（會出現在 server log / referer）
- 大量資料用 POST body

### 回應格式

```typescript
{
  messageCode: string      // '00000' = 成功
  messageDesc: string
  messageContent: T | null // 實際資料
}
```

## 4. 依賴套件安全

```bash
# 定期檢查
pnpm audit

# 升級已知漏洞
pnpm update <package>
```

- 新增依賴前確認 weekly downloads、最後更新時間、有無 CVE
- `pnpm-lock.yaml` 必須 commit（鎖版本）

## 5. CSP 與部署

- 生產 nginx 應設定 CSP header（已有 `nginx.conf`，可檢視）
- 不開啟 `*` CORS（後端責任，但前端要求合理 origin）

## 6. 環境變數的可見性

⚠️ Vite 的 `VITE_*` 變數**會被打包到前端 bundle**，所有人都看得到。

| 可放 VITE_* | 不可放 VITE_* |
|------------|--------------|
| API base URL | API secret key |
| Feature flags | 第三方 token（Google, OAuth secret） |
| 公開設定值 | 加密金鑰 |

機敏值應由後端代理或 OAuth flow 取得。

## 7. 登入狀態

- `AuthContext` 統一管理，**不要分散在各元件用 localStorage 直接讀**
- Logout 必須：
  1. 清除 localStorage 中 token / user
  2. 清除任何快取資料
  3. 導向 `/login`

## 8. 檔案下載/上傳

- 上傳檔名需 sanitize（前端先過濾，後端再驗證）
- 下載連結不要直接信任後端回傳的 URL（檢查 origin）

## 禁止清單

- 禁止在 git commit 中包含 `.env` 真實值
- 禁止 `dangerouslySetInnerHTML` 渲染未淨化內容
- 禁止使用 `eval` / `Function()` 動態執行字串
- 禁止 `Math.random()` 用於安全用途（用 `crypto.getRandomValues`）
- 禁止把 user input 直接作為 URL 的 protocol
