package io.github.beduality.clock_time.domain.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.translation.TranslationRegistry;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ClockMessageServiceTest {

  private static final TimeFormatter timeFormatter = new TimeFormatter();
  private static final LocaleTimeFormatter localeTimeFormatter = new LocaleTimeFormatter();
  private static final DimensionTimeResolver dimensionTimeResolver =
      new DimensionTimeResolver(List.of());
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
    World world = mock(World.class);
    when(world.getEnvironment()).thenReturn(World.Environment.NORMAL);
    when(world.getName()).thenReturn("world");
    when(world.getKey()).thenReturn(NamespacedKey.minecraft("overworld"));

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
    World world = mock(World.class);
    when(world.getEnvironment()).thenReturn(World.Environment.NORMAL);
    when(world.getName()).thenReturn("world");
    when(world.getKey()).thenReturn(NamespacedKey.minecraft("overworld"));

    // Ticks = 6000 (Noon, 12:00)
    Component component = messageService.getClockMessage(world, 6000, Locale.GERMANY);
    String plainText = PlainTextComponentSerializer.plainText().serialize(component);

    // Assert German translation and time format
    assertEquals("Aktuelle Uhrzeit: 12:00", plainText);
  }

  @Test
  void testGetClockMessageNormalOverworldSpanish() {
    World world = mock(World.class);
    when(world.getEnvironment()).thenReturn(World.Environment.NORMAL);
    when(world.getName()).thenReturn("world");
    when(world.getKey()).thenReturn(NamespacedKey.minecraft("overworld"));

    // Ticks = 18000 (Midnight, 0:00 or 12:00 AM)
    Component component = messageService.getClockMessage(world, 18000, new Locale("es"));
    String plainText = PlainTextComponentSerializer.plainText().serialize(component);

    // Assert Spanish translation
    assertTrue(plainText.startsWith("Hora Actual:"));
  }

  @Test
  void testGetClockMessageNetherWildSpin() {
    World world = mock(World.class);
    when(world.getEnvironment()).thenReturn(World.Environment.NETHER);
    when(world.getName()).thenReturn("world_nether");
    when(world.getKey()).thenReturn(NamespacedKey.minecraft("the_nether"));

    Component component = messageService.getClockMessage(world, 12345, Locale.US);
    String plainText = PlainTextComponentSerializer.plainText().serialize(component);

    assertEquals("The clock spins wildly... Time has no meaning here.", plainText);
  }

  @Test
  void testGetClockMessageEndWildSpin() {
    World world = mock(World.class);
    when(world.getEnvironment()).thenReturn(World.Environment.THE_END);
    when(world.getName()).thenReturn("world_the_end");
    when(world.getKey()).thenReturn(NamespacedKey.minecraft("the_end"));

    Component component = messageService.getClockMessage(world, 500, new Locale("de"));
    String plainText = PlainTextComponentSerializer.plainText().serialize(component);

    assertEquals("Die Uhr dreht sich wild... Zeit hat hier keine Bedeutung.", plainText);
  }
}
