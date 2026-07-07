package io.github.beduality.clock_time.infrastructure.config;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class ClockTimePluginConfig {

  @Setting("fallback-language")
  @Comment("The fallback language code to use if a player's client language is not supported.")
  private String fallbackLanguage = "en";

  @Setting("config-version")
  @Comment("Configuration version. Do not modify this value.")
  private int configVersion = 2;

  @Setting("wild-spin-worlds")
  @Comment(
      "A list of custom world names or dimension keys (e.g. 'custom_world' or 'custom:space') that should be treated as wild-spin dimensions.")
  private java.util.List<String> wildSpinWorlds = java.util.List.of();

  @Setting("item-frame-clocks")
  private ItemFrameClocks itemFrameClocks = new ItemFrameClocks();

  public String getFallbackLanguage() {
    return fallbackLanguage;
  }

  public int getConfigVersion() {
    return configVersion;
  }

  public void setConfigVersion(int configVersion) {
    this.configVersion = configVersion;
  }

  public java.util.List<String> getWildSpinWorlds() {
    return wildSpinWorlds;
  }

  public ItemFrameClocks getItemFrameClocks() {
    return itemFrameClocks;
  }

  @ConfigSerializable
  public static class ItemFrameClocks {
    @Setting("enabled")
    @Comment("Enable dynamic time updates for clocks placed in item frames")
    private boolean enabled = true;

    @Setting("update-interval")
    @Comment("How often to update the clock time (in ticks, 20 ticks = 1 second)")
    private int updateInterval = 16;

    public boolean isEnabled() {
      return enabled;
    }

    public int getUpdateInterval() {
      return updateInterval;
    }
  }
}
