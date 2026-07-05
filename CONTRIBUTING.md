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
3. **Building**:
   - Run `./gradlew build` to compile the code and package the plugin JAR (found in `build/libs/`).
4. **Testing**:
   - Write JUnit 5 tests for any new domain or application logic.
   - Run tests using `./gradlew test`. All tests must pass before submitting a pull request.
5. **Code Style**:
   - Follow clean, descriptive naming conventions.
   - Do not use Lombok dependencies if not needed; prefer standard Java features (such as Records).
   - Format your code using spotless: run `./gradlew spotlessApply` to automatically format all Java and Gradle files. Code style rules are checked on pull requests.

## Documentation

The documentation site is built with [MkDocs](https://www.mkdocs.org/) and the [Material for MkDocs](https://squidfunk.github.io/mkdocs-material/) theme. Python dependencies are managed with [uv](https://docs.astral.sh/uv/).

### Prerequisites

- [uv](https://docs.astral.sh/uv/getting-started/installation/) installed.

### Local Preview

```bash
uv run mkdocs serve
```

This starts a dev server at `http://localhost:8000` with live reload.

### Build

```bash
uv run mkdocs build
```

Output goes to `site/` (git-ignored).

### Structure

```text
docs/
├── index.md                          # Landing page
├── server-admins/
│   ├── tutorials/
│   │   ├── installation.md           # Installation guide
│   │   └── quick-start.md            # Quick start tutorial
│   ├── how-to-guides/
│   │   ├── restrict-access.md        # How to manage player permissions
│   │   ├── change-fallback-language.md # How to configure the fallback locale
│   │   └── translations.md           # Translation system guide
│   ├── explanation/
│   │   └── language-resolution.md    # Detail on the locale resolution fallback system
│   └── reference/
│       ├── settings.md               # config.yml reference
│       ├── permissions.md            # Permissions reference
│       └── translations.md           # Locale and translation keys reference
└── developers/
    ├── how-to-guides/
    │   ├── dependency-setup.md       # Project dependency configuration
    │   ├── converting-ticks.md       # Minecraft ticks to LocalTime conversion usage
    │   └── querying-translations.md  # Fetching translations programmatically
    ├── reference/
    │   └── api.md                    # Core Javadoc & mappings reference
    └── explanation/
        └── architecture.md           # Structural design detail
mkdocs.yml                            # Site configuration & navigation
pyproject.toml                        # Python dependencies (uv)
```

### Adding a Page

1. Create a `.md` file under `docs/`.
2. Add the page to the `nav` section in `mkdocs.yml`.
3. Preview with `uv run mkdocs serve`.

### Deployment

Documentation is automatically deployed to GitHub Pages on every push to `main` via the `.github/workflows/deploy-docs.yml` workflow. No manual deployment is needed.

## Submitting Pull Requests

1. Fork the repository and create your branch from `main`.
2. Commit your changes with clear, descriptive commit messages.
3. Push to your fork and submit a Pull Request targeting `main`.
4. Ensure the build and tests pass successfully on your branch.

## Release Process

We follow a manual tagging and automated publication workflow for releases.

### 1. Update Version Numbers

Update the version string to the new release version in the following files:
*   [gradle.properties](./gradle.properties): `version = X.Y.Z`
*   [pyproject.toml](./pyproject.toml): `version = "X.Y.Z"`

### 2. Update Changelog

Document the new release's features, fixes, and changes in [CHANGELOG.md](./CHANGELOG.md) under a new version heading (following the [Keep a Changelog](https://keepachangelog.com/en/1.1.0/) format):

```markdown
## [X.Y.Z] - YYYY-MM-DD

### Added
- New feature description.
```

### 3. Dry-Run Verification (Optional)

Before pushing the release, you can dry-run the distribution tasks locally:

*   **Dry-run Hangar & Modrinth**:
    ```bash
    DRY_RUN=true JAVA_HOME=/usr/lib/jvm/java-21-openjdk ./gradlew publishPluginPublicationToHangar modrinth --no-daemon
    ```

### 4. Create and Push the Tag

Commit your changes, tag the commit, and push it:

```bash
git add gradle.properties pyproject.toml CHANGELOG.md
git commit -m "bump: release version X.Y.Z"
git tag vX.Y.Z
git push origin main --tags
```

The GitHub Actions release workflow will automatically:

1. Parse the latest section of `CHANGELOG.md` for release notes.
2. Build and verify the project using JDK 21.
3. Create a GitHub Release page with the compiled shadow JAR and release notes.
4. Publish the release to Hangar and Modrinth.
