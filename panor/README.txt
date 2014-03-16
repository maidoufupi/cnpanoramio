AppFuse Modular Spring MVC Archetype
--------------------------------------------------------------------------------
If you're reading this then you've created your new project using Maven and
appfuse-basic-spring.  You have only created the shell of an AppFuse Java EE
application.  The project object model (pom) is defined in the file pom.xml.
The application is ready to run as a web application. The pom.xml file is
pre-defined with Hibernate as a persistence model and Spring MVC as the web
framework.

There are two modules in this project. The first is "core" and is meant to 
contain Services and DAOs. The second is "web" and contains any web-related
files. Using this modular archetype is recommended when you're planning on
using "core" in multiple applications, or you plan on having multiple clients
for the same backend.

To get started, complete the following steps:

1. Download and install a MySQL 5.x database from
   http://dev.mysql.com/downloads/mysql/5.0.html#downloads.

2. From the command line, cd into the core directory and run "mvn install".

3. From the command line, cd into the web directory and run "mvn jetty:run".

4. View the application at http://localhost:8080.

5. More information can be found at:

    http://appfuse.org/display/APF/AppFuse+QuickStart


# eclipse中运行项目在tomcat runtime上：
# panor上运行mvn tomcat:run
# 1. 更改panor\pom.xml中变量cargo.container.home（本地tomcat home地址）
# 2. 更改panor\pom.xml中变量jdbc.password（本地MYSQL数据库账户root的密码）

第一次运行创建数据库：
panor-core上运行mvn hibernate3:hbm2ddl

初始化数据库：
mvn dbunit:operation -Pprod

或者mvn install -DSkipTests将执行build里的所有goal

每次运行：
panor上运行mvn tomcat6:run
panor上运行mvn tomcat7:redeploy -DskipTests

浏览器中打开：
http://localhost:8080/panor-web

打war包 panor
mvn package -DskipTests