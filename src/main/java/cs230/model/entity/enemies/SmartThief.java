package cs230.model.entity.enemies;

import cs230.external.Settings;
import cs230.model.Board;
import cs230.model.Tile;
import cs230.model.TileColor;
import cs230.model.entity.Enemy;
import cs230.model.entity.Entity;
import cs230.model.entity.Item;
import cs230.model.entity.enums.Direction;
import cs230.model.entity.items.Bomb;
import cs230.model.entity.items.Clock;
import cs230.model.entity.items.Door;
import cs230.model.entity.items.Gate;
import cs230.model.entity.items.Lever;
import cs230.model.entity.items.Loot;
import cs230.model.entity.items.Note;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.stream.Collectors;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * A thief that works against the player by heading to the nearest loot/lever and collecting it,
 * before leaving through the exit.
 *
 * @author Dafydd-Rhys Maund
 */
public class SmartThief extends Enemy {

    /**
     * Starting tile
     */
    private Tile startingTile;
    /**
     * Goal tile to reach
     */
    private Tile goal;
    /**
     * HashMap used to store each reachable loot/levers tile and distance to it
     */
    private final HashMap<Integer, Tile> tileAndDistanceMap = new HashMap<>();
    /**
     * Array of moves to reach goal tile
     */
    private ArrayList<Tile> moves = new ArrayList<>();

    /**
     * Initializes a new instance of the SmartThief class.
     *
     * @param dir The direction the SmartThief is pointing
     */
    public SmartThief(Direction dir) {
        setEntityCode("E3");
        setDir(dir);
        setDirectionalImages("SmartThiefTemp.png");
        setImage(getUpdatedDirectionalImage(getDir()));
    }

    /**
     * sets the starting tile of this instance of thief
     *
     * @param tile starting tile
     */
    public void setStarting(Tile tile) {
        this.startingTile = tile;
    }

    /**
     * Begins process of searching for nearest loots and there paths
     *
     * @param board board being played on
     */
    public void initialize(Board board) {
        getNearestLootOrLever(board, startingTile);
    }

    /**
     * Identifies whether there is loot still able to be reached if so, make the best move to reach it
     * if not, check whether the doors are reachable, if so move to them. If neither are doable
     * move randomly.
     *
     * @param board The game board
     */
    @Override
    public void move(Board board) {
        ArrayList<Item> loot = (ArrayList<Item>) board.getItems().stream().filter(
                e -> e instanceof Loot || e instanceof Lever).collect(Collectors.toList());

        if (!loot.isEmpty()) {
            getNearestLootOrLever(board, board.getTile(getCurrentPosX(), getCurrentPosY()));
        }

        if (goal == null) {
            getNearestDoor(board, board.getTile(getCurrentPosX(), getCurrentPosY()));
        }

        if (goal != null) {
            if (goal.getItem() != null) {
                if (moves.size() > 0) {
                    Tile tile = moves.get(moves.size() - 1);

                    if (tile.isThiefOnTile() || tile.isPlayerOnTile()) {
                        oppositeMovement(board);
                        tileAndDistanceMap.clear();
                        getNearestLootOrLever(board, board.getTile(getCurrentPosX(), getCurrentPosY()));
                    } else {
                        hop(board, tile.getX(), tile.getY());
                        moves.remove(tile);
                    }
                }
            } else {
                tileAndDistanceMap.clear();
                getNearestLootOrLever(board, board.getTile(getCurrentPosX(), getCurrentPosY()));

                if (goal != null) {
                    if (goal.getItem() != null) {
                        if (moves.size() > 0) {
                            Tile tile = moves.get(moves.size() - 1);

                            if (tile.isThiefOnTile() || tile.isPlayerOnTile()) {
                                oppositeMovement(board);
                                tileAndDistanceMap.clear();
                                getNearestLootOrLever(board, board.getTile(getCurrentPosX(), getCurrentPosY()));
                            } else {
                                hop(board, tile.getX(), tile.getY());
                                moves.remove(tile);
                            }
                        }
                    } else {
                        randomMovement(board);
                    }
                } else {
                    randomMovement(board);
                }
            }
        } else {
            getNearestDoor(board, board.getTile(getCurrentPosX(), getCurrentPosY()));

            if (goal != null) {
                if (goal.getItem() != null) {
                    if (moves.size() > 0) {
                        System.out.println(moves);
                        Tile tile = moves.get(moves.size() - 1);

                        if (tile.isThiefOnTile() || tile.isPlayerOnTile()) {
                            oppositeMovement(board);
                            tileAndDistanceMap.clear();
                            getNearestLootOrLever(board, board.getTile(getCurrentPosX(), getCurrentPosY()));
                        } else {
                            hop(board, tile.getX(), tile.getY());
                            moves.remove(tile);
                        }
                    }
                } else {
                    randomMovement(board);
                }
            } else {
                randomMovement(board);
            }
        }
    }

