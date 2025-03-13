#   Library Management System
> This is a library management backend application which expose multiple endpoint with security layer based on JWT token and documented with OpenAPI.

## Tools
* Java 17
* Maven 3.6.0
* Spring Boot 3.3.2
* Sprint Security
* OpenAPI
* SonarQube
* Postgres DB
* Docker

## Environment Variables
Before to run the application create a .env file in the root and put the following variables and replace the values.
```properties
DB_USERNAME=myuser
DB_PASSWORD=mypassword
DB_NAME=mydb
JWT_SECRET=e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855
LIBRARY_SECRET=${JWT_SECRET}
INITIAL_ROLES=admin,mod,user
INITIAL_EMAIL=my.user@gmail.com
INITIAL_USER=my.user
INITIAL_PASSWORD=myinitialpassword
```

## Deployment
You have two ways to run the application:
### A. Run the application locally:
1. You have to create an application-local.properties file and put the following properties.
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/librarydb
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
library.app.jwtSecret=${LIBRARY_SECRET}
library.app.jwtExpirationMs=1800000
library.app.jwtCookieName=Bearer
library.app.initial.roles=${INITIAL_ROLES}
library.app.initial.email=${INITIAL_EMAIL}
library.app.initial.user=${INITIAL_USER}
library.app.initial.password=${INITIAL_PASSWORD}
```

1.1. By Maven
```shell
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

1.2. By Jar
To use this option you have to ensure the jar is already generated.
If you are not able to see the jar in the target folder, run the following command:
```shell
mvn package
```
Then run the following command:
```shell
java -jar -Dspring.profiles.active=local library-management-0.0.1-SNAPSHOT.jar
```

2. Run the application with docker:
   To use the containerization with docker you have to ensure the jar is already generated

If you are not able to see the jar in the target folder, run the following command:
```shell
mvn package
```
Then run the following docker commands:
2.1 Generate the image
```shell
docker build -t yourUsername/library-management:1.0.0 .
```
2.2 Run a container from the generated image:
```shell
docker run -p 8080:8080 yourUsername/library-management:1.0.0
```

## API Documentation
- [Swagger Documentation](http://localhost:8080/swagger-ui/index.html)

