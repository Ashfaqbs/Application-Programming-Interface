@echo off
cd /d %~dp0

echo === Committing all untracked files ===

git add mvnw
git commit -m "Add Maven Wrapper script"

git add mvnw.cmd
git commit -m "Add Maven Wrapper batch file"

git add pom.xml
git commit -m "Initialize Maven project with dependencies"

git add src/main/java/com/example/ExceptionHandlingSamplesApplication.java
git commit -m "Add Spring Boot main class"

git add commit-all.bat
git commit -m "Add batch script to commit project files with messages"

echo === Pushing changes to origin/main ===
git push origin main

echo.
echo === All files committed and pushed ===
pause
