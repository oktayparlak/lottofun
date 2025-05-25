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
git clone https://github.com/yourusername/lottofun.git
cd lottofun

# Start the application and database
docker-compose up -d
```

The application will be available at http://localhost:8080/api/v1

## API Documentation

Once the application is running, you can access the Swagger UI documentation at:

http://localhost:8080/api/v1/swagger-ui.html

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

## Docker Support

The project includes a `Dockerfile` and `docker-compose.yml` for easy containerization and deployment. The Docker Compose configuration sets up both the application and a PostgreSQL database.

## Security

The application implements a secure authentication mechanism using JWT tokens. All endpoints (except for authentication) require a valid JWT token to be included in the request header.

## Scheduled Tasks

The application includes a scheduled task that automatically performs draws at configured intervals (default is every 30 seconds in the development environment).

## License

[Include license information here]

## Contributing

[Include contribution guidelines here]

## Author

Oktay Parlak

---

For any questions or issues, please open an issue on the GitHub repository.
