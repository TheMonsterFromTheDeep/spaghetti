package fettuccine.geom;

public class Rectangle {
    public int x;
    public int y;
    public int width;
    public int height;
    
    public Rectangle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    
    public int left() { return x; }
    public int top() { return y; }
    public int right() { return x + width; }
    public int bottom() { return y + height; }
    
    public void setLeft(int x) { this.x = x; }
    public void setTop(int y) { this.y = y; }
    public void setRight(int x) { this.width = (x - this.x); }
    public void setBottom(int y) { this.height = (y - this.y); }
    
    public boolean contains(int x, int y) {
        return (x >= this.x && x < this.x + width && y >= this.y && y < this.y + width);
    }
    
    public boolean intersects(Rectangle r) {
        return this.contains(r.x, r.y) || this.contains(r.right(), r.y) || this.contains(r.x, r.bottom()) || this.contains(r.right(), r.bottom());
    }
}