package io.github.beduality.clock_time.domain.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalTime;
import org.junit.jupiter.api.Test;

class TimeFormatterTest {

  private final TimeFormatter formatter = new TimeFormatter();

  @Test
  void testMorningTicks() {
    LocalTime time = formatter.formatTicks(0);
    assertEquals(6, time.getHour());
    assertEquals(0, time.getMinute());
  }

  @Test
  void testNoonTicks() {
    LocalTime time = formatter.formatTicks(6000);
    assertEquals(12, time.getHour());
    assertEquals(0, time.getMinute());
  }

  @Test
  void testNightTicks() {
    LocalTime time = formatter.formatTicks(18000);
    assertEquals(0, time.getHour());
    assertEquals(0, time.getMinute());
  }

  @Test
  void testRandomTimeTicks() {
    LocalTime time = formatter.formatTicks(4500);
    assertEquals(10, time.getHour());
    assertEquals(30, time.getMinute());
  }

  @Test
  void testNegativeTicks() {
    LocalTime time = formatter.formatTicks(-6000);
    assertEquals(0, time.getHour());
    assertEquals(0, time.getMinute());
  }
}
