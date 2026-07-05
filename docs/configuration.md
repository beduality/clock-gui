# Configuration

ClockTime is configured via `config.yml` and localized through translation resource bundle files inside the plugin directory.

## Configuration Options

### fallback-language

```yaml
fallback-language: "en"
```

Specifies the language code to use if a player joins with a client locale that is not supported by the plugin's language files.

**Type**

String (ISO 639-1 language code)

**Default**

`"en"`

**Allowed Options**

Any valid language code that matches an available translation file. By default, this matches the standard files extracted: `"en"`, `"es"`, `"pt"`, `"de"`, etc.

**Details**

If set to `"en"`, the plugin will fallback to the base `languages/messages.properties` file. If configured to another code, players with unsupported languages will receive that specified language bundle.

---

## Translation & Localizations

All default supported translation files (such as `messages_de.properties`, `messages_pt.properties`, etc.) are automatically extracted on startup into the `plugins/ClockTime/languages/` directory.

### Custom Overrides & Extensibility

Server administrators can edit these files directly to customize translations. AM/PM symbols are fully localized natively according to the player's client language.

To support a completely new language (e.g. Swedish):
1. Create a file named `messages_sv.properties` inside the `plugins/ClockTime/languages/` folder.
2. Define the translation key:
   ```properties
   clock_time.message.time=Det är just nu {0}.
   clock_time.message.wild-spin=Klockans visare snurrar vilt!
   ```
3. The plugin will automatically load and prioritize this file for any player whose client language is set to Swedish (`sv`).
