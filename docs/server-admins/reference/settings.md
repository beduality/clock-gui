# Settings Reference

On **Paper/Spigot**, ClockTime is configured through `plugins/ClockTime/config.yml`. On **Fabric**, it is configured through `config/clock-time.json`. Both files are generated automatically on first startup.

## Default Configuration

=== "Paper/Spigot (config.yml)"

    ```yaml
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

    # Settings for placing clocks directly on surfaces (wall/floor/ceiling clocks)
    wall-clocks:
      # Allow placing clocks directly on walls/floors/ceilings as invisible frames
      enabled: true
    ```

=== "Fabric (clock-time.json)"

    ```json
    {
      "fallbackLanguage": "en",
      "wildSpinWorlds": [],
      "itemFrameClocks": {
        "enabled": true,
        "updateInterval": 16
      },
      "wallClocks": {
        "enabled": true
      }
    }
    ```

## Configuration Keys

### `fallback-language` (Fabric: `fallbackLanguage`)

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

Internal version schema tracker used to manage config format migrations. Modifying this key prevents configuration files from upgrading automatically. (Note: Fabric config files do not use config-version).

### `wild-spin-worlds` (Fabric: `wildSpinWorlds`)

| Detail | Description |
|---|---|
| **Data Type** | List of Strings |
| **Default Value** | `[]` |
| **Editable** | Yes |

A list of custom world names (e.g., `custom_nether`) or namespaced dimension keys (e.g., `custom:space`) that should be treated as wild-spin dimensions. When players use a clock in these dimensions, the clock will spin wildly and display the wild-spin translation message.

### `item-frame-clocks` (Fabric: `itemFrameClocks`)

Settings group managing realtime clock displays inside item frames.

#### `item-frame-clocks.enabled` (Fabric: `itemFrameClocks.enabled`)

| Detail | Description |
|---|---|
| **Data Type** | Boolean |
| **Default Value** | `true` |
| **Editable** | Yes |

Enable or disable dynamic updates for clocks placed in item frames. When enabled, a clock's display name updates to show the in-game time.

#### `item-frame-clocks.update-interval` (Fabric: `itemFrameClocks.updateInterval`)

| Detail | Description |
|---|---|
| **Data Type** | Integer |
| **Default Value** | `16` |
| **Editable** | Yes |

How often the plugin/mod updates item frame clocks, in ticks (e.g. `16` ticks = ~1 second). Higher values reduce CPU overhead.

### `wall-clocks` (Fabric: `wallClocks`)

Settings group managing direct placement of clocks on block faces (walls/floors/ceilings) via invisible item frames.

#### `wall-clocks.enabled` (Fabric: `wallClocks.enabled`)

| Detail | Description |
|---|---|
| **Data Type** | Boolean |
| **Default Value** | `true` |
| **Editable** | Yes |

Allow or disallow players from right-clicking block faces with a clock to place it directly on the wall as an invisible frame clock.

---

## Permissions (Paper/Spigot Only)

Admins can configure player permissions using standard permission managers (e.g. LuckPerms).

| Permission Node | Description | Default |
|---|---|---|
| `clock_time.use` | Allows right-clicking a clock in hand to display the time in chat. | `true` |
| `clock_time.place` | Allows placing clocks directly on block surfaces (wall clocks). | `true` |
| `clock_time.break` | Allows breaking wall clocks to retrieve the clock item. | `true` |

