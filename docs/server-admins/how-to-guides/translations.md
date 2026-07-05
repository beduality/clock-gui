# Customize translation messages

Use this guide to edit existing translation messages or add new language translations.

## Preconditions

- A Minecraft server with ClockTime installed.
- Access to the server's filesystem under `plugins/ClockTime/languages/`.

!!! tip "MiniMessage Formatting"

    ClockTime supports [Adventure MiniMessage](https://docs.advntr.dev/minimessage/format.html) formatting. You can customize translations with named colors (e.g. `<red>`), hex values (`<#ff5555>`), decorations (`<bold>`), or gradients.

## Editing Existing Translations

1. Go to `plugins/ClockTime/languages/` inside your server's plugins directory.
2. Open the file matching your desired language (e.g., `messages.properties` for English or `messages_es.properties` for Spanish).
3. Modify the translation messages. You can use standard [MiniMessage formatting](https://docs.advntr.dev/minimessage/format.html):

    ```properties
    clock_time.message.time=<gold>Current Time:</gold> <aqua>{0}</aqua>
    clock_time.message.wild-spin=<red>The clock spins wildly... Time has no meaning here.</red>
    ```

4. Save the file.
5. Restart your server.

---

## Adding a Custom Language

To add support for a custom language not bundled by default (e.g., Swedish):

1. **Find the language code**: Locate the Minecraft language code for the target language. You can find this in Minecraft under **Options > Language** (e.g. Swedish `sv_se` which maps to `sv` / `sv_SE`), or refer to the [Minecraft Wiki Language list](https://minecraft.wiki/w/Language).
2. **Create the file**: In `plugins/ClockTime/languages/`, create a new properties file using the format `messages_<code_or_suffix>.properties` (e.g. `messages_sv.properties`).
3. **Add translation keys**: Insert both required keys into the file:

    ```properties title="messages_sv.properties"
    clock_time.message.time=<gold>Aktuell tid:</gold> <aqua>{0}</aqua>
    clock_time.message.wild-spin=<red>Klockan snurrar vilt... Tiden har ingen betydelse här.</red>
    ```

4. Save the file.
5. Restart the server.

## Verification

To verify that your custom or edited translation is active:

1. Log into the server.
2. Change your Minecraft client language to match the edited or added locale (e.g., Swedish).
3. Right-click a clock in your main hand.
4. Verify that the time message displayed in chat matches your custom text and formatting.
