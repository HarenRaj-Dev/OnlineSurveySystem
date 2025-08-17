#!/usr/bin/env bash
set -e
ROOT="$(cd "$(dirname "$0")" && pwd)"
SRC="$ROOT/src"
OUT="$ROOT/out"
mkdir -p "$OUT"
find "$SRC" -name "*.java" > "$OUT/sources.txt"
javac -encoding UTF-8 -d "$OUT" @${OUT}/sources.txt
echo "Starting server at http://localhost:8080 ..."
java -cp "$OUT" com.example.survey.Main