# Configuration

## System Configuration

* **`config.yml`**: A configuration file is generated upon startup containing the following settings:
  ```yaml
  # Time format to use.
  # Options:
  #   "locale" - Auto-detect format based on player's client language (default)
  #   "12h"    - Force 12-hour format for all players (e.g., 09:45 PM)
  #   "24h"    - Force 24-hour format for all players (e.g., 21:45)
  time-format: "locale"
  ```
* **Translation / Localizations**: Messages are automatically localized based on the client's language preference using Java Resource Bundles. Supported locales are loaded automatically and fallback to `fallback-language` if not found.
  * The template key `clock_time.message.time` is used for all styles. It accepts a single `{0}` parameter representing the pre-formatted time.
  * AM/PM symbols are fully localized natively according to the player's client language.
  * **Configurable Fallback**: The setting `fallback-language` in `config.yml` dictates which language is used when a player joins with an unsupported client locale (defaults to `"en"`, which falls back to the base `languages/messages.properties`).
  * **Custom Overrides & Extensibility**: All default supported translation files (such as `messages_de.properties`, `messages_pt.properties`, etc.) are automatically extracted on startup into the `plugins/ClockTime/languages/` directory. Server administrators can edit these files directly to customize translations. To support a completely new language (e.g. Swedish), administrators can create and add a new `messages_sv.properties` file in that folder. The plugin automatically checks this directory for overrides before falling back to its internal files.
