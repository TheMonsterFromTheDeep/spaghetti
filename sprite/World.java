package fettuccine.sprite;

/**
 * The World contains a set of Sprites and stores all of their data. It automates
 * creation and destruction of Sprites. It also automates rendering of those Sprites
 * through a Camera.
 * @author TheMonsterFromTheDeep
 */
public class World {
    /** Stores the number of Sprites that the World starts with in its array. */
    static final int INIT_SPRITE_COUNT = 100;
    /** Stores the number of Sprites that the World expands its array by when it needs more space. */
    static final int GROW_SPRITE_COUNT = 10;
    
    /** 
     * Stores all the Sprites that the World is aware of. Sprites parented to this world can get
     * information about these Sprites.
     */
    Sprite[] sprites;
    
    /** Creates a new, empty World. It will contain World.INIT_SPRITE_COUNT empty Sprites. */
    public World() {
        sprites = new Sprite[INIT_SPRITE_COUNT];
    }
    
    /**
     * Creates a new, empty World of the specified length.
     * @param length The amount of Sprites the World will contain by default.
     */
    public World(int length) {
        sprites = new Sprite[length];
    }
    
    /** 
     * Creates an isolated World object that a Sprite is parented to when its
     * parent is cleared. This World will still be able to have new Sprites
     * added to it, but it will be isolated and not particularly useful.
     * 
     * The returned World will also not contain the Sprite. Thus, if other
     * Sprites are added to the empty World, they will not be able to access
     * the Sprite with a cleared parent unless it adds itself to the null World.
     * @return An isolated, completely empty World object.
     */
    public static World getNullWorld() {
        return new World(0);
    }
    
    /**
     * Adds the specified Sprite to the World.
     * 
     * If the World's array of Sprites contains a null pointer,
     * the Sprite will be placed there.
     * 
     * If the World's array of Sprites is completely full, it 
     * will be expanded by an amount equal to World.GROW_SPRITE_COUNT
     * and the new Sprite will fill one of the new empty values.
     * @param s 
     */
    public void addSprite(Sprite s) {
        for(int i = 0; i < sprites.length; i++) {
            if(sprites[i] == null) { //If there is an empty spot in the array, insert the Sprite there
                sprites[i] = s;
                return; //The function should end here
            }
        }
        //There wasn't an empty spot; the array needs to be expanded
        
        //Create a temporary array to hold the old Sprites
        Sprite[] tmp = sprites;
        
        //Expand the array of Sprites by GROW_SPRITE_COUNT
        sprites = new Sprite[tmp.length + GROW_SPRITE_COUNT];
        //Copy the old Sprites into the new array
        System.arraycopy(tmp,0,sprites,0,tmp.length);
        
        //Insert the new Sprite at the first empty index of the new array (equal to the length of the old array)
        sprites[tmp.length] = s;
    }
    
    /**
     * Removes the specified Sprite from the world and unparents it to this World.
     * 
     * If the World contains the specified Sprite, its position in the array will
     * be nullified. The Sprite will also be unparented from the World.
     * 
     * If the World does not contain the specified Sprite, nothing will happen. It
     * will not be unparented from the World, even if its parent is that of the World.
     * @param s 
     */
    public void clearSprite(Sprite s) {
        for(int i = 0; i < sprites.length; i++) {
            //If the Sprite is found inside the array, clear it
            if(sprites[i] == s) {
                //Clear the parent of the Sprite, it is now free-floating
                sprites[i].clearParent();
                //Disassociate the Sprite from the World
                sprites[i] = null;
            }
        }
    }
    
    /**
     * Removes the Sprite simply by nullifying this World's reference to it.
     * 
     * This method is called through a Sprite when the Sprite is destroying itself.
     * The Sprite wants to be removed from the World but does not care about having
     * its parent changed. Thus, it is simply nullified in this World's set of Sprites,
     * and the Sprite is left to deal with the rest.
     * 
     * If the Sprite is not found inside this World's set of Sprites, nothing will happen.
     * 
     * @param s The Sprite to kill.
     */
    public void killSprite(Sprite s) {
        for(int i = 0; i < sprites.length; i++) {
            //If the Sprite is found in the World, nullify it.
            if(sprites[i] == s) { 
                sprites[i] = null;
            }
        }
    }
}