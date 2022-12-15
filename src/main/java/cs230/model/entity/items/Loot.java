package cs230.model.entity.items;

import cs230.external.Settings;
import cs230.model.Board;
import cs230.model.entity.Item;
import java.io.IOException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * The Loot item (4 types) - gives player score on completion.
 *
 * @author Dafydd-Rhys Maund
 */
public class Loot extends Item {

    /**
     * value the loot is worth
     */
    private int value;

    /**
     * The enum Loot type.
     */
    public enum LOOT_TYPE {
        /**
         * Cent loot type.
         */
        CENT(),
        /**
         * Dollar loot type.
         */
        DOLLAR(),
        /**
         * Ruby loot type.
         */
        RUBY(),
        /**
         * Diamond loot type.
         */
        DIAMOND()
    }

    /**
     * Instantiates a new Loot.
     *
     * @param type the type
     */
    public Loot(LOOT_TYPE type) {
        setValue(getLootValue(type));

        switch (type) {
            case CENT -> setEntityCode("L1");
            case DOLLAR -> setEntityCode("L2");
            case RUBY -> setEntityCode("L3");
            default -> setEntityCode("L4");
        }

        if (type == LOOT_TYPE.CENT) {
            setImage(resources.tier1Loot);
        } else if (type == LOOT_TYPE.DOLLAR) {
            setImage(resources.tier2Loot);
        } else if (type == LOOT_TYPE.RUBY) {
            setImage(resources.tier3Loot);
        } else {
            setImage(resources.tier4Loot);
        }
    }

    /**
     * What happens after this has been collected - gives score to player.
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

        board.getPlayer().setScore(board.getPlayer().getScore() + value);
        destroy(board);
    }

    /**
     * Sets value of loot.
     *
     * @param value the value
     */
    public void setValue(int value) {
        this.value = value;
    }

    /**
     * Gets value of loot.
     *
     * @return the value
     */
    public int getValue() {
        return value;
    }

    /**
     * Converts type into appropriate value.
     *
     * @param type type of loot
     * @return the value of loot
     */
    private int getLootValue(LOOT_TYPE type) {
        int val;
        switch (type) {
            case CENT -> val = 1;
            case DOLLAR -> val = 3;
            case RUBY -> val = 6;
            default -> val = 10;
        }

        return val;
    }

    /**
     * Plays the sound unique to this item.
     *
     * @throws UnsupportedAudioFileException Audio is unsupported
     * @throws LineUnavailableException line unavailable
     * @throws IOException can't read file
     */
    @Override
    public void playSound() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        Settings.playGameEffect(resources.lootSound);
    }

}
