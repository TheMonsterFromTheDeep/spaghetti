package fettuccine.sprite;

/**
 * A Sprite is an object with a position, rotation, and potential graphical appearance. Each Sprite
 * is parented to a World and can use that handle in order to get information about other
 * Sprites.
 * 
 * When using a Camera, Sprites are automatically drawn based on their position relative to
 * the Camera and are rotated correctly.
 * 
 * Sprites themselves do not actually contain any graphical information. Rather, they contain
 * a reference to what graphics should be used, and the Camera draws it with its own graphics
 * resources that it has that correspond to the Sprite.
 * @author TheMonsterFromTheDeep
 */
public class Sprite {
    /** The parent World of the Sprite. The Sprite can access various data through the World object. */
    World parent;
    
    public Sprite() { }
    
    /**
     * Sets the parent World of the Sprite. The Sprite can access
     * information about other Sprites in the World.
     * @param world The new parent World of the Sprite.
     */
    public final void setParent(World world) { 
        this.parent = world;
    }
    
    /**
     * Clears the parent World of the Sprite. The Sprite will not be able
     * to access any information about other Sprites, as it won't be
     * associated with them in any particular World.
     * 
     * The World is cleared by setting it to World.getNullWorld(). This parent
     * World will still have the operations of other World objects but it
     * will be completely isolated.
     * 
     * The Sprite will also not be acknowledged by the null World unless it adds
     * itself to the World object. Thus, any Sprites added to the null parent
     * will not know about this Sprite unless it adds itself to the World.
     */
    public final void clearParent() { 
        this.parent = World.getNullWorld();
    }
    
    /**
     * Kills the sprite. This is done by completely removing it from the parent.
     * 
     * Subsequent method calls by this Sprite should not be expected to work. It
     * should be considered essentially unusable.
     */
    protected final void kill() { 
        this.parent.killSprite(this);
    }
}