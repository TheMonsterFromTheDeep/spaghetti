package fettuccine.graphics;

import util.Resources;
import java.awt.image.BufferedImage;

/**
 * Contains graphical image data.<br /><br />
 * 
 * Graphic objects are used to represent the appearance of Sprites and draw them
 * on to the screen. Graphic objects used for Sprite drawing are contained in
 * a Camera instance and drawn by it.<br /><br />
 * 
 * @author TheMonsterFromTheDeep
 */
public class Graphic {
    /** The graphical data of the Graphic. */
    BufferedImage data;
    
    /**
     * Creates a Graphic based on the specified BufferedImage.
     * @param data An image representing the appearance of the Graphic.
     */
    public Graphic(BufferedImage data) {
        this.data = data;
    }
    
    /**
     * Creates a Graphic by loading image data from a relative path.<br /><br />
     * 
     * The path is relative to the root of the compiled JAR file.
     * @param relPath The relative path to load image data from.
     */
    public Graphic(String relPath) {
        this.data = Resources.loadImageResource(relPath);
    }
    
    /**
     * Renders the Graphic using the specified Renderer at a specific<br /><br />
     * x and y coordinate.
     * 
     * The coordinates given are treated as the coordinates of the upper-left
     * corner of the image.<br /><br />
     * 
     * This will apply any transforms the Renderer currently contains.
     * @param target The Renderer to render the image with.
     * @param x The x coordinate to render the image at. 
     * @param y The y coordinate to render the image at.
     */
    public void render(Renderer target, int x, int y) {
        target.drawImage(data, x, y);
    }
    
    public int getWidth() { return data.getWidth(); }
    public int getHeight() { return data.getHeight(); }
}