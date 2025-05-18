@echo off

:: Crear directorios necesarios
if not exist bin mkdir bin
if not exist lib mkdir lib

:: Descargar la dependencia JSON si no existe
if not exist lib\json-20231013.jar (
    powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/json/json/20231013/json-20231013.jar' -OutFile 'lib\json-20231013.jar'"
)

:: Compilar el código fuente
javac -d bin -cp "lib/*" src\main\*.java src\gameObjects\*.java src\graphics\*.java src\input\*.java src\states\*.java

:: Copiar recursos
xcopy /E /I /Y res bin\res

:: Crear el archivo MANIFEST si no existe
if not exist manifest.mf (
    echo Main-Class: main.Window>manifest.mf
    echo.>>manifest.mf
)

:: Crear el archivo JAR
jar cfm SpaceShipGame.jar manifest.mf -C bin .

echo Compilación completada. Ejecuta 'java -jar SpaceShipGame.jar' para iniciar el juego.
pause