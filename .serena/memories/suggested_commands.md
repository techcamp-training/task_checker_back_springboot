# Suggested Commands

## Test & Build
- Run tests:       `./gradlew test`
- Build & Run:     `./gradlew bootRun`
- Clean build:     `./gradlew clean build`

## Database
- DB: PostgreSQL at localhost:5432/task_checker_db
- User: myrole / Password: kotonoha
- Migrations: Flyway (auto-applied on startup)

## Git
- Standard git commands available
- GitHub CLI: `gh` (installed)
- Repo: https://github.com/akihiro-usami/task_checker_back_springboot
- Upstream: https://github.com/techcamp-training/task_checker_back_springboot

## Notes
- No separate linting/formatting tools configured — Gradle handles compile checks
- Tests must all pass before committing (CLAUDE.md requirement)
