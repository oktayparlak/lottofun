# LottoFun

A modern lottery application built with Spring Boot that allows users to purchase tickets, participate in draws, and check results.

## Overview

The application offers user authentication, ticket purchasing, automated draws, and result management through a RESTful API.

## Features

- User registration and authentication using JWT
- Secure ticket purchasing
- Automated scheduled draws
- Result checking and prize management
- Complete API for managing all lottery operations

## Technology Stack

- **Backend Framework**: Spring Boot 3.3.3
- **Language**: Java 17
- **Database**: PostgreSQL 15
- **Security**: Spring Security with JWT authentication
- **API Documentation**: Springdoc OpenAPI (Swagger UI)
- **ORM**: Spring Data JPA
- **Build Tool**: Maven
- **Containerization**: Docker & Docker Compose
- **Testing**: JUnit, Mockito

## Getting Started

### Prerequisites

- Java 17 or higher
- Docker and Docker Compose (for containerized deployment)

### Running with Docker

The easiest way to run the application is using Docker Compose:

```bash
# Clone the repository
git clone https://github.com/oktayparlak/lottofun.git
cd lottofun

# Start the application and database
docker-compose up -d
```

The application will be available at http://localhost:8080/api/v1

## API Documentation

Once the application is running, you can access the Swagger UI documentation at:

http://localhost:8080/api/v1/swagger-ui.html

Alternatively, you can also use Postman

### Main API Endpoints

- **Authentication**

  - `POST /api/v1/auths/register` - Register a new user with username, password, and email
  - `POST /api/v1/auths/login` - Login with email and password to get JWT token

- **Tickets**

  - `POST /api/v1/tickets/` - Purchase a new lottery ticket with selected numbers
  - `GET /api/v1/tickets/my-tickets` - Get all tickets for the current user
  - `GET /api/v1/tickets/{id}` - Get a specific ticket by ID

- **Users**

  - `GET /api/v1/users/profile` - Get current user profile information

- **Draws**

  - `GET /api/v1/draws/active` - Get the currently active draw
  - `GET /api/v1/draws/{id}` - Get a specific draw by ID
  - `GET /api/v1/draws/history` - Get paginated history of past draws with sorting options

- **Results**
  - `GET /api/v1/results/tickets/{id}` - Check if a specific ticket has won

## Configuration

The application includes two configuration files:

1. `application.properties` - Used for local development

   - Database connection settings for local PostgreSQL instance
   - JWT secret and expiration time
   - Draw scheduling using cron expressions

2. `application-docker.properties` - Used when running in Docker environment
   - Configured to connect to the containerized PostgreSQL database
   - Automatically used when running with Docker Compose

## Docker Configuration

### Dockerfile

The project includes a `Dockerfile` that defines how the application is containerized:

```dockerfile
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar", "--spring.profiles.active=docker"]
```

The Dockerfile:

- Uses Eclipse Temurin's JDK 17 Alpine image as the base
- Sets the working directory to `/app`
- Copies the built JAR file from the target directory
- Configures the container to run the application with the Docker profile activated

### Docker Compose

The `docker-compose.yml` file orchestrates the entire application deployment:

```yaml
version: "3.8"

services:
  app:
    build: .
    container_name: lottofun-app
    depends_on:
      db:
        condition: service_healthy
    ports:
      - "8080:8080"
    restart: always
    networks:
      - lottofun-network
    healthcheck:
      test:
        ["CMD", "curl", "-f", "http://localhost:8080/api/v1/actuator/health"]
      interval: 30s
      timeout: 10s
      retries: 5
      start_period: 40s

  db:
    image: postgres:15-alpine
    container_name: lottofun-db
    volumes:
      - postgres-data:/var/lib/postgresql/data
    ports:
      - "5433:5432"
    environment:
      - POSTGRES_DB=lottofun
      - POSTGRES_USER=postgres
      - POSTGRES_PASSWORD=123456
    restart: always
    networks:
      - lottofun-network
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U postgres"]
      interval: 10s
      timeout: 5s
      retries: 5
      start_period: 10s
```

Key features:

- Creates two services: `app` (Spring Boot application) and `db` (PostgreSQL database)
- Configures the application to wait for the database to be healthy before starting
- Sets up proper networking between containers
- Implements health checks for both services to ensure reliability
- Maps the application to port 8080 and the database to port 5433 on the host
- Persists database data using a Docker volume

## Security

The application implements a secure authentication mechanism using JWT tokens. All endpoints (except for authentication) require a valid JWT token to be included in the request header.

## Scheduled Tasks

The application includes a scheduled task that automatically performs draws at configured intervals (default is every 30 seconds in the development environment). If you want to change it, you can change it from the application.properties files.

## Initial Data Setup

The application is configured to automatically initialize the database with some essential data when it starts for the first time. This is handled by the `InitData` class, which implements Spring Boot's `CommandLineRunner` interface.

### Prize Structure

The system automatically sets up the following prize structure for lottery draws:

| Match Count | Prize Type | Amount  |
| ----------- | ---------- | ------- |
| 5 numbers   | JACKPOT    | $10,000 |
| 4 numbers   | HIGH       | $1,000  |
| 3 numbers   | MEDIUM     | $100    |
| 2 numbers   | LOW        | $10     |
| 1 number    | NO_PRIZE   | $0      |
| 0 numbers   | NO_PRIZE   | $0      |

