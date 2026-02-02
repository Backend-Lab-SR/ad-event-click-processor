# Ad Event Click Processor

A distributed system POC for processing ad event clicks using Spring Boot and Redis.

## Overview

This project is a Spring Boot application designed to process ad event clicks in a distributed system architecture. It leverages Redis for distributed caching and event processing.

## Technology Stack

- **Java**: 17
- **Build Tool**: Maven
- **Framework**: Spring Boot 3.2.0
- **Cache/Message Broker**: Redis
- **Testing**: JUnit 5, Testcontainers

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── example/
│   │           └── adeventprocessor/
│   │               └── AdEventClickProcessorApplication.java
│   └── resources/
│       └── application.properties
└── test/
    └── java/
        └── com/
            └── example/
                └── adeventprocessor/
```

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- Redis (for local development) or Docker (for Testcontainers)

## Building the Project

```bash
mvn clean install
```

## Running the Application

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=local

```

## Running Tests

```bash
mvn test
```

## Configuration

Application configuration is managed through `application.properties` or `application.yml` files in the `src/main/resources` directory.

## License

This project is a POC and is provided as-is.
