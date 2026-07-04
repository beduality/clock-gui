package io.github.beduality.clock_time.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TimeFormatterTest {

    private final TimeFormatter formatter = new TimeFormatter();

    @Test
    void testMorningTicks() {
        FormattedTime time = formatter.formatTicks(0);
        assertEquals(6, time.hour12());
        assertEquals(0, time.minute());
        assertEquals("AM", time.period());
        assertEquals("06", time.getFormattedHour());
        assertEquals("00", time.getFormattedMinute());
    }

    @Test
    void testNoonTicks() {
        FormattedTime time = formatter.formatTicks(6000);
        assertEquals(12, time.hour12());
        assertEquals(0, time.minute());
        assertEquals("PM", time.period());
    }

    @Test
    void testNightTicks() {
        FormattedTime time = formatter.formatTicks(18000);
        assertEquals(12, time.hour12());
        assertEquals(0, time.minute());
        assertEquals("AM", time.period());
    }

    @Test
    void testRandomTimeTicks() {
        FormattedTime time = formatter.formatTicks(4500);
        assertEquals(10, time.hour12());
        assertEquals(30, time.minute());
        assertEquals("AM", time.period());
    }

    @Test
    void testNegativeTicks() {
        FormattedTime time = formatter.formatTicks(-6000);
        assertEquals(12, time.hour12());
        assertEquals(0, time.minute());
        assertEquals("AM", time.period());
    }
}
