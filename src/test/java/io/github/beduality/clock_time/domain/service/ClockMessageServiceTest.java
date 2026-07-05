package io.github.beduality.clock_time.domain.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.translation.TranslationRegistry;
import org.bukkit.World;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ClockMessageServiceTest {

  private static final TimeFormatter timeFormatter = new TimeFormatter();
  private static final LocaleTimeFormatter localeTimeFormatter = new LocaleTimeFormatter();
  private static final DimensionTimeResolver dimensionTimeResolver = new DimensionTimeResolver();
  private static ClockMessageService messageService;

  @BeforeAll
  static void setUpAll() {
    // Build and register the TranslationRegistry for pure unit testing
    TranslationRegistry registry = TranslationRegistry.create(Key.key("clocktime", "test"));
    registry.defaultLocale(Locale.ENGLISH);

    Locale[] locales = {Locale.ROOT, Locale.ENGLISH, Locale.GERMAN, new Locale("es")};

    for (Locale locale : locales) {
      try {
        ResourceBundle bundle = ResourceBundle.getBundle("languages.messages", locale);
        registry.registerAll(locale, bundle, true);
      } catch (MissingResourceException e) {
        // Ignore if missing in resources
      }
    }

    GlobalTranslator.translator().addSource(registry);
    messageService =
        new ClockMessageService(timeFormatter, localeTimeFormatter, dimensionTimeResolver);
  }

  @Test
  void testGetClockMessageNormalOverworldUS() {
    // Ticks = 0 (Sunrise, 6:00 AM)
    Component component = messageService.getClockMessage(World.Environment.NORMAL, 0, Locale.US);
    String plainText = PlainTextComponentSerializer.plainText().serialize(component);

    // Assert expected translation and short time formatting for US locale
    assertTrue(plainText.startsWith("Current Time:"), "Should start with translation prefix");
    assertTrue(
        plainText.contains("6:00")
            && (plainText.contains("AM")
                || plainText.contains("a.m.")
                || plainText.contains("a. m.")),
        "Plain text was: " + plainText);
  }

  @Test
  void testGetClockMessageNormalOverworldGerman() {
    // Ticks = 6000 (Noon, 12:00)
    Component component =
        messageService.getClockMessage(World.Environment.NORMAL, 6000, Locale.GERMANY);
    String plainText = PlainTextComponentSerializer.plainText().serialize(component);

    // Assert German translation and time format
    assertEquals("Aktuelle Uhrzeit: 12:00", plainText);
  }

  @Test
  void testGetClockMessageNormalOverworldSpanish() {
    // Ticks = 18000 (Midnight, 0:00 or 12:00 AM)
    Component component =
        messageService.getClockMessage(World.Environment.NORMAL, 18000, new Locale("es"));
    String plainText = PlainTextComponentSerializer.plainText().serialize(component);

    // Assert Spanish translation
    assertTrue(plainText.startsWith("Hora Actual:"));
  }

  @Test
  void testGetClockMessageNetherWildSpin() {
    Component component =
        messageService.getClockMessage(World.Environment.NETHER, 12345, Locale.US);
    String plainText = PlainTextComponentSerializer.plainText().serialize(component);

    assertEquals("The clock spins wildly... Time has no meaning here.", plainText);
  }

  @Test
  void testGetClockMessageEndWildSpin() {
    Component component =
        messageService.getClockMessage(World.Environment.THE_END, 500, new Locale("de"));
    String plainText = PlainTextComponentSerializer.plainText().serialize(component);

    assertEquals("Die Uhr dreht sich wild... Zeit hat hier keine Bedeutung.", plainText);
  }
}
