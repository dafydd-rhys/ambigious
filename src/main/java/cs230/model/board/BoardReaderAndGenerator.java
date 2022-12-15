package cs230.model.board;

import cs230.Themes;
import cs230.model.Board;
import cs230.model.Tile;
import cs230.model.entity.Entity;
import cs230.model.entity.Item;
import cs230.model.entity.Player;
import cs230.model.entity.enemies.FloorFollowingThief;
import cs230.model.entity.enemies.FlyingAssassin;
import cs230.model.entity.enemies.SmartThief;
import cs230.model.entity.enums.Direction;
import cs230.model.entity.enums.GateLeverColour;
import cs230.model.entity.items.Bomb;
import cs230.model.entity.items.Clock;
import cs230.model.entity.items.Door;
import cs230.model.entity.items.Gate;
import cs230.model.entity.items.Lever;
import cs230.model.entity.items.Loot;
import cs230.model.entity.items.Note;
import cs230.model.TileColor;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javafx.scene.paint.Paint;

/**
 * Class used to generate board.
 *
 * @author Dafydd-Rhys Maund (2003900)
 */
public class BoardReaderAndGenerator {
    /**
     * Total number of colours
     */
    public static final int TOTAL_NUMBER_OF_COLOURS = 6;
    /**
     * Number of colours per tile
     */
    public static final int TOTAL_NUMBER_OF_COLOURS_PER_TILE = 4;
    /**
     * The level file used to gather data.
     */
    private final File actFile;
    /**
     * Tile array used to store tiles.
     */
    private Tile[][] tiles;
    /**
     * Current board level.
     */
    private final int currentLevel;
    /**
     * Width of board.
     */
    private int sizeX = 0;
    /**
     * Height of board.
     */
    private int sizeY = 0;
    /**
     * Expected time to complete board level.
     */
    private int expectedTime = 0;
    /**
     * Colors used on board
     */
    private Paint[] colors = new Paint[TOTAL_NUMBER_OF_COLOURS];

    /**
     * Instantiates a new Board reader and generator instance.
     *
     * @param level wanted level to read
     */
    public BoardReaderAndGenerator(int level) {
        String file = System.getProperty("user.dir") + "\\src\\main\\resources\\cs230\\levels\\level-";
        actFile = new File(file + level + ".txt");
        currentLevel = level;
    }

    /**
     * Read board from .txt file.
     *
     * @param save whether its save file or not
     * @return the board
     * @throws FileNotFoundException the file not found exception
     */
    public Board readBoard(boolean save) throws FileNotFoundException {
        Scanner scanner = new Scanner(actFile);

        String[] split = scanner.next().split(",");

        sizeX = Integer.parseInt(split[0]);
        sizeY = Integer.parseInt(split[1]);
        tiles = new Tile[sizeX][sizeY];

        expectedTime = scanner.nextInt();

        split = scanner.next().split(",");
        Player player = new Player(Integer.parseInt(split[0]), Integer.parseInt(split[1]));

        if (!save && Themes.currentTheme != Themes.THEME_TYPE.MixedThemes) {
            //TODO skip line better than this
            for (int i = 0; i < colors.length; i++) {
                scanner.next();
            }

            colors = Themes.typeToTheme(currentLevel).getColors();
        } else {
            for (int i = 0; i < colors.length; i++) {
                colors[i] = Paint.valueOf(scanner.next());
            }
        }

        //read level
        for (int row = 0; scanner.hasNextLine() && row < sizeY; row++) {
            for (int i = 0; i < sizeX; i++) {
                String tileData = scanner.next();

                TileColor[] colors = new TileColor[TOTAL_NUMBER_OF_COLOURS_PER_TILE];
                for (int j = 0; j < tileData.length(); j++) {
                    switch (tileData.charAt(j)) {
                        case '1' -> colors[j] = TileColor.COL1;
                        case '2' -> colors[j] = TileColor.COL2;
                        case '3' -> colors[j] = TileColor.COL3;
                        case '4' -> colors[j] = TileColor.COL4;
                        case '5' -> colors[j] = TileColor.COL5;
                        default -> colors[j] = TileColor.COL6;
                    }
                }
                tiles[i][row] = new Tile(colors, i, row);
            }
        }
        Board board = new Board(tiles, sizeX, sizeY, currentLevel, player, expectedTime, colors);

        if (!save) {
            while (scanner.hasNext()) {
                split = scanner.next().split(":");
                String[] coords = split[1].split(",");

                switch (split[0]) {
                    case "E1" -> {
                        String[] dir = coords[1].split(";");
                        String[] col = dir[1].split("-");

                        board.placeEntity(new FloorFollowingThief(Direction.getDirectionRegex(col[0]),
                                TileColor.getColorRegex(col[1])), board.getTile(Integer.parseInt(coords[0]),
                                Integer.parseInt(dir[0])));
                    }
                    case "E2" -> {
                        String[] dir = coords[1].split(";");

                        board.placeEntity(new FlyingAssassin(Direction.getDirectionRegex(dir[1])),
                                board.getTile(Integer.parseInt(coords[0]), Integer.parseInt(dir[0])));
                    }
                    case "E3" -> {
                        String[] dir = coords[1].split(";");

                        SmartThief smart = new SmartThief(Direction.getDirectionRegex(dir[1]));
                        Tile tile = board.getTile(Integer.parseInt(coords[0]), Integer.parseInt(dir[0]));

                        smart.setStarting(tile);
                        board.placeEntity(smart, tile);
                    }
                    default -> {
                        Entity e = getEntity(split[0]);

                        if (e instanceof Item) {
                            if (board.getTile(Integer.parseInt(coords[0]),
                                    Integer.parseInt(coords[1])).getItem() == null) {
                                board.placeEntity(e, board.getTile(Integer.parseInt(coords[0]),
                                        Integer.parseInt(coords[1])));
                            }
                        } else {
                            board.placeEntity(e, board.getTile(Integer.parseInt(coords[0]),
                                    Integer.parseInt(coords[1])));
                        }
                    }
                }
            }
            setGatesAndLevers(board);

            for (Entity e : board.getEntities()) {
                if (e instanceof SmartThief s) {
                    s.initialize(board);
                }
            }
        }

        return board;
    }

