# Java Employee Management

This project contains a Java console application and a Jakarta Servlet web application.

## Requirements

- Java 17 or later
- Maven 3.9 or later
- A Jakarta Servlet 6.0-compatible container, such as Tomcat 10.1, to run the web application

## Build

Run `mvn package`. Maven creates `target/microjava-admin-web-1.0-SNAPSHOT.war`.

## Run the console application

From the project directory, compile and launch the console app:

```text
javac Main.java Admin.java Employee.java FileHandler.java
java Main
```

## Configure administrator login

Set both environment variables before starting the console app or servlet container:

- `EMPLOYEE_ADMIN_USER`
- `EMPLOYEE_ADMIN_PASSWORD`

Administrator login is unavailable if either variable is missing or blank. Employee records are stored locally and are not included in the repository.
