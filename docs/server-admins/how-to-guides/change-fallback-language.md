# Change the fallback language

Use this guide to change the default language used when a player's client language is not supported by the plugin.

## Preconditions

- A Minecraft server with ClockTime installed.
- Access to the server file system to edit configuration files.

## Changing the Fallback Language

1. Open `plugins/ClockTime/config.yml` in a text editor.
2. Locate the `fallback-language` configuration key.
3. Update the value to your preferred standard ISO 639-1 language code (e.g., `de` for German, `pt` for Portuguese, `es` for Spanish):

    ```yaml
    fallback-language: "de"
    ```

4. Save the file.
5. Restart your Minecraft server to apply the changes.

## Verification

To verify that the fallback language has changed:

1. Connect to your Minecraft server.
2. Set your Minecraft client language to a language not supported by the plugin (e.g., Tagalog or a custom test locale).
3. Right-click a clock in your main hand.
4. Verify that the time message in chat is displayed in your configured fallback language.
