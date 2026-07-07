# ClockTime

[![Modrinth](https://img.shields.io/badge/Modrinth-Fabric%20%2F%20Paper-00AD5F?style=for-the-badge)](https://modrinth.com/plugin/clock-time)
[![Hangar](https://img.shields.io/badge/Hangar-Paper-0052FF?style=for-the-badge)](https://hangar.papermc.io/beduality/clock-time)
[![GitHub](https://img.shields.io/badge/GitHub-Repository-181717?style=for-the-badge&logo=github)](https://github.com/beduality/clock-time)
[![Discord](https://img.shields.io/badge/Discord-Community-5865F2?style=for-the-badge&logo=discord)](https://discord.gg/D5meCv2Wnd)
[![Documentation](https://img.shields.io/badge/Docs-Read-FF5722?style=for-the-badge)](https://beduality.github.io/clock-time/)

**ClockTime** is a lightweight, zero-configuration, **multi-platform (Paper/Spigot & Fabric)** Minecraft plugin and mod that enhances the in-game clock. Players can simply right-click with a clock in their hand to display the current in-game time in their chat, fully formatted and localized according to their Minecraft client language!

---

## Features

- **Realtime Clocks in Item Frames (Paper/Spigot)**: Displays current world time dynamically on clocks placed in item frames. Updates are optimized to only modify item name tags when the Minecraft minute actually changes.
- **Full Multi-Platform Compatibility**: Run it seamlessly as a server-side plugin on **Paper, Purpur, and Spigot**, or as a server/client mod on **Fabric**. Enjoy 100% feature parity across all platforms.
- **Client-Side Locale Detection**: Automatically formats and translates the time according to the player's client language (supporting both 12-hour AM/PM and 24-hour formats natively).
- **Expandable Translation System**: Bundled with 16+ built-in languages. Scans the `languages/` subfolder dynamically to load custom properties files (e.g. `messages_sv.properties`), allowing effortless expansion.
- **Dimension-Aware Time Resolution**: Detects environments where standard time has no meaning (like the Nether or The End) and displays a special wild-spin message.
- **Custom Dimension Support**: Define custom or modded worlds/dimensions in the configuration that should also exhibit wild-spin clock behavior.
- **Easy Setup & Low Impact**: Zero-configuration needed out of the box. Drop the jar in, and you're ready!

---

## Media / Showcase

| Day Demo | Night Demo | Nether Demo |
| :---: | :---: | :---: |
| ![Day](https://raw.githubusercontent.com/beduality/clock-time/main/docs/assets/images/clocktime-demo-day.png) | ![Night](https://raw.githubusercontent.com/beduality/clock-time/main/docs/assets/images/clocktime-demo-night.png) | ![Nether](https://raw.githubusercontent.com/beduality/clock-time/main/docs/assets/images/clocktime-demo-nether.png) |

---

## How to Play

1. Hold a **Clock** in your **main hand**.
2. **Right-click**.
3. View the in-game time in chat, printed in your Minecraft language!

---

## Installation

### Fabric (Server-Side or Client/Singleplayer)

1. Ensure you have the [Fabric Loader](https://fabricmc.net/) and [Fabric API](https://modrinth.com/mod/fabric-api) installed.
2. Download the latest release `.jar` and place it in your `mods/` folder.
3. Start or restart the game/server.

### Paper / Purpur / Spigot

1. Download the latest release `.jar`.
2. Place it in your server's `plugins/` directory.
3. Start or restart the server.

---

## Configuration & Permissions

### `config.yml`

```yaml
# The fallback language code to use if a player's client language
# is not supported.
fallback-language: "en"

# Internal config version. Do not modify.
config-version: 2

# Custom world names or dimension keys (e.g. 'custom_world' or 'custom:space')
# that should be treated as wild-spin dimensions.
wild-spin-worlds: []

# Settings for clocks placed in item frames
item-frame-clocks:
  enabled: true
  update-interval: 16
```

### Permissions

- **`clock_time.use`** *(Default: `true`)* - Allows players to right-click a clock to view the formatted time.

---

## Support & Community

- **Bug Reports & Requests:** [GitHub Issues](https://github.com/beduality/clock-time/issues)
- **Discord Community:** [Join Discord](https://discord.gg/D5meCv2Wnd)
- **Detailed Manuals:** [Official Documentation](https://beduality.github.io/clock-time/)
