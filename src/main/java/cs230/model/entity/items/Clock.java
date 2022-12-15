package cs230.model.entity.items;

import cs230.external.Settings;
import cs230.model.Board;
import cs230.model.entity.Item;
import java.io.IOException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * The item clock - increases players time by x seconds.
 *
 * @author Dafydd-Rhys Maund
 */
public class Clock extends Item {

    /**
     * Instantiates a new Clock.
     */
    public Clock() {
        setEntityCode("I2");
        setImage(resources.clock);
    }

    /**
     * What happens after the clock has been collected - increases player time.
     *
     * @param board  the board
     */
    @Override
    public void interaction(Board board) {
        try {
            playSound();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }

        board.setExpectedTime(board.getExpectedTime() + 10);
        destroy(board);
    }

    /**
     * Enemy interaction with clock - destroys clock, minuses time.
     *
     * @param board the board
     */
    public void enemyInteraction(Board board) {
        try {
            playSound();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }

        board.setExpectedTime(board.getExpectedTime() - 5);
        destroy(board);
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
        Settings.playGameEffect(resources.clockSound);
    }

}