    /**
     * Assuming the thief is being blocked by player, thief, bomb or gate it
     * moves back to where it came from
     *
     * @param board game board
     */
    private void oppositeMovement(Board board) {
        Direction direction = Direction.oppositeDirection(getDir());

        switch (direction) {
            case UP -> tryMove(board, getCurrentPosX(), getCurrentPosY() - 1);
            case RIGHT -> tryMove(board, getCurrentPosX() + 1, getCurrentPosY());
            case DOWN -> tryMove(board, getCurrentPosX(), getCurrentPosY() + 1);
            default -> tryMove(board, getCurrentPosX() - 1, getCurrentPosY());
        }

        setImage(getUpdatedDirectionalImage(getDir()));
    }

    /**
     * Moves the thief in a random but valid direction
     *
     * @param board game board
     */
    private void randomMovement(Board board) {
        boolean moved = false;

        for (Direction dirs : Direction.scrambledDirectionList()) {
            if (moved) {
                break;
            }

            switch (dirs) {
                case UP -> moved = tryMove(board, getCurrentPosX(), getCurrentPosY() - 1);
                case RIGHT -> moved = tryMove(board, getCurrentPosX() + 1, getCurrentPosY());
                case DOWN -> moved = tryMove(board, getCurrentPosX(), getCurrentPosY() + 1);
                default -> moved = tryMove(board, getCurrentPosX() - 1, getCurrentPosY());
            }
        }

        setImage(getUpdatedDirectionalImage(getDir()));
    }

    /**
     * Attempts to move on passed tile, make sure its valid
     *
     * @param board game board
     * @param x x position of new tile
     * @param y y position of new tile
     * @return whether the move can be achieved
     */
    private boolean tryMove(Board board, int x, int y) {
        Tile original = board.getTile(getCurrentPosX(), getCurrentPosY());

        boolean blocking = false;
        while (x >= 0 && x < board.getWidth() && y >= 0 && y < board.getHeight() && !blocking) {
            boolean hop = true;

            Tile tile = board.getTile(x, y);
            if (tile.getItem() != null) {
                if (tile.getItem() instanceof Gate || tile.getItem() instanceof Bomb) {
                    hop = false;
                    blocking = true;
                }
            } else if (tile.isThiefOnTile() || tile.isPlayerOnTile()) {
                hop = false;
            }

            boolean onColor = false;
            for (TileColor col : original.getSubTiles()) {
                for (TileColor color : tile.getSubTiles()) {
                    if (col == color) {
                        onColor = true;
                        break;
                    }
                }
            }

            if (hop && onColor) {
                hop(board, x, y);
                return true;
            }

            switch (getDir()) {
                case UP -> y--;
                case RIGHT -> x++;
                case DOWN -> y++;
                default -> x--;
            }
        }
        return false;
    }


