# Existence

### Project Details

**Framework:** [Spring Boot](https://spring.io/projects/spring-boot) version 2.6.5
**Status:** Active - Ongoing  
**Last Release Date:** 10 March 2022  
**Latest Version:** v0.1-alpha  
**Author:** [bossbuwi](https://github.com/bossbuwi)

***

### Environment Setup

**Tools Needed:**
* Java IDE, preferably [IntelliJ IDEA](https://www.jetbrains.com/idea/)
* [PostgreSQL](https://www.postgresql.org/download/) installation, version used for this project is PostgreSQL 14.1. pgAdmin must be included in the installation
* A tool for testing REST APIs, preferably [Postman](https://www.postman.com/downloads/)

**Setting Up for Development**
1. Connect to the postgres installation using pgAdmin. It will ask for the master password. This is set during the installation of postgres.
2. On the left-hand side, there is a list of servers. A default server usually named _Postgres_ is automatically created. Connect to it. Again, it will prompt you for the master password.
3. Right-click the server's _Databases_ group, then _Create > Database.._
4. Enter the following details below on the _General_ tab.
    - Database: db_existence
    - Owner: postgres (this is automatically created)
5. Go to the _Security_ tab and add a _Privilege_ by clicking the "+" sign on the _Privileges_ group. Enter the following details for the privilege.
    - Grantee: postgres
    - Privileges: tick the checkbox beside _All_
    - Grantor: postgres
6. Go to the _SQL_ tab. The statement shown should at least be close to the statement below.  
  `CREATE DATABASE db_existence`  
    `WITH`  
    `OWNER = postgres`  
    `ENCODING = 'UTF8'`  
    `CONNECTION LIMIT = -1;`  
  `GRANT ALL ON DATABASE db_existence TO postgres;`  
7. Click _Save_ to finish the database creation. A new database, _db\_existence_, should now be visible on the _Databases_ group.
