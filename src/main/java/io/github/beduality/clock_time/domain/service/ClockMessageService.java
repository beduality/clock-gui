package io.github.beduality.clock_time.domain.service;

import java.time.LocalTime;
import java.util.Locale;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.translation.GlobalTranslator;
import org.bukkit.World;

/*
 * Domain service that coordinates time resolving, formatting, and translations to build the final
 * adventure message Component to be sent to players.
 */
public class ClockMessageService {

  private final TimeFormatter timeFormatter;
  private final LocaleTimeFormatter localeTimeFormatter;
  private final DimensionTimeResolver dimensionTimeResolver;

  /*
   * Constructs a ClockMessageService.
   *
   * @param timeFormatter the tick-to-time formatter
   * @param localeTimeFormatter the locale-aware time formatter
   * @param dimensionTimeResolver the dimension time resolver
   */
  public ClockMessageService(
      TimeFormatter timeFormatter,
      LocaleTimeFormatter localeTimeFormatter,
      DimensionTimeResolver dimensionTimeResolver) {
    this.timeFormatter = timeFormatter;
    this.localeTimeFormatter = localeTimeFormatter;
    this.dimensionTimeResolver = dimensionTimeResolver;
  }

  /*
   * Generates the clock message Component for the player.
   *
   * @param world the player's world
   * @param worldTime the world time in ticks
   * @param locale the player's locale
   * @return the localized message component
   */
  public Component getClockMessage(World world, long worldTime, Locale locale) {
    if (dimensionTimeResolver.isWildSpinDimension(world)) {
      String format = translate("clock_time.message.wild-spin", locale);
      return MiniMessage.miniMessage().deserialize(format);
    }

    LocalTime localTime = timeFormatter.formatTicks(worldTime);
    String formattedTime = localeTimeFormatter.format(localTime, locale);

    String format = translate("clock_time.message.time", locale, formattedTime);
    return MiniMessage.miniMessage().deserialize(format);
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
