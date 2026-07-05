# Installation

## Requirements

- **Java** 21 or higher
- **Server** — Paper 1.20+, Purpur, or any compatible fork

## Download

Grab the latest release from the [GitHub Releases](https://github.com/beduality/clock-time/releases) page.

## Install

1. Place the `.jar` file into your server's `plugins/` directory.
2. Start or restart the server.
3. Confirm the plugin loaded:

```log
[ClockTime] ClockTime Plugin Enabled
```

!!! success "That's it"

    ClockTime works out of the box with zero configuration. All players can immediately right-click a clock to see the time.

## Build from Source

If you prefer to compile the plugin yourself:

```bash
git clone https://github.com/beduality/clock-time.git
cd clock-time
./gradlew shadowJar
```

The compiled JAR will be at:

```
build/libs/ClockTime-<version>.jar
```

!!! tip

    Use `./gradlew buildmv` if you have a local test server at `../server/data/plugins` — it will build, clean old JARs, and copy the new one automatically.
