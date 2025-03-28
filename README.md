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
