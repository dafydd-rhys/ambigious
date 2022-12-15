package cs230;

import javafx.scene.image.Image;
import java.util.HashMap;

/**
 * The Sprite manager used to separate UI from backend.
 *
 * @author Oliver
 */
public class SpriteManager {

    /**
     * Sprite map, maps code to image
     */
    private final java.util.HashMap<String, Image> spriteMap;

    /**
     * Instantiates a new Sprite manager.
     */
    public SpriteManager() {
        spriteMap = new HashMap<>();
    }

    /**
     * Add sprite.
     *
     * @param name   the name
     * @param sprite the sprite
     */
    public void addSprite(String name, Image sprite) {
        spriteMap.put(name, sprite);
    }

    /**
     * Clear sprites.
     */
    public void clearSprites() {
        spriteMap.clear();
    }

    /**
     * Gets sprite.
     *
     * @param name the name
     * @return the sprite
     */
    public Image getSprite(String name) {
        return spriteMap.get(name);
    }

}
