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


eclipse中运行项目在tomcat runtime上：
panor上运行mvn tomcat:run
1. 更改panor\pom.xml中变量cargo.container.home（本地tomcat home地址）
2. 更改panor\pom.xml中变量jdbc.password（本地MYSQL数据库账户root的密码）

1、hbm2ddl：panor-core上运行build


安装源代码
mvn source:jar install

项目代码部署步骤：
更新数据库:
	core: mvn install -DSkipTests
	core: mvn dbunit:operation -Pprod

打包代码；
	panor: mvn package -DskipTests
更改配置文件；
	aliyun.properties
	applicationContext-resources.xml
	hibernate.properties
	jdbc.properties
上传war；

启动tomcat；

linux command:
## force remove dir
rm -rf dir/

unzip ROOT.war -d ./ROOT
cp -avr /opt/ponm/lib /opt/tomcat7/webapps/ROOT/WEB-INF/lib

## kill process by name
pkill java

database uri:
panoramioinner
panoramioinner.mysql.rds.aliyuncs.com

maven commands:
1、设置jvm内存大小
set MAVEN_OPTS=-Xms256m -Xmx512m -XX:MaxPermSize=512m
2、启动tomcat
mvn tomcat7:run -DskipTests

core
mvn hibernate3:hbm2ddl
mvn install -DskipTests

web
package -DskipTests
clean