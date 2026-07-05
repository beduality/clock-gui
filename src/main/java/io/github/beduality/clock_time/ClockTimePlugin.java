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

        // Extract all default translation properties files to plugins/ClockTime/languages/
        // Administrators can edit these files directly to customize translations.
        saveDefaultLanguages();

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

    private void saveDefaultLanguages() {
        try (java.util.jar.JarFile jar = new java.util.jar.JarFile(getFile())) {
            java.util.Enumeration<java.util.jar.JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                java.util.jar.JarEntry entry = entries.nextElement();
                String name = entry.getName();
                if (name.startsWith("languages/") && name.endsWith(".properties")) {
                    saveResource(name, false);
                }
            }
        } catch (Exception e) {
            getLogger().log(Level.WARNING, "Failed to dynamically extract default languages", e);
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("ClockTime Plugin Disabled");
    }
}
