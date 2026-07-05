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

    Locale defaultLocale = new Locale(fallbackLanguage != null ? fallbackLanguage : "en");
    registry.defaultLocale(defaultLocale);

    Locale[] locales = {
      Locale.ROOT,
      Locale.ENGLISH,
      Locale.GERMAN,
      new Locale("es"),
      new Locale("fr"),
      Locale.ITALIAN,
      Locale.JAPANESE,
      Locale.KOREAN,
      new Locale("nl"),
      new Locale("pl"),
      new Locale("pt"),
      new Locale("ru"),
      new Locale("tr"),
      new Locale("uk"),
      Locale.SIMPLIFIED_CHINESE,
      Locale.TRADITIONAL_CHINESE
    };

    for (Locale locale : locales) {
      try {
        ResourceBundle bundle = ResourceBundle.getBundle("languages.messages", locale, classLoader);
        registry.registerAll(locale, bundle, true);
      } catch (MissingResourceException e) {
        // Ignore missing bundles
      }
    }

    GlobalTranslator.translator().addSource(registry);
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
