# Florent Sor — Full-Stack Developer Portfolio

**Live portfolio:** [florent-sor.fr](https://florent-sor.fr/)  
**Project status:** In progress

A full-stack personal portfolio built with Angular, Spring Boot and PostgreSQL. The public site presents professional experience, education, projects, skills and interests. A REST API supplies portfolio content and provides authenticated endpoints for managing it.

## Features

- Single-page portfolio with profile information and sections for experience, education, projects, skills and interests.
- Angular clients that retrieve portfolio data from the REST API.
- Spring Boot REST endpoints for profile, projects, skills, skill categories, experience, education and interests.
- CRUD operations for portfolio resources, protected by backend authentication and authorization rules.
- Admin login that issues a signed JWT.
- PostgreSQL persistence with JPA/Hibernate.
- Optional initial content import from a JSON file when the database is empty.
- OpenAPI documentation served by the backend.
- Docker Compose setup for the database, API and static Angular site.

The project is actively being developed. Although backend management endpoints and frontend management components exist, the Angular router currently enables only the home page; the resource-specific routes are present but commented out. There is no contact-submission API or Redis integration in the current implementation.

## Technology stack

| Area | Technologies |
| --- | --- |
| Frontend | Angular 22, TypeScript, RxJS |
| Backend | Java 25, Spring Boot 4, Spring Security |
| API | REST, JSON, OpenAPI/Swagger UI |
| Database | PostgreSQL, Spring Data JPA, Hibernate |
| Authentication | RSA-signed JWT bearer tokens |
| Frontend tests | Vitest through Angular CLI |
| Containers | Docker, Docker Compose, Nginx |

## Architecture

```text
Visitors
   │
   │ https://florent-sor.fr
   ▼
DNS + HTTPS
   │
   ▼
AWS deployment
   │
   ├── Angular app served by Nginx
   │        │ /api/*
   │        ▼
   ├── Spring Boot REST API
   │        │
   │        ▼
   └── PostgreSQL
```

The portfolio is deployed on AWS and accessed through the `florent-sor.fr` domain over HTTPS. DNS directs visitors to the hosted application. The Angular frontend is served by Nginx, which forwards `/api` requests to the Spring Boot backend; the frontend and API exchange JSON, while the backend accesses PostgreSQL. The API follows a layered structure: controllers handle HTTP requests, services contain application logic, repositories access persisted entities, and DTOs/mappers define the API representation.

## Repository layout

```text
.
├── PortfolioBackend/
│   ├── src/main/java/com/neoblishange/portfolio/
│   │   ├── config/       # Security, startup initialization and configuration
│   │   ├── controller/   # REST endpoints
│   │   ├── dto/          # Request and response models
│   │   ├── entity/       # JPA entities
│   │   ├── exception/    # API error handling
│   │   ├── mapper/       # Entity/DTO mapping
│   │   ├── repository/   # Spring Data repositories
│   │   ├── security/     # JWT and user-detail handling
│   │   └── service/      # Application logic
│   ├── src/main/resources/application.yaml
│   ├── Dockerfile
│   └── pom.xml
├── PortfolioFrontend/
│   ├── src/app/features/ # Portfolio sections and resource components
│   ├── public/           # Static assets
│   ├── Dockerfile
│   └── package.json
└── docker-compose.yaml
```

## Requirements

For local development:

- Java 25
- Node.js 24 and npm
- PostgreSQL 18 (or a compatible PostgreSQL server)
- OpenSSL, for generating a local RSA key pair

For the containerized setup, Docker Engine and the Docker Compose plugin are required.

## Configuration

The backend reads its settings from environment variables. Do not put real passwords or private keys in source control.

| Variable | Purpose |
| --- | --- |
| `SPRING_DATASOURCE_URL` | JDBC URL for PostgreSQL; defaults to `jdbc:postgresql://localhost:5432/portfolio` |
| `SPRING_DATASOURCE_USERNAME` | PostgreSQL username; defaults to `postgres` |
| `SPRING_DATASOURCE_PASSWORD` | PostgreSQL password |
| `ADMIN_USERNAME` | Username for the initial administrator; defaults to `admin` |
| `ADMIN_PASSWORD` | Password for the initial administrator |
| `APP_JWT_ISSUER` | JWT issuer; defaults to `portfolio-api` |
| `APP_JWT_PRIVATE_KEY_PATH` | Path to the RSA private key in PKCS#8 PEM format |
| `APP_JWT_PUBLIC_KEY_PATH` | Path to the matching RSA public key in X.509 PEM format |
| `PORTFOLIO_DATA_FILE` | Optional override for the initial portfolio JSON file |

The admin account is created on startup only if no admin user exists. Its password is stored using the configured password encoder. Access tokens expire after 10 minutes.

### Create local JWT keys

From the repository root, create an RSA key pair for local development. Keep the private key private and do not commit either key:

```bash
mkdir keys
openssl genpkey -algorithm RSA -out keys/private-key.pem -pkeyopt rsa_keygen_bits:2048
openssl pkey -in keys/private-key.pem -pubout -out keys/public-key.pem
```

Docker Compose mounts `./keys` into the backend container at `/app/keys`. The corresponding key-path variables in the root `.env` file should therefore be `/app/keys/private-key.pem` and `/app/keys/public-key.pem`.

### Initial portfolio data

When all portfolio tables are empty, the backend imports profile, categories and skills, experience, education, interests and projects from the JSON file selected by `PORTFOLIO_DATA_FILE`. Without an override, the application looks for `data/portfolio-data.json` relative to its working directory. In Docker Compose, `./data` is mounted at `/app/data`, so provide `./data/portfolio-data.json` in the repository root. For a local backend run from `PortfolioBackend`, the default is `PortfolioBackend/data/portfolio-data.json`.

The JSON properties and nested object structure are defined by the `PortfolioData` record in `PortfolioBackend/src/main/java/com/neoblishange/portfolio/config/PortfolioData.java`. Dates use ISO format (`YYYY-MM-DD`), and the profile `availability` value must be one of `AVAILABLE`, `NOT_AVAILABLE` or `OPEN_TO_OPPORTUNITIES`. The importer skips the import if any of the relevant portfolio tables already contains data; it does not merge or refresh an existing database.

## Run with Docker Compose

1. Create the JWT keys as described above.
2. Create a root `.env` file with local-only values:

```dotenv
POSTGRES_PASSWORD=replace-with-a-local-database-password
ADMIN_USERNAME=admin
ADMIN_PASSWORD=replace-with-a-strong-local-password
APP_JWT_ISSUER=portfolio-api
APP_JWT_PRIVATE_KEY_PATH=/app/keys/private-key.pem
APP_JWT_PUBLIC_KEY_PATH=/app/keys/public-key.pem
```

3. Add `data/portfolio-data.json` using the structure described above.
4. Build and start the services:

   ```bash
   docker compose up --build
   ```

The services are available at:

| Service | Local address |
| --- | --- |
| Angular site | <http://localhost:4200> |
| Backend API | <http://localhost:8080> |
| Swagger UI | <http://localhost:8080/swagger-ui/index.html> |
| PostgreSQL | `localhost:5432` |

Stop the containers with `docker compose down`. The named `postgres_data` volume is retained. Use `docker compose down -v` only if you intentionally want to delete the database volume and its contents.

## Run locally without Docker

Start PostgreSQL and create a database named `portfolio`. Set the backend environment variables, including the database password, admin credentials and JWT key paths, then run the backend from `PortfolioBackend`:

```bash
./mvnw spring-boot:run
```

On Windows, use the wrapper script:

```powershell
.\mvnw.cmd spring-boot:run
```

The default frontend proxy forwards `/api` requests to `http://localhost:8080`. In another terminal:

```bash
cd PortfolioFrontend
npm ci
npm start
```

Open <http://localhost:4200>. For local backend execution, point the JWT key-path variables to the generated key files on your machine. Set `PORTFOLIO_DATA_FILE` if the seed JSON is not at the backend's default `data/portfolio-data.json` path.

## REST API overview

All endpoints are prefixed with `/api`. Public `GET` requests are available for portfolio content. Create, update and delete operations for the resource collections require the `ADMIN` role.

| Resource | Base path | Available operations |
| --- | --- | --- |
| Authentication | `/auth` | `POST /login` |
| Profile | `/profile` | `GET`, `PUT` |
| Projects | `/projects` | `GET`, `GET /{id}`, `POST`, `PUT /{id}`, `DELETE /{id}` |
| Skill categories | `/categories` | `GET`, `GET /{id}`, `POST`, `PUT /{id}`, `DELETE /{id}` |
| Skills | `/skills` | `GET`, `GET /{id}`, `POST`, `PUT /{id}`, `DELETE /{id}` |
| Experience | `/experiences` | `GET`, `GET /{id}`, `POST`, `PUT /{id}`, `DELETE /{id}` |
| Education | `/educations` | `GET`, `GET /{id}`, `POST`, `PUT /{id}`, `DELETE /{id}` |
| Interests | `/interests` | `GET`, `GET /{id}`, `POST`, `PUT /{id}`, `DELETE /{id}` |

The API schema and interactive endpoint documentation are available at `/swagger-ui/index.html` while the backend is running.

To authenticate, send a `POST` request to `/api/auth/login` with the login request fields and use the returned token on protected requests:

```http
Authorization: Bearer <token>
```

The API is stateless and validates JWT signatures against the configured RSA public key. The current CORS configuration permits `http://localhost:4200` for local development.

## Development commands

From `PortfolioFrontend`:

```bash
npm start       # Start the Angular development server
npm run build   # Create a production build in dist/
npm test        # Run frontend unit tests
```

From `PortfolioBackend`:

```bash
./mvnw test
./mvnw package
```

Use `.\mvnw.cmd` instead of `./mvnw` in Windows PowerShell.

## Current implementation notes

- Only the home route is enabled in the Angular router. Other feature routes are currently commented out while the application is being developed.
- The current JPA configuration uses `ddl-auto: create`, which recreates the database schema at application startup. This can delete persisted content; change the schema-management strategy and validate migrations before using this setup with production data.
- The application currently configures local-development CORS and default local ports. Review these settings, secrets, database lifecycle and deployment configuration before production use.
- Redis, contact form handling, article management, CI/CD workflows and monitoring are not implemented in the current repository.