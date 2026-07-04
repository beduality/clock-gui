package io.github.beduality.clock_time.application;

import java.text.MessageFormat;
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
}
