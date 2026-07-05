# How to Query ClockTime Translations

This guide explains how to query registered translations from ClockTime programmatically using Kyori Adventure APIs.

## Querying Translations

ClockTime registers its message bundles to Kyori Adventure's `GlobalTranslator` registry under the namespace `clocktime:main`.

To fetch translations programmatically:

```java
import net.kyori.adventure.translation.GlobalTranslator;
import java.util.Locale;

public class TranslationHook {

    public String getWildSpinMessage(Locale playerLocale) {
        // Translate the wild-spin message using the player's locale
        var format = GlobalTranslator.translator()
            .translate("clock_time.message.wild-spin", playerLocale);

        if (format != null) {
            return format.format(new Object[]{});
        }
        return "Translation missing";
    }
}
```

!!! note

    Ensure ClockTime is fully loaded before querying. Make sure your plugin declares `depend: [ClockTime]` in `plugin.yml` to guarantee correct loading order.
