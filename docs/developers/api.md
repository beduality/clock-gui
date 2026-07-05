# API Reference

ClockTime's core logic is fully decoupled from the Bukkit API. Developers can reuse the time conversion engine or hook into the translation system through standard Kyori Adventure APIs.

You can view the complete [Javadoc API Reference](../../apidocs/index.html).

## Dependency Configuration

To use ClockTime in your own project, add the JitPack repository and the dependency declaration to your build file.

=== "Gradle (Kotlin)"

    ```kotlin
    repositories {
        mavenCentral()
        maven("https://jitpack.io")
    }

    dependencies {
        compileOnly("com.github.beduality:clock-time:v1.0.0") // Replace with latest tag
    }
    ```

=== "Gradle (Groovy)"

    ```groovy
    repositories {
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }

    dependencies {
        compileOnly 'com.github.beduality:clock-time:v1.0.0' // Replace with latest tag
    }
    ```

=== "Maven"

    ```xml
    <repositories>
        <repository>
            <id>jitpack.io</id>
            <url>https://jitpack.io</url>
        </repository>
    </repositories>

    <dependency>
        <groupId>com.github.beduality</groupId>
        <artifactId>clock-time</artifactId>
        <version>v1.0.0</version> <!-- Replace with latest tag -->
        <scope>provided</scope>
    </dependency>
    ```

## Plugin Dependency Configuration

To ensure your plugin loads after ClockTime, declare it as a dependency in your `plugin.yml`:

```yaml
name: MyPlugin
version: 1.0.0
main: com.example.MyPlugin
# Require ClockTime to be installed and loaded first
depend: [ClockTime]
# OR if ClockTime is optional
# softdepend: [ClockTime]
```

## TimeFormatter

The domain service that converts Minecraft ticks to Java `LocalTime` objects.

**Package:** `io.github.beduality.clock_time.domain.service`

### Usage

```java
import io.github.beduality.clock_time.domain.service.TimeFormatter;

TimeFormatter formatter = new TimeFormatter();
java.time.LocalTime time = formatter.formatTicks(6000); // 12:00 (noon)
```

### Tick-to-Time Mapping

| Minecraft Tick | Real Time | Event |
|---|---|---|
| `0` | 6:00 AM | Sunrise |
| `6000` | 12:00 PM | Noon |
| `12000` | 6:00 PM | Sunset |
| `18000` | 12:00 AM | Midnight |

### Properties

| Property | Value |
|---|---|
| Thread-safe | :material-check: Yes — completely stateless |
| Nullable params | :material-close: No — `null` is not supported |
| Nullable return | :material-close: No — always returns a value |
| Dependencies | None — pure Java |

---

## Adventure Translation Keys

ClockTime registers translations with Kyori Adventure's `GlobalTranslator` under the namespace `clocktime:main`. You can query them programmatically.

### Keys

| Key | Description | Args |
|---|---|---|
| `clock_time.message.time` | Time display message | `{0}` = formatted time string |
| `clock_time.message.wild-spin` | Nether/End message | — |

### Example

```java
import net.kyori.adventure.translation.GlobalTranslator;
import java.util.Locale;

// Resolve the wild-spin message in English
var format = GlobalTranslator.translator()
    .translate("clock_time.message.wild-spin", Locale.ENGLISH);

if (format != null) {
    String result = format.format(new Object[]{});
}
```

!!! note

    Translations are available after ClockTime has finished loading. If you query them from another plugin, make sure ClockTime is listed as a dependency or loads first.
