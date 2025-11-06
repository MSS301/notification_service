# Notification Service

Notification Service manages in-app and email notifications. It stores notification history, exposes REST APIs for management, and pushes real-time updates via WebSocket topics.

## Features

- Create, update, delete notifications with delivery metadata
- Track read/unread state per user
- Deliver notifications over WebSocket (`/topic/notification/user/{userId}`)
- Optional email delivery via pluggable `EmailService`
- Pageable queries and filtering by user or type
- OpenAPI 3 documentation with Swagger UI

## Tech Stack

- **Java 21**
- **Spring Boot 3.3.5**
- **Spring Data JPA** with PostgreSQL
- **Spring Security** (JWT resource server)
- **Spring WebSocket** for real-time delivery
- **Lombok** for boilerplate reduction
- **Springdoc OpenAPI** for API documentation

## Database Schema

`user_notifications`

| Column            | Type                | Description                             |
|-------------------|---------------------|-----------------------------------------|
| `id`              | serial (PK)         | Notification identifier                 |
| `user_id`         | integer             | Target user ID                          |
| `recipient_email` | varchar             | Optional email used for delivery        |
| `title`           | varchar (not null)  | Notification title                      |
| `message`         | text                | Body content                            |
| `type`            | varchar (not null)  | info / warning / success / error        |
| `event`           | varchar             | Domain event triggering the notification|
| `link`            | varchar             | Optional deep-link                      |
| `seen`            | boolean             | Read status                             |
| `created_at`      | timestamp           | Creation time                           |
| `seen_at`         | timestamp           | When the notification was read          |

## REST API Overview

Base path: `/notification`

### Public APIs

```
GET  /notification/api/notifications               # list or filter by type
GET  /notification/api/notifications/{id}          # get by id
POST /notification/api/notifications               # create
PUT  /notification/api/notifications/{id}          # update
DELETE /notification/api/notifications/{id}        # delete
POST /notification/api/notifications/{id}/mark-as-read
POST /notification/api/notifications/{id}/send-email
GET  /notification/api/notifications/user/{userId}
GET  /notification/api/notifications/user/{userId}/paged?page=0&size=10
```

### Internal Service-to-Service API

```
POST /notification/internal/notifications
```
Use the same payload as creation to trigger notifications programmatically.

### WebSocket Endpoint

- STOMP endpoint: `ws://{host}:{port}/notification/ws/notifications`
- Subscribe to `/topic/notification/user/{userId}` to receive updates for a user.

## Configuration

Key properties live in `src/main/resources/application.properties`.

```properties
server.port=8086
server.servlet.context-path=/notification
spring.application.name=notification-service
spring.datasource.url=jdbc:postgresql://...
spring.kafka.bootstrap-servers=localhost:9094
jwt.secret=...
```

## Build & Run

```bash
mvn clean package
java -jar target/notification_service-0.0.1-SNAPSHOT.jar
```

For Docker-based development:

```bash
docker-compose up --build
```

Swagger UI is available at `http://localhost:8086/notification/swagger-ui.html`.

## Customisation

- Replace `LoggingEmailService` with SMTP/Ses implementation for production email.
- Extend `NotificationService` for batch delivery or templating as needed.

## License

Distributed under the same terms as the original project.
