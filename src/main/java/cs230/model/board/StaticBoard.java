package cs230.model.board;

import cs230.model.Board;

/**
 * Static board used to store board related information across FXMLs.
 *
 * @author Dafydd-Rhys Maund (2003900)
 */
public class StaticBoard {

    /**
     * Whether the level was won or lost.
     */
    public static boolean won = false;
    /**
     * The current level being played.
     */
    public static int level = 1;
    /**
     * The current instance of the generated board.
     */
    public static BoardReaderAndGenerator reader;
    /**
     * The board being played.
     */
    public static Board board;

}
