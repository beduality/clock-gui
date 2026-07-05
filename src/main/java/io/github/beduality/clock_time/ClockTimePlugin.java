package io.github.beduality.clock_time;

import io.github.beduality.clock_time.application.TranslationService;
import io.github.beduality.clock_time.domain.TimeFormatter;
import io.github.beduality.clock_time.infrastructure.ClockInteractListener;
import io.github.beduality.clock_time.infrastructure.PluginConfig;
import org.bukkit.plugin.java.JavaPlugin;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.logging.Level;

/**
 * Main entry point for the ClockTime Paper plugin.
 * Acts as the Composition Root bootstrapping dependency injections.
 */
public class ClockTimePlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            saveDefaultConfig();
        }

        var loader = YamlConfigurationLoader.builder()
                .file(configFile)
                .defaultOptions(options -> options.shouldCopyDefaults(true))
                .build();

        PluginConfig config;
        try {
            var node = loader.load();
            config = node.get(PluginConfig.class);
            if (config == null) {
                config = new PluginConfig();
            }

            int targetVersion = 1;
            if (config.getConfigVersion() < targetVersion) {
                getLogger().info("Migrating configuration from version " + config.getConfigVersion() + " to " + targetVersion);
                config.setConfigVersion(targetVersion);
                node.set(PluginConfig.class, config);
                loader.save(node);
            }
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "Failed to load or migrate configuration", e);
            config = new PluginConfig();
        }

        saveDefaultLanguages();
        ClassLoader classLoader = getClassLoaderWithExternalFolder();

        var timeFormatter = new TimeFormatter();
        var translationService = new TranslationService(classLoader, config.getFallbackLanguage());

        getServer().getPluginManager().registerEvents(
            new ClockInteractListener(timeFormatter, translationService),
            this
        );

        getLogger().info("ClockTime Plugin Enabled");
    }

    private ClassLoader getClassLoaderWithExternalFolder() {
        ClassLoader parent = this.getClassLoader();
        try {
            URL[] urls = new URL[] { getDataFolder().toURI().toURL() };
            return new URLClassLoader(urls, parent);
        } catch (MalformedURLException e) {
            getLogger().log(Level.WARNING, "Failed to initialize external translation classloader, falling back to internal", e);
            return parent;
        }
    }

    private void saveDefaultLanguages() {
        File jarFile = getFile();
        if (jarFile == null || !jarFile.exists()) {
            return;
        }
        try (java.util.jar.JarFile jar = new java.util.jar.JarFile(jarFile)) {
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
