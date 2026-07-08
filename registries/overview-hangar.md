# ClockTime

[![Hangar](https://img.shields.io/badge/Hangar-Paper-0052FF?style=for-the-badge)](https://hangar.papermc.io/beduality/clock-time)
[![Modrinth](https://img.shields.io/badge/Modrinth-Fabric%20%2F%20Paper-00AD5F?style=for-the-badge)](https://modrinth.com/plugin/clock-time)
[![GitHub](https://img.shields.io/badge/GitHub-Repository-181717?style=for-the-badge&logo=github)](https://github.com/beduality/clock-time)
[![Discord](https://img.shields.io/badge/Discord-Community-5865F2?style=for-the-badge&logo=discord)](https://discord.gg/D5meCv2Wnd)
[![Documentation](https://img.shields.io/badge/Docs-Read-FF5722?style=for-the-badge)](https://beduality.github.io/clock-time/)

**ClockTime** is a lightweight, zero-configuration Minecraft plugin that enhances the in-game clock. It allows players to simply right-click with a clock in their hand to display the current in-game time in their chat, fully formatted and localized according to their Minecraft client language.

---

## Features

- **Realtime Clocks in Item Frames**: Displays current world time dynamically on clocks placed in item frames. Updates are optimized to only modify item name tags when the Minecraft minute actually changes.
- **Bedrock Compatible**: Time strings in item frame custom names can be encoded with non-breaking spaces (enabled by default) so Bedrock clients (e.g., via Geyser) render them correctly.
- **Dynamic Localized Formatting**: Auto-detects and formats time using the player's client language locale (supporting both 12-hour AM/PM and 24-hour formats natively).
- **Extensible Translations**: Ships with 16+ built-in languages. Scans the `languages/` subfolder dynamically to load custom properties files (e.g. `messages_sv.properties`), allowing effortless expansion.
- **Dimension-Aware Time**: Detects environments where standard time has no meaning (like the Nether or The End) and displays a special wild-spin message.
- **Custom Dimension Support**: Easily define custom or modded worlds/dimensions in the configuration that should also exhibit wild-spin behaviors.
- **Zero Configuration**: Drop the jar in, and you're good to go!

---

## Media / Showcase

| Day Demo | Night Demo | Nether Demo |
| :---: | :---: | :---: |
| ![Day](https://raw.githubusercontent.com/beduality/clock-time/main/docs/assets/images/clocktime-demo-day.png) | ![Night](https://raw.githubusercontent.com/beduality/clock-time/main/docs/assets/images/clocktime-demo-night.png) | ![Nether](https://raw.githubusercontent.com/beduality/clock-time/main/docs/assets/images/clocktime-demo-nether.png) |
| ![Realtime](https://raw.githubusercontent.com/beduality/clock-time/main/docs/assets/images/clocktime-demo-realtime.png) |

---

## How to Use

1. Obtain a **Clock**.
2. Hold it in your **main hand**.
3. **Right-click**.
4. The current time will display in chat matching your client language!

---

## Configuration & Permissions

### `config.yml`

```yaml
# The fallback language code to use if a player's client language
# is not supported.
fallback-language: "en"

# Internal config version. Do not modify.
config-version: 3

# Custom world names or dimension keys (e.g. 'custom_world' or 'custom:space')
# that should be treated as wild-spin dimensions.
wild-spin-worlds: []

# Settings for clocks placed in item frames
item-frame-clocks:
  enabled: true
  update-interval: 16
  encode-spaces: true
```

### Permissions

- **`clock_time.use`** *(Default: `true`)* - Allows players to right-click a clock to view the formatted time.

---

## Installation

### Paper / Purpur / Spigot

1. Download the latest release `.jar` file.
2. Place the file inside your server's `plugins/` directory.
3. Start or restart the server.

---

## Support & Feedback

If you run into issues, want to request features, or want to contribute translations, join us:

- **[Discord Server](https://discord.gg/D5meCv2Wnd)**
- **[GitHub Issues](https://github.com/beduality/clock-time/issues)**
- **[Official Documentation](https://beduality.github.io/clock-time/)**
