# Placeholders

ClockTime handles its own localizations and formatting internally.

## Placeholders Support

* **PlaceholderAPI**: This plugin currently does not register custom placeholders (such as `%clocktime_time%`) in PlaceholderAPI.
* **Internal Formatting**: The plugin parses translations internally using standard `{0}`, `{1}`, and `{2}` parameter formatting corresponding to `hour`, `minute`, and `period` (AM/PM) respectively.