### Initial Draw

The system also creates an initial draw with status `DRAW_OPEN` if no draws exist in the database. This ensures that users can immediately purchase tickets when the application is first deployed.

The initialization code can be found in `src/main/java/com/oktayparlak/lottofun/core/InitData.java`:

```java
@Configuration
public class InitData {

    @Bean
    public CommandLineRunner initPrizes(PrizeRepository prizeRepository, DrawRepository drawRepository) {
        return args -> {
            // Initialize prize structure
            if (prizeRepository.count() == 0) {
                prizeRepository.save(Prize.builder()
                        .matchCount(5)
                        .prizeType(PrizeType.JACKPOT)
                        .amount(new BigDecimal("10000.00"))
                        .build());

                // Additional prize tiers...
            }

            // Create initial draw
            if (drawRepository.count() == 0) {
                drawRepository.save(Draw.builder()
                        .drawNumber(3432423L)
                        .drawDate(LocalDateTime.now())
                        .status(DrawStatus.DRAW_OPEN)
                        .build());
            }
        };
    }
}
```

This initialization ensures that the application is ready to use immediately after deployment without requiring manual database setup.

## API Request and Response Examples

Below are examples of request and response structures for the main endpoints based on the Postman collection.

### Authentication

#### Register User

**Request:**

```json
{
  "username": "username", // must be unique
  "password": "password",
  "email": "user@example.com" // must be valid email
}
```

**Response:**

```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
  "username": "username",
  "email": "user@example.com"
}
```

#### Login

**Request:**

```json
{
  "password": "password",
  "email": "user@example.com"
}
```

**Response:**

```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
  "username": "username",
  "email": "user@example.com"
}
```

### Tickets

#### Purchase Ticket

**Request:**

```json
{
  "selectedNumbers": [3, 4, 9, 5, 8]
}
```

**Response:**

```json
{
  "id": 3,
  "ticketNumber": "50FDD5D7",
  "drawId": 159,
  "selectedNumbers": [3, 4, 5, 8, 9],
  "matchCount": null,
  "prizeAmount": null,
  "status": "WAITING_FOR_DRAW",
  "purchaseTimestamp": "2025-05-25T17:12:44.532730999",
  "winningNumbers": []
}
```

#### Get My Tickets

**Response:**

```json
[
  {
    "id": 1,
    "ticketNumber": "69A8F924",
    "drawId": 143,
    "selectedNumbers": [3, 5, 6, 7, 8],
    "matchCount": 0,
    "prizeAmount": 0,
    "status": "NOT_WON",
    "purchaseTimestamp": "2025-05-25T17:04:07.135224",
    "winningNumbers": [34, 48, 9, 42, 35]
  },
  {
    "id": 2,
    "ticketNumber": "DFE92094",
    "drawId": 145,
    "selectedNumbers": [3, 4, 5, 8, 9],
    "matchCount": 1,
    "prizeAmount": 0,
    "status": "NOT_WON",
    "purchaseTimestamp": "2025-05-25T17:05:39.397736",
    "winningNumbers": [3, 44, 6, 39, 12]
  }
]
```

### User Profile

**Response:**

```json
{
  "id": 3,
  "username": "username",
  "email": "user@example.com",
  "balance": 1000
}
```

### Draws

#### Get Active Draw

**Response:**

```json
{
  "id": 148,
  "drawNumber": 3432570,
  "drawDate": "2025-06-01T17:07:00.013248",
  "status": "DRAW_OPEN",
  "winningNumbers": [],
  "createdAt": "2025-05-25T17:07:00.01343"
}
```

#### Get Draw History

**Request Parameters:**

- `page`: Page number (starting from 0)
- `size`: Number of items per page
- `sort`: Sorting criteria (e.g., "id,desc", "drawDate,asc")

**Response:**

```json
{
  "content": [
    {
      "id": 153,
      "drawNumber": 3432575,
      "drawDate": "2025-06-01T17:09:30.007723",
      "status": "DRAW_OPEN",
      "winningNumbers": [],
      "createdAt": "2025-05-25T17:09:30.007886"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 1,
    "sort": {
      "unsorted": false,
      "sorted": true,
      "empty": false
    },
    "offset": 0,
    "unpaged": false,
    "paged": true
  },
  "totalPages": 153,
  "last": false,
  "totalElements": 153,
  "first": true,
  "numberOfElements": 1,
  "size": 1,
  "number": 0,
  "sort": {
    "unsorted": false,
    "sorted": true,
    "empty": false
  },
  "empty": false
}
```

#### Get Completed Draw

**Response:**

```json
{
  "id": 154,
  "drawNumber": 3432576,
  "drawDate": "2025-06-01T17:10:00.012844",
  "status": "DRAW_CLOSED",
  "winningNumbers": [38, 40, 1, 34, 32],
  "createdAt": "2025-05-25T17:10:00.013233"
}
```

### Results

#### Get Result by Ticket ID

**Response:**

```json
{
  "id": 1,
  "ticketNumber": "69A8F924",
  "drawId": 143,
  "selectedNumbers": [3, 5, 6, 7, 8],
  "matchCount": 0,
  "prizeAmount": 0,
  "status": "NOT_WON",
  "purchaseTimestamp": "2025-05-25T17:04:07.135224",
  "winningNumbers": [34, 48, 9, 42, 35]
}
```

## Author

Oktay Parlak

---
