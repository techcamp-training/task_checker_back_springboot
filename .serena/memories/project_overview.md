# Project Overview: task-checker-api

## Purpose
Spring Boot REST API backend for a task management application (Task Checker).
Provides CRUD endpoints for Tasks and Genres.

## Tech Stack
- Java 21
- Spring Boot 3.5.14
- MyBatis 3.0.5 (ORM / SQL mapping)
- Flyway (DB migration)
- PostgreSQL (localhost:5432/task_checker_db)
- Lombok (code generation)
- JUnit 5 + MockMvc (testing)
- Gradle (build tool)

## Package Structure
Base package: `in.tech_camp.task_checker_back`

- `controller/`   — REST controllers (TaskController, GenreController)
- `service/`      — Business logic (TaskService, TaskDuplicateService)
- `repository/`   — MyBatis mapper interfaces (TaskRepository, GenreRepository)
- `entity/`       — DB entity classes (TaskEntity, GenreEntity)
- `dto/`          — Request DTOs (UpdateStatusDTO)
- `config/`       — Configuration classes (CorsConfig)

## DB Schema (Flyway migrations in src/main/resources/db/migration/)
- V1: genres table
- V2: tasks table (id, name, explanation, deadline_date, status, genre_id, created_at, updated_at)
- V3: initial seed data
- V4: priority column added to tasks

## Architecture Pattern
Controller → Service → Repository
- Controllers: handle HTTP request/response only
- Services: contain all business logic
- Repositories: MyBatis interfaces for DB access
