# Query translation messages programmatically

Use this guide to query registered translations from ClockTime programmatically using Kyori Adventure APIs.

## Preconditions

- ClockTime configured as a project dependency.
- Kyori Adventure API libraries present on your development project classpath.

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

## Verification

To verify that translation querying is working correctly:

1. Deploy your plugin to a server with ClockTime enabled.
2. Trigger the method in your plugin with a specific player locale (e.g., `Locale.US` or `Locale.GERMANY`).
3. Verify that the returned string matches the expected translation defined in ClockTime's language files (e.g., German translations if `Locale.GERMANY` is queried).
