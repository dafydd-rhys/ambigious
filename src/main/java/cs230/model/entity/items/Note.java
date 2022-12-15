package cs230.model.entity.items;

import cs230.external.Settings;
import cs230.model.Board;
import cs230.model.entity.Item;
import cs230.profiles.Profile;
import java.io.IOException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * The item note - collect these to find out information about player.
 *
 * @author Dafydd-Rhys Maund
 */
public class Note extends Item {

    /**
     * Instantiates a new Note.
     */
    public Note() {
        setEntityCode("NO");
        setImage(resources.note);
    }

    /**
     * What happens after the note has been collected - retrieves a fact about player.
     *
     * @param board  the board
     */
    @Override
    public void interaction(Board board) {
        Profile.notesCollected[board.getCurrentLevel() - 1] = true;
        try {
            playSound();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }

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
        Settings.playGameEffect(resources.noteSound);
    }

}
