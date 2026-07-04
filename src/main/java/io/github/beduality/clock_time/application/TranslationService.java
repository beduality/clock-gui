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

public class TranslationService {
    private final ClassLoader classLoader;
    private final Locale defaultLocale;
    private final Control control;

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

    public String getFormattedTimeMessage(LocalTime time, Locale locale, String formatStyle) {
        DateTimeFormatter formatter;
        if ("12h".equalsIgnoreCase(formatStyle)) {
            formatter = DateTimeFormatter.ofPattern("hh:mm a", locale);
        } else if ("24h".equalsIgnoreCase(formatStyle)) {
            formatter = DateTimeFormatter.ofPattern("HH:mm", locale);
        } else {
            // Auto-detect localized short style (handles 12h vs 24h & native AM/PM marker translations)
            formatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(locale);
        }
        String formattedTime = time.format(formatter);
        return getMessage("clock_time.message.time", locale, formattedTime);
    }
}
