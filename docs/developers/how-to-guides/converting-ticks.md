# Convert Minecraft ticks to LocalTime

Use this guide to calculate the real-world time representation of Minecraft world ticks using ClockTime's core domain classes.

## Preconditions

- ClockTime added as a project dependency in your plugin project.

## Converting Ticks

You can use the stateless `TimeFormatter` service from the domain layer to convert in-game ticks directly:

```java
import io.github.beduality.clock_time.domain.service.TimeFormatter;
import java.time.LocalTime;

public class TimeUtility {

    public LocalTime getRealTimeRepresentation(long worldTicks) {
        TimeFormatter formatter = new TimeFormatter();
        // Convert world ticks (e.g., 6000 ticks = 12:00 PM noon)
        return formatter.formatTicks(worldTicks);
    }
}
```

## Verification

To verify that your tick conversion works correctly:

1. Create a unit test or call your utility method in your plugin logic.
2. Assert or log the result of converting common values, for example:
   - `0` ticks should yield `06:00` (6:00 AM)
   - `6000` ticks should yield `12:00` (12:00 PM)
   - `12000` ticks should yield `18:00` (6:00 PM)
   - `18000` ticks should yield `00:00` (12:00 AM)
