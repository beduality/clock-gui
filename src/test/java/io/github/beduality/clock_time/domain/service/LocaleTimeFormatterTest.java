package io.github.beduality.clock_time.domain.service;

import org.junit.jupiter.api.Test;
import java.time.LocalTime;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class LocaleTimeFormatterTest {

    private final LocaleTimeFormatter formatter = new LocaleTimeFormatter();

    @Test
    void testFormatUS() {
        LocalTime morning = LocalTime.of(6, 0);
        String formatted = formatter.format(morning, Locale.US);
        // Under standard Java behavior for Locale.US, short time format contains AM/PM
        assertTrue(formatted.contains("6:00") && (formatted.contains("AM") || formatted.contains("a.m.") || formatted.contains("a. m.")));
    }

    @Test
    void testFormatGermany() {
        LocalTime noon = LocalTime.of(12, 0);
        String formatted = formatter.format(noon, Locale.GERMANY);
        // German format uses 24-hour style
        assertTrue(formatted.contains("12:00"));
    }

    @Test
    void testNullLocaleFallsBack() {
        LocalTime evening = LocalTime.of(18, 30);
        String formatted = formatter.format(evening, null);
        assertNotNull(formatted);
    }

    @Test
    void testNullTimeThrows() {
        assertThrows(IllegalArgumentException.class, () -> formatter.format(null, Locale.US));
    }
}
