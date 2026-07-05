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
        String[] languages = {
            "messages.properties",
            "messages_de.properties",
            "messages_es.properties",
            "messages_fr.properties",
            "messages_it.properties",
            "messages_ja.properties",
            "messages_ko.properties",
            "messages_nl.properties",
            "messages_pl.properties",
            "messages_pt.properties",
            "messages_ru.properties",
            "messages_tr.properties",
            "messages_uk.properties",
            "messages_zh_CN.properties",
            "messages_zh_TW.properties"
        };
        for (String lang : languages) {
            saveResource("languages/" + lang, false);
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("ClockTime Plugin Disabled");
    }
}
