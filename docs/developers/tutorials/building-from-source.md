# Build the plugin from source

If you prefer to compile the plugin yourself or customize the source code, follow this tutorial to build the ClockTime plugin.

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
