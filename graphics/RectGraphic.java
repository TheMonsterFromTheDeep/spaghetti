package fettuccine.graphics;

import java.awt.Color;

public class RectGraphic implements Graphic {

    public int width;
    public int height;
    public Color color;
    
    public RectGraphic() {
       this.width = 20;
       this.height = 20;
       this.color = new Color(0);
    }
    
    public RectGraphic(Color c) {
        this.width = 20;
        this.height = 20;
        this.color = c;
    }
    
    public RectGraphic(int width, int height) {
        this.width = width;
        this.height = height;
        this.color = new Color(0);
    }
    
    public RectGraphic(int width, int height, Color color) {
        this.width = width;
        this.height = height;
        this.color = color;
    }
    
    @Override
    public void render(Renderer r, int x, int y) {
        r.drawRectangle(x, y, width, height, color);
    }
    
    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }
    
    public void setWidth(int width) { this.width = width; }
    public void setHeight(int height) { this.height = height; }
    
    public void setColor(Color c) { this.color = c; }

    @Override
    public int getWidth() { return width; }

    @Override
    public int getHeight() { return height; }
}