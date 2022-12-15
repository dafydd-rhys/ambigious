package cs230.external;

import java.util.Arrays;
import java.util.Locale;

/**
 * Used to manage selected language of the user
 */
public class LocalizationManager {

    /**
     * List of all supported languages in there language code
     */
    public static final Locale[] SUPPORTED_LOCALES = {
            Locale.forLanguageTag("en-GB"), // British English
            Locale.forLanguageTag("yue"),   // Cantonese
            Locale.forLanguageTag("in"),    // Indonesian
            Locale.forLanguageTag("pt"),    // Portuguese
            Locale.forLanguageTag("tl"),    // Tagalog
            Locale.forLanguageTag("cy"),    // Welsh
    };

    /**
     * The language that is used if all others aren't supported
     */
    public static final Locale FALLBACK_LOCALE = SUPPORTED_LOCALES[0];

    /**
     * current language
     */
    private static Locale currentLocale = FALLBACK_LOCALE;

    /**
     * Get default system locale
     *
     * @return the language the user is currently using on their OS
     */
    public static Locale getSystemLocale() {
        return Locale.getDefault();
    }

    /**
     * Get current locale
     *
     * @return current language
     */
    public static Locale getCurrentLocale() {
        return currentLocale;
    }

    /**
     * Sets current language
     *
     * @param currentLocale the new language
     */
    public static void setCurrentLocale(Locale currentLocale) {
        LocalizationManager.currentLocale = currentLocale;
    }

    /**
     * Identifies whether a language is supported by the users computer
     *
     * @param locale wanted language
     * @return whether the user can use this language
     */
    public static boolean localeIsSupported(Locale locale) {
        return Arrays.asList(SUPPORTED_LOCALES).contains(locale);
    }

}
