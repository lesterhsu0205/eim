#!/bin/bash
# PreToolUse hook: 防止敏感檔案被修改

set -e

INPUT=$(cat)
TOOL_NAME=$(echo "$INPUT" | jq -r '.tool_name // empty')
FILE_PATH=$(echo "$INPUT" | jq -r '.tool_input.file_path // empty')

if [[ "$TOOL_NAME" != "Write" && "$TOOL_NAME" != "Edit" ]]; then
    exit 0
fi

if [[ -z "$FILE_PATH" ]]; then
    exit 0
fi

FILENAME=$(basename "$FILE_PATH")

PROTECTED_PATTERNS=(
    ".env"
    ".env.local"
    ".env.production"
    "credentials.json"
    "secrets.json"
    "*.pem"
    "*.key"
    "*.p12"
    "*.jks"
    "id_rsa"
    "id_ed25519"
)

for pattern in "${PROTECTED_PATTERNS[@]}"; do
    if [[ "$FILENAME" == $pattern ]]; then
        echo "已阻擋: 不允許修改敏感檔案 '$FILENAME'，此檔案可能含有憑證或機密資訊。" >&2
        exit 2
    fi
done

if echo "$FILE_PATH" | grep -qiE "(secret|credential|password|private.?key)" 2>/dev/null; then
    echo "注意: 檔案路徑含有敏感關鍵字，請確認此操作是否有意為之。" >&2
fi

exit 0
