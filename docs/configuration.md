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
* **Translation / Localizations**: Messages are automatically localized based on the client's language preference using Java Resource Bundles (`messages.properties`). Supported locales are loaded automatically fallback to English if not found.
  * The template key `clock_time.message.time` is used for all styles. It accepts a single `{0}` parameter representing the pre-formatted time.
  * AM/PM symbols are fully localized natively according to the player's client language.
