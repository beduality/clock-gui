# Configuration

ClockTime is designed to work **completely out of the box** without any complex configurations.

## System Configuration

* **No `config.yml` is generated**: The default behaviors and permissions are hardcoded for performance and zero-configuration setups.
* **Translation / Localizations**: Messages are automatically localized based on the client's language preference using Java Resource Bundles (`messages.properties`). Supported locales are loaded automatically fallback to English if not found.
