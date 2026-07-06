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
  private int configVersion = 1;

  @Setting("wild-spin-worlds")
  @Comment(
      "A list of custom world names or dimension keys (e.g. 'custom_world' or 'custom:space') that should be treated as wild-spin dimensions.")
  private java.util.List<String> wildSpinWorlds = java.util.List.of();

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
}
