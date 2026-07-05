# ClockTime Plugin

Localized in-game time displays for Minecraft.

## Features

- **Dynamic Localized Formatting**: Auto-detects and formats time using the player's client language locale, including native AM/PM translations.
- **Custom Format Overrides**: Force 12-hour or 24-hour clocks globally for all players.
- **Administrator Localization Overrides**: Customize translation files (`.properties`) dynamically in the data folder.
- **Decoupled Engine**: Core calculations are separated from the Paper/Bukkit API for testing and reuse.

## Requirements

- **Minecraft Version**: 1.20+
- **API**: Paper / Purpur API
- **Java**: Java 21+

## Installation

1. Download the latest release `.jar` file.
2. Place the file inside your server's `plugins/` directory.
3. Start or restart the server.

## Quick Start

1. Hold a **Clock** in your hand.
2. **Right-click** with the clock.
3. The current in-game time will be displayed in your chat according to your language.

## Documentation

Full documentation is available at **[beduality.github.io/clock-time](https://beduality.github.io/clock-time/)**.

## Support

If you run into issues or have feature requests, please report them via:
- [Discord Server](https://discord.gg/D5meCv2Wnd)
- [GitHub Issues](https://github.com/beduality/clock-time/issues)

## License

This project is licensed under the MIT License - see the [LICENSE](./LICENSE) file for details.
