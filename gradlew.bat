@echo off
setlocal
set "DIR=%~dp0"
set "JAVACMD=%JAVA_HOME%\bin\java.exe"
if not defined JAVA_HOME set "JAVACMD=java"
"%JAVACMD%" -jar "%DIR%gradle\wrapper\gradle-wrapper.jar" %*
endlocal
