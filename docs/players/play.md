# Player Guide

To check the current in-game time, hold a **Clock** and **right-click**. 

---

## Locale & Time Formatting

ClockTime automatically reads your Minecraft client's language settings to customize your experience.

* **Language**: Message text is displayed in your chosen client language (if the server administrator has loaded translations for it).
* **12-Hour vs. 24-Hour Format**: Minecraft does not have a direct setting for time formats. Instead, the format is determined by your client's selected locale:
  * **12-hour format (e.g., 2:15 PM)**: Enabled by choosing a locale that uses 12-hour time, such as *English (US)*.
  * **24-hour format (e.g., 14:15)**: Enabled by choosing a locale that uses 24-hour time, such as *English (UK)* or *Español (España)*.

---

## Dimension-Aware Time

In dimensions where standard day and night cycles do not exist (or in custom dimensions configured by the server administrator), the clock displays a custom status message instead of a standard time:

| Dimension | Clock Readout |
| :--- | :--- |
| **Overworld** (or normal time dimensions) | Current formatted time (e.g., `2:15 PM`) |
| **The Nether** | Wild-spin message (e.g., `The clock spins wildly... Time has no meaning here.`) |
| **The End** | Wild-spin message (e.g., `The clock spins wildly... Time has no meaning here.`) |
| **Custom Configured Dimensions** | Wild-spin message (e.g., `The clock spins wildly... Time has no meaning here.`) |

---

## Bedrock Compatibility

When playing on a Bedrock client (e.g., via Geyser), item frame clock names are encoded with non-breaking spaces so they display correctly. This is controlled server-side by the `item-frame-clocks.encode-spaces` setting (enabled by default) and has no effect on chat messages.
