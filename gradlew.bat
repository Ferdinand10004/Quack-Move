@echo off
setlocal
if not exist "%~dp0gradle\wrapper\gradle-wrapper.jar" (
  call "%~dp0instalar-wrapper.bat"
)
java -classpath "%~dp0gradle\wrapper\gradle-wrapper.jar" org.gradle.wrapper.GradleWrapperMain %*
