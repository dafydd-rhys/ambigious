package cs230.model.entity.items;

import cs230.external.Settings;
import cs230.model.Board;
import cs230.model.entity.Entity;
import cs230.model.entity.Item;
import java.io.IOException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * The item door - finished the game if collected.
 *
 * @author Dafydd-Rhys Maund
 */
public class Door extends Item {

    /**
     * Instantiates a new Door.
     */
    public Door() {
        setEntityCode("I1");
        setImage(resources.door);
    }

    /**
     * What happens after the door has been interacted with - ends game if criteria met.
     *
     * @param board  the board
     */
    @Override
    public void interaction(Board board) {
        boolean complete = true;

        for (Entity entity : board.getEntities()) {
            if (entity instanceof Item item) {
                if (item instanceof Loot || item instanceof Gate || item instanceof Lever || item instanceof Clock) {
                    complete = false;
                    break;
                }
            }
        }

        if (complete) {
            try {
                playSound();
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                e.printStackTrace();
            }

            board.gameWon();
        }
    }

    /**
     * Plays the sound unique to this item
     *
     * @throws UnsupportedAudioFileException Audio is unsupported
     * @throws LineUnavailableException line unavailable
     * @throws IOException can't read file
     */
    @Override
    public void playSound() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        Settings.playGameEffect(resources.doorSound);
    }

}
