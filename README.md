
# Customer Hub

A Spring Boot-based RESTful API for managing customer data, with full CRUD operations and integration tests. Includes a command-line client for API consumption.

## Features

- Customer CRUD API (Create, Read, Update, Delete)
- RESTful endpoints with JSON payloads
- Integration tests using RestAssured and WireMock
- Command-line client for API interaction
- Solid API contracts with DTOs
- MySQL database integration
- Containerization with Docker and Deployments in kubernetes  with MiniKube.

## Tech Stack

- Java 17+
- Spring Boot
- Gradle
- MySQL
- RestAssured (integration testing)
- WireMock (mocking external dependencies)
- Command-line client (Spring Boot CLI)
- Docker desktop
- minikube
- postman

## Getting Started

### Prerequisites

- Java 17 or higher
- Gradle
- MySQL (running locally on default port)

### Clone the Repository
git clone -b cust-dev-task1 https://github.com/raghu588/customer-hub.git 

### Configure Database

Update `src/main/resources/application.properties` if needed:

### Build and Run the API
/gradlew clean build

The API will be available at `http://localhost:8080/api/customers`.

## API Endpoints

- `POST /api/customers` - Create a customer
- `GET /api/customers/{id}` - Get customer by ID
- `PUT /api/customers/{id}` - Update customer
- `DELETE /api/customers/{id}` - Delete customer

Sample JSON payload:

```json
{
  "firstName": "abc",
  "middleName": "A",
  "lastName": "test",
  "emailAddress": "test123@example.com",
  "phoneNumber": "+1 (999) 888-4567"
}

Note:
Containerization: refer Dockerfile
Kubernetes : Refer app-depoyment,db-deployment yaml.
CI/CD: refer Jenkinsfile
App Integration:https://github.com/raghu588/Custmer-hub-cli

Observability:
Used SLF4J for structured logging.
Used Micrometer for metrics (Prometheus).
// OpenTelemetry for distributed tracing.
