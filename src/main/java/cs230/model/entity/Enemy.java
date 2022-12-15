package cs230.model.entity;

import cs230.model.Board;
import cs230.model.Tile;
import cs230.model.entity.enums.Direction;
import java.io.IOException;
import javafx.scene.image.Image;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * The Entity enemy, holds all data and inherited methods for each enemy.
 *
 * @author Dafydd-Rhys Maund
 * @author Oliver
 */
public abstract class Enemy extends Entity {

    /**
     * The image of enemy.
     */
    private Image image;
    /**
     * The direction of enemy.
     */
    private Direction dir;

    /**
     * Sets direction of enemy.
     *
     * @param dir the direction
     */
    public void setDir(Direction dir) {
        this.dir = dir;
    }

    /**
     * Gets direction of enemy.
     *
     * @return the direction
     */
    public Direction getDir() {
        return dir;
    }

    /**
     * Method used to move enemy.
     *
     * @param board  the board
     */
    public abstract void move(Board board);

    /**
     * Checks tile to see if it can be moved to.
     *
     * @param board the board
     * @param tile  the tile
     */
    public abstract void check(Board board, Tile tile);

    /**
     * Identifies whether moving to the tile at pos x,y can be moved to, if so move there
     *
     * @param board the current board being played on
     * @param x x of tile
     * @param y y of tile
     */
    public abstract void hop(Board board, int x, int y);

    /**
     * Plays the sound unique to this enemy
     *
     * @throws UnsupportedAudioFileException Audio is unsupported
     * @throws LineUnavailableException line unavailable
     * @throws IOException can't read file
     */
    public abstract void playSound() throws UnsupportedAudioFileException, LineUnavailableException, IOException;

    /**
     * Sets image of enemy.
     *
     * @param paramImage the image
     */
    @Override
    public void setImage(final Image paramImage) {
        this.image = paramImage;
    }

    /**
     * Get current X position.
     *
     * @return the current X position
     */
    @Override
    public int getCurrentPosX() {
        return this.currentPosX;
    }

    /**
     * Set the current X position.
     *
     * @param currentPosX the current x position
     */
    @Override
    public void setCurrentPosX(final int currentPosX) {
        this.currentPosX = currentPosX;
    }

    /**
     * Get the current Y position.
     *
     * @return the current Y position
     */
    @Override
    public int getCurrentPosY() {
        return this.currentPosY;
    }

    /**
     * Set the current Y position.
     *
     * @param currentPosY the current Y position
     */
    @Override
    public void setCurrentPosY(final int currentPosY) {
        this.currentPosY = currentPosY;
    }

    /**
     * Get the entity code.
     *
     * @return the entity code
     */
    @Override
    public String getEntityCode() {
        return this.entityCode;
    }

    /**
     * Set the entity code.
     *
     * @param entityName the entity code
     */
    @Override
    protected void setEntityCode(final String entityName) {
        this.entityCode = entityName;
    }

    /**
     * Get the image.
     *
     * @return the image
     */
    @Override
    public Image getImage() {
        return image;
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
            result += " ";
        }

        return result;
    }

}
