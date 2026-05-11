# Task Completion Checklist

When a coding task is complete, verify ALL of the following:

## Required Before Commit
1. [ ] `./gradlew test` — all tests pass
2. [ ] Spring Boot app starts without errors (`./gradlew bootRun`)
3. [ ] Self-review of implemented code (check for logic errors, security issues)
4. [ ] Report implementation + review results to user and get approval

## Workflow (from CLAUDE.md)
- Step 1: Receive GitHub Issue number → run `/create-gh-branch` → investigate related code
- Step 2: Draft implementation plan → present to user → get approval before coding
- Step 3: Implement → self-review → report → user approves (`yes`) or requests fix (`fix`)
- On approval: run tests → commit

## Commit
- Only commit after explicit user approval
- Run `./gradlew test` before every commit
