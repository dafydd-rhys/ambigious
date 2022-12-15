package cs230.model.entity.enemies;

import cs230.external.Settings;
import cs230.model.Board;
import cs230.model.Tile;
import cs230.model.TileColor;
import cs230.model.entity.Enemy;
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
import java.util.stream.Collectors;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Class used to describe and store information about FloorFollowing Thief
 *
 * @author Dafydd-Rhys Maund (2003900)
 * @author Sam Morgan (2145027)
 */
public class FloorFollowingThief extends Enemy {

    /**
     * the specific color the thief needs to follow
     */
    private final TileColor specifiedColor;
    /**
     * the specific color the thief needs to follow
     */
    private Direction newDir;

    /**
     * Instantiates a new Floor following thief.
     *
     * @param dir the direction the thief is facing
     * @param col the color the thief should stay on
     */
    public FloorFollowingThief(Direction dir, TileColor col) {
        setEntityCode("E1");
        setDir(dir);
        setDirectionalImages("FloorFollowingThiefTemp.png");
        setImage(getUpdatedDirectionalImage(getDir()));
        specifiedColor = col;
    }

    /**
     * Implements how this enemy should move, hugging the left-side and staying on its color
     *
     * @param board  the board being played on
     */
    @Override
    public void move(Board board) {
        //gets directions in order of priority
        Direction[] directions = {Direction.getHugging(getDir()), getDir(), Direction.oppositeDirection(
                Direction.getHugging(getDir())), Direction.oppositeDirection(getDir())};

        for (Direction direction : directions) {
            int x = 0;
            int y = 0;
            switch (direction) {
                case UP -> y = -1;
                case RIGHT -> x = 1;
                case DOWN -> y = 1;
                default -> x = -1;
            }

            //checks if the enemy can move there
            if (valid(board, getCurrentPosX() + x, getCurrentPosY() + y)) {
                newDir = direction;
                hop(board, getCurrentPosX() + x, getCurrentPosY() + y);
                break;
            }
        }
    }

    /**
     * Identifies whether moving to the tile at pos x,y can be moved to
     *
     * @param board the current board being played on
     * @param x x of tile
     * @param y y of tile
     * @return whether the move is valid
     */
    private Boolean valid(Board board, int x, int y) {
        //if tile is on board and include a sub-tile with set colour
        if (x >= 0 && x < board.getWidth() && y >= 0 && y < board.getHeight()) {
            Tile tile = board.getTile(x, y);

            if (!tile.isPlayerOnTile() && !tile.isThiefOnTile()) {
                if (!(tile.getItem() instanceof Bomb) && !(tile.getItem() instanceof Gate)) {
                    for (TileColor colour : tile.getSubTiles()) {
                        if (colour == specifiedColor) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Moves this enemy to new tile.
     *
     * @param board the board being played on
     * @param x x of new tile
     * @param y y of new tile
     */
    @Override
    public void hop(Board board, int x, int y) {
        Tile original = board.getTile(getCurrentPosX(), getCurrentPosY());
        Tile newTile = board.getTile(x, y);

        newTile.addEntity(this);
        this.setCurrentPosX(x);
        this.setCurrentPosY(y);

        setDir(newDir);
        setImage(getUpdatedDirectionalImage(getDir()));

        original.removeEntity(this);
        check(board, newTile);
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
     * @throws LineUnavailableException line unavailable
     * @throws IOException can't read file
     */
    @Override
    public void playSound() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        Settings.playGameEffect(resources.steal);
    }

    /**
     * Prints string that encapsulates data needed to save this enemy on case of user exiting.
     *
     * @return the data about this enemy
     */
    @Override
    public String toString() {
        String result = "";
        result += getEntityCode();

        if (getCurrentPosX() >= 0 && getCurrentPosY() >= 0) {
            result += ":";
            result += String.format("%02d", getCurrentPosX());
            result += ",";
            result += String.format("%02d", getCurrentPosY());
            result += ";";
            result += getDir();
            result += "-";
            result += specifiedColor;
            result += " ";
        }

        return result;
    }

}
