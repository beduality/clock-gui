package io.github.beduality.clock_time;

import io.github.beduality.clock_time.application.TranslationService;
import io.github.beduality.clock_time.domain.TimeFormatter;
import io.github.beduality.clock_time.infrastructure.ClockInteractListener;
import org.bukkit.plugin.java.JavaPlugin;

public class ClockTimePlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // Save default config if not present
        saveDefaultConfig();

        // Retrieve config values
        String timeFormat = getConfig().getString("time-format", "locale");

        // Instantiate core components (Manual Dependency Injection)
        var timeFormatter = new TimeFormatter();
        var translationService = new TranslationService(this.getClassLoader());

        // Register listeners
        getServer().getPluginManager().registerEvents(
            new ClockInteractListener(timeFormatter, translationService, timeFormat),
            this
        );

        getLogger().info("ClockTime Plugin Enabled");
    }

    @Override
    public void onDisable() {
        getLogger().info("ClockTime Plugin Disabled");
    }
}
