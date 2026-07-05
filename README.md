# ClockTime Plugin

[Docs](https://beduality.github.io/clock-time/) | [Javadoc](https://beduality.github.io/clock-time/apidocs/) | [Hangar](https://hangar.papermc.io/beduality/clock-time) | [Modrinth](https://modrinth.com/plugin/clock-time) | [MIT License](./LICENSE)

Localized in-game time displays for Minecraft.

<p align="center">
  <img src="./docs/assets/images/clocktime-demo-day.png" alt="Day Demo" width="32%" />
  <img src="./docs/assets/images/clocktime-demo-night.png" alt="Night Demo" width="32%" />
  <img src="./docs/assets/images/clocktime-demo-nether.png" alt="Nether Demo" width="32%" />
</p>

## Features

- **Dynamic Localized Formatting**: Auto-detects and formats time using the player's client language locale.
- **Administrator Localization Overrides**: Customize translation files (`.properties`) dynamically in the plugin's data folder.
- **Dimension-Aware Time Resolution**: Detects environment details (like Nether or The End) where standard time has no meaning and displays custom message behaviors accordingly.

## Requirements

- **Minecraft Version**: 1.20+
- **API**: Paper / Purpur API
- **Java**: Java 21+

## Installation

1. Download the latest release `.jar` file from [Hangar](https://hangar.papermc.io/beduality/clock-time), [Modrinth](https://modrinth.com/plugin/clock-time), or the [GitHub Releases](https://github.com/beduality/clock-time/releases) page.

2. Place the file inside your server's `plugins/` directory.
3. Start or restart the server.

## Quick Start

1. Hold a **Clock** in your hand.
2. **Right-click** with the clock.
3. The current in-game time will be displayed in your chat according to your language.

## Support

If you run into issues or have feature requests, please report them via:
- [Discord Server](https://discord.gg/D5meCv2Wnd)
- [GitHub Issues](https://github.com/beduality/clock-time/issues)
