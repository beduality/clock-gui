package io.github.beduality.clock_time.infrastructure;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.logging.Level;

/**
 * Manages translation properties files on the disk and provides
 * a ClassLoader configured to resolve externalized translation overrides.
 */
public class LanguageAssetManager {

    private final JavaPlugin plugin;
    private final File jarFile;

    public LanguageAssetManager(JavaPlugin plugin, File jarFile) {
        this.plugin = plugin;
        this.jarFile = jarFile;
    }

    /**
     * Extracts default language properties files from the plugin JAR to the data folder.
     */
    public void saveDefaultLanguages() {
        if (jarFile == null || !jarFile.exists()) {
            return;
        }
        try (java.util.jar.JarFile jar = new java.util.jar.JarFile(jarFile)) {
            java.util.Enumeration<java.util.jar.JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                java.util.jar.JarEntry entry = entries.nextElement();
                String name = entry.getName();
                if (name.startsWith("languages/") && name.endsWith(".properties")) {
                    plugin.saveResource(name, false);
                }
            }
        } catch (Exception e) {
            plugin.getLogger().log(Level.WARNING, "Failed to dynamically extract default languages", e);
        }
    }

    /**
     * Creates a ClassLoader that prioritizes resource resolution from the external data directory.
     *
     * @return the ClassLoader to be used by the translation service
     */
    public ClassLoader getClassLoaderWithExternalFolder() {
        ClassLoader parent = plugin.getClass().getClassLoader();
        try {
            URL[] urls = new URL[] { plugin.getDataFolder().toURI().toURL() };
            return new URLClassLoader(urls, parent);
        } catch (MalformedURLException e) {
            plugin.getLogger().log(Level.WARNING, "Failed to initialize external translation classloader, falling back to internal", e);
            return parent;
        }
    }
}
