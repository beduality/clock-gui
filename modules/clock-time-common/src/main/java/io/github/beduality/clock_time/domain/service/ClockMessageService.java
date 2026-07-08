package io.github.beduality.clock_time.domain.service;

import io.github.beduality.clock_time.domain.model.WorldInfo;
import java.time.LocalTime;
import java.util.Locale;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.translation.GlobalTranslator;

/*
 * Domain service that coordinates time resolving, formatting, and translations to build the final
 * adventure message Component to be sent to players.
 */
public class ClockMessageService {

  private final TimeFormatter timeFormatter;
  private final LocaleTimeFormatter localeTimeFormatter;
  private final DimensionTimeResolver dimensionTimeResolver;
  private final boolean encodeSpaces;

  /*
   * Constructs a ClockMessageService.
   *
   * @param timeFormatter the tick-to-time formatter
   * @param localeTimeFormatter the locale-aware time formatter
   * @param dimensionTimeResolver the dimension time resolver
   * @param encodeSpaces if true, regular spaces in item frame display names are replaced with
   *     non-breaking spaces (U+00A0) so Bedrock clients render them correctly
   */
  public ClockMessageService(
      TimeFormatter timeFormatter,
      LocaleTimeFormatter localeTimeFormatter,
      DimensionTimeResolver dimensionTimeResolver,
      boolean encodeSpaces) {
    this.timeFormatter = timeFormatter;
    this.localeTimeFormatter = localeTimeFormatter;
    this.dimensionTimeResolver = dimensionTimeResolver;
    this.encodeSpaces = encodeSpaces;
  }

  /*
   * Generates the clock message Component for the player.
   *
   * @param world the player's world representation
   * @param worldTime the world time in ticks
   * @param locale the player's locale
   * @return the localized message component
   */
  public Component getClockMessage(WorldInfo world, long worldTime, Locale locale) {
    if (dimensionTimeResolver.isWildSpinDimension(world)) {
      String format = translate("clock_time.message.wild-spin", locale);
      return MiniMessage.miniMessage().deserialize(format);
    }

    LocalTime localTime = timeFormatter.formatTicks(worldTime);
    String formattedTime = localeTimeFormatter.format(localTime, locale);

    String format = translate("clock_time.message.time", locale, formattedTime);
    return MiniMessage.miniMessage().deserialize(format);
  }

  /**
   * Generates just the formatted time string (or wild spin symbol) for the item frame display name.
   *
   * @param world the world representation
   * @param worldTime the world time in ticks
   * @param locale the locale
   * @param wildSpinSymbol the configurable wild spin symbol
   * @return the time only component
   */
  public Component getFormattedTimeOnly(
      WorldInfo world, long worldTime, Locale locale, String wildSpinSymbol) {
    if (dimensionTimeResolver.isWildSpinDimension(world)) {
      String format = translate("clock_time.item.wild-spin", locale);
      if (format.equals("clock_time.item.wild-spin")) {
        return Component.text(wildSpinSymbol != null ? wildSpinSymbol : "🌀");
      }
      return MiniMessage.miniMessage().deserialize(format);
    }

    LocalTime localTime = timeFormatter.formatTicks(worldTime);
    String formattedTime = localeTimeFormatter.format(localTime, locale);
    if (encodeSpaces) {
      // Bedrock's item CUSTOM_NAME renderer treats U+0020 (regular space) and U+202F
      // (narrow no-break space, emitted by Java 21+ DateTimeFormatter for AM/PM locales)
      // as word-break boundaries, causing names like "3:45 PM" to split or truncate.
      // Replace both with U+00A0 (non-breaking space) which Bedrock renders correctly.
      // This does NOT affect chat messages — only item frame / wall clock display names
      // pass through this path.
      formattedTime = formattedTime.replace('\u0020', '\u00A0').replace('\u202F', '\u00A0');
    }
    return Component.text(formattedTime);
  }

  private String translate(String key, Locale locale, Object... args) {
    if (locale == null) {
      locale = Locale.ROOT;
    }
    var format = GlobalTranslator.translator().translate(key, locale);
    if (format == null) {
      format = GlobalTranslator.translator().translate(key, Locale.ROOT);
    }
    return format != null ? format.format(args) : key;
  }
}
