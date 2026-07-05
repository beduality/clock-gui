package io.github.beduality.clock_time.infrastructure.config;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

/** Maps the ClockTime configuration schema using Configurate. */
@ConfigSerializable
public class PluginConfig {

  @Setting("fallback-language")
  @Comment("The fallback language code to use if a player's client language is not supported.")
  private String fallbackLanguage = "en";

  @Setting("config-version")
  @Comment("Configuration version. Do not modify this value.")
  private int configVersion = 1;

  public String getFallbackLanguage() {
    return fallbackLanguage;
  }

  public int getConfigVersion() {
    return configVersion;
  }

  public void setConfigVersion(int configVersion) {
    this.configVersion = configVersion;
  }
}
