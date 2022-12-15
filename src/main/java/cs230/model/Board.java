package cs230.model;

import cs230.model.entity.*;
import cs230.model.entity.enemies.SmartThief;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

/**
 * Board class, is the host of all entities and handles everything related to the gameplay aspect.
 *
 * @author Dafydd-Rhys Maund
 */
public class Board {

    /**
     * Current level of board.
     */
    private final int currentLevel;
    /**
     * Colors used of board.
     */
    private Paint[] colors;
    /**
     * Expected time to completed board.
     */
    private int expectedTime;
    /**
     * Current time played on board.
     */
    private int currentTime = 0;
    /**
     * Current tick on board.
     */
    private int tick = 0;
    /**
     * Tiles on board.
     */
    private final Tile[][] tiles;
    /**
     * Player on board.
     */
    private final Player player;
    /**
     * Entities on board.
     */
    private List<Entity> entities;
    /**
     * Enemies on board.
     */
    private List<Enemy> enemies;
    /**
     * Items on board.
     */
    private List<Item> items;
    /**
     * Height of board.
     */
    private final int height;
    /**
     * Width of board.
     */
    private final int width;
    /**
     * Stage hosting board.
     */
    private Stage stage;
    /**
     * Whether player is dead.
     */
    private boolean playerDead = false;
    /**
     * Whether game has been won.
     */
    private boolean gameWon = false;

    /**
     * Instantiates a new Board.
     *
     * @param colors colors of board
     * @param tiles        the tiles
     * @param width        the width
     * @param height       the height
     * @param level        the level
     * @param p            the p
     * @param expectedTime the expected time
     */
    public Board(Tile[][] tiles, int width, int height, int level, Player p, int expectedTime, Paint[] colors) {
        this.width = width;
        this.height = height;
        this.currentLevel = level;
        this.tiles = tiles;
        this.expectedTime = expectedTime;
        this.colors = colors;

        player = p;
        entities = new ArrayList<>();
        enemies = new ArrayList<>();
        items = new ArrayList<>();

        placeEntity(player, this.getTile(player.getCurrentPosX(), player.getCurrentPosY()));
    }

    /**
     * Instantiates a new Board from save file.
     *
     * @param colors colors of board
     * @param tiles        the tiles
     * @param x            the x
     * @param y            the y
     * @param level        the level
     * @param player       the player
     * @param expectedTime the expected time
     * @param tick         the tick
     * @param score        the score
     */
    public Board(Tile[][] tiles, int x, int y, int level, Player player, int expectedTime,
                 Paint[] colors, int tick, int score) {
        this.width = x;
        this.height = y;
        this.currentLevel = level;
        this.tiles = tiles;
        this.expectedTime = expectedTime;
        this.currentTime = expectedTime - tick;
        this.player = player;
        this.colors = colors;

        this.player.setScore(score);
        entities = new ArrayList<>();
        enemies = new ArrayList<>();
        items = new ArrayList<>();

        placeEntity(player, getTile(player.getCurrentPosX(), player.getCurrentPosY()));
    }

    /**
     * Sets stage.
     *
     * @param thisStage the stage
     */
    public void setStage(Stage thisStage) {
        stage = thisStage;
    }

    /**
     * Gets stage.
     *
     * @return the stage
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * Sets tick.
     *
     * @param tick the tick
     */
    public void setTick(int tick) {
        this.tick = tick;
    }

    /**
     * Gets tick.
     *
     * @return the tick
     */
    public int getTick() {
        return tick;
    }

    /**
     * Add second to current time.
     */
    public void addSecondCurrentTime() {
        this.currentTime++;
    }

    /**
     * Gets time remaining.
     *
     * @return the time remaining
     */
    public int getTimeRemaining() {
        return expectedTime - currentTime;
    }

    /**
     * Place entity.
     *
     * @param entity the entity
     * @param tile   the tile
     */
    public void placeEntity(final cs230.model.entity.Entity entity, final Tile tile) {
            if (entity instanceof Item item) {
            item.setCurrentPosX(tile.getX());
            item.setCurrentPosY(tile.getY());
            tile.addItemToTile(item);
            items.add(item);
        } else {
            entity.setCurrentPosX(tile.getX());
            entity.setCurrentPosY(tile.getY());
            tile.addEntity(entity);

            if (entity instanceof Enemy enemy) {
                enemies.add(enemy);
            }
        }
        entities.add(entity);
    }

    /**
     * Gets the board tiles.
     *
     * @return tiles [][]
     */
    public Tile[][] getTiles() {
        return tiles;
    }

    /**
     * Gets current level.
     *
     * @return the current level
     */
    public int getCurrentLevel() {
        return currentLevel;
    }

    /**
     * Sets expected time.
     *
     * @param expectedTime the expected time
     */
    public void setExpectedTime(int expectedTime) {
        this.expectedTime = expectedTime;
    }

    /**
     * Gets expected time.
     *
     * @return the expected time
     */
    public int getExpectedTime() {
        return expectedTime;
    }

    /**
     * Sets items on board.
     *
     * @param items the items
     */
    public void setItems(List<Item> items) {
        this.items = items;
    }

    /**
     * Gets items on board.
     *
     * @return the items
     */
    public List<Item> getItems() {
        return items;
    }

    /**
     * Sets entities on board.
     *
     * @param entities the entities
     */
    public void setEntities(List<Entity> entities) {
        this.entities = entities;
    }

    /**
     * Gets entities on board.
     *
     * @return the entities
     */
    public List<Entity> getEntities() {
        return entities;
    }

    /**
     * Sets enemies on board.
     *
     * @param enemies the enemies
     */
    public void setEnemies(List<Enemy> enemies) {
        this.enemies = enemies;
    }

    /**
     * Gets enemies on board.
     *
     * @return the enemies
     */
    public List<Enemy> getEnemies() {
        return enemies;
    }

    /**
     * Gets player.
     *
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets height of board.
     *
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Gets width of board.
     *
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Gets tile on board.
     *
     * @param x the x
     * @param y the y
     * @return the tile
     */
    public Tile getTile(int x, int y) {
        return tiles[x][y];
    }

    /**
     * sets that the game has been won.
     */
    public void gameWon() {
        this.gameWon = true;
    }

    /**
     * Whether game has been one.
     *
     * @return the boolean
     */
    public boolean isGameWon() {
        return gameWon;
    }

    /**
     * sets game has been lost.
     */
    public void gameLost() {
        this.playerDead = true;
    }

    /**
     * Whether players is dead.
     *
     * @return player dead state
     */
    public boolean isPlayerDead() {
        return playerDead;
    }

    /**
     * sets Colors of board
     *
     * @param colors colors being set
     */
    public void setColors(Paint[] colors) {
        this.colors = colors;
    }

    /**
     * Gets colors used on board
     *
     * @return the board colors
     */
    public Paint[] getColors() {
        return colors;
    }

}
