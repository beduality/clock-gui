package io.github.beduality.clock_time;

import io.github.beduality.clock_time.domain.service.ClockMessageService;
import io.github.beduality.clock_time.domain.service.DimensionTimeResolver;
import io.github.beduality.clock_time.domain.service.LocaleTimeFormatter;
import io.github.beduality.clock_time.domain.service.TimeFormatter;
import io.github.beduality.clock_time.infrastructure.config.ClockTimeFabricConfig;
import io.github.beduality.clock_time.infrastructure.listener.FabricClockInteractListener;
import io.github.beduality.clock_time.infrastructure.manager.FabricConfigLoader;
import io.github.beduality.clock_time.infrastructure.manager.FabricTranslationRegistryManager;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClockTimeFabric implements ModInitializer {

  private static final Logger LOGGER = LoggerFactory.getLogger("clock-time");

  @Override
  public void onInitialize() {
    // Load config
    ClockTimeFabricConfig config = new FabricConfigLoader().loadAndMigrate();

    // Register translations
    new FabricTranslationRegistryManager().setup(config.fallbackLanguage);

    // Bootstrap services and listeners
    var timeFormatter = new TimeFormatter();
    var localeTimeFormatter = new LocaleTimeFormatter();
    var dimensionTimeResolver = new DimensionTimeResolver(config.wildSpinWorlds);
    var clockMessageService =
        new ClockMessageService(timeFormatter, localeTimeFormatter, dimensionTimeResolver);

    new FabricClockInteractListener(clockMessageService).register();

    LOGGER.info("ClockTime Fabric Mod Initialized");
  }
}
