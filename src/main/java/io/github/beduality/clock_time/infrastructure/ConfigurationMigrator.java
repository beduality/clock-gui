package io.github.beduality.clock_time.infrastructure;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Handles configuration version checks and automatic migration logic.
 */
public class ConfigurationMigrator {

    private final JavaPlugin plugin;

    public ConfigurationMigrator(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Performs a check of the configuration file version and executes migration tasks if needed.
     */
    public void migrate() {
        FileConfiguration config = plugin.getConfig();
        int currentVersion = config.getInt("config-version", 0);
        int targetVersion = 1;
        if (currentVersion < targetVersion) {
            plugin.getLogger().info("Migrating configuration from version " + currentVersion + " to " + targetVersion);
            config.set("config-version", targetVersion);
            plugin.saveConfig();
        }
    }
}
