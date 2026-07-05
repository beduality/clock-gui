# ClockTime Plugin

A lightweight, clean-architecture Minecraft plugin that enhances the in-game clock by providing quick-access, localized time displays in customizable formats.

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

Detailed guides and specifications are available in the [docs](./docs/) directory:

* 📥 **[Installation Guide](./docs/installation.md)**: Requirements, compilation, and setup.
* ⚙️ **[Configuration](./docs/configuration.md)**: Settings and translation resources.
* 🔑 **[Permissions](./docs/permissions.md)**: Group permission references.
* 🛠️ **[Developer API](./docs/api.md)**: Leveraging decoupled library components.
* 📐 **[Architecture](./docs/architecture.md)**: Design principles and module flow.

## Support

If you run into issues or have feature requests, please report them via:
- [Discord Server](https://discord.gg/D5meCv2Wnd)
- [GitHub Issues](https://github.com/beduality/clock-time/issues)

## License

This project is licensed under the MIT License - see the [LICENSE](./LICENSE) file for details.
