package cs230.model.entity.enemies;

import cs230.external.Settings;
import cs230.model.Board;
import cs230.model.entity.enums.Direction;
import cs230.model.entity.Enemy;
import cs230.model.entity.Entity;
import cs230.model.entity.Player;
import cs230.model.Tile;
import java.io.IOException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Class used to describe and store information about FlyingAssassin
 *
 * @author Dafydd-Rhys Maund (2003900)
 */
public class FlyingAssassin extends Enemy {

    /**
     * Instantiates a new Flying assassin from save file.
     *
     * @param dir the direction the assassin is facing
     */
    public FlyingAssassin(Direction dir) {
        setEntityCode("E2");
        setDir(dir);
        setDirectionalImages("FlyingAssassinTemp.png");
        setImage(getUpdatedDirectionalImage(getDir()));
    }

    /**
     * Implements how this enemy should move, going straight then 180 on wall impact
     *
     * @param board  the board being played on
     */
    @Override
    public void move(Board board) {
        switch (getDir()) {
            case UP -> hop(board,  getCurrentPosX(), getCurrentPosY() - 1);
            case RIGHT -> hop(board,  getCurrentPosX() + 1, getCurrentPosY());
            case DOWN -> hop(board, getCurrentPosX(), getCurrentPosY() + 1);
            default -> hop(board, getCurrentPosX() - 1, getCurrentPosY());
        }
    }

    /**
     * Identifies whether moving to the tile at pos x,y can be moved to, if so move there
     *
     * @param board the current board being played on
     * @param x x of tile
     * @param y y of tile
     */
    @Override
    public void hop(Board board, int x, int y) {
        Tile original = board.getTile(getCurrentPosX(), getCurrentPosY());

        if (x >= 0 && x < board.getWidth() && y >= 0 && y < board.getHeight()) {
            Tile tile = board.getTile(x, y);
            tile.addEntity(this);

            this.setCurrentPosX(x);
            this.setCurrentPosY(y);
            original.removeEntity(this);
            check(board, tile);
        } else {
            setDir(Direction.oppositeDirection(getDir()));
            setImage(getUpdatedDirectionalImage(getDir()));
        }
    }

    /**
     * Checks for entities on the new tile and performs the appropriate action according to the case.
     *
     * @param board the board
     * @param tile  the tile
     */
    @Override
    public void check(Board board, Tile tile) {
        for (int i = 0; i < tile.getEntities().size() - 1; i++) {
            Entity entity = tile.getEntities().get(i);

            if (entity instanceof Player pl) {
                try {
                    playSound();
                    pl.playSound();
                } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                    e.printStackTrace();
                }
                board.gameLost();
            } else if (entity instanceof Enemy) {
                entity.destroy(board);

                try {
                    playSound();
                } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Plays the sound unique to this enemy
     *
     * @throws UnsupportedAudioFileException Audio is unsupported
     * @throws LineUnavailableException line unavailable
     * @throws IOException can't read file
     */
    @Override
    public void playSound() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        Settings.playGameEffect(resources.kill);
    }

}
