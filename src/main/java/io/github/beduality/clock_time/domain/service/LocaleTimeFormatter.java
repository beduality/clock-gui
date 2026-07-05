package io.github.beduality.clock_time.domain.service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

/* Domain service responsible for formatting a LocalTime based on a specific client locale. */
public class LocaleTimeFormatter {

  /*
   * Formats the given LocalTime using the localized short time format for the specified locale.
   *
   * @param localTime the time to format
   * @param locale the client's locale
   * @return the formatted time string
   */
  public String format(LocalTime localTime, Locale locale) {
    if (localTime == null) {
      throw new IllegalArgumentException("localTime cannot be null");
    }
    if (locale == null) {
      locale = Locale.ROOT;
    }
    DateTimeFormatter dtf = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(locale);
    return localTime.format(dtf);
  }
}
