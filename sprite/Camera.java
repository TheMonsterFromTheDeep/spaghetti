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
    
    public Camera() {
        graphics = new Graphic[0];
    }
    
    public Camera(ImageGraphic[] graphics) {
        this.graphics = graphics;
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
    
    public void render(World w, Renderer renderer) {
        if(!w.isCorrectlyAligned()) {
            w.align();
        }
        
        renderer.begin();
        
        //The Sprites being rendered are guaranteed to be non-null
        for (int i = 0; i < w.getAlignedSize(); i++) {
            Sprite sprite = w.sprites[i];
            
            if(sprite.graphic >= 0 && sprite.graphic < graphics.length) {
                float xoff = (float)graphics[sprite.graphic].getWidth() / 2;
                float yoff = (float)graphics[sprite.graphic].getHeight() / 2;
                renderer.rotate(sprite.direction, xoff, yoff);
                graphics[sprite.graphic].render(renderer, sprite.x - (int)xoff, sprite.y - (int)yoff);
            }
        }
    }
}