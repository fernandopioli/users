# User Microservice

This microservice handles basic user management, following industry-standard best practices such as Domain-Driven Design (DDD), Clean Architecture, SOLID principles, and automated tests. The microservice serves as an illustrative example of how clean and dependency-free domain-driven Java applications can be developed.

## Features
- User registration with the following fields:
  - ID
  - Name
  - Email
- Domain validation and business logic encapsulation.
- Domain layer without external dependencies.
- Automated unit and integration tests.
- After creating a user, the microservice publishes an event to an Apache Kafka topic indicating that the user was successfully created.

## Database
- Currently using an in-memory H2 database for simplicity and easy demonstration purposes.

## Kafka Integration
- Once a user is created, an event is published to the Kafka topic `users`, informing other services that a user has been successfully created.
- **Kafka Topic**: The topic must currently be created manually.

## Build and Run

### Build
```shell
./mvnw clean package
```

### Run (Spring Boot plugin)
```shell
./mvnw spring-boot:run
```

### Run (Executable JAR)
```shell
java -jar target/user-service.jar
```

## Testing and Coverage

### Run tests
```shell
./mvnw clean test
```

### Generate test coverage report (JaCoCo)
```shell
./mvnw jacoco:report
```

The coverage report will be generated at:
```
target/site/jacoco/index.html
```

## Notes
- PostgreSQL implementation is planned, but currently, the service uses an in-memory H2 database.
- Java 21 and Spring Boot 3.2.3 are the base technologies currently in use.

## Dependencies

- Java 21
- Spring Boot 3.2.3
- Apache Kafka
- H2 Database (In-memory)
- Maven (via wrapper)
- JaCoCo (test coverage reporting)

*Specific version numbers will be updated or adjusted as development continues.*


----
COMMENTS:
- Dominio e application isolados de framework
- pasta config configurando o framework
- Pricipios do DDD como value object, aggregate root, entity, domain event com classes bases
- Validatations no domain
- Exceptions personalizadas
- Inversao de dependencias com interfaces para isolar o dominio (usecases) de libs externas
- Disparando Eventos na criacao de usuarios
- Paginacao personalizada
- spring configurations and annotations on the config and presentation layer
- usando tester do spring ( é o Junit? )
TODO:
- Continuar app mesmo com erro no kafka ou handle errors
- Criar uma interface para usecases
- Falar que poderia ter um tratamento de erro mais robusto devolvendo todos os erros de uma vez só
- Criar exceptions de required e missed mais detalhadas
- Ter uma classe de uniqueUUID
- Profile em PROD
- Profile de testes
- Add a prod DB
- correct config log e em prod
- improve domain events - more rich

TESTS:
- melhorar os testes do entry point
- Testar melhor o controller com mock
- checar o e2e
- colocar testes em paralelo
- testar kafa