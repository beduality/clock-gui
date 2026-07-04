package io.github.beduality.clock_time;

import io.github.beduality.clock_time.application.TranslationService;
import io.github.beduality.clock_time.domain.TimeFormatter;
import io.github.beduality.clock_time.infrastructure.ClockInteractListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.logging.Level;

public class ClockTimePlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // Save default config if not present
        saveDefaultConfig();

        // Extract the main default translation properties file to plugins/ClockTime/languages/
        // Users can copy others or create custom overrides if desired.
        saveResource("languages/messages.properties", false);

        // Retrieve config values
        String timeFormat = getConfig().getString("time-format", "locale");
        String fallbackLanguage = getConfig().getString("fallback-language", "en");

        // Set up the custom classloader pointing to the plugin's data folder
        ClassLoader classLoader = getClassLoaderWithExternalFolder();

        // Instantiate core components (Manual Dependency Injection)
        var timeFormatter = new TimeFormatter();
        var translationService = new TranslationService(classLoader, fallbackLanguage);

        // Register listeners
        getServer().getPluginManager().registerEvents(
            new ClockInteractListener(timeFormatter, translationService, timeFormat),
            this
        );

        getLogger().info("ClockTime Plugin Enabled");
    }

    private ClassLoader getClassLoaderWithExternalFolder() {
        ClassLoader parent = this.getClassLoader();
        try {
            // We want the ClassLoader to search inside plugins/ClockTime/
            URL[] urls = new URL[] { getDataFolder().toURI().toURL() };
            return new URLClassLoader(urls, parent);
        } catch (MalformedURLException e) {
            getLogger().log(Level.WARNING, "Failed to initialize external translation classloader, falling back to internal", e);
            return parent;
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("ClockTime Plugin Disabled");
    }
}
