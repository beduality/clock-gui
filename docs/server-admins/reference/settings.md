# Settings Reference

ClockTime is configured through `plugins/ClockTime/config.yml`. The file is generated automatically on first startup.

## Default Configuration

```yaml title="config.yml"
# The fallback language code to use if a player's client language
# is not supported.
# Options: "en", "es", "pt", "de", etc.
fallback-language: "en"

# Configuration version. Do not modify this value.
config-version: 2

# A list of custom world names or dimension keys (e.g. 'custom_world' or 'custom:space')
# that should be treated as wild-spin dimensions.
wild-spin-worlds: []

# Settings for clocks placed in item frames
item-frame-clocks:
  # Enable dynamic time updates for clocks placed in item frames
  enabled: true
  # How often to update the clock time (in ticks, 20 ticks = 1 second)
  update-interval: 16
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
| **Default Value** | `2` |
| **Editable** | No |

Internal version schema tracker used to manage config format migrations. Modifying this key prevents configuration files from upgrading automatically.

### `wild-spin-worlds`

| Detail | Description |
|---|---|
| **Data Type** | List of Strings |
| **Default Value** | `[]` |
| **Editable** | Yes |

A list of custom world names (e.g., `custom_nether`) or namespaced dimension keys (e.g., `custom:space`) that should be treated as wild-spin dimensions. When players use a clock in these dimensions, the clock will spin wildly and display the wild-spin translation message.

### `item-frame-clocks`

Settings group managing realtime clock displays inside item frames.

#### `item-frame-clocks.enabled`

| Detail | Description |
|---|---|
| **Data Type** | Boolean |
| **Default Value** | `true` |
| **Editable** | Yes |

Enable or disable dynamic updates for clocks placed in item frames. When enabled, a clock's display name updates to show the in-game time.

#### `item-frame-clocks.update-interval`

| Detail | Description |
|---|---|
| **Data Type** | Integer |
| **Default Value** | `16` |
| **Editable** | Yes |

How often the plugin updates item frame clocks, in ticks (e.g. `20` ticks = 1 second). Higher values reduce CPU overhead.
