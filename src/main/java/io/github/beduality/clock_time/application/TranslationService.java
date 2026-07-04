package io.github.beduality.clock_time.application;

import io.github.beduality.clock_time.domain.FormattedTime;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
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
            bundle = ResourceBundle.getBundle("messages", locale, classLoader);
        } catch (MissingResourceException e) {
            try {
                bundle = ResourceBundle.getBundle("messages", Locale.ENGLISH, classLoader);
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

    public String getFormattedTimeMessage(FormattedTime time, Locale locale, String formatStyle) {
        boolean is12h;
        if ("12h".equalsIgnoreCase(formatStyle)) {
            is12h = true;
        } else if ("24h".equalsIgnoreCase(formatStyle)) {
            is12h = false;
        } else {
            is12h = prefers12h(locale);
        }

        if (is12h) {
            String message = getMessage("clock_time.message.time.12h", locale, time.getFormattedHour12(), time.getFormattedMinute(), time.period());
            if ("clock_time.message.time.12h".equals(message)) {
                return getMessage("clock_time.message.time", locale, time.getFormattedHour12(), time.getFormattedMinute(), time.period());
            }
            return message;
        } else {
            String message = getMessage("clock_time.message.time.24h", locale, time.getFormattedHour24(), time.getFormattedMinute());
            if ("clock_time.message.time.24h".equals(message)) {
                String legacy = getMessage("clock_time.message.time", locale, time.getFormattedHour24(), time.getFormattedMinute(), "");
                return legacy.trim();
            }
            return message;
        }
    }

    public boolean prefers12h(Locale locale) {
        try {
            java.text.DateFormat df = java.text.DateFormat.getTimeInstance(java.text.DateFormat.SHORT, locale);
            if (df instanceof SimpleDateFormat sdf) {
                return sdf.toPattern().contains("a");
            }
        } catch (Exception e) {
            // Fallback
        }
        return true;
    }
}
