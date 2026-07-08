# Contributing to ClockTime

Thank you for your interest in contributing to ClockTime! To keep the project clean, robust, and maintainable, please follow these guidelines when submitting contributions.

## Architectural Guidelines
 
This project is structured as a **Multi-Module Gradle project** under the `modules/` directory to separate platform-agnostic business logic from specific platform wrappers:
 
1. **Domain Isolation (`clock-time-common` under `modules/clock-time-common`)**:
   - Contains all domain models (e.g., `WorldInfo` interface) and core services (`TimeFormatter`, `LocaleTimeFormatter`, `DimensionTimeResolver`, `ClockMessageService`).
   - Must compile and run on pure Java (depends only on standard Java APIs and Kyori Adventure). Strictly **no platform-specific dependencies** (no Paper/Bukkit/Mojang/Fabric API).
2. **Infrastructure Modules (`clock-time-paper`, `clock-time-fabric` under `modules/`)**:
   - Provide concrete implementations of the domain adapters (e.g. `PaperWorldInfo`, `FabricWorldInfo`).
   - Handle config loaders, resource translations setup, and event listening (`ClockInteractListener`, `FabricClockInteractListener`).
3. **Dependency Injection**:
   - Do not use global singletons or static instance accessors.
   - Inject dependencies explicitly via constructors.
 
## Development Workflow
 
1. **Java Version**: Ensure you are using JDK 21 or higher (due to Gradle compatibility issues, prefix commands with `JAVA_HOME=/usr/lib/jvm/java-21-openjdk` if your system default is Java 26+).
2. **Build Tool**: Use the Gradle wrapper `./gradlew` rather than a global Gradle installation.
3. **Building**:
   - Run `./gradlew build` to compile and package all module artifacts.
   - Built artifacts are generated in:
     - Paper Plugin JAR: `modules/clock-time-paper/build/libs/ClockTime-*.jar`
     - Fabric Mod JAR: `modules/clock-time-fabric/build/libs/clock-time-fabric-*.jar`
4. **Testing**:
   - Write Mockito/JUnit 5 tests for any new domain or application logic under the `common` test suite.
   - Run tests using `./gradlew test`. All tests must pass before submitting a pull request.
5. **Code Style**:
   - Follow clean, descriptive naming conventions.
   - Do not use Lombok dependencies if not needed; prefer standard Java features (such as Records).
   - Format your code using Spotless: run `./gradlew spotlessApply` to automatically format all Java and Gradle files. Code style rules are checked on pull requests.

## Documentation

The documentation site is built with [MkDocs](https://www.mkdocs.org/) and the [Material for MkDocs](https://squidfunk.github.io/mkdocs-material/) theme. Python dependencies are managed with [uv](https://docs.astral.sh/uv/).

### Prerequisites

- [uv](https://docs.astral.sh/uv/getting-started/installation/) installed.

### Local Preview

Since we use `mike` for documentation versioning, you can preview the multi-version documentation locally:

1. Build the documentation versions you want to test (e.g. the unreleased docs):
   ```bash
   uv run mike deploy -t "Unreleased" unreleased
   uv run mike set-default unreleased
   ```

2. Start the local server:
   ```bash
   uv run mike serve
   ```

This starts a local server at `http://localhost:8000` serving all built versions.

### Build

```bash
uv run mike deploy <version> <alias>
```

Output goes to `site/` (git-ignored).

### Rebuilding All Versions

If you change templates, overrides, hooks, or configurations and want to apply them across all historical versions (e.g. `0.1.0` through `0.4.0` and `unreleased`), you can run:

```bash
python3 scripts/redeploy_all_docs.py
```

This automation script checks out each tag, applies the latest config/overrides/hooks from the `main` branch, compiles the respective Javadocs, and deploys it to the local `gh-pages` branch using `mike`.

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

We use an automated release script to handle bumping versions, updating the changelog, running dry-run verification, tagging, and pushing, with built-in rollback capabilities.

### Running the Release Script

You can run the release script using `uv`:

```bash
uv run scripts/release.py
```

To see all available CLI modes, options, and commands (including how to trigger a rollback), query the help command:

```bash
uv run scripts/release.py --help
```
