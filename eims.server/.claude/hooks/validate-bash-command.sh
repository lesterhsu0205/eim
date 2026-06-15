#!/bin/bash
# PreToolUse hook: 驗證 Bash 指令安全性

set -e

INPUT=$(cat)
TOOL_NAME=$(echo "$INPUT" | jq -r '.tool_name // empty')
COMMAND=$(echo "$INPUT" | jq -r '.tool_input.command // empty')

if [[ "$TOOL_NAME" != "Bash" ]]; then
    exit 0
fi

BLOCKED=false
MESSAGE=""

# 阻擋對根目錄或家目錄的 rm -rf
if echo "$COMMAND" | grep -qE "rm\s+-rf\s+(/|~|\$HOME|/Users|/home)" 2>/dev/null; then
    BLOCKED=true
    MESSAGE="已阻擋: 對根目錄或家目錄執行 rm -rf 風險過高"
fi

# 阻擋對 main/master 的強制推送
if echo "$COMMAND" | grep -qE "git\s+push.*(-f|--force).*\s+(main|master)" 2>/dev/null; then
    BLOCKED=true
    MESSAGE="已阻擋: 不允許對 main/master 執行 force push"
fi

# 阻擋刪除資料庫（需明確使用者確認）
if echo "$COMMAND" | grep -qiE "drop\s+(database|schema)" 2>/dev/null; then
    BLOCKED=true
    MESSAGE="已阻擋: 刪除資料庫需要明確的使用者確認"
fi

SUGGESTIONS=""

if echo "$COMMAND" | grep -qE "^\s*grep\s+" 2>/dev/null; then
    SUGGESTIONS="建議使用 'rg'（ripgrep）以獲得更好的效能"
fi

if echo "$COMMAND" | grep -qE "^\s*find\s+" 2>/dev/null; then
    SUGGESTIONS="建議使用 Glob 工具進行檔案搜尋"
fi

if [[ "$BLOCKED" == "true" ]]; then
    echo "$MESSAGE" >&2
    exit 2
fi

if [[ -n "$SUGGESTIONS" ]]; then
    echo "{\"hookSpecificOutput\":{\"hookEventName\":\"PreToolUse\",\"additionalContext\":\"$SUGGESTIONS\"}}"
fi

exit 0
