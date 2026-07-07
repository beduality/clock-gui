package io.github.beduality.clock_time.domain.util;

import java.util.Locale;

/** Shared locale parsing utility used across platforms. */
public final class LocaleUtils {

  private LocaleUtils() {}

  /**
   * Parses a Minecraft-style locale string (e.g. "en_us", "pt_br") into a {@link Locale}.
   *
   * @param localeStr the locale string, may be null or empty
   * @return the parsed Locale, or {@link Locale#ROOT} if input is null/empty/root
   */
  public static Locale parseLocale(String localeStr) {
    if (localeStr == null || localeStr.isEmpty() || localeStr.equalsIgnoreCase("root")) {
      return Locale.ROOT;
    }
    String[] parts = localeStr.split("_");
    if (parts.length == 1) {
      return Locale.of(parts[0]);
    } else if (parts.length == 2) {
      return Locale.of(parts[0], parts[1]);
    } else if (parts.length >= 3) {
      return Locale.of(parts[0], parts[1], parts[2]);
    }
    return Locale.of(localeStr);
  }
}
