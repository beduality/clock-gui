package io.github.beduality.clock_time.application;

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
    void testFormattingTranslation() {
        String message = service.getMessage("clock_time.message.time", Locale.ENGLISH, "09", "45", "AM");
        assertNotNull(message);
        assertTrue(message.contains("09"));
        assertTrue(message.contains("45"));
        assertTrue(message.contains("AM"));
    }

    @Test
    void testLocaleFallback() {
        String message = service.getMessage("clock_time.message.wild-spin", new Locale("xx", "XX"));
        assertNotNull(message);
    }

    @Test
    void testMissingKeyFallback() {
        String message = service.getMessage("non.existent.key", Locale.ENGLISH);
        assertEquals("non.existent.key", message);
    }
}
