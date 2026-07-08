package io.github.beduality.clock_time;

import io.github.beduality.clock_time.domain.manager.ClockItemFrameRegistry;
import io.github.beduality.clock_time.domain.manager.ClockItemFrameUpdater;
import io.github.beduality.clock_time.domain.service.ClockMessageService;
import io.github.beduality.clock_time.domain.service.DimensionTimeResolver;
import io.github.beduality.clock_time.domain.service.LocaleTimeFormatter;
import io.github.beduality.clock_time.domain.service.TimeFormatter;
import io.github.beduality.clock_time.infrastructure.adapter.PaperItemFrameAdapter;
import io.github.beduality.clock_time.infrastructure.adapter.PaperWorldInfo;
import io.github.beduality.clock_time.infrastructure.config.ClockTimePluginConfig;
import io.github.beduality.clock_time.infrastructure.listener.ClockInteractListener;
import io.github.beduality.clock_time.infrastructure.listener.ClockItemFrameListener;
import io.github.beduality.clock_time.infrastructure.manager.ConfigLoader;
import io.github.beduality.clock_time.infrastructure.manager.TranslationRegistryManager;
import org.bukkit.plugin.java.JavaPlugin;

/*
 * Main entry point for the ClockTime Paper plugin. Acts as the Composition Root bootstrapping
 * dependency injections.
 */
public class ClockTimePlugin extends JavaPlugin {

  private ClockItemFrameRegistry clockItemFrameRegistry;

  @Override
  public void onEnable() {
    // Load and migrate configuration
    ClockTimePluginConfig config = new ConfigLoader(this).loadAndMigrate();

    // Register translations to Adventure GlobalTranslator
    new TranslationRegistryManager(this, getFile()).setup(config.getFallbackLanguage());

    // Setup domain logic and listener
    var timeFormatter = new TimeFormatter();
    var localeTimeFormatter = new LocaleTimeFormatter();
    var dimensionTimeResolver = new DimensionTimeResolver(config.getWildSpinWorlds());
    var clockMessageService =
        new ClockMessageService(
            timeFormatter,
            localeTimeFormatter,
            dimensionTimeResolver,
            config.getItemFrameClocks().isEncodeSpaces());

    ClockItemFrameRegistry registry = null;
    ClockItemFrameUpdater updater = null;

    if (config.getItemFrameClocks().isEnabled()) {
      clockItemFrameRegistry = new ClockItemFrameRegistry();
      registry = clockItemFrameRegistry;
      var itemFrameListener = new ClockItemFrameListener(this, clockItemFrameRegistry);
      getServer().getPluginManager().registerEvents(itemFrameListener, this);

      var up =
          new ClockItemFrameUpdater(
              clockItemFrameRegistry,
              clockMessageService,
              config.getFallbackLanguage(),
              config.getItemFrameClocks().getUpdateInterval(),
              config.getItemFrameClocks().getWildSpinSymbol());
      updater = up;

      itemFrameListener.setOnRegisterCallback(
          frame -> {
            up.updateFrame(new PaperItemFrameAdapter(frame), new PaperWorldInfo(frame.getWorld()));
          });
      itemFrameListener.registerAlreadyLoadedFrames();

      getServer()
          .getScheduler()
          .runTaskTimer(
              this,
              () -> {
                for (org.bukkit.World world : getServer().getWorlds()) {
                  up.tick(new PaperWorldInfo(world));
                }
              },
              0L,
              1L);
    }

    getServer()
        .getPluginManager()
        .registerEvents(
            new ClockInteractListener(this, config, clockMessageService, registry, updater), this);

    getLogger().info("ClockTime Plugin Enabled");
  }

  @Override
  public void onDisable() {
    getLogger().info("ClockTime Plugin Disabled");
  }

  public ClockItemFrameRegistry getClockItemFrameRegistry() {
    return clockItemFrameRegistry;
  }
}
