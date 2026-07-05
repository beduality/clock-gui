package io.github.beduality.clock_time.infrastructure.config;

import org.bukkit.plugin.java.JavaPlugin;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.util.logging.Level;

/**
 * Handles loading, validation, and migration of the plugin configuration.
 */
public class ConfigLoader {

    private final JavaPlugin plugin;

    public ConfigLoader(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Loads the config file and performs migration updates if required.
     *
     * @return the loaded and possibly migrated PluginConfig
     */
    public PluginConfig loadAndMigrate() {
        File configFile = new File(plugin.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            plugin.saveDefaultConfig();
        }

        var loader = YamlConfigurationLoader.builder()
                .file(configFile)
                .defaultOptions(options -> options.shouldCopyDefaults(true))
                .build();

        try {
            var node = loader.load();
            PluginConfig config = node.get(PluginConfig.class);
            if (config == null) {
                config = new PluginConfig();
            }

            int targetVersion = 1;
            if (config.getConfigVersion() < targetVersion) {
                plugin.getLogger().info("Migrating configuration from version " + config.getConfigVersion() + " to " + targetVersion);
                config.setConfigVersion(targetVersion);
                node.set(PluginConfig.class, config);
                loader.save(node);
            }
            return config;
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to load or migrate configuration", e);
            return new PluginConfig();
        }
    }
}
