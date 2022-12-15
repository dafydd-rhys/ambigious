package cs230.model.entity;

import cs230.external.Settings;
import cs230.model.Board;
import cs230.model.Tile;
import cs230.model.entity.enemies.FlyingAssassin;
import cs230.model.entity.items.Bomb;
import cs230.model.entity.enums.Direction;
import cs230.model.entity.items.Door;
import cs230.model.entity.items.Gate;
import cs230.model.TileColor;
import cs230.model.entity.items.Lever;
import cs230.model.entity.items.Loot;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;
import javafx.scene.image.Image;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * The Player class handles the player data and describes how the can act in the game.
 *
 * @author Dafydd-Rhys Maund
 */
public class Player extends Entity {

    /**
     * score of player
     */
    private int score = 0;
    /**
     * direction player is facing
     */
    private Direction direction;

    /**
     * Instantiates a new Player.
     *
     * @param x the x
     * @param y the y
     */
    public Player(int x, int y) {
        setEntityCode("PL");
        setCurrentPosX(x);
        setCurrentPosY(y);
        setDirection(Direction.getRandomDirection());

        setDirectionalImages("player.png");
        setImage(getUpdatedDirectionalImage(getDirection()));
    }

    /**
     * Instantiates a new Player from save file.
     *
     * @param dir direction player was facing
     * @param x the x
     * @param y the y
     */
    public Player(Direction dir, int x, int y) {
        setEntityCode("PL");
        setCurrentPosX(x);
        setCurrentPosY(y);
        setDirection(dir);

        setDirectionalImages("player.png");
        setImage(getUpdatedDirectionalImage(getDirection()));
    }

    /**
     * Moves the player.
     *
     * @param board the board
     * @param dir   the dir
     */
    public void move(Board board, Direction dir) {
        setDirection(dir);

        if (dir == Direction.UP) {
            tryMove(board, getCurrentPosX(), getCurrentPosY() - 1);
        } else if (dir == Direction.RIGHT) {
            tryMove(board, getCurrentPosX() + 1, getCurrentPosY());
        } else if (dir == Direction.DOWN) {
            tryMove(board, getCurrentPosX(), getCurrentPosY() + 1);
        } else {
            tryMove(board, getCurrentPosX() - 1, getCurrentPosY());
        }

        setImage(getUpdatedDirectionalImage(getDirection()));
    }

    /**
     * Checks to see if the player can move the tile x, y if so moves there
     *
     * @param board the board
     * @param x x of tile
     * @param y y of tile
     */
    private void tryMove(Board board, int x, int y) {
        Tile original = board.getTile(getCurrentPosX(), getCurrentPosY());

        while (x >= 0 && x < board.getWidth() && y >= 0 && y < board.getHeight()) {
            boolean hop = true;

            Tile tile = board.getTile(x, y);
            if (tile.getItem() != null) {
                if (tile.getItem() instanceof Gate || tile.getItem() instanceof Bomb) {
                    hop = false;
                }
            } else if (tile.isThiefOnTile()) {
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
                hop(tile, original, x, y, board);
                break;
            }

            switch (getDirection()) {
                case UP -> y--;
                case RIGHT -> x++;
                case DOWN -> y++;
                default -> x--;
            }
        }
    }

    /**
     * moves the player to the desired tile - removing from old and checking what it interacts with
     *
     * @param tile the desired tile
     * @param original original tile
     * @param x x of new tile
     * @param y y of new tile
     * @param board the board
     */
    private void hop(Tile tile, Tile original, int x, int y, Board board) {
        tile.addEntity(this);
        this.setCurrentPosX(x);
        this.setCurrentPosY(y);
        original.removeEntity(this);

        ArrayList<Item> loot = (ArrayList<Item>) board.getItems().stream().filter(
                e -> e instanceof Loot || e instanceof Lever).collect(Collectors.toList());

        if (tile.getItem() instanceof Door) {
            if (loot.isEmpty()) {
                board.gameWon();
            }
        }

        for (Entity entity : tile.getEntities()) {
            if (entity instanceof FlyingAssassin) {
                board.gameLost();
            }
        }

        if (tile.getItem() != null && !(tile.getItem() instanceof Bomb)) {
            tile.getItem().interaction(board);
        }
    }

    /**
     * Set image of player.
     *
     * @param paramImage the image
     */
    @Override
    public void setImage(final Image paramImage) {
        this.image = paramImage;
    }

    /**
     * Get current X position of player.
     *
     * @return the current X position
     */
    @Override
    public int getCurrentPosX() {
        return this.currentPosX;
    }

    /**
     * Set the current X position of player.
     *
     * @param currentPosX the current x position
     */
    @Override
    public void setCurrentPosX(final int currentPosX) {
        this.currentPosX = currentPosX;
    }

    /**
     * Get the current Y position of player.
     *
     * @return the current Y position
     */
    @Override
    public int getCurrentPosY() {
        return this.currentPosY;
    }

    /**
     * Set the current Y position of player.
     *
     * @param currentPosY the current Y position
     */
    @Override
    public void setCurrentPosY(final int currentPosY) {
        this.currentPosY = currentPosY;
    }

    /**
     * Get the entity code of player.
     *
     * @return the entity name
     */
    @Override
    public String getEntityCode() {
        return this.entityCode;
    }

    /**
     * Set the entity code of player.
     *
     * @param entityName the entity name
     */
    @Override
    protected void setEntityCode(final String entityName) {
        this.entityCode = entityName;
    }

    /**
     * Gets the image of player.
     *
     * @return the image
     */
    @Override
    public Image getImage() {
        return image;
    }

    /**
     * Sets direction.
     *
     * @param direction the direction
     */
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    /**
     * Gets direction.
     *
     * @return the direction
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Sets score.
     *
     * @param value the value
     */
    public void setScore(int value) {
        this.score = value;
    }

    /**
     * Gets score.
     *
     * @return the score
     */
    public int getScore() {
        return score;
    }

    /**
     * Plays the sound unique to player
     *
     * @throws UnsupportedAudioFileException Audio is unsupported
     * @throws LineUnavailableException line unavailable
     * @throws IOException can't read file
     */
    @Override
    public void playSound() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        Settings.playGameEffect(resources.death);
    }

    /**
     * Prints string that encapsulates data needed to save the player on case of user exiting.
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
            result += getDirection();
            result += " ";
        }

        return result;
    }

}
