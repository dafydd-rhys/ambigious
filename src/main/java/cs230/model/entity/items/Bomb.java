package cs230.model.entity.items;

import cs230.external.Settings;
import cs230.model.Board;
import cs230.model.Tile;
import cs230.model.entity.Enemy;
import cs230.model.entity.Entity;
import cs230.model.entity.Item;
import cs230.model.entity.Player;
import cs230.model.entity.enemies.FloorFollowingThief;
import cs230.model.entity.enemies.SmartThief;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * The Bomb item class, used to describe how it interacts with board and entities.
 *
 * @author Dafydd-Rhys Maund
 */
public class Bomb extends Item {
    /**
     * Specified tick of the bomb
     */
    private static final int STARTING_TICK = 4;
    /**
     * Whether the bomb is active or not.
     */
    private boolean ticking = false;
    /**
     * Current tick of bomb.
     */
    private int tickCount = STARTING_TICK;
    /**
     * Whether bomb has exploded or not.
     */
    public boolean hasExploded = false;

    /**
     * Instantiates a new Bomb.
     */
    public Bomb() {
        setEntityCode("I3");
        setImage(resources.bomb);
    }

    /**
     * Instantiates a new Bomb from save file.
     *
     * @param tick the current tick of bomb
     */
    public Bomb(int tick) {
        setEntityCode("I3");
        setImage(resources.bomb);

        if (tick < STARTING_TICK) {
            tickCount = tick;
            ticking = true;
            getTickImage();
        }
    }

    /**
     * What happens after the bomb has been triggered - begins ticking.
     *
     * @param board the board
     */
    @Override
    public void interaction(Board board) {
        if (ticking) {
            return;
        }

        ticking = true;
        tickCount--;
        getTickImage();
    }

    /**
     * Runs every tick - updates image and explodes on final tick.
     */
    public void tick() {
        tickCount--;
        getTickImage();

        if (tickCount == 0) {
            explode();
        }
    }

    /**
     * Returns whether the bomb is ticking.
     *
     * @return whether the bomb is ticking
     */
    public boolean isTicking() {
        return ticking;
    }

    /**
     * Explodes the bomb.
     */
    public void explode() {
        hasExploded = true;

        try {
            playSound();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    /**
     * Remove entities from board that were in LOS of bomb.
     *
     * @param entity the entity getting destroyed
     * @param board the board
     */
    public void remove(List<Entity> entity, Board board) {
        if (entity.size() > 0) {
            for (int i = 0; i < entity.size(); i++) {
                Entity e = entity.get(i);

                if (e instanceof Loot || e instanceof Clock || e instanceof Lever || e instanceof Note) {
                    e.destroy(board);
                } else if (e instanceof Player pl) {
                    try {
                        playSound();
                        pl.playSound();
                    } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
                        ex.printStackTrace();
                    }

                    board.gameLost();
                } else if (e instanceof Enemy) {
                    e.destroy(board);

                    try {
                        playSound();
                    } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * Checks for players and entities on neighbouring tiles
     *
     * @param board board being played on
     */
    public void checkForPlayersAndEnemies(Board board) {
        List<Tile> tiles = new ArrayList<>();
        int x = this.getCurrentPosX();
        int y = this.getCurrentPosY();

        if (x - 1 >= 0 && x - 1 < board.getWidth()) {
            tiles.add(board.getTile(x - 1, y));
        }
        if (x + 1 >= 0 && x + 1 < board.getWidth()) {
            tiles.add(board.getTile(x + 1, y));
        }
        if (y - 1 >= 0 && y - 1 < board.getHeight()) {
            tiles.add(board.getTile(x, y - 1));
        }
        if (y + 1 >= 0 && y + 1 < board.getHeight()) {
            tiles.add(board.getTile(x, y + 1));
        }

        for (Tile tile : tiles) {
            if (tile.getEntities() != null) {
                for (Entity entity : tile.getEntities()) {
                    if (entity instanceof Player || entity instanceof FloorFollowingThief
                            || entity instanceof SmartThief) {
                        this.interaction(board);
                    }
                }
            }
        }
    }

    /**
     * Gets image relative to current tick of bomb
     */
    private void getTickImage() {
        switch (tickCount) {
            case 3 -> setImage(resources.bomb_3);
            case 2 -> setImage(resources.bomb_2);
            case 1 -> setImage(resources.bomb_1);
            default -> setImage(resources.bomb_exp);
        }
    }

    /**
     * Plays the sound unique to this item
     *
     * @throws UnsupportedAudioFileException Audio is unsupported
     * @throws LineUnavailableException      line unavailable
     * @throws IOException                   can't read file
     */
    @Override
    public void playSound() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        Settings.playGameEffect(resources.bombSound);
    }

    /**
     * Prints string that encapsulates data needed to save this item on case of user exiting.
     *
     * @return the data about this item
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
            result += ";" + tickCount + " ";
        }

        return result;
    }

}
