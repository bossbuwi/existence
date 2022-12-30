@echo off
echo.
echo.

echo Starting Existence's launcher..
echo.

echo Reading application home directory..
set home=%cd%
echo %home% is the app's home directory.
echo.

echo Reading system variable EXISTENCE_PATH..
echo %EXISTENCE_PATH%
echo.

echo The Java version on environment variable EXISTENCE_PATH will be used.
echo The minimum Java version needed is Java 17.
echo Earlier versions may cause runtime problems.
echo.
echo Checking Java version..
echo.
%EXISTENCE_PATH%\java -version
echo.

echo This launcher can start the server in either the production[P] or test[T] environment.
echo Each environment will use its own database.
echo.
choice /C PTE /M "Plese select the environment in which the server will be launched (or exit[E])"
if %errorlevel%==1 (
	set env=prod
	set state=production
)
if %errorlevel%==2 (
	set env=test
	set state=test
)
if %errorlevel%==3 (
	goto skip
)
echo.

echo Server will be launched in a %state% environment.
echo The app will be using database db_%state%
echo.

echo Launching server in a new window.
start "Existence 2.0" cmd.exe /k %EXISTENCE_PATH%\java -jar "%home%\existence-2.0draco.jar" --spring.profiles.active=%env%
echo.

echo Logs may be found at %home%\logs
cd %home%
echo.
goto end

: skip
echo.
echo The operation has been cancelled.

: end
echo This window may now be closed.