# How to Convert Minecraft Ticks to LocalTime

This guide explains how to use ClockTime's core domain classes to calculate the real-world time representation of Minecraft world ticks.

## Converting Ticks

You can use the stateless `TimeFormatter` service from the domain layer to convert in-game ticks directly:

```java
import io.github.beduality.clock_time.domain.service.TimeFormatter;
import java.time.LocalTime;

public class TimeUtility {

    public LocalTime getRealTimeRepresentation(long worldTicks) {
        TimeFormatter formatter = new TimeFormatter();
        // Convert world ticks (e.g. 6000 ticks = 12:00 PM noon)
        return formatter.formatTicks(worldTicks);
    }
}
```
