#!/usr/bin/env bash
# check-api-catalog.sh
# 比對 src/main/java/eims/web/controller/*.java 中之 (method, path) 集合與
# docs/api/openapi.yaml `paths` 區段是否一致。差異時以非零碼結束。
#
# 使用：./scripts/check-api-catalog.sh
# 依賴：bash、grep、sed、sort、comm、python3（解析 yaml）

set -euo pipefail

REPO_ROOT="$(cd "$(dirname "$0")/.." && pwd)"
CONTROLLER_DIR="${REPO_ROOT}/src/main/java/eims/web/controller"
OPENAPI="${REPO_ROOT}/docs/api/openapi.yaml"
DOCS_DIR="${REPO_ROOT}/docs/api"
TMP_DIR="$(mktemp -d)"
trap 'rm -rf "${TMP_DIR}"' EXIT

if [[ ! -d "${CONTROLLER_DIR}" ]]; then
  echo "[check-api-catalog] controller dir not found: ${CONTROLLER_DIR}" >&2
  exit 2
fi
if [[ ! -f "${OPENAPI}" ]]; then
  echo "[check-api-catalog] openapi.yaml not found: ${OPENAPI}" >&2
  exit 2
fi

# -----------------------------------------------------------------------------
# 1. 從 controller 萃取 (METHOD path)，由 python3 解析以避開 BSD awk/sed 限制
# -----------------------------------------------------------------------------
controller_pairs="${TMP_DIR}/controller.txt"
python3 - "${CONTROLLER_DIR}" > "${controller_pairs}" <<'PY'
import os, re, sys, glob

ctrl_dir = sys.argv[1]
re_rm = re.compile(r'@RequestMapping\s*\(([^)]*)\)')
re_short = re.compile(r'@(Get|Post|Put|Delete|Patch)Mapping\s*\(\s*"([^"]+)"')
re_value = re.compile(r'value\s*=\s*"([^"]+)"')
re_method = re.compile(r'method\s*=\s*RequestMethod\.([A-Z]+)')
re_path_param = re.compile(r'\{([A-Za-z0-9_]+)\s*:[^}]+\}')

pairs = set()
for f in sorted(glob.glob(os.path.join(ctrl_dir, "*.java"))):
    with open(f, encoding="utf-8") as fh:
        for line in fh:
            stripped = line.lstrip()
            if stripped.startswith("//") or stripped.startswith("*"):
                continue
            m = re_short.search(line)
            if m:
                method, path = m.group(1).upper(), m.group(2)
                path = re_path_param.sub(r'{\1}', path)
                pairs.add(f"{method} {path}")
                continue
            m = re_rm.search(line)
            if m:
                inside = m.group(1)
                pv = re_value.search(inside)
                pm = re_method.search(inside)
                if pv:
                    path = re_path_param.sub(r'{\1}', pv.group(1))
                    method = pm.group(1) if pm else "GET"
                    pairs.add(f"{method} {path}")

for p in sorted(pairs):
    print(p)
PY

# -----------------------------------------------------------------------------
# 2. 從 openapi.yaml 萃取 (METHOD path)
# -----------------------------------------------------------------------------
openapi_pairs="${TMP_DIR}/openapi.txt"
python3 - "${OPENAPI}" > "${openapi_pairs}" <<'PY'
import sys, re

with open(sys.argv[1], encoding="utf-8") as fh:
    text = fh.read()

# 簡易解析：找 paths: 區段，path 開頭 `^  /`、method 開頭 `^    (get|post|...)`：
pairs = set()
in_paths = False
current_path = None
re_path = re.compile(r'^  (/[^:]+):\s*$')
re_method = re.compile(r'^    (get|post|put|delete|patch):\s*$')
for line in text.splitlines():
    if line.startswith("paths:"):
        in_paths = True
        continue
    if in_paths and re.match(r'^[A-Za-z]', line):
        in_paths = False
        continue
    if not in_paths:
        continue
    mp = re_path.match(line)
    if mp:
        current_path = mp.group(1)
        continue
    mm = re_method.match(line)
    if mm and current_path:
        pairs.add(f"{mm.group(1).upper()} {current_path}")

for p in sorted(pairs):
    print(p)
PY

# -----------------------------------------------------------------------------
# 3. 比對
# -----------------------------------------------------------------------------
status=0

missing_in_openapi="$(comm -23 "${controller_pairs}" "${openapi_pairs}" || true)"
extra_in_openapi="$(comm -13 "${controller_pairs}" "${openapi_pairs}" || true)"

if [[ -n "${missing_in_openapi}" ]]; then
  echo "[check-api-catalog] controller 中存在但 openapi.yaml 缺少之 endpoint："
  echo "${missing_in_openapi}" | sed 's/^/  - /'
  status=1
fi

if [[ -n "${extra_in_openapi}" ]]; then
  echo "[check-api-catalog] openapi.yaml 中存在但 controller 找不到之 endpoint："
  echo "${extra_in_openapi}" | sed 's/^/  - /'
  status=1
fi

# -----------------------------------------------------------------------------
# 4. 領域 md 存在與含 path token 之粗略檢查
# -----------------------------------------------------------------------------
expected_domains=(main menu role perm user app biz commcode enc mask trx srsys depolysys extrnlinst mappingfunc meta msglayout intrfccom actionhist)
for d in "${expected_domains[@]}"; do
  f="${DOCS_DIR}/${d}.md"
  if [[ ! -f "${f}" ]]; then
    echo "[check-api-catalog] 缺少領域文件：docs/api/${d}.md"
    status=1
    continue
  fi
  if ! grep -qE "\`/" "${f}"; then
    echo "[check-api-catalog] 領域文件 ${d}.md 不含任何 path token"
    status=1
  fi
done

if [[ "${status}" -eq 0 ]]; then
  echo "[check-api-catalog] OK：controller 與 openapi.yaml 之 (method, path) 集合一致。"
fi

exit "${status}"
