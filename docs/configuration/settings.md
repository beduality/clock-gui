# Settings

ClockTime is configured through `plugins/ClockTime/config.yml`. The file is generated automatically on first startup.

## Default Configuration

```yaml title="config.yml"
# The fallback language code to use if a player's client language
# is not supported.
# Options: "en", "es", "pt", "de", etc.
fallback-language: "en"

# Configuration version. Do not modify this value.
config-version: 1
```

## Options

### `fallback-language`

The language to use when a player's Minecraft client language is not included in the bundled translations.

| Property | Value |
|---|---|
| **Type** | String (ISO 639-1 code) |
| **Default** | `"en"` |
| **Examples** | `"en"`, `"es"`, `"pt"`, `"de"`, `"fr"` |

!!! info "How language resolution works"

    1. The plugin reads the player's **client language** from their Minecraft settings.
    2. If a matching translation file exists (e.g., `messages_pt.properties`), it uses that.
    3. If no match is found, it falls back to the language specified here.

### `config-version`

!!! warning

    Do not modify this value. It is used internally for automatic configuration migrations between plugin versions.
