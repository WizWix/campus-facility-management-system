@echo off
chcp 65001 >nul
echo ============================================
echo   한빛대학교 캠퍼스 시설관리 시스템
echo ============================================
echo.
echo [필수 조건]
echo   1. Java 21 이상 설치
echo   2. MySQL 8.x 실행 중 (localhost:3306, root/mysql)
echo   3. MySQL에 campus DB 생성 (CREATE DATABASE campus;)
echo.
echo [안내] 첫 실행 시 Gradle/Bun 다운로드로 2~3분 걸릴 수 있습니다.
echo        이후 실행부터는 약 20~30초 소요됩니다.
echo.
echo 서버가 시작되면 브라우저에서 접속:
echo   → http://localhost:8000
echo.
echo [테스트 계정]
echo   일반 사용자: 26000001 / 123
echo   관리자:      26999999 / 123
echo.
echo ============================================
echo.

set SPRING_PROFILES_ACTIVE=dev
call gradlew.bat bootRun --args="--server.port=8000"

pause
