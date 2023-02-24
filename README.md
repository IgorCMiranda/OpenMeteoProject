# OpenMeteoProject

:arrow_right:  :page_facing_up: Instructions:  :arrow_left:

The project was built using Maven and the IntelliJ IDE. If the test is done on the same, it is possible
follow the following steps:

>> PS: All the necessary dependencies for the program to run have been added to the pom.xml
so they can be downloaded. >>

1 - Open IntelliJ and select "Open" from the "File" menu.

2 - Navigate to the root folder of the project and select the pom.xml file.

3 - Click "Open" to open the project.

4 - IntelliJ should automatically detect that this is a Maven project and start download dependencies. You can check the download progress on the dashboard "Maven Projects" on the right side of the screen.

5 - To run all the tests, you can right-click on the "src/test/java" folder and select "Run 'All Tests'".

6 - To run the main class, you can right click on the main class and select "Run 'OpenMeteoAPI'".

7 - IntelliJ should compile and run the project. You can check the output in the console.

If IntelliJ does not automatically detect the Maven project, you can select "Import Project" instead of "Open", select the pom.xml file and follow the import wizard instructions.

______________________________________________________________________________________________________________________________________

:arrow_right:  :hammer_and_wrench: Test WAR File  :arrow_left:

To test a WAR file, you need to deploy it to an application server, like Tomcat or Jetty.

Here are the general steps for testing a WAR file:

1 - Make sure the application server is installed and configured correctly.

2 - Copy the WAR file to the "webapps" folder on the application server.

3 - Start the application server.

4 - Access the app URL in the web browser to check if the app is working properly.

______________________________________________________________________________________________________________________________________

:arrow_right:  :gear: Infos  :arrow_left:

* Source Code - src/main/java/OpenMeteoAPI.java

* JUnit Tests - src/test/java/OpenMeteoAPITest.java

______________________________________________________________________________________________________________________________________

About Postgres

1 - The Postgres was setted up with this infos:
    user: postgres
    password: 1234
    
2 - The port used was the standard 5432

3 - Database created:
    name: OpenMeteo
    
    ```bash
    CREATE DATABASE "OpenMeteo"
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'C'
    LC_CTYPE = 'C'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;
    ```
    
    Tables:
            Master:
            ```bash
            CREATE TABLE Master (
                id SERIAL PRIMARY KEY,
                latitude FLOAT NOT NULL,
                longitude FLOAT NOT NULL
	          );
            ```
            
            Detailed:
            ```bash
            CREATE TABLE Detailed (
                id SERIAL PRIMARY KEY,
                temperature FLOAT NOT NULL,
                wind_speed FLOAT NOT NULL,
                wind_direction FLOAT NOT NULL,
                time_utc TIMESTAMP NOT NULL,
                local_time TIMESTAMP NOT NULL,
                id_master INTEGER NOT NULL,
                FOREIGN KEY (id_master) REFERENCES Master (id)
            );
            ```
            