    /**
     * Sets gates and levers of same color to each other.
     *
     * @param board the board
     */
    public void setGatesAndLevers(Board board) {
        for (Entity entity : board.getEntities()) {
            if (entity instanceof Lever lever) {

                for (Entity e : board.getEntities()) {
                    if (e instanceof Gate gate) {
                        if (lever.getColour() == gate.getColour()) {
                            lever.addGate(gate);
                        }
                    }
                }
            }
        }
    }

    /**
     * Gets entity related to passed code.
     *
     * @param regex entity code
     * @return a new instance of said entity
     */
    private Entity getEntity(String regex) {
        return switch (regex) {
            case "L1" -> new Loot(Loot.LOOT_TYPE.CENT);
            case "L2" -> new Loot(Loot.LOOT_TYPE.DOLLAR);
            case "L3" -> new Loot(Loot.LOOT_TYPE.RUBY);
            case "L4" -> new Loot(Loot.LOOT_TYPE.DIAMOND);
            case "I1" -> new Door();
            case "I2" -> new Clock();
            case "I3" -> new Bomb();
            case "RL" -> new Lever(GateLeverColour.RED);
            case "GL" -> new Lever(GateLeverColour.GREEN);
            case "BL" -> new Lever(GateLeverColour.BLUE);
            case "RG" -> new Gate(GateLeverColour.RED);
            case "GG" -> new Gate(GateLeverColour.GREEN);
            case "NO" -> new Note();
            default -> new Gate(GateLeverColour.BLUE);
        };
    }

    /**
     * Gets width of board.
     *
     * @return the size x
     */
    public int getSizeX() {
        return sizeX;
    }

    /**
     * Gets height of board.
     *
     * @return the size y
     */
    public int getSizeY() {
        return sizeY;
    }

    /**
     * Get the tiles on board.
     *
     * @return the tile [][]
     */
    public Tile[][] getTiles() {
        return tiles;
    }

    /**
     * Gets expected time.
     *
     * @return the expected time
     */
    public int getExpectedTime() {
        return expectedTime;
    }

    /**
     * Gets colors used on the board
     *
     * @return board colors
     */
    public Paint[] getColors() {
        return colors;
    }

    /**
     * Initializes smart thief, looks for closest reachable loot
     *
     * @param board board being played on
     */
    public void initSmartThieves(Board board) {
        for (Entity e : board.getEntities()) {
            if (e instanceof SmartThief s) {
                s.initialize(board);
            }
        }
    }
}
