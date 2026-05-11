# Code Style and Conventions

## Java Conventions
- Java 21 features can be used
- Lombok `@Data` on entity classes (auto-generates getters/setters/equals/hashCode/toString)
- Constructor injection for dependencies (via Lombok `@RequiredArgsConstructor`)
- No comments unless explaining non-obvious WHY

## Naming
- Classes: PascalCase (e.g., TaskService, TaskEntity)
- Methods/fields: camelCase
- DB columns: snake_case → mapped to camelCase via MyBatis `map-underscore-to-camel-case=true`
- Entity classes: suffixed with `Entity` (e.g., TaskEntity)
- Repository interfaces: suffixed with `Repository`
- DTOs: suffixed with `DTO` (e.g., UpdateStatusDTO)

## Architecture Rules (from CLAUDE.md)
- Service layer: all business logic
- Controller layer: request/response bridging only
- Single Responsibility per class/method
- TDD: Red → Green → Refactor cycle

## Testing
- Controller tests: Spring MockMvc + `@WebMvcTest`, mock services
- Service tests: plain unit tests with mocked repositories
- Test classes use nested `@Nested` inner classes grouped by behavior
- Test file: `src/test/java/in/tech_camp/task_checker_back/`
