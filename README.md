# OpenMeteoProject

:page_facing_up: Instructions:

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

:hammer_and_wrench: Test WAR File

To test a WAR file, you need to deploy it to an application server, like Tomcat or Jetty.

Here are the general steps for testing a WAR file:

1 - Make sure the application server is installed and configured correctly.

2 - Copy the WAR file to the "webapps" folder on the application server.

3 - Start the application server.

4 - Access the app URL in the web browser to check if the app is working properly.

______________________________________________________________________________________________________________________________________

:gear: Infos

* Source Code - src/main/java/OpenMeteoAPI.java

* JUnit Tests - src/test/java/OpenMeteoAPITest.java
