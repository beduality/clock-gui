# How to Change the Fallback Language

This guide explains how to change the language that is used when a player's Minecraft client language is not supported by the plugin.

## Changing the Fallback Language

1. Open `plugins/ClockTime/config.yml` in a text editor.
2. Locate the `fallback-language` configuration key.
3. Update the value to your preferred standard ISO 639-1 language code (e.g., `de` for German, `pt` for Portuguese, `es` for Spanish):

    ```yaml
    fallback-language: "de"
    ```

4. Save the file.
5. Restart your Minecraft server to apply the changes.
