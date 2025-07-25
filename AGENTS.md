# Gemini Agent Instructions

This document provides instructions for the Gemini agent to effectively assist with the development of this project.

## Project Overview

This is a Java-based order management service built with Spring Boot. It provides APIs for creating, retrieving, and managing orders.

## Key Technologies & Frameworks

*   **Language:** Java 17
*   **Framework:** Spring Boot
*   **Build Tool:** Maven
*   **Database:** MySQL (runtime), H2 (testing)
*   **API Documentation:** SpringDoc (Swagger UI)
*   **Service Discovery:** Spring Cloud Netflix Eureka Client
*   **Declarative REST Client:** Spring Cloud OpenFeign
*   **Circuit Breaker:** Resilience4j
*   **Logging:** SLF4J (use this for all logging purposes)
*   **Data Access:** Spring Data JPA
*   **Code Generation:** Lombok (use annotations like `@Data`, `@Getter`, `@Setter`, etc. to reduce boilerplate code)

## Development Workflow

*   **Code Style:** Follow standard Java conventions.
*   **Testing:** Unit tests are located in `src/test/java`. Use `mvn test` to run them. The project uses JUnit 5 and Mockito for testing.
*   **Dependencies:** Manage dependencies in the `pom.xml` file.

## How to Run the Application

1.  Build the project: `mvn clean install`
2.  Run the application: `java -jar target/order-management-service-*.jar`

## Important Files

*   `pom.xml`: Defines project dependencies and build settings.
*   `src/main/java/org/scaler/ordermanagementservice/OrderManagementServiceApplication.java`: The main application class.
*   `src/main/resources/application.properties`: Configuration file for the application.
*   `src/main/java/org/scaler/ordermanagementservice/controllers/OrderController.java`: The main controller for order-related APIs.
*   `src/main/java/org/scaler/ordermanagementservice/services/OrderService.java`: The service layer containing business logic for orders.
