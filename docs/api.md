# Developer API

The core business logic of ClockTime is completely decoupled from the Bukkit/Paper API. Developers looking to reuse the formatting systems or build integrations can interact with the domain layer directly or query Adventure's GlobalTranslator for translations.

---

## Architecture Components

### `TimeFormatter`

Located in package `io.github.beduality.clock_time.domain`.

Converts Minecraft ticks (0-24000) into a standard Java `java.time.LocalTime`.

#### Example

```java
TimeFormatter formatter = new TimeFormatter();
java.time.LocalTime time = formatter.formatTicks(4500); // 10:30 AM
```

### Adventure Translatable Keys

ClockTime registers its localization files directly with Kyori Adventure's `GlobalTranslator` under the `clocktime` key. You can use standard translatable components to resolve messages automatically in the player's locale.

#### Translation Keys
* `clock_time.message.time`: The current time message. Accepts one argument `{0}` which is the formatted time string.
* `clock_time.message.wild-spin`: The message displayed when a player uses a clock in the Nether or the End.

#### Example

```java
import net.kyori.adventure.text.Component;
import net.kyori.adventure.translation.GlobalTranslator;

// Translate the message manually
String message = GlobalTranslator.translator().translate("clock_time.message.wild-spin", Locale.US).format(new Object[]{});
```

---

## API Specifications

### Lifecycle

* **Instantiation**: `TimeFormatter` is completely stateless and read-only once constructed. You can instantiate it whenever needed.

### Thread Safety

* **`TimeFormatter`** is completely stateless and thread-safe. `formatTicks()` can be safely invoked concurrently from any thread.

### Nullability

* **Parameters**: Passing `null` as a parameter to `formatTicks()` is not supported.
* **Return Values**: `TimeFormatter` methods always return non-null values.

### Exceptions

* **`DateTimeException`**: May occur if out-of-bounds inputs are supplied to the `LocalTime` mappings, though the standard calculations clamp tick values safely.


