# Project Notes

This is a small order management REST API for learning Spring Boot and MongoDB.

## Stack

- Java 21
- Spring Boot 4 and Maven
- Spring MVC, Bean Validation, Spring Data MongoDB
- MongoDB 8 for local development
- Springdoc OpenAPI and Actuator

## Local workflow

- Start MongoDB with `docker compose up -d`.
- Connect MongoDB Compass to `mongodb://localhost:27017`.
- Run the service with `./mvnw spring-boot:run` (or `mvnw.cmd spring-boot:run` on Windows).
- The application uses `MONGODB_URI` when set; otherwise it connects to `mongodb://localhost:27017/ordermanagementservice`.
- API base path is `/order-management-service`; the order collection is `orders`.

Keep changes focused on the existing CRUD behavior. Do not add order-management features unless requested.
