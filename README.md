# Task Manager API

> A production-grade Spring Boot 3 REST API for task management — demonstrating JWT authentication, Spring Security, JPA/Hibernate, Swagger/OpenAPI documentation, JUnit 5 + Mockito testing, Docker containerization, and GitHub Actions CI/CD.

![Java](https://img.shields.io/badge/Java-17-orange.svg) ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.x-green.svg) ![License](https://img.shields.io/badge/license-MIT-blue.svg)

## Features

- JWT Authentication with Spring Security
- User Management: register, login, profile
- Task CRUD with pagination and sorting
- Task categories, status, and priority filtering
- Dashboard stats: task counts by status and priority
- Swagger/OpenAPI interactive API docs
- Comprehensive tests: JUnit 5, Mockito, MockMvc
- Docker multi-stage build for production
- GitHub Actions CI/CD pipeline
- H2 (dev) / PostgreSQL (prod) profiles

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Java 17 |
| Framework | Spring Boot 3.2.x |
| Security | Spring Security + JWT (jjwt) |
| Data | Spring Data JPA / Hibernate |
| Database | H2 (dev), PostgreSQL (prod) |
| Documentation | springdoc-openapi (Swagger UI) |
| Testing | JUnit 5, Mockito, MockMvc |
| Build | Maven |
| Container | Docker (multi-stage) |
| CI/CD | GitHub Actions |

## Quick Start

### Run Locally (H2 in-memory DB)

```bash
git clone https://github.com/shivamkumarhub-web/taskmanager-api.git
cd taskmanager-api
mvn spring-boot:run
```

API runs at http://localhost:8080
Swagger UI at http://localhost:8080/swagger-ui.html
H2 Console at http://localhost:8080/h2-console (JDBC URL: jdbc:h2:mem:taskdb)

### Run with Docker Compose (PostgreSQL)

```bash
docker-compose up --build
```

API at http://localhost:8080 | PostgreSQL at localhost:5432

## API Documentation

Once running, open Swagger UI: http://localhost:8080/swagger-ui.html

### Auth Endpoints

| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| POST | /api/auth/register | Register a new user | No |
| POST | /api/auth/login | Login and get JWT token | No |

### Task Endpoints

| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| GET | /api/tasks | Get all tasks (paginated) | Yes |
| GET | /api/tasks/{id} | Get task by ID | Yes |
| POST | /api/tasks | Create a new task | Yes |
| PUT | /api/tasks/{id} | Update a task | Yes |
| DELETE | /api/tasks/{id} | Delete a task | Yes |
| GET | /api/tasks/status/{status} | Filter tasks by status | Yes |

### Example Requests

```bash
# Register
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"shivam","email":"shivam@example.com","password":"Password123!"}'

# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"shivam","password":"Password123!"}'

# Create a task (use token from login)
curl -X POST http://localhost:8080/api/tasks \
  -H "Authorization: Bearer <your-jwt-token>" \
  -H "Content-Type: application/json" \
  -d '{"title":"Complete portfolio","description":"Finish 3 GitHub projects","status":"TODO","priority":"HIGH"}'
```

## Running Tests

```bash
mvn test
```

Test coverage: AuthController, TaskController (MockMvc), TaskService (Mockito), JwtTokenProvider.

## Docker

```bash
# Build image
docker build -t taskmanager-api .

# Run standalone
docker run -p 8080:8080 taskmanager-api

# Or use Docker Compose (includes PostgreSQL)
docker-compose up --build
```

## CI/CD

GitHub Actions workflow runs on every push and PR:
1. Checkout code
2. Set up Java 17
3. Cache Maven dependencies
4. Run tests
5. Build JAR
6. Build Docker image and push to GitHub Container Registry

## License

MIT