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
    
    /** 
     * Stores whether the World is "correctly aligned".<br />
     * Criteria:<br />
     * -All Sprites are sorted in order of layer number<br />
     * -All non-null Sprites are in one section of the array, and all others are in another section<br />
     * -The size of the non-null part of the array is stored in alignedSize
     */
    private boolean correctlyAligned = false;
    /** If correctlyAligned is true, this indicates the size of the non-null section of Sprites the World contains. */
    private int alignedSize = 0;
    
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
     * added to it, but it will be isolated and not particularly useful.<br /><br />
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
     * Adds the specified Sprite to the World.<br /><br />
     * 
     * If the World's array of Sprites contains a null pointer,
     * the Sprite will be placed there.<br /><br />
     * 
     * If the World's array of Sprites is completely full, it 
     * will be expanded by an amount equal to World.GROW_SPRITE_COUNT
     * and the new Sprite will fill one of the new empty values.
     * @param s 
     */
    public void addSprite(Sprite s) {
        s.parent = this;
        
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
        
        correctlyAligned = false;
    }
    
    /**
     * Removes the specified Sprite from the world and unparents it to this World.<br /><br />
     * 
     * If the World contains the specified Sprite, its position in the array will
     * be nullified. The Sprite will also be unparented from the World.<br /><br />
     * 
     * If the World does not contain the specified Sprite, nothing will happen. It
     * will not be unparented from the World, even if its parent is that of the World.
     * @param s The Sprite to be cleared from the World.
     * @see #killSprite(fettuccine.sprite.Sprite) 
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
        
        correctlyAligned = false;
    }
    
    /**
     * Removes the Sprite simply by nullifying this World's reference to it.<br /><br />
     * 
     * This method is called through a Sprite when the Sprite is destroying itself.
     * The Sprite wants to be removed from the World but does not care about having
     * its parent changed. Thus, it is simply nullified in this World's set of Sprites,
     * and the Sprite is left to deal with the rest.<br /><br />
     * 
     * If the Sprite is not found inside this World's set of Sprites, nothing will happen.
     * 
     * @param s The Sprite to kill.
     * @see #clearSprite(fettuccine.sprite.Sprite) 
     */
    public void killSprite(Sprite s) {
        for(int i = 0; i < sprites.length; i++) {
            //If the Sprite is found in the World, nullify it.
            if(sprites[i] == s) { 
                sprites[i] = null;
            }
        }
        
        correctlyAligned = false;
    }
    
    /**
     * Notifies the World that one of its child Sprites has had its layer changed.<br /><br />
     * 
     * This causes the World to no longer guarantee correct alignment, at least until
     * align() is called again.<br /><br />
     * 
     * If this method is called repeatedly for any reason, it could potentially bottleneck<br /><br />
     * graphical calls.
     * 
     * @see #isCorrectlyAligned() 
     */
    public void notifyLayerChange() {
        correctlyAligned = false;
    }
    
    /**
     * Gets whether the World is correctly aligned. The World is considered correctly aligned
     * based on the following criteria:<br />
     * -All Sprites are sorted in order of layer number<br />
     * -All non-null Sprites are in one section of the array, and all others are in another section<br />
     * -The size of the non-null part of the array can be retrieved by calling getAlignedSize()
     * @return Whether the World is correctly aligned.
     * @see #getAlignedSize() 
     * @see #align()
     */
    public boolean isCorrectlyAligned() { return correctlyAligned; }
    
    /**
     * If the World is correctly aligned, this will return the size of the section of
     * the World's array of Sprites that is non-null.
     * @return The count of non-null Sprites.
     * @see #isCorrectlyAligned() 
     * @see #align()
     */
    public int getAlignedSize() { return alignedSize; }
    
    private void swapSprites(int i1, int i2) {
        Sprite tmp = sprites[i1];
        sprites[i1] = sprites[i2];
        sprites[i2] = tmp;
    }
    
    private int sortSpritePartition(int start, int end) {
        int index = (start + end) / 2;
        int pivot = sprites[index].getLayer();
        swapSprites(index, end); 
        for (int i = index = start; i < end; ++ i) {
            if (sprites[i].getLayer() <= pivot) {
                swapSprites(index++, i);
            }
        }
        swapSprites(index, end); 
        return index;
    }
    
    private void quickSortSprites(int start, int end) {
        if (end > start) {
            int index = sortSpritePartition(start, end);
            quickSortSprites(start, index - 1);
            quickSortSprites(index + 1,  end);
        }
    }
    
    /**
     * Moves all the non-null sprites that the World contains to one section with
     * the rest of the Sprites being null objects. Thus, all the non-null Sprite 
     * objects are guaranteed to be in one chunk.
     * @return The length of the chunk of Sprites in the array that are not null.
     */
    private int deNullify() {
        int lastValidIndex = sprites.length - 1;
        while(lastValidIndex > 0 && sprites[lastValidIndex] == null) { lastValidIndex--; }
        if(sprites[lastValidIndex] == null) { return 0; }
        if(lastValidIndex == 0) { return 1; }
        
        int index = lastValidIndex - 1;
        while(index >= 0) {
            if(sprites[index] == null) {
                for(int i = index; i < lastValidIndex; i++) {
                    sprites[i] = sprites[i + 1];
                }
                sprites[lastValidIndex] = null;
                lastValidIndex--;
            }
            index--;
        }
        return lastValidIndex;
    }
    
    /**
     * Aligns the World's Sprites in its array such that several criteria are filled.<br />
     * Criteria:<br />
     * -All Sprites are sorted in order of layer number<br />
     * -All non-null Sprites are in one section of the array, and all others are in another section<br />
     * -The size of the non-null part of the array can be retrieved by calling getAlignedSize()
     * @see #isCorrectlyAligned() 
     * @see #getAlignedSize() 
     */
    public void align() {        
        //TODO: Make World alignment much more efficient
        if(!correctlyAligned) {
            alignedSize = deNullify();
            if(alignedSize > 0) {
                quickSortSprites(0, alignedSize - 1);
            }
            correctlyAligned = true;
        }
    }
}