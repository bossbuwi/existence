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

set tm=%time:~0,2%%time:~3,2%
set tm=%tm: =0%

set dt=%date%
set dd=%dt:~4,2%
set mm=%dt:~7,2%
set yy=%dt:~-4%

set filename=%yy%%mm%%dd%_%tm%_%dbname%.tar

if not exist backups/%dbname%/NUL (
	echo Target directory not found.
	echo Creating the required directories.
	mkdir backups\%dbname%
	echo Directories created.
)

echo A backup named %filename% will be created on directory backups\%dbname%
cd backups/%dbname%
call pg_dump -U postgres -F t db_existence > %filename%

: exit
echo Utility finished.
cd %home%