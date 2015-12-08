package fettuccine.geom;

public class Vector2 {
    public int x;
    public int y;
    
    public Vector2(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public void shift(int x, int y) {
        this.x += x;
        this.y += y;
    }
    
    public void rotate(int pivotX, int pivotY, double degrees) {
        double distance = Math.sqrt((x - pivotX) * (x - pivotX) + (y - pivotY) * (y - pivotY));
        double oldAngle = Math.atan2(y - pivotY, x - pivotX);
        x = (int)(Math.cos(oldAngle + degrees) * distance);
        y = (int)(Math.sin(oldAngle + degrees) * distance);
    }
}