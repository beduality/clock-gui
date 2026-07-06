package io.github.beduality.clock_time.infrastructure.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.beduality.clock_time.infrastructure.config.ClockTimeFabricConfig;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FabricConfigLoader {

  private static final Logger LOGGER = LoggerFactory.getLogger("clock-time");
  private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

  public ClockTimeFabricConfig loadAndMigrate() {
    Path configPath = FabricLoader.getInstance().getConfigDir().resolve("clock-time.json");
    ClockTimeFabricConfig config = null;

    if (Files.exists(configPath)) {
      try (Reader reader = Files.newBufferedReader(configPath)) {
        config = GSON.fromJson(reader, ClockTimeFabricConfig.class);
      } catch (Exception e) {
        LOGGER.error("Failed to load clock-time configuration", e);
      }
    }

    if (config == null) {
      config = new ClockTimeFabricConfig();
      try {
        Files.createDirectories(configPath.getParent());
        try (Writer writer = Files.newBufferedWriter(configPath)) {
          GSON.toJson(config, writer);
        }
      } catch (Exception e) {
        LOGGER.error("Failed to write default clock-time configuration", e);
      }
    }
    return config;
  }
}
