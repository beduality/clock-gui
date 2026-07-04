# ClockTime Plugin

**ClockTime** is a Minecraft plugin that enhances the in-game clock by providing a quick-access time check. When players right-click with a clock in hand, they are sent a colorful chat message displaying the current in-game time in a **12-hour format**.

## Features

- **12-Hour Time Format**: Displays the current in-game time as `HH:MM AM/PM`, calculated from Minecraft's internal day-night cycle.
- **Quick Access**: Right-click with a clock to view the time in chat instantly.
- **Dimension Awareness**: Correctly handles dimensions like the Nether and The End where time has no meaning by warning the player that the clock is spinning wildly.
- **Internationalization (i18n)**: Automatically translates time and error messages to the player's client language.
- **Clean Chat Messages**: Lightweight, colorized text display that doesn't clutter the gameplay experience.

### Supported Languages:

- English (US)
- Spanish (es)
- Portuguese (pt)
- French (fr)
- German (de)
- Japanese (ja)
- Korean (ko)
- Italian (it)
- Polish (pl)
- Turkish (tr)
- Traditional Chinese (zh_TW)
- Ukrainian (uk)
- Dutch (nl)

## Requirements

To build and run this plugin, you will need:

- Git
- JDK (Java Development Kit) 21 or higher

## Installation Guide

### Build from Source

1. **Clone the Repository**  
   Clone the project repository from GitHub:
   ```bash
    git clone https://github.com/beduality/clock-time
    cd clock-time
   ```

2. **Build the Plugin**  
   Run the following command to build the plugin:
   ```bash
   ./gradlew build
   ```

3. **Locate the Built Plugin**  
   The compiled plugin will be located at `build/libs/ClockTime-0.1.0.jar`.

4. **Add to Your Server**  
   Move the `ClockTime-0.1.0.jar` file into the `plugins` folder of your Minecraft server.

5. **Start the Server**  
   Restart your server if it's already running. The plugin will automatically enable itself.

6. **Verify Installation**  
   Check your server logs for the message:  
   ```
   [ClockTime] ClockTime Plugin Enabled
   ```

## Usage

1. Hold a clock in your hand.
2. Right-click while holding the clock.
3. A chat message will display the current in-game time in **12-hour format**.

### Example:

If the in-game time is 4500 ticks, the plugin will display:  
```
Current Time: 09:45 AM
```

## Configuration

No additional configuration is required! The plugin works out of the box.

## Permissions

- `clock_time.use`: Allows the player to right-click a clock to check the time (default: `true`).

## Compatibility

- **Minecraft Version**: 1.20 or higher
- **API**: Built using the Paper API.

## Contribution

We welcome contributions! Please read [CONTRIBUTING.md](./CONTRIBUTING.md) for details on our architectural guidelines and code standards.

Feel free to fork the project, submit pull requests, or report issues on our [GitHub repository](https://github.com/beduality/clock-time).

## Authors

- **Luis Emidio**
- **Block-Entity Duality Team**

## License

This project is licensed under the MIT License - see the [LICENSE](./LICENSE) file for details.
