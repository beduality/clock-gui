# Building from Source

If you prefer to compile the plugin yourself or customize the source code, follow this guide to build the ClockTime plugin.

## Prerequisites

- **Java Development Kit (JDK)**: Version 21 or higher.
- **Git**: Installed and configured on your system path.

## Compile the Plugin

1. Clone the repository:
   ```bash
   git clone https://github.com/beduality/clock-time.git
   cd clock-time
   ```

2. Build the shadowed JAR using Gradle:
   ```bash
   ./gradlew shadowJar
   ```

The compiled JAR will be located at:
```
build/libs/ClockTime-<version>.jar
```

!!! tip

    Use `./gradlew buildmv` if you have a local test server at `../server/data/plugins` — it will build, clean old JARs, and copy the new one automatically.
