package io.github.beduality.clock_time.application;

import java.text.MessageFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.ResourceBundle.Control;

/**
 * Application service managing localization resources.
 * Handles language resolution, resource bundle lookups, custom fallbacks,
 * and time-based localizations. It is decoupled from the Paper/Bukkit API
 * to support multi-platform reuse or unit testing.
 */
public class TranslationService {
    private final ClassLoader classLoader;
    private final Locale defaultLocale;
    private final Control control;

    /**
     * Constructs a new TranslationService.
     *
     * @param classLoader      the classloader utilized to locate and load resource bundles (e.g. languages/messages.properties)
     * @param fallbackLanguage the default ISO 639-1 language code fallback if a requested locale is unavailable
     */
    public TranslationService(ClassLoader classLoader, String fallbackLanguage) {
        this.classLoader = classLoader;
        this.defaultLocale = new Locale(fallbackLanguage != null ? fallbackLanguage : "en");
        
        // Custom control that inserts our configured fallback locale before the ROOT fallback
        this.control = new Control() {
            @Override
            public List<Locale> getCandidateLocales(String baseName, Locale locale) {
                List<Locale> candidates = new ArrayList<>(super.getCandidateLocales(baseName, locale));
                if (!candidates.contains(defaultLocale)) {
                    int rootIndex = candidates.indexOf(Locale.ROOT);
                    List<Locale> fallbackCandidates = super.getCandidateLocales(baseName, defaultLocale);
                    if (rootIndex != -1) {
                        // Insert fallback candidate locales before the ROOT (base) bundle
                        for (int i = 0; i < fallbackCandidates.size(); i++) {
                            Locale fb = fallbackCandidates.get(i);
                            if (!candidates.contains(fb)) {
                                candidates.add(rootIndex + i, fb);
                            }
                        }
                    } else {
                        candidates.addAll(fallbackCandidates);
                    }
                }
                return candidates;
            }
        };
    }

    /**
     * Resolves a localized string value from resource bundles.
     *
     * @param key    the message translation key (e.g. "clock_time.message.time")
     * @param locale the target {@link Locale} to translate the message into
     * @param args   optional arguments for message format compilation ({0}, {1}, etc.)
     * @return the localized string value, or the key itself if the resource bundle or key is missing
     */
    public String getMessage(String key, Locale locale, Object... args) {
        ResourceBundle bundle;
        try {
            bundle = ResourceBundle.getBundle("languages.messages", locale, classLoader, control);
        } catch (MissingResourceException e) {
            return key;
        }

        try {
            String pattern = bundle.getString(key);
            if (args.length > 0) {
                return MessageFormat.format(pattern, args);
            }
            return pattern;
        } catch (MissingResourceException e) {
            return key;
        }
    }

    /**
     * Formats a given {@link LocalTime} into a localized, styled message.
     *
     * @param time        the {@link LocalTime} object representation of the time
     * @param locale      the {@link Locale} of the player client receiving the message
     * @return the fully localized and formatted chat message string
     */
    public String getFormattedTimeMessage(LocalTime time, Locale locale) {
        // Auto-detect localized short style (handles 12h vs 24h & native AM/PM marker translations)
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(locale);
        String formattedTime = time.format(formatter);
        return getMessage("clock_time.message.time", locale, formattedTime);
    }
}
