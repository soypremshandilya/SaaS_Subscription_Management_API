# SaaS Subscription Management API

A fully functional RESTful backend system for managing SaaS subscriptions (like AWS, Netflix, Slack). This API helps users track their subscriptions, calculate spending, and get notified about upcoming renewals.

## Features
- **User Management**: Register and login functionalities.
- **Subscription Tracking**: Add, list, update, and delete subscriptions (e.g., Netflix, AWS).
- **Spending Analytics**: Calculate total monthly and yearly spending across all active subscriptions.
- **Renewal Alerts**: Fetch upcoming subscription renewals within a configurable number of days (default next 7 days).

## Tech Stack
- **Java 17**
- **Spring Boot 3.2.x**
- **Spring Data JPA**
- **MySQL** (Primary Database for Production)
- **H2 Database** (In-Memory Database for Development)
- **Hibernate / MySQL Driver**
- **Lombok**
- **Maven**

## Project Architecture
This project follows a clean layered architecture:
`Controller` \u2192 `Service` \u2192 `Repository` \u2192 `Model`

- Extensively utilizes DTOs for Request/Response mapping.
- Implements global exception handling.
- Validates inputs using `jakarta.validation` annotations.

## Setup and Running the Project Locally

1. **Clone the repository or extract the project files**.
2. **Navigate to the project root:**
   ```bash
   cd SaaS_Subscription_Management_API
   ```
3. **Database Configuration:**
   - By default, the application runs on **H2 Database**. You don't need to configure anything.
   - To use **MySQL**, open `src/main/resources/application.properties`, comment out the H2 section, and uncomment the MySQL section. Ensure you have a database named `saas_db` in your local MySQL instance.

4. **Build and Run:**
   Using Maven wrapper:
   ```bash
   # Windows
   .\mvnw clean install -DskipTests
   .\mvnw spring-boot:run

   # Mac/Linux
   ./mvnw clean install -DskipTests
   ./mvnw spring-boot:run
   ```
   The API will be available at `http://localhost:8080`.
   *(If using H2, the console is available at `http://localhost:8080/h2-console` with JDBC URL `jdbc:h2:mem:saasdb`)*

## API Endpoints

### User Endpoints
| HTTP Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/users/register` | Register a new user. |
| `POST` | `/api/users/login` | Login user (Basic authentication/Plain text validation). |

### Subscription Endpoints
| HTTP Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/subscriptions` | Add a new subscription. |
| `GET` | `/api/subscriptions/user/{userId}` | Get all subscriptions for a user. |
| `PUT` | `/api/subscriptions/{id}` | Update an existing subscription. |
| `DELETE` | `/api/subscriptions/{id}` | Delete a subscription. |
| `GET` | `/api/subscriptions/user/{userId}/spending/monthly` | Calculate total monthly spending. |
| `GET` | `/api/subscriptions/user/{userId}/spending/yearly` | Calculate total yearly spending. |
| `GET` | `/api/subscriptions/user/{userId}/renewals?days=7` | Fetch upcoming renewals (next 7 days). |

## Postman Collection
A complete Postman collection (`postman_collection.json`) is included in the root directory. You can import this file into Postman to test all available API endpoints seamlessly.
