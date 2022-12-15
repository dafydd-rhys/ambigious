package cs230.model;

import cs230.model.entity.Entity;
import cs230.model.entity.Item;
import cs230.model.entity.Player;
import cs230.model.entity.enemies.FloorFollowingThief;
import cs230.model.entity.enemies.SmartThief;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.paint.Paint;

/**
 * The tile class, handles and enforces rules on what can be held on a tile at any instance
 *
 * @author Dafydd-Rhys Maund
 */
public class Tile {

    /**
     * holds sub tiles of current tile
     */
    private TileColor[] subTiles;
    /**
     * item on tile
     */
    private cs230.model.entity.Item tileItem;
    /**
     * entities on tile
     */
    private final List<Entity> entities;
    /**
     * x of current tile
     */
    private int x;
    /**
     * y of current tile
     */
    private int y;

    /**
     * Parent tile of this tile
     */
    private Tile parent;
    /**
     * whether this tile has been checked
     */
    private boolean checked = false;
    /**
     * whether this tile is open
     */
    private boolean open = false;
    /**
     * distance between starting and current tile
     */
    int gCost = 0;
    /**
     * distance between current tile and end tile
     */
    int hCost = 0;
    /**
     * sum of hCost and fCost
     */
    int fCost = 0;

    /**
     * Instantiates a new Tile.
     *
     * @param tileColors the tile colors
     * @param x          the x
     * @param y          the y
     */
    public Tile(TileColor[] tileColors, int x, int y) {
        //System.arraycopy(tileColors, 0, this.subTiles, 0, NUM_OF_SUB_TILES);
        this.entities = new ArrayList<>();
        this.subTiles = tileColors;
        this.x = x;
        this.y = y;
    }

    /**
     * Sets tile item.
     *
     * @param tileItem the tile item
     */
    public void setTileItem(Item tileItem) {
        this.tileItem = tileItem;
    }

    /**
     * Gets item on tile.
     *
     * @return the item
     */
    public cs230.model.entity.Item getItem() {
        return tileItem;
    }

    /**
     * Sets x.
     *
     * @param x the x
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Gets x.
     *
     * @return the x
     */
    public int getX() {
        return x;
    }

    /**
     * Sets y.
     *
     * @param y the y
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Gets y.
     *
     * @return the y
     */
    public int getY() {
        return y;
    }

    /**
     * Get sub tiles.
     *
     * @return the sub tiles
     */
    public TileColor[] getSubTiles() {
        return subTiles;
    }

    /**
     * Gets entities on tile.
     *
     * @return the entities
     */
    public List<Entity> getEntities() {
        return entities;
    }

    /**
     * Add entity to tile.
     *
     * @param entity the entity
     */
    public void addEntity(cs230.model.entity.Entity entity) {
        entities.add(entity);
    }

    /**.
     * Remove entity from tile
     *
     * @param entity the entity
     */
    public void removeEntity(cs230.model.entity.Entity entity) {
        entities.remove(entity);
    }

    /**
     * Add item to tile.
     *
     * @param item the item
     */
    public void addItemToTile(cs230.model.entity.Item item) {
        tileItem = item;
    }

    /**
     * Returns whether thief is on tile.
     *
     * @return thief on tile
     */
    public boolean isThiefOnTile() {
        for (Entity entity : entities) {
            if (entity instanceof FloorFollowingThief || entity instanceof SmartThief) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns whether player is on tile.
     *
     * @return player on tile
     */
    public boolean isPlayerOnTile() {
        for (Entity entity : entities) {
            if (entity instanceof Player) {
                return true;
            }
        }

        return false;
    }

    /**
     * Identifies if two tiles share a color
     *
     * @param tile next tile
     * @return whether they share a color
     */
    public boolean shareColor(Tile tile) {
        for (TileColor sub : this.subTiles) {
            for (TileColor subT : tile.subTiles){
                if (sub == subT) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * sets distance between starting and current tile
     *
     * @param hCost distance between starting and current tile
     */
    public void sethCost(int hCost) {
        this.hCost = hCost;
    }

    /**
     * sets distance between starting and current tile
     *
     * @param gCost distance
     */
    public void setgCost(int gCost) {
        this.gCost = gCost;
    }

    /**
     * sets sum of gCost and hCost
     *
     * @param fCost sum of values
     */
    public void setfCost(int fCost) {
        this.fCost = fCost;
    }

    /**
     * gets sets sum of gCost and hCost
     *
     * @return sum of costs
     */
    public int getfCost() {
        return fCost;
    }

    /**
     * distance between starting and current tile
     *
     * @return distance
     */
    public int getgCost() {
        return gCost;
    }

    /**
     * gets distance between starting and current tile
     *
     * @return distance
     */
    public int gethCost() {
        return hCost;
    }

    /**
     * sets this tiles parent
     *
     * @param parent parent tile
     */
    public void setParent(Tile parent) {
        this.parent = parent;
    }

    /**
     * gets parent of this tile
     *
     * @return parent tile
     */
    public Tile getParent() {
        return parent;
    }

    /**
     * Set tile checked or not
     *
     * @param checked is tile checked
     */
    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    /**
     * Returns whether tile has been checked
     *
     * @return has tile been checked
     */
    public boolean isChecked() {
        return checked;
    }

    /**
     * sets tile open to passed boolean value
     *
     * @param open whether tile is open
     */
    public void setOpen(boolean open) {
        this.open = open;
    }

    /**
     * returns whether the tile is open
     *
     * @return is tile open
     */
    public boolean isOpen() {
        return open;
    }

    /**
     * sets all tile properties to null or false.
     */
    public void setDefault() {
        checked = false;
        parent = null;
        open = false;
    }

}
