@echo off
setlocal enabledelayedexpansion

rem Set source and output directories
set SRC_DIR=src
set BIN_DIR=bin
set JAR_DIR=jar
set CLASSPATH=%BIN_DIR%;%JAR_DIR%\mysql.jar

rem Compile the Java source files
echo Compiling Java files...
javac -d %BIN_DIR% -cp %JAR_DIR%\mysql.jar %SRC_DIR%\Hostel\*.java

rem Check if compilation was successful
if %ERRORLEVEL% neq 0 (
    echo Compilation failed!
    pause
    exit /b
)

rem Run the LoginPage Java program
echo Running the LoginPage program...
java -cp %CLASSPATH% Hostel.Main


