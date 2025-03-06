mvn spring-boot:run

mvn clean install

mvn spring-boot:run -Dspring.profiles.active=dev

# Run all tests
mvn test

# Run tests with coverage report (using JaCoCo)
mvn test jacoco:report
