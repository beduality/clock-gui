package io.github.beduality.clock_time;

import io.github.beduality.clock_time.application.TranslationService;
import io.github.beduality.clock_time.domain.TimeFormatter;
import io.github.beduality.clock_time.infrastructure.ClockInteractListener;
import io.github.beduality.clock_time.infrastructure.ConfigurationMigrator;
import io.github.beduality.clock_time.infrastructure.LanguageAssetManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main entry point for the ClockTime Paper plugin.
 * Acts as the Composition Root bootstrapping dependency injections.
 */
public class ClockTimePlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();

        // Delegate configuration migration
        new ConfigurationMigrator(this).migrate();

        // Delegate translation assets extraction & custom ClassLoader creation
        var assetManager = new LanguageAssetManager(this, getFile());
        assetManager.saveDefaultLanguages();
        ClassLoader classLoader = assetManager.getClassLoaderWithExternalFolder();

        String fallbackLanguage = getConfig().getString("fallback-language", "en");

        // Instantiate core components (Manual Dependency Injection)
        var timeFormatter = new TimeFormatter();
        var translationService = new TranslationService(classLoader, fallbackLanguage);

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
