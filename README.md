# WAES Technical Assignment

### The Rest Service

This Spring Boot application that exposes the diff API specified by the assignment document

To run this service use the following command:
```
mvn spring-boot:run
```

To run the unit tests use the following command:
```
mvn test
```

PIT mutation tests are also included in the project. To run use the following command:
```
mvn test pitest:mutationCoverage
```
A result report is available at the `./target/pit-tests/.../index.html` folder


Integration tests are handled by the Karate Framework. To run
them you need to start the service, then issue the following command:
```
mvn test -Dtest=KarateTests
```
A result report is available at the `./target/surefire-reports/karate-summary.html` folder
