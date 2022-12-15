package cs230.model.entity;

import cs230.model.Board;
import javafx.scene.image.Image;

/**
 * An abstract class representing Items (Weapons).
 * All sprites were created by us.
 *
 * @author Dafydd-Rhys Maund (2003900)
 */
public abstract class Item extends Entity {

    /**
     * The Image.
     */
    private Image image;

    /**
     * Interaction.
     *
     * @param board  the board
     */
    public abstract void interaction(Board board);

    /**
     * Set image.
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
     * Get the entity name.
     *
     * @return the entity name
     */
    @Override
    public String getEntityCode() {
        return this.entityCode;
    }

    /**
     * Set the entity name.
     *
     * @param entityName the entity name
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
     * Prints string that encapsulates data needed to save this item on case of user exiting.
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
            result += " ";
        }

        return result;
    }

}
