package io.github.beduality.clock_time.application;

import org.junit.jupiter.api.Test;
import java.time.LocalTime;
import java.util.Locale;
import static org.junit.jupiter.api.Assertions.*;

class TranslationServiceTest {

    private final TranslationService service = new TranslationService(getClass().getClassLoader());

    @Test
    void testEnglishTranslation() {
        String message = service.getMessage("clock_time.message.wild-spin", Locale.ENGLISH);
        assertNotNull(message);
        assertTrue(message.contains("spin") || message.contains("wildly"));
    }

    @Test
    void testFormattingTranslation12h() {
        LocalTime time = LocalTime.of(21, 45);
        String message = service.getFormattedTimeMessage(time, Locale.ENGLISH, "12h");
        assertNotNull(message);
        assertTrue(message.contains("09:45"));
        assertTrue(message.toLowerCase().contains("pm"));
    }

    @Test
    void testFormattingTranslation24h() {
        LocalTime time = LocalTime.of(21, 45);
        String message = service.getFormattedTimeMessage(time, Locale.ENGLISH, "24h");
        assertNotNull(message);
        assertTrue(message.contains("21:45"));
        assertFalse(message.toLowerCase().contains("pm"));
    }

    @Test
    void testLocaleAutoDetection() {
        LocalTime time = LocalTime.of(21, 45);

        String usMessage = service.getFormattedTimeMessage(time, Locale.US, "locale");
        assertTrue(usMessage.contains("9:45") || usMessage.contains("09:45"));
        assertTrue(usMessage.toLowerCase().contains("pm"));

        String deMessage = service.getFormattedTimeMessage(time, Locale.GERMANY, "locale");
        assertTrue(deMessage.contains("21:45"));
        assertFalse(deMessage.toLowerCase().contains("pm"));
    }
}
