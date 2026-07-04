package io.github.beduality.clock_time.domain;

import java.time.LocalTime;

public class TimeFormatter {
    public LocalTime formatTicks(long ticks) {
        // Minecraft day starts at 6:00 AM (sunrise) = 6 hours * 3600 seconds = 21600 seconds.
        // Map 24000 ticks to 86400 seconds (1 tick = 3.6 seconds).
        long totalSeconds = (long) (((ticks % 24000) + 6000) % 24000 * 3.6);
        return LocalTime.ofSecondOfDay(totalSeconds);
    }
}
