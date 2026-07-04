package io.github.beduality.clock_time.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TimeFormatterTest {

    private final TimeFormatter formatter = new TimeFormatter();

    @Test
    void testMorningTicks() {
        FormattedTime time = formatter.formatTicks(0);
        assertEquals(6, time.hour12());
        assertEquals(6, time.hour24());
        assertEquals(0, time.minute());
        assertEquals("AM", time.period());
        assertEquals("06", time.getFormattedHour12());
        assertEquals("06", time.getFormattedHour24());
        assertEquals("00", time.getFormattedMinute());
    }

    @Test
    void testNoonTicks() {
        FormattedTime time = formatter.formatTicks(6000);
        assertEquals(12, time.hour12());
        assertEquals(12, time.hour24());
        assertEquals(0, time.minute());
        assertEquals("PM", time.period());
    }

    @Test
    void testNightTicks() {
        FormattedTime time = formatter.formatTicks(18000);
        assertEquals(12, time.hour12());
        assertEquals(0, time.hour24());
        assertEquals(0, time.minute());
        assertEquals("AM", time.period());
        assertEquals("12", time.getFormattedHour12());
        assertEquals("00", time.getFormattedHour24());
    }

    @Test
    void testRandomTimeTicks() {
        FormattedTime time = formatter.formatTicks(4500);
        assertEquals(10, time.hour12());
        assertEquals(10, time.hour24());
        assertEquals(30, time.minute());
        assertEquals("AM", time.period());
    }

    @Test
    void testEveningTimeTicks() {
        FormattedTime time = formatter.formatTicks(15000);
        assertEquals(9, time.hour12());
        assertEquals(21, time.hour24());
        assertEquals(0, time.minute());
        assertEquals("PM", time.period());
        assertEquals("09", time.getFormattedHour12());
        assertEquals("21", time.getFormattedHour24());
    }
}
