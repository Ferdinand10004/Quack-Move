@echo off
setlocal
powershell -NoProfile -ExecutionPolicy Bypass -Command "$url = 'https://raw.githubusercontent.com/space112921-cell/QuackMove/entrega-3/gradle/wrapper/gradle-wrapper.jar'; Invoke-WebRequest -Uri $url -OutFile 'gradle/wrapper/gradle-wrapper.jar'"
if errorlevel 1 (echo No se pudo descargar Gradle Wrapper. & pause & exit /b 1)
echo Gradle Wrapper instalado. Ya puedes abrir el proyecto en Android Studio.
pause
