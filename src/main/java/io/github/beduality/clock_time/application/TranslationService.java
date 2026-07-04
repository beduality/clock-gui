package io.github.beduality.clock_time.application;

import java.text.MessageFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class TranslationService {
    private final ClassLoader classLoader;

    public TranslationService(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public String getMessage(String key, Locale locale, Object... args) {
        ResourceBundle bundle;
        try {
            bundle = ResourceBundle.getBundle("languages.messages", locale, classLoader);
        } catch (MissingResourceException e) {
            try {
                bundle = ResourceBundle.getBundle("languages.messages", Locale.ENGLISH, classLoader);
            } catch (MissingResourceException ex) {
                return key;
            }
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
