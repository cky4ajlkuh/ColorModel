@echo off
setlocal enabledelayedexpansion

echo === Check Java version ===
java -version

echo === Check Maven version ===
call mvn -v

echo === Install ===
call mvn clean package

pause