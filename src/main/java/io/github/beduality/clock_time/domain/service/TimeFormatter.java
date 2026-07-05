package io.github.beduality.clock_time.domain.service;

import java.time.LocalTime;

/**
 * Core domain service for time conversion. Translates abstract Minecraft world tick values into
 * standard Java {@link LocalTime} objects. This class is completely decoupled from Minecraft/Bukkit
 * APIs, making it highly testable.
 */
public class TimeFormatter {

  /**
   * Translates a Minecraft tick value into a {@link LocalTime}.
   *
   * <p>In Minecraft: - Ticks range from 0 to 24000 per in-game day. - Sunrise (6:00 AM) begins at
   * tick 0 (represented as tick 6000 in mathematical offset calculations). - Noon (12:00 PM) is at
   * tick 6000. - Sunset (6:00 PM) is at tick 12000. - Night (12:00 AM) is at tick 18000.
   *
   * @param ticks the Minecraft tick count (typically 0-24000)
   * @return the corresponding {@link LocalTime} mapped to a 24-hour day structure
   */
  public LocalTime formatTicks(long ticks) {
    long totalSeconds = (long) (((ticks % 24000) + 6000) % 24000 * 3.6);
    return LocalTime.ofSecondOfDay(totalSeconds);
  }
}
