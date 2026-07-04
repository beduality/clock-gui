package io.github.beduality.clock_time;

import io.github.beduality.clock_time.application.TranslationService;
import io.github.beduality.clock_time.domain.TimeFormatter;
import io.github.beduality.clock_time.infrastructure.ClockInteractListener;
import org.bukkit.plugin.java.JavaPlugin;

public class ClockTimePlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // Instantiate core components
        var timeFormatter = new TimeFormatter();
        var translationService = new TranslationService(this.getClassLoader());

        // Register listeners
        getServer().getPluginManager().registerEvents(
            new ClockInteractListener(timeFormatter, translationService),
            this
        );

        getLogger().info("ClockTime Plugin Enabled");
    }

    @Override
    public void onDisable() {
        getLogger().info("ClockTime Plugin Disabled");
    }
}
