# Developer API

The core business logic of ClockTime is separated from Bukkit. Developers looking to reuse the formatting/translation systems can reference the packages directly:

## Packages

Contains the `TimeFormatter` (maps Minecraft ticks 0-24000 to standard Java `java.time.LocalTime` representations).

```java
TimeFormatter formatter = new TimeFormatter();
java.time.LocalTime time = formatter.formatTicks(4500); // returns 10:30
```

### `io.github.beduality.clock_time.application`

Contains the stateless `TranslationService` used to read i18n properties and format messages.
