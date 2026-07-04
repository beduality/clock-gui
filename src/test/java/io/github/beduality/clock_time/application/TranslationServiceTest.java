package io.github.beduality.clock_time.application;

import io.github.beduality.clock_time.domain.FormattedTime;
import org.junit.jupiter.api.Test;
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
        FormattedTime time = new FormattedTime(9, 21, 45, "PM");
        String message = service.getFormattedTimeMessage(time, Locale.ENGLISH, "12h");
        assertNotNull(message);
        assertTrue(message.contains("09"));
        assertTrue(message.contains("45"));
        assertTrue(message.contains("PM"));
    }

    @Test
    void testFormattingTranslation24h() {
        FormattedTime time = new FormattedTime(9, 21, 45, "PM");
        String message = service.getFormattedTimeMessage(time, Locale.ENGLISH, "24h");
        assertNotNull(message);
        assertTrue(message.contains("21"));
        assertTrue(message.contains("45"));
        assertFalse(message.contains("PM"));
    }

    @Test
    void testPrefers12h() {
        assertTrue(service.prefers12h(Locale.US));
        assertFalse(service.prefers12h(Locale.GERMANY));
    }

    @Test
    void testLocaleAutoDetection() {
        FormattedTime time = new FormattedTime(9, 21, 45, "PM");

        String usMessage = service.getFormattedTimeMessage(time, Locale.US, "locale");
        assertTrue(usMessage.contains("09"));
        assertTrue(usMessage.contains("PM"));

        String deMessage = service.getFormattedTimeMessage(time, Locale.GERMANY, "locale");
        assertTrue(deMessage.contains("21"));
        assertFalse(deMessage.contains("PM"));
    }
}
