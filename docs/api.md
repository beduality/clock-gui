# Developer API

The core business logic of ClockTime is separated from Bukkit. Developers looking to reuse the formatting/translation systems can reference the packages directly:

## Packages

### `io.github.beduality.clock_time.domain`

Contains domain representations such as `FormattedTime` and `TimeFormatter` (computes Minecraft ticks 0-24000 to standard 12-hour values).

```java
TimeFormatter formatter = new TimeFormatter();
FormattedTime time = formatter.formatTicks(4500); // returns 10:30 AM
```

### `io.github.beduality.clock_time.application`

Contains the stateless `TranslationService` used to read i18n properties and format messages.
