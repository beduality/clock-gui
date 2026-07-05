package io.github.beduality.clock_time;

import io.github.beduality.clock_time.domain.service.TimeFormatter;
import io.github.beduality.clock_time.infrastructure.config.ConfigLoader;
import io.github.beduality.clock_time.infrastructure.config.PluginConfig;
import io.github.beduality.clock_time.infrastructure.listener.ClockInteractListener;
import io.github.beduality.clock_time.infrastructure.translation.TranslationRegistryManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main entry point for the ClockTime Paper plugin.
 * Acts as the Composition Root bootstrapping dependency injections.
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

        getServer().getPluginManager().registerEvents(
            new ClockInteractListener(timeFormatter),
            this
        );

        getLogger().info("ClockTime Plugin Enabled");
    }

    @Override
    public void onDisable() {
        getLogger().info("ClockTime Plugin Disabled");
    }
}
