# Customer Service

This is a Spring Boot application that manages customer data using PostgreSQL for storage.

## Prerequisites

- Java 21
- Docker and Docker Compose
  - If you get "docker-compose: command not found", you may need to:
    - Install Docker Compose separately: [Docker Compose Installation Guide](https://docs.docker.com/compose/install/)
    - Or use the integrated Docker CLI command `docker compose` (without hyphen) if you have Docker Desktop or newer Docker Engine versions

## Getting Started

### 1. Start the Docker containers

Before running the application, make sure to start the required Docker containers:

For older Docker versions:
```bash
docker-compose up -d
```

For Docker Desktop or newer Docker Engine versions:
```bash
docker compose up -d
```

This will start PostgreSQL, PgAdmin, Keycloak, SonarQube, and Jenkins containers.

### 2. Wait for PostgreSQL to initialize

The PostgreSQL container needs a few seconds to initialize. The application is configured with connection retry mechanisms, but it's best to ensure PostgreSQL is fully up before starting the application.

### 3. Run the application

```bash
./mvnw spring-boot:run
```

## Connection Issues

If you encounter the following error:

```
Caused by: org.flywaydb.core.internal.exception.FlywaySqlException: Unable to obtain connection from database: Connection to localhost:5432 refused. Check that the hostname and port are correct and that the postmaster is accepting TCP/IP connections.
```

Make sure:
1. The Docker containers are running:
   - For older Docker versions: `docker-compose ps`
   - For newer Docker versions: `docker compose ps`
2. PostgreSQL container is healthy (`docker logs my-postgre`)
3. The port 5432 is not being used by another process

## Database Migrations

This application uses Flyway for database migrations. The migration scripts are located in:
- `src/main/resources/db/migration/`

## Configuration

The application configuration is in `src/main/resources/application.properties`.

## API Documentation

The Customer Service provides a RESTful API for managing customer data. All endpoints are prefixed with `/api/customers`.

### Get All Customers

Retrieves a list of all customers.

- **URL**: `/api/customers`
- **Method**: `GET`
- **Response**: Array of customer objects
- **Response Code**: 200 OK

Example Response:
```json
[
  {
    "id": 1,
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "phone": "123-456-7890",
    "createdAt": "2023-06-15T10:30:00Z",
    "updatedAt": "2023-06-15T10:30:00Z",
    "status": "ACTIVE"
  },
  {
    "id": 2,
    "firstName": "Jane",
    "lastName": "Smith",
    "email": "jane.smith@example.com",
    "phone": "987-654-3210",
    "createdAt": "2023-06-16T14:20:00Z",
    "updatedAt": "2023-06-16T14:20:00Z",
    "status": "ACTIVE"
  }
]
```

### Get Customer by ID

Retrieves a specific customer by their ID.

- **URL**: `/api/customers/{id}`
- **Method**: `GET`
- **URL Parameters**: `id=[Long]` - The ID of the customer
- **Response**: Customer object
- **Response Codes**:
  - 200 OK - Customer found
  - 404 Not Found - Customer not found

Example Response (200 OK):
```json
{
  "id": 1,
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "phone": "123-456-7890",
  "createdAt": "2023-06-15T10:30:00Z",
  "updatedAt": "2023-06-15T10:30:00Z",
  "status": "ACTIVE"
}
```

### Get Customer by Email

Retrieves a specific customer by their email address.

- **URL**: `/api/customers/by-email`
- **Method**: `GET`
- **Query Parameters**: `email=[String]` - The email of the customer
- **Response**: Customer object
- **Response Codes**:
  - 200 OK - Customer found
  - 404 Not Found - Customer not found

Example Request:
```
GET /api/customers/by-email?email=john.doe@example.com
```

Example Response (200 OK):
```json
{
  "id": 1,
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "phone": "123-456-7890",
  "createdAt": "2023-06-15T10:30:00Z",
  "updatedAt": "2023-06-15T10:30:00Z",
  "status": "ACTIVE"
}
```

### Create Customer

Creates a new customer.

- **URL**: `/api/customers`
- **Method**: `POST`
- **Request Body**: Customer creation data
- **Response**: Created customer object
- **Response Codes**:
  - 201 Created - Customer successfully created
  - 409 Conflict - Customer with the provided email already exists

Example Request:
```json
{
  "firstName": "Alice",
  "lastName": "Johnson",
  "email": "alice.johnson@example.com",
  "phone": "555-123-4567",
  "status": "ACTIVE"
}
```

Example Response (201 Created):
```json
{
  "id": 3,
  "firstName": "Alice",
  "lastName": "Johnson",
  "email": "alice.johnson@example.com",
  "phone": "555-123-4567",
  "createdAt": "2023-06-17T09:45:00Z",
  "updatedAt": "2023-06-17T09:45:00Z",
  "status": "ACTIVE"
}
```

### Update Customer

Updates an existing customer.

- **URL**: `/api/customers/{id}`
- **Method**: `PUT`
- **URL Parameters**: `id=[Long]` - The ID of the customer to update
- **Request Body**: Customer update data
- **Response**: Updated customer object
- **Response Codes**:
  - 200 OK - Customer successfully updated
  - 404 Not Found - Customer not found

Example Request:
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe.updated@example.com",
  "phone": "123-456-7890",
  "status": "INACTIVE"
}
```

Example Response (200 OK):
```json
{
  "id": 1,
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe.updated@example.com",
  "phone": "123-456-7890",
  "createdAt": "2023-06-15T10:30:00Z",
  "updatedAt": "2023-06-17T11:20:00Z",
  "status": "INACTIVE"
}
```

### Delete Customer

Deletes a customer by their ID.

- **URL**: `/api/customers/{id}`
- **Method**: `DELETE`
- **URL Parameters**: `id=[Long]` - The ID of the customer to delete
- **Response Codes**:
  - 204 No Content - Customer successfully deleted
  - 404 Not Found - Customer not found

Example Request:
```
DELETE /api/customers/3
```

## Customer Status Values

The following status values are available for customers:

- `ACTIVE` - Customer is active and can use all services
- `INACTIVE` - Customer is inactive (e.g., account dormant)
- `SUSPENDED` - Customer account has been suspended
