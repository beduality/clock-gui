# Developer API

The core business logic of ClockTime is completely decoupled from the Bukkit/Paper API. Developers looking to reuse the formatting/translation systems or build integrations can interact with the domain and application layers directly.

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

### `TranslationService`
Located in package `io.github.beduality.clock_time.application`.

Handles resource bundle-based translation resolution, fallback logic, and message formatting.

#### Example
```java
ClassLoader classLoader = getClass().getClassLoader();
TranslationService service = new TranslationService(classLoader, "en");

// Format a specific LocalTime in the player's locale format
String message = service.getFormattedTimeMessage(LocalTime.of(21, 45), Locale.US);
// Returns: "It is currently 09:45 PM."
```

---

## API Specifications

### Lifecycle
* **Instantiation**: Both `TimeFormatter` and `TranslationService` are stateless/read-only once constructed. You must construct them during your plugin initialization phase.
* **ClassLoader dependency**: `TranslationService` accepts a `ClassLoader` during construction to determine where translation `.properties` resource files are resolved. To support dynamic user modifications outside the JAR, pass a classloader referencing the external plugin folder directory.

### Thread Safety
* **`TimeFormatter`** is completely stateless and thread-safe. `formatTicks()` can be safely invoked concurrently from any thread.
* **`TranslationService`** is thread-safe. Its internal localization resolution delegates to standard Java `ResourceBundle` cache layers, which handle multithreaded lookups.

### Nullability
* **Parameters**: Passing `null` as a parameter to any method in the API (e.g., `ticks` or `locale`) is not supported and will result in a `NullPointerException`.
* **Return Values**: Methods in `TimeFormatter` and `TranslationService` always return non-null values. If a localized message key is not found, `TranslationService#getMessage` returns the key itself as a fallback rather than `null`.

### Exceptions
* **`DateTimeException`**: May occur if out-of-bounds inputs are supplied to the `LocalTime` mappings, though the standard calculations clamp tick values safely.
* **`MissingResourceException`**: Handled internally by `TranslationService` and caught silently, returning default keys to ensure the plugin never crashes during messaging pipelines.

