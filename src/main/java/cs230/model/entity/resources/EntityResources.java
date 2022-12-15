package cs230.model.entity.resources;

import cs230.Application;
import java.io.File;
import java.io.InputStream;
import java.util.Objects;
import javafx.scene.image.Image;

/**
 * The Entity resources class - holds all data for entities.
 *
 * @author Dafydd-Rhys Maund
 */
public class EntityResources {

    /**
     * The Audio directory.
     */
    String audDir = System.getProperty("user.dir") + "\\src\\main\\resources\\cs230\\audio\\";
    /**
     * The Notes image.
     */
    public Image note = i(Objects.requireNonNull(Application.class.getResourceAsStream("images/note.png")));
    /**
     * The Note sound.
     */
    public File noteSound = new File(audDir + "note.wav");
    /**
     * The Bomb image.
     */
    public Image bomb = i(Objects.requireNonNull(Application.class.getResourceAsStream("images/bomb.png")));
    /**
     * The Bomb explosion image.
     */
    public Image bomb_exp = i(Objects.requireNonNull(Application.class.getResourceAsStream("images/bomb_0.png")));
    /**
     * The Bomb 1-tick image.
     */
    public Image bomb_1 = i(Objects.requireNonNull(Application.class.getResourceAsStream("images/bomb_1.png")));
    /**
     * The Bomb 2-tick image.
     */
    public Image bomb_2 = i(Objects.requireNonNull(Application.class.getResourceAsStream("images/bomb_2.png")));
    /**
     * The Bomb 3-tick image.
     */
    public Image bomb_3 = i(Objects.requireNonNull(Application.class.getResourceAsStream("images/bomb_3.png")));
    /**
     * The Bomb explosion sound.
     */
    public File bombSound = new File(audDir + "bomb.wav");
    /**
     * The Clock image.
     */
    public Image clock = i(Objects.requireNonNull(Application.class.getResourceAsStream("images/clock.png")));
    /**
     * The Clock sound.
     */
    public File clockSound = new File(audDir + "clock.wav");
    /**
     * The Door image.
     */
    public Image door = i(Objects.requireNonNull(Application.class.getResourceAsStream("images/door.png")));
    /**
     * The Door sound.
     */
    public File doorSound = new File(audDir + "level-win.wav");
    /**
     * The Tier 1 loot image.
     */
    public Image tier1Loot = i(Objects.requireNonNull(Application.class.getResourceAsStream("images/cent.png")));
    /**
     * The Tier 2 loot image.
     */
    public Image tier2Loot =
            i(Objects.requireNonNull(Application.class.getResourceAsStream("images/dollar.png")));
    /**
     * The Tier 3 loot image.
     */
    public Image tier3Loot = i(Objects.requireNonNull(Application.class.getResourceAsStream("images/ruby.png")));
    /**
     * The Tier 4 loot image.
     */
    public Image tier4Loot =
            i(Objects.requireNonNull(Application.class.getResourceAsStream("images/diamond.png")));
    /**
     * The Loot collected sound.
     */
    public File lootSound = new File(audDir + "loot.wav");
    /**
     * The Red lever image.
     */
    public Image redLever =
            i(Objects.requireNonNull(Application.class.getResourceAsStream("images/red_lever.png")));
    /**
     * The Red gate image.
     */
    public Image redGate =
            i(Objects.requireNonNull(Application.class.getResourceAsStream("images/red_gate.png")));
    /**
     * The Blue lever image.
     */
    public Image blueLever =
            i(Objects.requireNonNull(Application.class.getResourceAsStream("images/blue_lever.png")));
    /**
     * The Blue gate image.
     */
    public Image blueGate =
            i(Objects.requireNonNull(Application.class.getResourceAsStream("images/blue_gate.png")));
    /**
     * The Green lever image.
     */
    public Image greenLever =
            i(Objects.requireNonNull(Application.class.getResourceAsStream("images/green_lever.png")));
    /**
     * The Green gate image.
     */
    public Image greenGate =
            i(Objects.requireNonNull(Application.class.getResourceAsStream("images/green_gate.png")));
    /**
     * The Lever sound.
     */
    public File leverSound = new File(audDir + "lever.wav");
    /**
     * The Gate sound.
     */
    public File gateSound = new File(audDir + "gate.wav");
    /**
     * The Kill sound.
     */
    public File kill = new File(audDir + "kill.wav");
    /**
     * The Steal sound.
     */
    public File steal = new File(audDir + "steal.wav");
    /**
     * The Death sound.
     */
    public File death = new File(audDir + "death.wav");

    /**
     * dynamically sets image size to that wanted for board
     *
     * @param path path of image
     * @return image scaled to board
     */
    private Image i(InputStream path) {
        return new Image(path);
    }

}