    /**
     * Moves this enemy to new tile.
     *
     * @param board the board being played on
     * @param x     x of new tile
     * @param y     y of new tile
     */
    @Override
    public void hop(Board board, int x, int y) {
        Tile original = board.getTile(getCurrentPosX(), getCurrentPosY());
        Tile tile = board.getTile(x, y);

        if (tile == goal) {
            tileAndDistanceMap.clear();
            getNearestLootOrLever(board, tile);
        }

        if (tile.getX() - original.getX() > 0) {
            setDir(Direction.RIGHT);
        } else if (tile.getY() - original.getY() > 0) {
            setDir(Direction.DOWN);
        } else if (tile.getX() - original.getX() < 0) {
            setDir(Direction.LEFT);
        } else {
            setDir(Direction.UP);
        }

        tile.addEntity(this);
        this.setCurrentPosX(x);
        this.setCurrentPosY(y);
        original.removeEntity(this);
        setImage(getUpdatedDirectionalImage(getDir()));

        check(board, tile);
    }

    /**
     * Gets the nearest reachable loot/lever and sets it as the goal,
     * if none is reachable sets goal to null
     *
     * @param board game board
     * @param tile new tile
     */
    private void getNearestLootOrLever(Board board, Tile tile) {
        for (Entity entity : board.getEntities()) {
            if (!(entity instanceof Item))
                continue;

            if (entity instanceof Loot || entity instanceof Lever) {
                Tile goalTile = board.getTile(entity.getCurrentPosX(), entity.getCurrentPosY());
                PathFinder path = new PathFinder(board, tile, goalTile);

                if (path.getDistance() > 0) {
                    tileAndDistanceMap.put(path.getDistance(), goalTile);
                }

                Tile[][] tiles = board.getTiles();
                for (Tile[] tilesX : tiles) {
                    for (Tile value : tilesX) {
                        value.setDefault();
                    }
                }
            }
        }

        TreeMap<Integer, Tile> treeMap = new TreeMap<>(tileAndDistanceMap);
        if (!treeMap.isEmpty()) {
            goal = treeMap.firstEntry().getValue();
            moves = new PathFinder(board, tile, goal).moves;
        } else {
            goal = null;
        }
    }

    /**
     * Gets the nearest reachable door and sets it as the goal,
     * if none is reachable sets goal to null
     *
     * @param board game board
     * @param tile new tile
     */
    private void getNearestDoor(Board board, Tile tile) {
        for (Entity entity : board.getEntities()) {
            if (!(entity instanceof Item))
                continue;

            if (entity instanceof Door) {
                Tile goalTile = board.getTile(entity.getCurrentPosX(), entity.getCurrentPosY());
                PathFinder path = new PathFinder(board, tile, goalTile);

                if (path.getDistance() > 0) {
                    tileAndDistanceMap.put(path.getDistance(), goalTile);
                }

                Tile[][] tiles = board.getTiles();
                for (Tile[] tilesX : tiles) {
                    for (Tile value : tilesX) {
                        value.setDefault();
                    }
                }
            }
        }

        TreeMap<Integer, Tile> treeMap = new TreeMap<>(tileAndDistanceMap);
        if (!treeMap.isEmpty()) {
            goal = treeMap.firstEntry().getValue();
            moves = new PathFinder(board, tile, goal).moves;
        } else {
            goal = null;
        }
    }

    /**
     * Checks for entities on the new tile and performs the appropriate action according to the case.
     *
     * @param board the board
     * @param tile  the tile
     */
    @Override
    public void check(Board board, Tile tile) {
        boolean play = true;
        Item item = tile.getItem();

        if (item instanceof Loot || item instanceof Note || item instanceof Lever) {
            item.destroy(board);
        } else if (item instanceof Clock c) {
            c.enemyInteraction(board);
        } else if (item instanceof Door) {
            ArrayList<Item> lootAndLevers = (ArrayList<Item>) board.getItems().stream().filter(
                    e -> e instanceof Loot || e instanceof Lever).collect(Collectors.toList());

            if (lootAndLevers.isEmpty()) {
                board.gameLost();
            } else {
                play = false;
            }
        } else {
            play = false;
        }

        if (play) {
            try {
                playSound();
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Plays the sound unique to this enemy
     *
     * @throws UnsupportedAudioFileException Audio is unsupported
     * @throws LineUnavailableException      line unavailable
     * @throws IOException                   can't read file
     */
    @Override
    public void playSound() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        Settings.playGameEffect(resources.steal);
    }

}
