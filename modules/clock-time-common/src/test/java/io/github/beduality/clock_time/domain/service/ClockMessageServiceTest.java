package io.github.beduality.clock_time.domain.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import io.github.beduality.clock_time.domain.model.WorldInfo;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.translation.TranslationRegistry;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ClockMessageServiceTest {

  private static final TimeFormatter timeFormatter = new TimeFormatter();
  private static final LocaleTimeFormatter localeTimeFormatter = new LocaleTimeFormatter();
  private static final DimensionTimeResolver dimensionTimeResolver =
      new DimensionTimeResolver(List.of());
  private static ClockMessageService messageService;
  private static ClockMessageService messageServiceNoEncoding;

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
        new ClockMessageService(timeFormatter, localeTimeFormatter, dimensionTimeResolver, true);
    messageServiceNoEncoding =
        new ClockMessageService(timeFormatter, localeTimeFormatter, dimensionTimeResolver, false);
  }

  @Test
  void testGetClockMessageNormalOverworldUS() {
    WorldInfo world = mock(WorldInfo.class);
    when(world.isNetherOrEnd()).thenReturn(false);
    when(world.getName()).thenReturn("world");
    when(world.getKey()).thenReturn("minecraft:overworld");

    // Ticks = 0 (Sunrise, 6:00 AM)
    Component component = messageService.getClockMessage(world, 0, Locale.US);
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
    WorldInfo world = mock(WorldInfo.class);
    when(world.isNetherOrEnd()).thenReturn(false);
    when(world.getName()).thenReturn("world");
    when(world.getKey()).thenReturn("minecraft:overworld");

    // Ticks = 6000 (Noon, 12:00)
    Component component = messageService.getClockMessage(world, 6000, Locale.GERMANY);
    String plainText = PlainTextComponentSerializer.plainText().serialize(component);

    // Assert German translation and time format
    assertEquals("Aktuelle Uhrzeit: 12:00", plainText);
  }

  @Test
  void testGetClockMessageNormalOverworldSpanish() {
    WorldInfo world = mock(WorldInfo.class);
    when(world.isNetherOrEnd()).thenReturn(false);
    when(world.getName()).thenReturn("world");
    when(world.getKey()).thenReturn("minecraft:overworld");

    // Ticks = 18000 (Midnight, 0:00 or 12:00 AM)
    Component component = messageService.getClockMessage(world, 18000, new Locale("es"));
    String plainText = PlainTextComponentSerializer.plainText().serialize(component);

    // Assert Spanish translation
    assertTrue(plainText.startsWith("Hora Actual:"));
  }

  @Test
  void testGetClockMessageNetherWildSpin() {
    WorldInfo world = mock(WorldInfo.class);
    when(world.isNetherOrEnd()).thenReturn(true);
    when(world.getName()).thenReturn("world_nether");
    when(world.getKey()).thenReturn("minecraft:the_nether");

    Component component = messageService.getClockMessage(world, 12345, Locale.US);
    String plainText = PlainTextComponentSerializer.plainText().serialize(component);

    assertEquals("The clock spins wildly... Time has no meaning here.", plainText);
  }

  @Test
  void testGetClockMessageEndWildSpin() {
    WorldInfo world = mock(WorldInfo.class);
    when(world.isNetherOrEnd()).thenReturn(true);
    when(world.getName()).thenReturn("world_the_end");
    when(world.getKey()).thenReturn("minecraft:the_end");

    Component component = messageService.getClockMessage(world, 500, new Locale("de"));
    String plainText = PlainTextComponentSerializer.plainText().serialize(component);

    assertEquals("Die Uhr dreht sich wild... Zeit hat hier keine Bedeutung.", plainText);
  }

  @Test
  void testGetFormattedTimeOnlyEncodeSpacesEnabled() {
    // US locale at tick 0 → "6:00 AM" (Java ≤17: U+0020 space; Java 21+: U+202F NNBSP).
    // With encode-spaces=true both space variants must be replaced with U+00A0.
    WorldInfo world = mock(WorldInfo.class);
    when(world.isNetherOrEnd()).thenReturn(false);
    when(world.getName()).thenReturn("world");
    when(world.getKey()).thenReturn("minecraft:overworld");

    Component component = messageService.getFormattedTimeOnly(world, 0, Locale.US, "🌀");
    String plainText = PlainTextComponentSerializer.plainText().serialize(component);

    assertFalse(
        plainText.contains("\u0020"),
        "encode-spaces=true: result must contain no regular spaces (U+0020), got: " + plainText);
    assertFalse(
        plainText.contains("\u202F"),
        "encode-spaces=true: result must contain no narrow no-break spaces (U+202F), got: "
            + plainText);
    assertTrue(
        plainText.contains("\u00A0"),
        "encode-spaces=true: result must contain non-breaking space (U+00A0), got: " + plainText);
  }

  @Test
  void testGetFormattedTimeOnlyEncodeSpacesDisabled() {
    // With encode-spaces=false the formatted string must be returned as-is (no substitution).
    WorldInfo world = mock(WorldInfo.class);
    when(world.isNetherOrEnd()).thenReturn(false);
    when(world.getName()).thenReturn("world");
    when(world.getKey()).thenReturn("minecraft:overworld");

    Component component = messageServiceNoEncoding.getFormattedTimeOnly(world, 0, Locale.US, "🌀");
    String plainText = PlainTextComponentSerializer.plainText().serialize(component);

    assertFalse(
        plainText.contains("\u00A0"),
        "encode-spaces=false: result must contain no non-breaking spaces (U+00A0), got: "
            + plainText);
  }
}
