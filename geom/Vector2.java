package fettuccine.geom;

public class Vector2 {
    public float x;
    public float y;
    
    public Vector2(float f) {
        this.x = this.y = f;
    }
    
    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }
    
    public void shift(float x, float y) {
        this.x += x;
        this.y += y;
    }
    
    public Vector2(Vector2 v) {
        this.x = v.x;
        this.y = v.y;
    }
    
    public void rotate(float px, float py, double degrees) {
        double distance = Math.sqrt((x - px) * (x - px) + (y - py) * (y - py));
        double oldAngle = Math.atan2(y - py, x - px);
        x = px + (float)(Math.cos(oldAngle + Math.toRadians(degrees)) * distance);
        y = py + (float)(Math.sin(oldAngle + Math.toRadians(degrees)) * distance);
    }
}