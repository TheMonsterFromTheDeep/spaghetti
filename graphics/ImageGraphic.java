package fettuccine.graphics;

import util.Resources;
import java.awt.image.BufferedImage;

/**
 * Contains image data.<br /><br />
 * 
 * ImageGraphic objects represent actual image data. They are used for appearance of Sprites
 * and other game objects.
 * 
 * @author TheMonsterFromTheDeep
 */
public class ImageGraphic implements Graphic {
    /** The graphical data of the Graphic. */
    BufferedImage data;
    
    /**
     * Creates an ImageaGraphic based on the specified BufferedImage.
     * @param data An image representing the appearance of the Graphic.
     */
    public ImageGraphic(BufferedImage data) {
        this.data = data;
    }
    
    /**
     * Creates a ImageaGraphic by loading image data from a relative path.<br /><br />
     * 
     * The path is relative to the root of the compiled JAR file.
     * @param relPath The relative path to load image data from.
     */
    public ImageGraphic(String relPath) {
        this.data = Resources.loadImageResource(relPath);
    }
    
    /**
     * Renders the ImageaGraphic using the specified Renderer at a specific<br /><br />
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
    @Override
    public void render(Renderer target, float x, float y) {
        target.drawImageCentered(data, x, y);
    }
    
    /**
     * Gets the width of the ImageGraphic. This is the width, in pixels, of the image that it represents.
     * @returns The width, in pixels, of the ImageGraphic.
     */
    @Override
    public int getWidth() { return data.getWidth(); }
    /**
     * Gets the height of the ImageGraphic. This is the height, in pixels, of the image that it represents.
     * @returns The height, in pixels, of the ImageGraphic.
     */
    @Override
    public int getHeight() { return data.getHeight(); }
}