package fettuccine.sprite;

import fettuccine.graphics.Graphic;
import fettuccine.graphics.ImageGraphic;
import fettuccine.graphics.Renderer;

/**
 * A Camera is used to draw a World and all of the Sprites that it contains.<br /><br />
 * 
 * The Camera contains unique graphical data that is referenced by the Sprites.
 * The Sprites themselves do not contain any graphical data.<br /><br />
 * 
 * A Camera is not uniquely associated with Sprites. Different Cameras can
 * be used to draw the same World with different graphical appearances.
 * @author TheMonsterFromTheDeep
 */
public class Camera {
    /** The graphical image data that the Camera will draw Sprites with. */
    Graphic[] graphics;
    
    /** Stores the x position of the top-left corner of the Camera. */
    public float x;
    /** Stores the y position of the top-left corner of the Camera. */
    public float y;
    
    /** Stores the width of the Camera lens. */
    float width;
    /** Stores the height of the Camera lens. */
    float height;
    
    public Camera(float width, float height) {
        graphics = new Graphic[0];
        x = y = 0;
        this.width = width;
        this.height = height;
    }
    
    public Camera(float width, float height, ImageGraphic[] graphics) {
        this.graphics = graphics;
        x = y = 0;
        this.width = width;
        this.height = height;
    }
    
    public void addGraphic(Graphic g) {
        Graphic[] tmp = graphics;
        graphics = new Graphic[graphics.length + 1];
        System.arraycopy(tmp,0,graphics,0,tmp.length);
        graphics[tmp.length] = g;
    }
    
    public void addGraphics(Graphic[] newGraphics) {
        Graphic[] tmp = graphics;
        graphics = new Graphic[graphics.length + newGraphics.length];
        System.arraycopy(tmp,0,graphics,0,tmp.length);
        System.arraycopy(newGraphics, 0, graphics, tmp.length, newGraphics.length);
    }
    
    public void addImageGraphic(String relPath) {
        Graphic[] tmp = graphics;
        graphics = new Graphic[graphics.length + 1];
        System.arraycopy(tmp,0,graphics,0,tmp.length);
        graphics[tmp.length] = new ImageGraphic(relPath);
    }
    
    public void addImageGraphics(String[] relPaths) {
        Graphic[] tmp = graphics;
        graphics = new Graphic[graphics.length + relPaths.length];
        System.arraycopy(tmp,0,graphics,0,tmp.length);
        for(int i = 0; i < relPaths.length; i++) {
            graphics[tmp.length + i] = new ImageGraphic(relPaths[i]);
        }
    }
    
    public void track(Sprite s) {
        x = -s.getX() + width / 2;
        y = -s.getY() + height / 2;
    }
    
    public void render(World w, Renderer renderer) {
        if(!w.isCorrectlyAligned()) {
            w.align();
        }
        
        renderer.begin();
        
        //The Sprites being rendered are guaranteed to be non-null
        for (int i = 0; i < w.getAlignedSize(); i++) {
            Sprite sprite = w.sprites[i];
            
            if(sprite.graphic >= 0 && sprite.graphic < graphics.length) {
                renderer.rotate(sprite.getDirection(), 8, 8);
                graphics[sprite.graphic].render(renderer, x + sprite.getX(), y + sprite.getY());//(int)(sprite.getX() - xoff), (int)(sprite.getY() - yoff));
            }
        }
    }
}