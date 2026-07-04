package io.github.beduality.clock_time.domain;

public record FormattedTime(int hour12, int minute, String period) {
    public String getFormattedHour() {
        return String.format("%02d", hour12);
    }
    public String getFormattedMinute() {
        return String.format("%02d", minute);
    }
}
