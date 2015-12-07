package fettuccine.graphics;

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
        AffineTransformOp op = new AffineTransformOp(transform, interpolation);
        BufferedImage transAnchor = op.createCompatibleDestImage(anchor, null);
        transform.concatenate(AffineTransform.getRotateInstance(Math.toRadians(degrees), (((float)transAnchor.getWidth() / anchor.getWidth()) * anchor.getWidth()) / 2, (((float)transAnchor.getHeight() / anchor.getHeight()) * anchor.getHeight()) / 2));
    }
    
    public void reset() { 
        transform = new AffineTransform();
    }
    
    /**
     * Draws the specified image at the specified x and y coordinates.<br /><br />
     * 
     * Any transforms that the Renderer currently possesses are applied to the
     * image.<br /><br />
     * 
     * The coordinates given are treated as the coordinates of the upper-left
     * corner of the image. 
     * @param image The image to draw.
     * @param x The x coordinate of the upper-left corner of the destination of the image.
     * @param y The y coordinate of the upper-left corner of the destination of the image.
     */
    public void drawImage(BufferedImage image, int x, int y) {
        AffineTransformOp op = new AffineTransformOp(transform, interpolation);
        BufferedImage result = op.createCompatibleDestImage(image, null);
        op.filter(image, result);
        graphics.drawImage(result, x, y, null);
    }
    
    /**
     * Draws the specified image centered on the specified x and y coordinates.<br /><br />
     * 
     * Any transforms that the Renderer currently possesses are applied to the
     * image.<br /><br />
     * 
     * The coordinates given are treated as the coordinates of the center of the image.
     * @param image The image to draw.
     * @param x The x coordinate of the upper-left corner of the destination of the image.
     * @param y The y coordinate of the upper-left corner of the destination of the image.
     */
    public void drawImageCentered(BufferedImage image, int x, int y) {
        AffineTransformOp op = new AffineTransformOp(transform, interpolation);
        BufferedImage result = op.createCompatibleDestImage(image, null);
        op.filter(image, result);
        graphics.drawImage(result, x - ((result.getWidth() / image.getWidth()) * image.getWidth()) / 2, y - ((result.getHeight() / image.getHeight()) * image.getHeight()) / 2, null);
    }
    
    public void renderTo(Graphics target, int x, int y, int width, int height) {
        target.drawImage(buffer, x, y, width, height, null);
    }
    
    public void renderTo(Graphics target, RenderData data) {
        target.drawImage(buffer, data.renderX, data.renderY, data.renderWidth, data.renderHeight, null);
    }
}