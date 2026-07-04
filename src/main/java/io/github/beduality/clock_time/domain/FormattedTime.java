package io.github.beduality.clock_time.domain;

public record FormattedTime(int hour12, int hour24, int minute, String period) {
    public String getFormattedHour12() {
        return String.format("%02d", hour12);
    }
    public String getFormattedHour24() {
        return String.format("%02d", hour24);
    }
    public String getFormattedMinute() {
        return String.format("%02d", minute);
    }
}
