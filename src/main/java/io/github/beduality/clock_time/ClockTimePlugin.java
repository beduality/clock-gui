package io.github.beduality.clock_time;

import io.github.beduality.clock_time.domain.service.ClockMessageService;
import io.github.beduality.clock_time.domain.service.DimensionTimeResolver;
import io.github.beduality.clock_time.domain.service.LocaleTimeFormatter;
import io.github.beduality.clock_time.domain.service.TimeFormatter;
import io.github.beduality.clock_time.infrastructure.config.PluginConfig;
import io.github.beduality.clock_time.infrastructure.listener.ClockInteractListener;
import io.github.beduality.clock_time.infrastructure.manager.ConfigLoader;
import io.github.beduality.clock_time.infrastructure.manager.TranslationRegistryManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main entry point for the ClockTime Paper plugin. Acts as the Composition Root bootstrapping
 * dependency injections.
 */
public class ClockTimePlugin extends JavaPlugin {

  @Override
  public void onEnable() {
    // Load and migrate configuration
    PluginConfig config = new ConfigLoader(this).loadAndMigrate();

    // Register translations to Adventure GlobalTranslator
    new TranslationRegistryManager(this, getFile()).setup(config.getFallbackLanguage());

    // Setup domain logic and listener
    var timeFormatter = new TimeFormatter();
    var localeTimeFormatter = new LocaleTimeFormatter();
    var dimensionTimeResolver = new DimensionTimeResolver();
    var clockMessageService =
        new ClockMessageService(timeFormatter, localeTimeFormatter, dimensionTimeResolver);

    getServer()
        .getPluginManager()
        .registerEvents(new ClockInteractListener(clockMessageService), this);

    getLogger().info("ClockTime Plugin Enabled");
  }

  @Override
  public void onDisable() {
    getLogger().info("ClockTime Plugin Disabled");
  }
}
