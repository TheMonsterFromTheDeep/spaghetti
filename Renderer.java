package fettuccine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

/**
 * @author TheMonsterFromTheDeep
 */
public class Renderer {
    int width;
    int height;
    
    BufferedImage buffer;
    Graphics graphics;
    
    Color background;
    
    AffineTransform transform;
    
    int interpolation;
    
    public Renderer(int width, int height) {
        this.width = width;
        this.height = height;
        
        buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        graphics = buffer.createGraphics();
        
        background = new Color(0);
        
        interpolation = AffineTransformOp.TYPE_NEAREST_NEIGHBOR;
    }
    
    public void setBackgroundColor(Color c) {
        background = c;
    }
    
    public void begin() {
        graphics.setColor(background);
        graphics.fillRect(0, 0, width, height);
        transform = new AffineTransform();
    }
    
    public void scale(double sx, double sy) {
        transform.concatenate(AffineTransform.getScaleInstance(sx, sy));
    }
    
    public void rotate(double degrees) {
        transform.concatenate(AffineTransform.getRotateInstance(Math.toRadians(degrees)));
    }
    
    public void rotate(double degrees, double ax, double ay) {
        transform.concatenate(AffineTransform.getRotateInstance(Math.toRadians(degrees), ax, ay));
    }
    
    public void rotate(double degrees, BufferedImage anchor) {
        transform.concatenate(AffineTransform.getRotateInstance(Math.toRadians(degrees), ((double)anchor.getWidth()) / 2, ((double)anchor.getHeight()) / 2));
    }
    
    public void reset() { 
        transform = new AffineTransform();
    }
    
    /**
     * Draws the specified image centered on the specified x and y coordinates.
     * 
     * Applies any transformation matrix that the renderer currently possesses to
     * the drawn image.
     * @param image
     * @param x
     * @param y 
     */
    public void drawImage(BufferedImage image, int x, int y) {
        AffineTransformOp op = new AffineTransformOp(transform, interpolation);
        BufferedImage result = op.createCompatibleDestImage(image, null);
        System.err.println(result.getWidth() + " " + result.getHeight());
        op.filter(image, result);
        graphics.drawImage(result, x - (image.getWidth() / 2), y - (image.getHeight() / 2), null);
    }
    
    public void renderTo(Graphics target, int x, int y, int width, int height) {
        target.drawImage(buffer, x, y, width, height, null);
    }
    
    public void renderTo(Graphics target, RenderData data) {
        target.drawImage(buffer, data.renderX, data.renderY, data.renderWidth, data.renderHeight, null);
    }
}