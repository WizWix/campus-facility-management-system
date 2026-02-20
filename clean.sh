#!/bin/bash

SCR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

echo "Cleaning project directories in $SCR..."

TARGETS=(
  ".frontend-gradle-plugin"
  ".gradle"
  "build"
  "frontend/dist"
  "frontend/node_modules"
  "node"
  "pnpm-*.yaml"
  "src/main/resources/static/assets"
)

for ITEM in "${TARGETS[@]}"; do
  rm -rf $SCR/$ITEM 2>/dev/null
done

echo "Cleanup complete."
