@echo off
:: setlocal EnableDelayedExpansion
set home=%cd%

echo This utility can backup either the production[P] or the test[T] database.
choice /C PT /M "Specify the database to backup"
if %errorlevel%==1 set dbname=db_existence
if %errorlevel%==2 set dbname=db_test

echo Starting backup process for database: %dbname%
echo Active user profile: postgres
echo You might be prompted to input the password later in the process.

:: store the current time
set tm=%time:~0,2%%time:~3,2%
set tm=%tm: =0%

:: store the current date
set dt=%date%
set dd=%dt:~4,2%
set mm=%dt:~7,2%
set yy=%dt:~-4%

:: combine the date and time to create a unique filename
set filename=%yy%%mm%%dd%_%tm%_%dbname%.tar

:: check if backup directory exists
:: if not, create it
if not exist backups/%dbname%/NUL (
	echo Target directory not found.
	echo Creating the required directories.
	mkdir backups\%dbname%
	echo Directories created.
)

echo A backup named %filename% will be created on directory backups\%dbname%
cd backups/%dbname%
:: default postgres command for backing up a database
call pg_dump -U postgres -F t %dbname% > %filename%

: exit
echo Utility finished.
cd %home%