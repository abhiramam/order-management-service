# Order Management Service

A small Spring Boot REST API for creating, listing, reading, and deleting orders. It uses MongoDB so you can run it locally, inspect saved documents in MongoDB Compass, and use the code as a Spring Boot interview study project.

## Requirements

- Java 21
- Docker Desktop with Docker Compose, or a MongoDB server listening on `localhost:27017`
- MongoDB Compass (optional, for visual inspection)

## Run locally

From this repository directory, start MongoDB:

```powershell
docker compose up -d
```

Start the API in another terminal:

```powershell
.\mvnw.cmd spring-boot:run
```

The API listens on `http://localhost:8080/order-management-service`. The default database is `ordermanagementservice`; MongoDB creates the database and `orders` collection when the first order is saved.

The order API uses HTTP Basic authentication. Spring Boot logs a generated password for the default user `user` when the app starts. Use that password for requests to `/orders`; the health endpoint and OpenAPI docs remain public.

To use another MongoDB deployment, set `MONGODB_URI` before starting the app. For example:

```powershell
$env:MONGODB_URI = 'mongodb://localhost:27017/ordermanagementservice'
.\mvnw.cmd spring-boot:run
```

## Connect Compass

1. Open MongoDB Compass and choose **Add new connection**.
2. Enter `mongodb://localhost:27017` and connect.
3. Create an order through the API, then refresh Compass. Open `ordermanagementservice` → `orders` to see the stored MongoDB document.

The local Compose database has no authentication and is bound to localhost for development. Do not expose it to a network or reuse it for production data.

## Try the API

Create an order:

```powershell
Invoke-RestMethod -Method Post `
  -Uri 'http://localhost:8080/order-management-service/orders' `
  -ContentType 'application/json' `
  -Authentication Basic `
  -Credential (Get-Credential -UserName 'user' -Message 'Enter the generated Spring Security password') `
  -Body '{"customerName":"Ada Lovelace","bookId":101,"orderStatus":"NEW"}'
```

Use the returned `orderId` in these requests:

```powershell
$credential = Get-Credential -UserName 'user' -Message 'Enter the generated Spring Security password'
Invoke-RestMethod -Uri 'http://localhost:8080/order-management-service/orders' -Authentication Basic -Credential $credential
Invoke-RestMethod -Uri 'http://localhost:8080/order-management-service/orders/{orderId}' -Authentication Basic -Credential $credential
Invoke-WebRequest -Method Delete -Uri 'http://localhost:8080/order-management-service/orders/{orderId}' -Authentication Basic -Credential $credential
```

Replace `{orderId}` with the ID returned by the create request. Valid statuses are `NEW`, `PROCESSING`, `COMPLETED`, and `CANCELLED`. Invalid requests return a structured `400` response; an unknown ID returns `404`.

OpenAPI UI: `http://localhost:8080/order-management-service/swagger-ui/index.html`

Health check: `http://localhost:8080/order-management-service/actuator/health`

Stop MongoDB with `docker compose down`. The named volume keeps your local data. To remove it and delete the local database contents, use `docker compose down --volumes`.

## Spring Boot interview study guide

Walk through one `POST /orders` request from top to bottom:

1. **Application startup and auto-configuration** — `@SpringBootApplication` component-scans this package and lets Boot configure MVC, validation, and MongoDB from the starters and properties.
2. **Controller** — `OrderController` handles HTTP mapping and status codes. It delegates instead of containing persistence logic.
3. **Validation** — `@Valid` triggers Jakarta Bean Validation constraints on `OrderRequestDto` before the service is called.
4. **Service** — `OrderService` converts between the API DTO and persisted document and contains the application operation.
5. **Repository** — `OrderRepository` extends `MongoRepository<OrderInfoVo, String>`. Spring Data creates the implementation at runtime; the `String` ID maps to MongoDB's `_id` field.
6. **Persistence mapping** — `@Document(collection = "orders")` maps `OrderInfoVo` to a MongoDB collection. Mongo is document-oriented, so the stored shape is not a relational table or JPA entity.
7. **Exception translation** — `ApiExceptionHandler` maps missing orders and validation failures to useful HTTP responses.

Useful topics to explain in an interview: dependency injection and constructor injection, the controller/service/repository separation, DTOs versus persistence documents, Bean Validation, HTTP status codes, Spring Data repository generation, MongoDB documents and IDs, externalized configuration, Actuator health, and why production APIs need an explicit authentication/authorization design. The Basic authentication here is for learning; it is not a production authentication design.

## Configuration

| Setting | Default | Purpose |
| --- | --- | --- |
| `server.port` | `8080` | HTTP port |
| `server.servlet.context-path` | `/order-management-service` | API base path |
| `spring.mongodb.uri` | `mongodb://localhost:27017/ordermanagementservice` | MongoDB connection URI (Boot 4 property) |
| `MONGODB_URI` | unset | Optional environment override for the MongoDB URI |

