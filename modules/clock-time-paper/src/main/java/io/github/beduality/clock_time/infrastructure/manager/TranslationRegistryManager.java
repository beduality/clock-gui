package io.github.beduality.clock_time.infrastructure.manager;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.translation.TranslationRegistry;
import org.bukkit.plugin.java.JavaPlugin;

/* Handles translation registry bindings and resource bundle loading. */
public class TranslationRegistryManager {

  private final JavaPlugin plugin;
  private final File jarFile;

  public TranslationRegistryManager(JavaPlugin plugin, File jarFile) {
    this.plugin = plugin;
    this.jarFile = jarFile;
  }

  /*
   * Extracts files, loads bundles, and binds translation keys to Adventure GlobalTranslator.
   *
   * @param fallbackLanguage fallback ISO 639-1 language code
   */
  public void setup(String fallbackLanguage) {
    extractDefaultLanguages();
    ClassLoader classLoader = getClassLoaderWithExternalFolder();

    TranslationRegistry registry = TranslationRegistry.create(Key.key("clocktime", "main"));

    Locale defaultLocale = parseLocale(fallbackLanguage != null ? fallbackLanguage : "en");
    registry.defaultLocale(defaultLocale);

    java.util.Set<Locale> localesToLoad = new java.util.HashSet<>();
    localesToLoad.add(Locale.ROOT);
    localesToLoad.add(Locale.ENGLISH);
    localesToLoad.add(defaultLocale);

    // Always include default supported locales to ensure they load from the classpath
    // in test or development environments where file extraction is skipped or fails.
    for (String lang :
        new String[] {
          "de", "es", "fr", "it", "ja", "ko", "nl", "pl", "pt", "ru", "tr", "uk", "zh_CN", "zh_TW"
        }) {
      localesToLoad.add(parseLocale(lang));
    }

    File languagesFolder = new File(plugin.getDataFolder(), "languages");
    if (languagesFolder.exists() && languagesFolder.isDirectory()) {
      File[] files =
          languagesFolder.listFiles(
              (dir, name) -> name.startsWith("messages") && name.endsWith(".properties"));
      if (files != null) {
        for (File file : files) {
          String filename = file.getName();
          if (filename.equals("messages.properties")) {
            localesToLoad.add(Locale.ROOT);
          } else if (filename.startsWith("messages_") && filename.endsWith(".properties")) {
            String suffix =
                filename.substring(
                    "messages_".length(), filename.length() - ".properties".length());
            localesToLoad.add(parseLocale(suffix));
          }
        }
      }
    }

    for (Locale locale : localesToLoad) {
      try {
        ResourceBundle bundle = ResourceBundle.getBundle("languages.messages", locale, classLoader);
        registry.registerAll(locale, bundle, true);
      } catch (MissingResourceException e) {
        // Ignore missing bundles
      }
    }

    GlobalTranslator.translator().addSource(registry);
  }

  private Locale parseLocale(String localeStr) {
    if (localeStr == null || localeStr.isEmpty() || localeStr.equalsIgnoreCase("root")) {
      return Locale.ROOT;
    }
    String[] parts = localeStr.split("_");
    if (parts.length == 1) {
      return Locale.of(parts[0]);
    } else if (parts.length == 2) {
      return Locale.of(parts[0], parts[1]);
    } else if (parts.length >= 3) {
      return Locale.of(parts[0], parts[1], parts[2]);
    }
    return Locale.of(localeStr);
  }

  private ClassLoader getClassLoaderWithExternalFolder() {
    ClassLoader parent = plugin.getClass().getClassLoader();
    try {
      URL[] urls = new URL[] {plugin.getDataFolder().toURI().toURL()};
      return new URLClassLoader(urls, parent);
    } catch (MalformedURLException e) {
      plugin
          .getLogger()
          .log(
              Level.WARNING,
              "Failed to initialize external translation classloader, falling back to internal",
              e);
      return parent;
    }
  }

  private void extractDefaultLanguages() {
    if (jarFile == null || !jarFile.exists()) {
      return;
    }
    try (java.util.jar.JarFile jar = new java.util.jar.JarFile(jarFile)) {
      java.util.Enumeration<java.util.jar.JarEntry> entries = jar.entries();
      while (entries.hasMoreElements()) {
        java.util.jar.JarEntry entry = entries.nextElement();
        String name = entry.getName();
        if (name.startsWith("languages/") && name.endsWith(".properties")) {
          File outFile = new File(plugin.getDataFolder(), name);
          if (!outFile.exists()) {
            plugin.saveResource(name, false);
          }
        }
      }
    } catch (Exception e) {
      plugin.getLogger().log(Level.WARNING, "Failed to dynamically extract default languages", e);
    }
  }
}
