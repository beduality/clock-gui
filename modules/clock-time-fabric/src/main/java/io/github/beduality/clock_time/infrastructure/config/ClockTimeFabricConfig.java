package io.github.beduality.clock_time.infrastructure.config;

import java.util.ArrayList;
import java.util.List;

public class ClockTimeFabricConfig {
  public String fallbackLanguage = "en";
  public List<String> wildSpinWorlds = new ArrayList<>();
  public ItemFrameClocks itemFrameClocks = new ItemFrameClocks();
  public WallClocks wallClocks = new WallClocks();

  public static class ItemFrameClocks {
    public boolean enabled = true;
    public int updateInterval = 16;
    public String wildSpinSymbol = "🌀";
  }

  public static class WallClocks {
    public boolean enabled = true;
  }
}
