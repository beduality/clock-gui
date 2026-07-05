# Settings Reference

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

## Configuration Keys

### `fallback-language`

| Detail | Description |
|---|---|
| **Data Type** | String (ISO 639-1 code) |
| **Default Value** | `"en"` |
| **Valid Options** | Standard lowercase two-letter language codes (e.g. `en`, `es`, `pt`, `de`, `fr`) |

The fallback translation bundle used when a player's client language does not match any registered localization file.

### `config-version`

| Detail | Description |
|---|---|
| **Data Type** | Integer |
| **Default Value** | `1` |
| **Editable** | No |

Internal version schema tracker used to manage config format migrations. Modifying this key prevents configuration files from upgrading automatically.

