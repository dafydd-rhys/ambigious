package cs230.model.entity.items;

import cs230.external.Settings;
import cs230.model.Board;
import cs230.model.entity.Item;
import cs230.model.entity.enums.GateLeverColour;
import java.io.IOException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * The gate item - blocks player from moving, can be opened by levers.
 *
 * @author Dafydd-Rhys Maund
 */
public class Gate extends Item {

    /**
     * Color of gate
     */
    private GateLeverColour colour;

    /**
     * Instantiates a new Gate.
     *
     * @param col the color of gate
     */
    public Gate(GateLeverColour col) {
        setColour(col);

        switch (col) {
            case RED -> {
                setEntityCode("RG");
                setImage(resources.redGate);
            }
            case GREEN -> {
                setEntityCode("GG");
                setImage(resources.greenGate);
            }
            default -> {
                setEntityCode("BG");
                setImage(resources.blueGate);
            }
        }
    }

    /**
     * What happens after the appropriate lever has been collected - removes gate.
     *
     * @param board  the board
     */
    @Override
    public void interaction(Board board) {
        destroy(board);
    }

    /**
     * Sets colour.
     *
     * @param colour the colour
     */
    public void setColour(GateLeverColour colour) {
        this.colour = colour;
    }

    /**
     * Gets colour of gate.
     *
     * @return the colour
     */
    public GateLeverColour getColour() {
        return colour;
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
        Settings.playGameEffect(resources.gateSound);
    }

}
