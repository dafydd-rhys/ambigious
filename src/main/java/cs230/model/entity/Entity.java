package cs230.model.entity;

import cs230.model.Board;
import cs230.model.entity.enums.Direction;
import java.io.IOException;
import cs230.model.entity.resources.EntityResources;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Entity class is used to describe all game elements as it is inherited by all.
 *
 * @author Dafydd-Rhys Maund
 * @author Oliver
 */
public abstract class Entity {

    /**
     * The Image resource directory.
     */
    final protected String imageResource = System.getProperty("user.dir") + "\\src\\main\\resources\\cs230\\images\\";
    /**
     * The Resources for each entity.
     */
    final protected EntityResources resources = new EntityResources();
    /**
     * The Entity code.
     */
    protected String entityCode;
    /**
     * The Image of entity.
     */
    protected Image image;
    /**
     * The UP Image of entity.
     */
    protected Image upImage;
    /**
     * The RIGHT Image of entity.
     */
    protected Image rightImage;
    /**
     * The DOWN Image of entity.
     */
    protected Image downImage;
    /**
     * The LEFT Image of entity.
     */
    protected Image leftImage;
    /**
     * The Current pos x of entity.
     */
    protected int currentPosX;
    /**
     * The Current pos y of entity.
     */
    protected int currentPosY;

    /**
     * Gets entity code.
     *
     * @return the entity code
     */
    protected abstract String getEntityCode();

    /**
     * Sets entity code.
     *
     * @param code the entity code
     */
    protected abstract void setEntityCode(String code);

    /**
     * Gets image of entity.
     *
     * @return the image
     */
    public abstract Image getImage();

    /**
     * Sets image of entity.
     *
     * @param newImage the image
     */
    protected abstract void setImage(Image newImage);

    /**
     * Gets current pos x of entity.
     *
     * @return the current pos x
     */
    public abstract int getCurrentPosX();

    /**
     * Sets current pos x of entity.
     *
     * @param newPosX the changed pos x
     */
    public abstract void setCurrentPosX(int newPosX);

    /**
     * Gets current pos y of entity.
     *
     * @return the current pos y
     */
    public abstract int getCurrentPosY();

    /**
     * Sets current pos y of entity.
     *
     * @param newPosY the changed pos y
     */
    public abstract void setCurrentPosY(int newPosY);

    /**
     * Abstract method to be implemented into subclass that plays audio file when called.
     *
     * @throws UnsupportedAudioFileException the unsupported audio file exception
     * @throws LineUnavailableException      the line unavailable exception
     * @throws IOException                   the io exception
     */
    public abstract void playSound() throws UnsupportedAudioFileException, LineUnavailableException, IOException;

    /**
     * Prints string that encapsulates data needed to save this entity on case of user exiting.
     *
     * @return the data about this enemy
     */
    @Override
    public abstract String toString();

    /**
     * Remove entity of board.
     *
     * @param board the board
     */
    public void destroy(Board board) {
        if (this instanceof Item item) {
            board.getTile(getCurrentPosX(), getCurrentPosY()).setTileItem(null);
            board.getEntities().remove(this);
            board.getItems().remove(item);
        } else {
            board.getTile(getCurrentPosX(), getCurrentPosY()).removeEntity(this);
            board.getEntities().remove(this);
        }
    }

    /**
     * Sets directional images of image.
     *
     * @param filename the filename
     */
    public void setDirectionalImages(String filename) {
        upImage = new Image(imageResource + filename);
        rightImage = rotate(upImage, 90);
        downImage = rotate(upImage, 180);
        leftImage = rotate(upImage, 270);
    }

    /**
     * Rotate image.
     *
     * @param img   the img
     * @param angle the angle
     * @return the image
     */
    public Image rotate(final Image img, final int angle) {
        ImageView iv = new ImageView(img);
        iv.setRotate(angle);
        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);

        return iv.snapshot(params, null);
    }

    /**
     * Gets updated directional image.
     *
     * @param direction the direction
     * @return the updated directional image
     */
    public Image getUpdatedDirectionalImage(Direction direction) {
        Image image;

        if (direction == Direction.UP) {
            image = upImage;
        } else if (direction == Direction.RIGHT) {
            image = rightImage;
        } else if (direction == Direction.DOWN) {
            image = downImage;
        } else {
            image = leftImage;
        }
        return image;
    }

    /**
     * gets entity resources
     *
     * @return resources
     */
    public EntityResources getResources() {
        return resources;
    }

}
