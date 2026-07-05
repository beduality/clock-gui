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

# A list of custom world names or dimension keys (e.g. 'custom_world' or 'custom:space')
# that should be treated as wild-spin dimensions.
wild-spin-worlds: []
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

### `wild-spin-worlds`

| Detail | Description |
|---|---|
| **Data Type** | List of Strings |
| **Default Value** | `[]` |
| **Editable** | Yes |

A list of custom world names (e.g., `custom_nether`) or namespaced dimension keys (e.g., `custom:space`) that should be treated as wild-spin dimensions. When players use a clock in these dimensions, the clock will spin wildly and display the wild-spin translation message.
