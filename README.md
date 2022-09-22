# Existence

### Project Details

**Framework:** [Spring Boot](https://spring.io/projects/spring-boot) version 2.6.7  
**Status:** Active - Ongoing  
**Last Release Date:** 24 September 2022  
**Latest Version:** v2.0cassiopeia  
**Author:** [bossbuwi](https://github.com/bossbuwi)

_This project contains sources from the late project [reality](https://github.com/bossbuwi/reality),
serving as the de facto frontend._

***

### Environment Setup
_The below steps must be done in order for first time project setup._

**Tools Needed:**
* Java IDE, preferably [IntelliJ IDEA](https://www.jetbrains.com/idea/). _Note that if other IDEs are used, there might be additional setup needed._
* [PostgreSQL](https://www.postgresql.org/download/) installation, at least version 13. **pgAdmin** must be included in the installation.
* [node.js](https://nodejs.org/en/) installation at version 16.17.0. Other versions may work but are not guaranteed.  This is required by the frontend.
* A tool for testing REST APIs, preferably [Postman](https://www.postman.com/downloads/).
* A simple but capable text editing tool, preferably [Visual Studio Code](https://code.visualstudio.com/download). _Note that if the IntelliJ IDEA used is the Ultimate version, this may not be needed._

**Setting Up Database for Development**
1. Connect to the postgres installation using pgAdmin. It will ask for the master password. This is set during the installation of postgres.
2. On the left-hand side, there is a list of servers. A default server usually named _Postgres_ is automatically created. Connect to it. Again, it will prompt you for the master password.
3. Right-click the server's _Databases_ group, then _Create > Database.._
4. Enter the following details below on the _General_ tab.
    - Database: db_dev
    - Owner: postgres (this is automatically created)
5. Go to the _Security_ tab and add a _Privilege_ by clicking the "+" sign on the _Privileges_ group. Enter the following details for the privilege.
    - Grantee: postgres
    - Privileges: tick the checkbox beside _All_
    - Grantor: postgres
6. Go to the _SQL_ tab. The statement shown should at least be close to the statement below.  
```  
    CREATE DATABASE db_dev  
    WITH  
    OWNER = postgres  
    ENCODING = 'UTF8'  
    CONNECTION LIMIT = -1;  
    GRANT ALL ON DATABASE db_existence TO postgres;  
```
7. Click _Save_ to finish the database creation. A new database, _db_existence_, should now be visible on the _Databases_ group.

**Setting Up Backend Sources for Development**
1. Clone the project sources from Github.
Note the directory where the sources are, this would be referred to as _sourcedir_ from this point onward.
2. Launch IntelliJ, and click _Open_.
3. Navigate to `sourcedir` and select the file `pom.xml`.
4. IntelliJ will ask if you want to open the project. Select `Open as project`.
5. Wait until IntelliJ has finished downloading and setting up the dependencies.
6. Go to _Run > Edit Configurations..._
7. Create a new Application run configuration with the following data:
```
Name: Existence - Dev
SDK: Java 17 of existence module
Main Class: com.stargazerstudios.existence.ExistenceApplication
Program Arguments: --spring.profiles.active=dev
Working directory: <sourcedir>
```
8. On the _Modify Options_ dropdown, add the run option _Specify classes and packages_ under _Code Coverage_.
9. Add the package _com.stargazerstudios.existence_ on the list of _Packages and classes to include in Coverage Data_.
10. Use this newly created run configuration when starting the server for development work.

**Setting Up Frontend Sources for Development**   
_Note that if the IDE used in setting up the backend is IntelliJ Ultimate, this may not be needed._
1. Open Visual Studio code and click _File > Open Folder..._
2. Select the **reality** folder located on `sourcedir/src/main/resources/frontend`.
Note the directory where the frontend sources are, this would be referred to as _frontenddir_ from this point onward.

**Setting Up Dependencies for the First Time**   
_Note that this only applies to the dependencies used by the frontend (Vuejs).
The dependencies used by the backend (Spring Boot) are automatically setup once the project is first loaded._

1. Execute the section **Setting Up Database for Development** but name the database _db_test_ instead.
2. Launch command prompt on sourcedir.
2. Enter `mvnw clean package` and wait for the process to finish.
It might take a while depending on the internet connection and speed of the machine.

_Below is an easier alternative using IntelliJ._   
_This setup has an added bonus of running the process in debug mode, giving the user access to more verbose logs._
1. Execute the section **Setting Up Database for Development** but name the database _db_test_ instead.
2. On IntelliJ, go to _Run > Edit Configurations..._
2. Add a new Maven configuration with the following details:
```
Name: clean package
Run: -X -e clean package
Working directory: <sourcedir>
```
3. Go to _Run > Run..._ and select the newly created profile. This may also be started from the standard IntelliJ toolbar.   

**Launching Development Server for Vuejs**   

_The below process needs to be done because Vue runs under Nodejs and needs a separate server for live reload during development.
This is not needed during production or if a standalone JAR file is published because the Vuejs files are running under Spring Boot's Tomcat server._

1. Make a copy of the file `.env` on _frontenddir_ and rename it to `.env.local`
2. Amend the properties shown below:
```
VUE_DEV_SERVER=http://localhost:8080
VUE_DEFAULT_URL=http://localhost:8090
```
_The **VUE_DEV_SERVER** property is the current Spring Boot address.
If Spring Boot's port is modified, this needs to be modified as well.
The **VUE_DEFAULT_URL** property is the current Vuejs server address.
If the server address is modified, this needs to be modified as well._
3. Launch command prompt on _frontenddir_.
4. Enter `npm run serve` and wait for the process to finish.
5. After the process has finished, the app may be accessed at `http:localhost:8090`

_Note that for the Vuejs server to run properly, an instance of the Spring Boot server must also be running._

**Deploying For Production**
1. Open the file `.env.local` on _frontenddir_.
2. Fill out the properties `VUE_APP_NAME` and `VUE_APP_SUBTITLE` depending on the installation requirements.
3. Blank out the `VUE_DEV_SERVER` property and replace `VUE_DEFAULT_URL` property with the IP address and port to be used on the deployment server.
4. Execute **Setting Up Dependencies for the First Time** heading. Note that if a database of name _db_test_ is already existing, delete it and create a new one.
This will result in a `target` folder being created on the source directory.
5. Copy the resulting *.jar file and the `libs` folder on the production machine. From now on, this directory will be known as _proddir_
6. Create a folder named `config` on the _proddir_. Copy the properties files from `prod\properties` (both prod and test) into this folder.
7. Create two new databases named `db_existence` and `db_test`. If this is an initial deployment and either of the databases are already existing, delete them and create new ones.
8. Copy all other files located on folder `prod` into the _proddir_.
9. Run a command prompt window as administrator and navigate to the _proddir_. Run the `runserver.bat` on the command prompt window and follow the prompts. The application is now ready to use!
