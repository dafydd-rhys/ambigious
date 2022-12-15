package cs230;

import java.util.ArrayList;
import javafx.scene.image.Image;
import javafx.scene.paint.Paint;

/**
 * Theme manager, allows user to select themes for there game
 */
public class Themes {

    /**
     * Current selected theme
     */
    public static THEME_TYPE currentTheme;
    /**
     * directory of themes
     */
    public static String themeDir = System.getProperty("user.dir") + "\\src\\main\\resources\\cs230\\themes\\";
    /**
     * winter theme palette
     */
    public static Paint[] winterColors =
            {p("c2e1f0"), p("76cbf5"), p("5e7ba8"), p("a5a7a8"), p("d2d5d6"), p("f0f1f2")};
    /**
     * party theme palette
     */
    public static Paint[] partyColors =
            {p("e6ad8c"), p("F3908F"), p("70C2E2"), p("a08ce6"), p("8AF3E9"), p("F897E6")};
    /**
     * autumn theme palette
     */
    public static Paint[] autumnColors =
            {p("6d7178"), p("b1b4b5"), p("7d9fb0"), p("636d83"), p("96b0b5"), p("b9e8f0")};
    /**
     * jungle theme palette
     */
    public static Paint[] jungleColors =
            {p("aaf0a1"), p("19270d"), p("25591f"), p("98a353"), p("72601b"), p("593a0e")};
    /**
     * winter theme instance
     */
    public static Theme winterTheme = new Theme("Winter", winterColors, new Image(themeDir + "1.gif"));
    /**
     * party theme instance
     */
    public static Theme partyTheme = new Theme("Party", partyColors, new Image(themeDir + "2.gif"));
    /**
     * autumn theme instance
     */
    public static Theme autumnTheme = new Theme("Autumn", autumnColors, new Image(themeDir + "3.gif"));
    /**
     * jungle theme instance
     */
    public static Theme jungleTheme = new Theme("Jungle", jungleColors, new Image(themeDir + "4.gif"));

    /**
     * Enum used to distinguish current theme
     */
    public enum THEME_TYPE {
        /**
         * Mixed Theme
         */
        MixedThemes,
        /**
         * Winter Theme
         */
        Winter,
        /**
         * Party Theme
         */
        Party,
        /**
         * Autumn Theme
         */
        Autumn,
        /**
         * Jungle Theme
         */
        Jungle
    }

    /**
     * Retrieves themes' specific palette
     *
     * @param level current played level
     * @return the wanted palette
     */
    public static Theme typeToTheme(int level) {
        Theme theme;

        switch (currentTheme) {
            case Winter -> theme = winterTheme;
            case Party -> theme = partyTheme;
            case Autumn -> theme = autumnTheme;
            case Jungle -> theme = jungleTheme;
            default -> {
                ArrayList<Theme> themes = new ArrayList<>();
                themes.add(winterTheme);
                themes.add(partyTheme);
                themes.add(autumnTheme);
                themes.add(jungleTheme);

                if (level > 4) {
                    theme = themes.get(level - 5);
                } else {
                    theme = themes.get(level - 1);
                }
            }
        }

        return theme;
    }

    /**
     * retrieves paint value of hex string
     *
     * @param value hex string
     * @return paint color
     */
    public static Paint p(String value) {
        return Paint.valueOf(value);
    }

    /**
     * holds data for each theme
     */
    public record Theme(String name, Paint[] colors, Image gif) {

        /**
         * returns theme name
         *
         * @return theme name
         */
        public String getName() {
            return name;
        }

        /**
         * retrieves themes' palette
         *
         * @return colors of palette
         */
        public Paint[] getColors() {
            return colors;
        }

        /**
         * retrieves the FX of said theme
         *
         * @return theme FX
         */
        public Image getGIF() {
            return gif;
        }

    }

}
