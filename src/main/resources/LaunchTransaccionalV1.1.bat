@echo off
setlocal
:: Comprueba si el script se está ejecutando con privilegios de administrador
net session >nul 2>&1
if %errorLevel% == 0 (
    goto :ADMIN
) else (
    echo Solicitud de privilegios de administrador...
    goto :UACPrompt
)

:UACPrompt
:: Solicita privilegios de administrador y reejecuta el script
echo Set UAC = CreateObject^("Shell.Application"^) > "%temp%\getadmin.vbs"
echo UAC.ShellExecute "cmd.exe", "/c ""%~f0""", "", "runas", 1 >> "%temp%\getadmin.vbs"
"%temp%\getadmin.vbs"
del "%temp%\getadmin.vbs"
exit /B

:ADMIN
:: Código del script con privilegios de administrador

REM Obtiene el valor actual de la variable de entorno EJECUTABLE_JAR
set "currentValue=%EJECUTAR_JAR%"
set "currentValueOld=%EJECUTAR_JAR%"

REM Comprueba si la variable EJECUTAR_JAR está definida
if "%currentValue%"=="" (
    echo La variable de entorno EJECUTAR_JAR no está definida. Estableciendo valor inicial a JAR-PYME.
    setx EJECUTAR_JAR "JAR-PYME" /M
    echo Variable de entorno EJECUTAR_JAR creada con el valor "JAR-PYME".
) else (
    REM Cambia el valor de EJECUTAR_JARsegún su valor actual
    if "%currentValue%" == "JAR-PYME" (
        echo Variable de entorno EJECUTAR_JAR ya tiene el valor de %EJECUTAR_JAR%.
    ) else (
	setx EJECUTAR_JAR "JAR-PYME" /M
         echo Variable de entorno EJECUTAR_JAR cambiada de "%currentValueOld%" a "JAR-PYME".
    )
)

pause
endlocal