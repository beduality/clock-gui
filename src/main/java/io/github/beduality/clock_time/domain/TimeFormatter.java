package io.github.beduality.clock_time.domain;

public class TimeFormatter {
    public FormattedTime formatTicks(long ticks) {
        long normalizedTicks = ((ticks % 24000) + 24000) % 24000;
        int hour = (int) ((normalizedTicks / 1000 + 6) % 24);
        int minute = (int) ((normalizedTicks % 1000) * 60 / 1000);
        int hour12 = hour % 12 == 0 ? 12 : hour % 12;
        String period = hour < 12 ? "AM" : "PM";
        return new FormattedTime(hour12, minute, period);
    }
}
