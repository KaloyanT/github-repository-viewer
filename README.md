# GitHub Repository Viewer
Simple Spring Boot service written in Java 111, which allows you to make requests to the GitHub API and get the projects with most stars. The programming language used for the projects and a creation date can be specified.
The creation date indicates that the project must be newer than the specified date. Furthermore, the results can be limited up to a 100. The default is 30.  

## Build the project
Simply run `mvn clean install`

## Run the project
Simply run `java -jar target/github-repository-viewer-0.0.1-SNAPSHOT.jar`. Then, you can make requests to the Endpoint or check the health status and the Swagger documentation for the project 

## Endpoint
http://localhost:8080/repositories

Query Parameters:

count: Type: Integer. Min Value is 1, Max Value is 100

creationDate: ISO Formatted LocalDate (yyyy-MM-dd)

language: Type: String

Example requests: http://localhost:8080/repositories?count=10&language=java&creationDate=2018-06-07

## Health Status
http://localhost:8080/actuator/health

## Swagger Documentation
http://localhost:8080/swagger-ui.html