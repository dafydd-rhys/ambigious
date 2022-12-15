package cs230.model.entity.items;

import cs230.external.Settings;
import cs230.model.Board;
import cs230.model.entity.Item;
import cs230.model.entity.enums.GateLeverColour;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * The lever item - opens gates.
 *
 * @author Dafydd-Rhys Maund
 */
public class Lever extends Item {

    /**
     * gates linked to this lever.
     */
    private final List<Gate> gates;
    /**
     * color of this lever.
     */
    private GateLeverColour colour;

    /**
     * Instantiates a new Lever.
     *
     * @param col the color of the lever
     */
    public Lever(GateLeverColour col) {
        gates = new ArrayList<>();

        switch (col) {
            case RED -> setEntityCode("RL");
            case GREEN -> setEntityCode("GL");
            default -> setEntityCode("BL");
        }

        setColour(col);
        setOpenLeverImage();
    }

    /**
     * What happens after the players collects lever - removes gate(s).
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

        for (Gate gate : gates) {
            gate.interaction(board);
        }
        destroy(board);
    }

    /**
     * Adds a gate to the list of gates this lever controls.
     *
     * @param gate The gate to add
     */
    public void addGate(Gate gate) {
        gates.add(gate);
    }

    /**
     * Sets open lever image.
     */
    public void setOpenLeverImage() {
        if (colour == GateLeverColour.RED) {
            setImage(resources.redLever);
        } else if (colour == GateLeverColour.GREEN) {
            setImage(resources.greenLever);
        } else {
            setImage(resources.blueLever);
        }
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
     * Gets colour.
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
        Settings.playGameEffect(resources.leverSound);
    }

}
