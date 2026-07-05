# Architecture

This project strictly adheres to **Clean Architecture** principles to separate core business/time formatting logic from the Minecraft server API (Paper API). This makes components highly testable, modular, and maintainable.

---

## Architectural Layout

```mermaid
graph TD
    subgraph Infrastructure [Infrastructure Layer]
        ClockTimePlugin[ClockTimePlugin]
        ConfigLoader[ConfigLoader]
        TranslationRegistryManager[TranslationRegistryManager]
        ClockInteractListener[ClockInteractListener]
        PluginConfig[PluginConfig]
    end

    subgraph PureJava [Pure Java Layers]
        TimeFormatter[TimeFormatter]
    end

    subgraph Platform [Platform Services]
        GlobalTranslator[Adventure GlobalTranslator]
    end

    ClockTimePlugin --> |Uses| ConfigLoader
    ClockTimePlugin --> |Uses| TranslationRegistryManager
    ClockTimePlugin --> |Instantiates & Injects| ClockInteractListener
    ConfigLoader --> |Loads| PluginConfig
    TranslationRegistryManager --> |Registers| GlobalTranslator
    ClockInteractListener --> |Uses| TimeFormatter
    ClockInteractListener --> |Queries| GlobalTranslator
```

### 1. Domain Layer (`io.github.beduality.clock_time.domain.service`)

* **Role**: Houses pure business rules and calculations.
* **Dependencies**: None. Pure Java library.
* **Key Components**:
  * `TimeFormatter`: Translates abstract Minecraft ticks (0-24000) to standard `java.time.LocalTime` objects.

### 2. Infrastructure Layer (`io.github.beduality.clock_time.infrastructure` / plugin root)

* **Role**: Integrates the plugin with the Minecraft server platform (Paper/Bukkit) and third-party libraries (SpongePowered Configurate).
* **Dependencies**: Paper API, Domain layer, Configurate.
* **Key Components**:
  * `ClockTimePlugin`: Plugin lifecycle manager and Composition Root. Delegates config loading and translation registration to dedicated managers.
  * `PluginConfig` (under `infrastructure.config`): Maps YAML settings to a typed config class.
  * `ConfigLoader` (under `infrastructure.manager`): Manages YamlConfigurationLoader initialization, loading, validation, and version-based configuration transformations.
  * `TranslationRegistryManager` (under `infrastructure.manager`): Handles properties asset extraction from JAR, custom ClassLoader generation, and registers locales to Adventure's `GlobalTranslator`.
  * `ClockInteractListener` (under `infrastructure.listener`): Event listener handling player click actions, permission validation, and querying Adventure for localized translations.

---

## Request Flow Diagram

When a player right-clicks with a clock:

```mermaid
sequenceDiagram
    actor Player
    participant Listener as ClockInteractListener
    participant Formatter as TimeFormatter
    participant Translator as Adventure GlobalTranslator

    Player->>Listener: Right-click with Clock
    activate Listener
    Note over Listener: Verify Item & Permission
    Note over Listener: Get World Environment & Ticks
    Listener->>Formatter: formatTicks(ticks)
    Formatter-->>Listener: Return LocalTime
    Listener->>Translator: translate(key, locale)
    activate Translator
    Note over Translator: Resolves text via registered TranslationRegistry
    Translator-->>Listener: Return localized pattern format
    deactivate Translator
    Note over Listener: Format pattern with time & deserialize tags
    Listener->>Player: Send message (chat time display)
    deactivate Listener
```


