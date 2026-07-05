# How to Configure Project Dependencies

This guide explains how to add ClockTime as a project dependency to build plugins that integrate with ClockTime.

## Build Configuration

ClockTime releases are hosted on JitPack. Add the repository and dependency coordinate to your project configuration.

=== "Gradle (Kotlin)"

    ```kotlin
    repositories {
        mavenCentral()
        maven("https://jitpack.io")
    }

    dependencies {
        compileOnly("com.github.beduality:clock-time:v1.0.0") // Replace with latest release tag
    }
    ```

=== "Gradle (Groovy)"

    ```groovy
    repositories {
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }

    dependencies {
        compileOnly 'com.github.beduality:clock-time:v1.0.0' // Replace with latest release tag
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
        <version>v1.0.0</version> <!-- Replace with latest release tag -->
        <scope>provided</scope>
    </dependency>
    ```

---

## Plugin Dependency Configuration

To declare ClockTime as a dependency in your plugin description file (`plugin.yml` or `paper-plugin.yml`), add it under the `depend` or `softdepend` properties:

```yaml title="plugin.yml"
name: MyPlugin
version: 1.0.0
main: com.example.MyPlugin
# Ensure ClockTime loads before your plugin
depend: [ClockTime]
```

!!! tip "Automate description files with Gradle"

    Instead of writing and maintaining `plugin.yml` manually, you can automate this using the [net.minecrell.plugin-yml](https://github.com/minecrell/plugin-yml) Gradle plugin:

    ```kotlin title="build.gradle.kts"
    plugins {
        id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
    }

    bukkit {
        main = "com.example.MyPlugin"
        // Declares dependencies dynamically
        depend = listOf("ClockTime")
    }
    ```
