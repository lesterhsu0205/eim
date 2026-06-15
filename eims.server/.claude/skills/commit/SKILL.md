---
name: commit
description: Git Commit 輔助。建立符合 Conventional Commits 規範的 commit。用於提交程式碼、產生 commit 訊息時使用。
disable-model-invocation: true
allowed-tools: Bash(git:*), Bash(pnpm lint:*)
---

# Git Commit 輔助

協助建立符合本專案規範的 commit。

## 使用

$ARGUMENTS

若未提供訊息，將分析 staged + unstaged 變更並建議 commit 訊息。

## Conventional Commits 格式

```
<type>(<scope>): <subject>

<body>
```

Type / Scope 對照表正本見 **`.claude/rules/git-workflow.md`「Commit 訊息格式」**（單一來源，不在此重列）。

## 訊息壓縮規則（caveman）

**Subject**
- ≤ 50 字（硬上限 72），無句點
- 中文 imperative：「新增 / 修正 / 移除 / 重構 / 升級」，不用「新增了 / 目前 / 現在 / 已經」
- `<type>(<scope>): <摘要>`，scope 必填（前端模組表）

**Body**
- 只在 why 非顯而易見時寫
- 破壞變更、security fix、資料遷移、revert **必寫**
- 72 字折行，bullet 用 `-` 不用 `*`
- 寫動機 / 限制 / 影響，不寫 diff 已表達的 what

**禁用**
- 「This commit」「目前」「現在」「I/we」「As requested」開頭
- emoji（除非使用者要求）
- `Co-Authored-By:` 行、任何 AI 署名

**對照**
- ❌ `feat(pages): 新增了一個使用者維護頁面，目前使用 DataTable 元件來處理 CRUD`
- ✅ `feat(pages): 新增 USER_MAINT 使用者維護頁`

- ❌ `fix: This commit fixes the 401 issue`
- ✅ `fix(services): 修正 ApiService 401 流程`

## 執行步驟

### 1. 預檢
```bash
git status
git diff --staged
git diff
git log -3 --oneline   # 對照近期風格
```

### 2. 執行 lint（建議）
```bash
pnpm lint
```
若有 error → 提示先修正再 commit。

### 3. 分析變更
- 識別變更類型與 scope
- 列出主要變更點

### 4. 建議訊息（範例）

```
feat(pages): 新增 USER_MAINT 使用者維護頁

- 新增 UserMaint.tsx，套用 DataTable
- 整合 UserService.loadData / delete
- 頁面內 `export const route = '/USER_MAINT'`（DynamicRoutes 自動掃）
```

```
fix(services): 修正 ApiService 401 處理流程

當回傳 messageCode 為 9997/9996/0402 時，
改觸發 auth:logout 自訂事件統一導向登入頁。
```

```
chore(deps): 升級 mantine 至 8.3.18
```

### 5. Pre-commit 檢查清單

對照 **git-workflow.md「Commit 前檢查清單」+「禁止事項」。

### 6. 執行 Commit
```bash
git add <檔案>
git commit -m "<訊息>"
```

## 禁止事項

- 不使用 `git commit -a`（避免一次帶入未審查的檔案）
- 不使用 `--no-verify`
- 不 commit `.env*` 真實值
- 不在 commit 訊息中使用 emoji（除非使用者要求）
