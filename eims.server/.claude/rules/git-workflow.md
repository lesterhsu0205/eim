# Git 分支與提交規範

## 分支策略

| 類型 | 命名格式 | 範例 |
|------|---------|------|
| 功能 | `feature/<description>` | `feature/user-maintain` |
| 修復 | `fix/<description>` | `fix/login-redirect` |
| 熱修復 | `hotfix/<description>` | `hotfix/api-token-leak` |
| 發布 | `release/<version>` | `release/1.2.0` |

## Commit 訊息格式

```
<type>(<scope>): <subject>

<body>
```

### Type
| Type | 說明 |
|------|------|
| `feat` | 新功能 |
| `fix` | 錯誤修復 |
| `docs` | 文件變更 |
| `style` | 格式調整（CSS / Biome） |
| `refactor` | 重構 |
| `perf` | 效能改善 |
| `test` | 測試 |
| `chore` | 雜項（依賴、設定） |
| `build` | 建置設定（vite、biome、tsconfig） |

### Scope（前端模組）
- `pages` — 頁面元件
- `components` — 共用元件
- `services` — API 服務
- `context` — Context Provider
- `models` — 型別定義
- `utils` — 工具函式
- `theme` — 主題/樣式
- `routes` — 路由設定
- `env` — 環境變數
- `deps` — 依賴套件

### 範例
```
feat(pages): 新增 USER_MAINT 使用者維護頁

- 新增 UserMaint.tsx，整合 DataTable
- 對接 UserService.loadData / delete
- 頁面內 `export const route = '/USER_MAINT'`（DynamicRoutes 自動掃）
```

```
fix(services): 修正 ApiService 401 處理

當回傳 messageCode 9997 時觸發 auth:logout 事件，
導向登入頁。
```

```
chore(deps): 更新 mantine 至 8.3.18
```

## Commit 訊息格式規定

- **禁止**在 commit 訊息加入 `Co-Authored-By:` 行
- commit 訊息只包含 type(scope): subject 與 body，不加任何 AI 署名

## 禁止事項

- 禁止直接 push 到 `main` / `master`
- 禁止 force push 到共享分支
- 禁止提交 `.env*` 真實金鑰
- 禁止提交 `node_modules/`、`dist/`、`stats.html`
- 禁止提交 IDE 設定（`.vscode/`、`.idea/`）

## .gitignore 必須包含

```gitignore
# Dependencies
node_modules/

# Build
dist/
stats.html

# Env
.env
.env.local
.env.*.local

# Logs
*.log
npm-debug.log*
pnpm-debug.log*

# Editor
.vscode/
.idea/
.DS_Store
*.swp
```

## Commit 前檢查清單

- [ ] `pnpm lint` 通過
- [ ] 無 `console.log` / `console.debug` 殘留（hook 會提醒）
- [ ] 無 `any` 型別（hook 會提醒）
- [ ] 無 `env` 檔
- [ ] commit 訊息符合規範
