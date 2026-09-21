My current portfolio : http://13.60.55.204/

# Full-Stack Developer Portfolio (WIP)

A production-oriented personal portfolio built as a full-stack web application.

The project goes beyond a traditional static portfolio: it provides a public-facing website, a secured administration area, persistent content management, caching, event-driven processing, automated testing and deployment.

The application is designed to demonstrate practical skills in **Java, Spring Boot, Angular, TypeScript, PostgreSQL, Docker, AWS, Redis ** through a single coherent architecture.

---

## Overview

The application provides two main areas:

### Public website

Visitors can:

* View personal information and professional profile
* Browse projects
* View technical skills
* Explore professional experience
* Access external project resources
* Submit a contact request

### Administration area

The administrator can:

* Authenticate securely
* Create, update and delete projects
* Manage skills and professional experience
* Manage articles
* Manage profile
* Manage interests
* Manage education
* Review contact messages
* Manage portfolio content without modifying the frontend source code

The portfolio is therefore both a **personal presentation website** and a demonstration of a complete software architecture.

---

# Architecture

```text
                         ┌──────────────────────┐
                         │       Visitors       │
                         │      / Recruiters    │
                         └──────────┬───────────┘
                                    │
                                  HTTPS
                                    │
                                    ▼
                       ┌──────────────────────────┐
                       │   Angular + TypeScript   │
                       │                          │
                       │     Public Portfolio     │
                       │    Administration UI     │
                       └────────────┬─────────────┘
                                    │
                               REST / JSON
                                    │
                                    ▼
                ┌────────────────────────────────────────┐
                │          Java / Spring Boot            │
                │                                        │
                │                REST API                │
                │                                        │
                │   Controller → Service → Repository    │
                └────────────────────┬───────────────────┘
                                     │
                     ┌───────────────┼
                     │               │              
                     ▼               ▼              
              ┌─────────────┐ ┌─────────────┐ 
              │ PostgreSQL  │ │    Redis    │ 
              │             │ │             │ 
              │ Persistence │ │    Cache    │ 
              └─────────────┘ └─────────────┘ 

                ┌────────────────────────────────────────┐
                │ Infrastructure                         │
                │                                        │
                │ Docker → CI/CD → AWS                   │
                │ Logs → Monitoring → HTTPS              │
                └────────────────────────────────────────┘
```

The architecture is intentionally progressive: the core application remains a conventional Spring Boot application, while specialized technologies are introduced only where they provide a clear architectural benefit.

---

# Technology Stack

| Layer             | Technology                                  |
| ----------------- | ------------------------------------------- |
| Frontend          | Angular 22                                  |
| Frontend language | TypeScript                                  |
| Backend           | Java 25 LTS                                 |
| Backend framework | Spring Boot                                 |
| Build tool        | Maven                                       |
| API               | REST / JSON                                 |
| Persistence       | PostgreSQL                                  |
| ORM               | Spring Data JPA / Hibernate                 |
| Validation        | Jakarta Validation                          |
| Cache             | Redis                                       |
| Testing           | Spring Boot Test / JUnit                    |
| Containerization  | Docker                                      |
| CI/CD             | GitHub Actions                              |
| Cloud             | AWS                                         |
| Monitoring        | Application and infrastructure logs/metrics |

---

# Backend Architecture

The Java backend is the central component of the application.

It follows a layered architecture that separates HTTP handling, business logic, persistence and data transfer.

```text
                         REST Request
                              │
                              ▼
                     ┌──────────────────┐
                     │    Controller    │
                     │                  │
                     │    HTTP / REST   │
                     └────────┬─────────┘
                              │
                              ▼
                     ┌──────────────────┐
                     │     Service      │
                     │                  │
                     │  Business Logic  │
                     └────────┬─────────┘
                              │
                              ▼
                     ┌──────────────────┐
                     │    Repository    │
                     │                  │
                     │    Data Access   │
                     └────────┬─────────┘
                              │
                              ▼
                          PostgreSQL
```

## Package structure

```text
com.neoblishange.portfolio
│
├── controller/
│
├── service/
│
├── repository/
│
├── entity/
│
├── dto/
│
├── mapper/
│
├── exception/
│
├── security/
│
├── config/
│
└── PortfolioApplication.java
```

### Controller

Controllers expose the REST API and handle HTTP concerns.

They are responsible for:

* Receiving HTTP requests
* Validating request parameters
* Calling application services
* Returning appropriate HTTP responses

Controllers do not contain business logic.

### Service

Services contain the application's business logic.

They coordinate operations such as:

* Creating and updating portfolio content
* Applying business rules
* Coordinating repositories
* Triggering asynchronous operations
* Interacting with external services

Keeping business logic in services prevents controllers from becoming tightly coupled to application rules.

### Repository

Repositories handle persistence through Spring Data JPA.

They abstract database access from the business layer and provide operations such as:

* Finding entities
* Saving entities
* Updating entities
* Deleting entities
* Executing custom queries

### Entity

Entities represent the persistence model used by JPA/Hibernate.

They map Java objects to PostgreSQL tables.

### DTO

Data Transfer Objects define the data exchanged through the REST API.

DTOs deliberately separate the external API contract from the internal persistence model.

For example:

```text
     HTTP Request
          │
          ▼
 CreateProjectRequest
          │
          ▼
        Mapper
          │
          ▼
       Project
          │
          ▼
      PostgreSQL
```

This prevents database entities from becoming the public API contract.

### Exception handling

A centralized exception handling mechanism provides consistent HTTP error responses.

Examples include:

```text
400 Bad Request
404 Not Found
401 Unauthorized
403 Forbidden
500 Internal Server Error
```

---

# Frontend Architecture

