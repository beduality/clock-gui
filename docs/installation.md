# Installation Guide

## Requirements

* JDK (Java Development Kit) 21 or higher.
* A Paper, Purpur, or compatible Minecraft server running version 1.20 or higher.

## Download & Build

### 1. Download

You can download pre-built releases directly from the GitHub releases page, or compile it from source.

### 2. Build from Source

```bash
git clone https://github.com/beduality/clock-time
cd clock-time
./gradlew build
```

The compiled plugin will be located at `build/libs/ClockTime-0.1.0.jar`.

## Installation Steps

1. Place the generated `.jar` file into your server's `plugins/` directory.
2. Start or restart the server.
3. Verify that the plugin loaded by checking for the following startup log:
   ```
   [ClockTime] ClockTime Plugin Enabled
   ```
