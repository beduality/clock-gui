package io.github.beduality.clock_time.infrastructure.manager;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.translation.TranslationRegistry;

public class FabricTranslationRegistryManager {

  public void setup(String fallbackLanguage) {
    TranslationRegistry registry = TranslationRegistry.create(Key.key("clocktime", "main"));
    registry.defaultLocale(parseLocale(fallbackLanguage));

    Set<Locale> localesToLoad = new java.util.HashSet<>();
    localesToLoad.add(Locale.ROOT);
    localesToLoad.add(Locale.ENGLISH);
    localesToLoad.add(parseLocale(fallbackLanguage));

    for (String lang :
        new String[] {
          "de", "es", "fr", "it", "ja", "ko", "nl", "pl", "pt", "ru", "tr", "uk", "zh_CN", "zh_TW"
        }) {
      localesToLoad.add(parseLocale(lang));
    }

    ClassLoader classLoader = getClass().getClassLoader();
    for (Locale locale : localesToLoad) {
      try {
        ResourceBundle bundle = ResourceBundle.getBundle("languages.messages", locale, classLoader);
        registry.registerAll(locale, bundle, true);
      } catch (MissingResourceException e) {
        // Ignore missing resource bundles
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
}
