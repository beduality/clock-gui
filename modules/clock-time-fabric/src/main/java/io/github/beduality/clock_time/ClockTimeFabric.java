package io.github.beduality.clock_time;

import io.github.beduality.clock_time.domain.manager.ClockItemFrameRegistry;
import io.github.beduality.clock_time.domain.manager.ClockItemFrameUpdater;
import io.github.beduality.clock_time.domain.service.ClockMessageService;
import io.github.beduality.clock_time.domain.service.DimensionTimeResolver;
import io.github.beduality.clock_time.domain.service.LocaleTimeFormatter;
import io.github.beduality.clock_time.domain.service.TimeFormatter;
import io.github.beduality.clock_time.infrastructure.adapter.FabricItemFrameAdapter;
import io.github.beduality.clock_time.infrastructure.adapter.FabricWorldInfo;
import io.github.beduality.clock_time.infrastructure.config.ClockTimeFabricConfig;
import io.github.beduality.clock_time.infrastructure.listener.FabricClockInteractListener;
import io.github.beduality.clock_time.infrastructure.listener.FabricClockItemFrameListener;
import io.github.beduality.clock_time.infrastructure.manager.FabricConfigLoader;
import io.github.beduality.clock_time.infrastructure.manager.FabricTranslationRegistryManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
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

    new FabricClockInteractListener(config, clockMessageService).register();

    if (config.itemFrameClocks.enabled) {
      var registry = new ClockItemFrameRegistry();
      var listener = new FabricClockItemFrameListener(registry);
      var updater =
          new ClockItemFrameUpdater(
              registry,
              clockMessageService,
              config.fallbackLanguage,
              config.itemFrameClocks.updateInterval,
              config.itemFrameClocks.wildSpinSymbol);

      listener.setOnRegisterCallback(
          frame -> {
            updater.updateFrame(
                new FabricItemFrameAdapter(frame),
                new FabricWorldInfo((net.minecraft.server.world.ServerWorld) frame.getWorld()));
          });
      listener.register();

      ServerWorldEvents.LOAD.register(
          (server, world) -> {
            listener.registerAlreadyLoadedFrames(world);
          });

      ServerTickEvents.END_WORLD_TICK.register(world -> updater.tick(new FabricWorldInfo(world)));
    }

    LOGGER.info("ClockTime Fabric Mod Initialized");
  }
}
