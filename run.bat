@echo off
set ROOT=%~dp0
set SRC=%ROOT%src
set OUT=%ROOT%out
if not exist "%OUT%" mkdir "%OUT%"
dir /s /b "%SRC%\*.java" > "%OUT%\sources.txt"
javac -encoding UTF-8 -d "%OUT%" @%OUT%\sources.txt
echo Starting server at http://localhost:8080 ...
java -cp "%OUT%" com.example.survey.Main