The frontend is implemented with **Angular and TypeScript**.

Its responsibilities include:

* Rendering the public portfolio
* Communicating with the REST API
* Managing administration interfaces
* Handling client-side navigation
* Managing forms and validation
* Presenting API errors appropriately

The frontend does not directly access PostgreSQL.

```text
         Angular
            │
            │ HTTP / JSON
            │
            ▼
   Spring Boot REST API
            │
            ▼
        PostgreSQL
```

This keeps the frontend independent of the persistence layer.

---

# Data Architecture

PostgreSQL is the primary source of persistent application data.

Typical entities include:

```text
Project
Experience
Skill
ContactMessage
User
Profile
Interest
Education
```

The database is accessed exclusively through the Java backend.

```text
          Angular
             │
             │ REST
             │
             ▼
        Spring Boot
             │
             │ JPA / Hibernate
             │
             ▼
         PostgreSQL
```

This separation provides a clear security boundary and prevents clients from accessing the database directly.

---

# Authentication and Security

The administration area is protected by Spring Security.

The public portfolio remains accessible without authentication, while administrative operations require an authenticated user.

Conceptually:

```text
Public API
    │
    ├── GET projects
    ├── GET skills
    ├── GET experience
    ├── POST contact
    └──...      
Admin API
    │
    ├── Authentication
    ├── Project management
    ├── Skill management
    ├── Contact management
    └──...
```

Security is handled at the backend rather than relying exclusively on frontend restrictions.

---

# Caching with Redis

Redis is used as a high-speed cache for data that is expensive or unnecessary to retrieve repeatedly.

For example:

```text
Angular
   │
   ▼
Spring Boot
   │
   ├── Cache hit  ─────► Redis
   │
   └── Cache miss ─────► PostgreSQL
```

Potential cache candidates include:

* Public project lists
* Skills
* Frequently requested portfolio content
* Temporary application data

PostgreSQL remains the source of truth.

Redis is therefore an optimization layer, not the primary database.

---

# Infrastructure

The application is containerized using Docker.

A typical deployment consists of independent services:

```text
┌───────────────────────────────────────────────┐
│                     AWS                       │
│                                               │
│  ┌─────────────┐     ┌───────────────┐        │
│  │   Angular   │     │  Spring Boot  │        │
│  │  Container  │     │   Container   │        │
│  └─────────────┘     └───────┬───────┘        │
│                              │                │
│                 ┌────────────┼                │
│                 ▼            ▼                │
│            PostgreSQL      Redis              │
│                                               │
└───────────────────────────────────────────────┘
```

Docker provides reproducible environments between development, testing and production.

---

# CI/CD

The project uses automated CI/CD to validate and deploy changes.

A typical pipeline is:

```text
      Git Push
         │
         ▼
   GitHub Actions
         │
         ├── Build
         ├── Unit Tests
         ├── Integration Tests
         ├── Code Quality Checks
         ├── Docker Build
         │
         ▼
     Deployment
         │
         ▼
        AWS
```

The goal is to make every change automatically testable and deployable.

---

# Architectural Principles

The project follows several principles.

### Separation of concerns

Each layer has a clearly defined responsibility.

```text
Controller  → HTTP
Service     → Business logic
Repository  → Persistence
Entity      → Database model
DTO         → API contract
```

### API-first communication

The frontend and backend communicate through a documented REST API rather than sharing implementation details.

### Database independence

The frontend never accesses PostgreSQL directly.

### Specialized services

Redis is used for caching rather than replacing PostgreSQL.

### Progressive complexity

The core application remains simple:

```text
Angular
   ↓
Spring Boot
   ↓
PostgreSQL
```

Additional infrastructure is introduced only when it solves a real problem:

```text
Redis  → caching
Docker → reproducible deployment
AWS    → production hosting
```

This avoids adding technologies purely for demonstration purposes.

---

# Project Goals

The project is designed to demonstrate practical knowledge of:

* Java development
* Spring Boot
* REST API design
* SQL and PostgreSQL
* JPA / Hibernate
* Clean backend architecture
* Angular and TypeScript
* Authentication and authorization
* Automated testing
* Docker
* CI/CD
* AWS deployment
* Logging and monitoring
* Redis caching
* Event-driven architecture

The final result is intended to be a **realistic production-oriented application**, rather than a collection of disconnected technology demos.

---

# Final Architecture

```text
                              INTERNET
                                  │
                                HTTPS
                                  │
                                  ▼
                        ┌───────────────────┐
                        │   Angular / TS    │
                        │                   │
                        │ Portfolio + Admin │
                        └─────────┬─────────┘
                                  │
                               REST API
                                  │
                                  ▼
                    ┌────────────────────────────┐
                    │   Java 25 / Spring Boot    │
                    │                            │
                    │ Controllers                │
                    │ Services                   │
                    │ Repositories               │
                    │ DTOs / Mappers             │
                    │ Security                   │
                    └───────┬────────────┬───────┘
                            │            │
                 ┌──────────┘            └──────────┐
                 ▼                                  ▼
          ┌─────────────┐                    ┌─────────────┐
          │  PostgreSQL │                    │    Redis    │
          │             │                    │             │
          │  Source of  │                    │    Cache    │
          │    Truth    │                    │             │
          └─────────────┘                    └─────────────┘                                              

                   ┌──────────────────────────────┐
                   │    Docker / GitHub Actions   │
                   │                              │
                   │ CI/CD / Testing / Deployment │
                   └──────────────┬───────────────┘
                                  │
                                  ▼
                                 AWS
```

The architecture combines a conventional **Java/Spring enterprise backend** with a modern **Angular frontend**, relational persistence, cloud infrastructure, caching and event-driven processing while maintaining clear separation of responsibilities between each component.
