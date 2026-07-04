package io.github.beduality.clock_time.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
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

    @Test
    void testExternalOverride(@TempDir Path tempDir) throws Exception {
        // Create plugins/ClockTime/languages/messages.properties file
        File languagesDir = new File(tempDir.toFile(), "languages");
        assertTrue(languagesDir.mkdirs());

        File overrideFile = new File(languagesDir, "messages.properties");
        try (FileWriter writer = new FileWriter(overrideFile)) {
            writer.write("clock_time.message.time=OVERRIDDEN {0}\n");
        }

        // Set up the custom URLClassLoader without delegating application resources to the parent
        URL[] urls = new URL[] { tempDir.toUri().toURL() };
        URLClassLoader customLoader = new URLClassLoader(urls, ClassLoader.getSystemClassLoader().getParent());

        TranslationService overrideService = new TranslationService(customLoader);
        LocalTime time = LocalTime.of(12, 0);
        String message = overrideService.getFormattedTimeMessage(time, Locale.ENGLISH, "24h");

        // Verify that the external file override was loaded instead of the internal one
        assertTrue(message.contains("OVERRIDDEN"));
        assertTrue(message.contains("12:00"));
    }
}
