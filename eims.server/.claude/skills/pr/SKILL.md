---
name: pr
description: 建立 Pull Request，含變更摘要與測試計畫。用於建立 PR、準備 code review 時使用。
disable-model-invocation: true
allowed-tools: Bash(git:*), Bash(gh:*), Bash(pnpm:*)
---

# 建立 Pull Request

協助建立完整的前端 PR。

## 選項

$ARGUMENTS

- `base`：目標分支（預設 develop）
- `title`：PR 標題（不指定 → 從 commits 推導）
- `draft`：草稿 PR

## 執行步驟

### 1. 分支狀態
```bash
git branch --show-current
git status
git fetch origin
git log origin/<base>..HEAD --oneline
git diff origin/<base>...HEAD --stat
```

### 2. 本機驗證
建議先跑：
```bash
pnpm lint
pnpm build:stg   # 確認可建置
```

### 3. 分析變更
- 涉及哪些頁面 / 元件 / 服務
- 是否新增 / 刪除路由（頁面 `export const route` 增減）
- 是否新增依賴（pnpm-lock.yaml 變動）
- 是否影響 .env 設定

### 4. PR 模板

```markdown
## Summary

[1-3 行說明此 PR 目的]

## Changes

- [變更項目 1]
- [變更項目 2]

## Type

- [ ] Bug fix
- [ ] New feature
- [ ] Breaking change
- [ ] Refactor
- [ ] Docs / Chore

## Affected Routes / Pages

- [ ] 新增路由：`/XXX`（頁面內已 `export const route`）
- [ ] 修改頁面：XXX

## Test Plan

- [ ] `pnpm lint` 通過
- [ ] `pnpm build:stg` 成功
- [ ] 本機 `pnpm local` 走過主要流程
- [ ] 主要瀏覽器測試（Chrome / Safari）
- [ ] 響應式檢查（375 / 768 / 1440）
- [ ] 鍵盤操作可用

## Screenshots

[UI 變更請附截圖，含桌機與行動版]

## Notes for Reviewer

[特別需要審查者注意的地方、設計取捨]
```

### 5. 建立 PR
```bash
git push -u origin <branch>

gh pr create \
  --base develop \
  --title "feat(pages): 新增 USER_MAINT 頁面" \
  --body-file pr-body.md
```

或使用 heredoc：
```bash
gh pr create --title "..." --body "$(cat <<'EOF'
## Summary
...
EOF
)"
```

### 6. 草稿 PR（早期回饋用）
```bash
gh pr create --draft --base develop ...
```

## 結果報告

- PR 連結
- 變更摘要
- 建議的審查者
- 是否需要 QA 測試特定場景
