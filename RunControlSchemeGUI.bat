@echo off
echo Compiling and running Control Scheme GUI...
echo.

cd /d "%~dp0"

REM Compile the GUI
javac -d out TeamCode\src\main\java\org\firstinspires\ftc\teamcode\Util\DriverUtil\ControlSchemeGUI.java

REM Run the GUI
java -cp out org.firstinspires.ftc.teamcode.Util.DriverUtil.ControlSchemeGUI

pause

