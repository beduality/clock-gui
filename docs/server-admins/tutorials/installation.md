# Install and run ClockTime

Follow this tutorial to install the ClockTime plugin on your Minecraft server and verify it is working.

## Requirements

- **Java**: Version 21 or higher
- **Platform**: Paper 1.20+, Purpur, or Fabric Loader (with Fabric API)

## Download the Mod/Plugin

Download the latest version `.jar` file from one of the following official sources:

- [Hangar](https://hangar.papermc.io/beduality/clock-time) (Paper only)
- [Modrinth](https://modrinth.com/plugin/clock-time) (Paper & Fabric)
- [GitHub Releases](https://github.com/beduality/clock-time/releases) (Paper & Fabric)

## Install and Run

=== "Paper / Purpur"

    1. Place the downloaded Paper `.jar` file into your server's `plugins/` directory.
    2. Start or restart the server.
    3. Verify that the plugin has loaded successfully by checking the server logs:

       ```log
       [ClockTime] ClockTime Plugin Enabled
       ```

=== "Fabric"

    1. Place the downloaded Fabric `.jar` file into your `mods/` directory along with the correct [Fabric API](https://modrinth.com/mod/fabric-api) version.
    2. Start or restart the server/client.
    3. Verify that the mod has loaded successfully by checking the logs:

       ```log
       [Render thread/INFO]: ClockTime Fabric Mod Initialized
       ```

## Verify In-Game

1. Log into your Minecraft server.
2. Hold a **Clock** :material-clock: in your main hand.
3. Right-click.
4. Verify that the current in-game time is displayed in your chat.

## Next Steps

- [Configure settings](../reference/settings.md) — Change the fallback language.
- [Customize translations](../how-to-guides/translations.md) — Edit messages or add new languages.
- [Permissions reference](../reference/permissions.md) — Control who can use the clock.
