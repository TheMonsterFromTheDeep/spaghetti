package fettuccine.geom;

public class Rectangle {
    public float x;
    public float y;
    public float width;
    public float height;
    
    public Rectangle(float fill) {
        this.x = this.y = this.width = this.height = fill;
    }
    
    public Rectangle(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    
    public Rectangle(Rectangle r) {
        this.x = r.x;
        this.y = r.y;
        this.width = r.width;
        this.height = r.height;
    }
    
    public Rectangle(Vector2 dupe) {
        this.x = dupe.x;
        this.y = dupe.y;
        this.width = dupe.x;
        this.height = dupe.y;
    }
    
    public Rectangle(Vector2 position, Vector2 size) {
        this.x = position.x;
        this.y = position.y;
        this.width = size.x;
        this.height = size.y;
    }
    
    public float left() { return x; }
    public float top() { return y; }
    public float right() { return x + width; }
    public float bottom() { return y + height; }
    
    public void setLeft(float x) { this.width += (this.x - x); this.x = x; }
    public void setTop(float y) { this.height += (this.y - y); this.y = y; }
    public void setRight(float x) { this.width = (x - this.x); }
    public void setBottom(float y) { this.height = (y - this.y); }
    
    public void shift(float dx, float dy) {
        x += dx;
        y += dy;
    }
    
    public boolean contains(float x, float y) {
        return (x >= this.x && x < this.x + width && y >= this.y && y < this.y + width);
    }
    
    public boolean intersects(Rectangle r) {
        boolean cond1 = (this.x + this.width >= r.x && this.x + this.width <= r.x + r.width) || (r.x + r.width >= this.x && r.x + r.width <= this.x + this.width);
        boolean cond2 = (this.y + this.height >= r.y && this.y + this.height <= r.y + r.height) || (r.y + r.height >= this.y && r.y + r.height <= this.y + this.height);
        return cond1 && cond2;
    }
}