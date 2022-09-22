@echo off
set home=%cd%

echo This launcher can start the server in either the production[P] or test[T] environment.
choice /C PT /M "Select the environment in which the server will be launched"
if %errorlevel%==1 (
	set env=prod
	set state=production
)
if %errorlevel%==2 (
	set env=test
	set state=test
)

echo Server will be launched in a %state% environment.
echo Setting Java version to 17.
cd C:\Program Files\Java\jdk-17\bin

echo Launching server in a new window.
start "Existence 1.0" java -jar "%home%\existence-1.0cassiopeia.jar" --spring.profiles.active=%env% --spring.config.location=%home%\config\ --logging.file.name=%home%\log\%env%\existence.log

echo Logs may be found at %home%\logs
cd %home%

echo This window may now be closed.