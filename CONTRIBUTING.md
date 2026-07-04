# Contributing to ClockTime

Thank you for your interest in contributing to ClockTime! To keep the project clean, robust, and maintainable, please follow these guidelines when submitting contributions.

## Architectural Guidelines

This project adheres to **Clean Architecture** principles to separate business logic from the Minecraft server API (Paper API):

1. **Separation of Concerns**:
   - Keep domain logic (e.g., tick math, conversions) in the `domain` package.
   - Keep application-level adapters (e.g., translating messages) in the `application` package.
   - Keep Bukkit/Paper listener code and configuration entry points in the `infrastructure` package.
2. **Pure Java**:
   - Code inside the `domain` and `application` layers must compile and run on pure Java (no Bukkit/Paper API dependencies).
3. **Dependency Injection**:
   - Do not use global singletons or static instance accessors.
   - Inject dependencies explicitly via constructors.

## Development Workflow

1. **Java Version**: Ensure you are using JDK 21 or higher.
2. **Build Tool**: Use the Gradle wrapper `./gradlew` rather than a global Gradle installation.
3. **Testing**:
   - Write JUnit 5 tests for any new domain or application logic.
   - Run tests using `./gradlew test`. All tests must pass before submitting a pull request.
4. **Code Style**:
   - Follow clean, descriptive naming conventions.
   - Do not use Lombok dependencies if not needed; prefer standard Java features (such as Records).

## Submitting Pull Requests

1. Fork the repository and create your branch from `main`.
2. Commit your changes with clear, descriptive commit messages.
3. Push to your fork and submit a Pull Request targeting `main`.
4. Ensure the build and tests pass successfully on your branch.
