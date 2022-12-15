package cs230.model;

/**
 * Represents the 6 possible colors of a tile.
 *
 * @author Dafydd-Rhys Maund
 * @author Oliver
 */
public enum TileColor {
    /**
     * Color 1.
     */
    COL1,
    /**
     * Color 2.
     */
    COL2,
    /**
     * Color 3.
     */
    COL3,
    /**
     * Color 4.
     */
    COL4,
    /**
     * Color 5.
     */
    COL5,
    /**
     * Color 6.
     */
    COL6;

    /**
     * Gets color regex.
     *
     * @param s the regex
     * @return the color
     */
    public static TileColor getColorRegex(String s) {
        TileColor color;

        switch (s) {
            case "COL1" -> color = COL1;
            case "COL2" -> color = COL2;
            case "COL3" -> color = COL3;
            case "COL4" -> color = COL4;
            case "COL5" -> color = COL5;
            default -> color = COL6;
        }

        return color;
    }

}
