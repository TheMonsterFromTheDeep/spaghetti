package fettuccine.sprite;

import fettuccine.geom.Rectangle;
import fettuccine.geom.Vector2;
import java.awt.Color;
import java.awt.image.BufferedImage;

public class CollisionMap {
    public static final class MapComponent {       
        Rectangle bounds;
        
        Vector2[] data;
        
        public static MapComponent createMapComponentFromMap(BufferedImage b, int red) {
            MapComponent mc = new MapComponent();
            
            int pointCount = 0;
            
            for(int x = 0; x < b.getWidth(); x++) {
                for(int y = 0; y < b.getHeight(); y++) {
                    Color c = new Color(b.getRGB(x, y));
                    if(c.getBlue() < 1 && c.getRed() == red) {
                        if(pointCount < c.getGreen()) {
                            pointCount = c.getGreen();
                        }
                    }
                }
            }
            
            mc.data = new Vector2[pointCount + 1];
            
            for(int x = 0; x < b.getWidth(); x++) {
                for(int y = 0; y < b.getHeight(); y++) {
                    Color c = new Color(b.getRGB(x, y));
                    if(c.getBlue() < 1 && c.getRed() == red) {
                        mc.data[c.getGreen()] = new Vector2(x, y);
                    }
                }
            }
            
            mc.calculateBounds();
            
            return mc;
        }
        
        public MapComponent() {
            data = new Vector2[0];
            bounds = new Rectangle(0, 0, 0, 0);
        }
        
        public MapComponent(Vector2[] data) {
            this.data = data;
            calculateBounds();
        }
        
        public void calculateBounds() {
            bounds = new Rectangle(0, 0, 0, 0);
            for(Vector2 v : data) {
                if(v.x < bounds.x) { bounds.x = v.x; }
                if(v.y < bounds.y) { bounds.y = v.y; }
                if(v.x > bounds.right()) { bounds.setRight(v.x); }
                if(v.y > bounds.bottom()) { bounds.setBottom(v.y); }
            }
        }
        
        public void calculateBounds(Vector2 point) {
            if(point.x < bounds.x) { bounds.x = point.x; }
            if(point.y < bounds.y) { bounds.y = point.y; }
            if(point.x > bounds.right()) { bounds.setRight(point.x); }
            if(point.y < bounds.bottom()) { bounds.setBottom(point.y); }
        }
        
        public void shift(int x, int y) {
            for(Vector2 v : data) {
                v.shift(x, y);
            }
        }
        
        public void rotate(int x, int y, double degrees) {
            for(Vector2 v : data) {
                v.rotate(x, y, degrees);
            }
        }
        
        public boolean intersects(Vector2 v) {
            //Thanks to http://stackoverflow.com/questions/217578/how-can-i-determine-whether-a-2d-point-is-within-a-polygon#answer-2922778
            int i, j;
            boolean c = false;
            for (i = 0, j = data.length - 1; i < data.length; j = i++) {
              if ( ((data[i].y > v.y) != (data[j].y > v.y)) &&
                (v.x < (data[j].x-data[i].x) * (v.y-data[i].y) / (data[j].y-data[i].y) + data[i].x) )
                { c = !c; }
            }
            return c;
        }
        
        public boolean intersects(MapComponent m) {
            //System.err.println("begin intersection check...");
            if(bounds.intersects(m.bounds)) {
                //System.err.println("checking for intersection!");
                for(Vector2 v : m.data) {
                    if(this.intersects(v)) { return true; }
                }
                for(Vector2 v : data) {
                    if(m.intersects(v)) { return true; }
                }
                return false;
            }
            else { return false; } //If the bounds do not intersect, neither do the polygons
        }
        
        public boolean isColliding(MapComponent m) {
            return intersects(m);
        }
    }
    
    MapComponent[] components;
    
    public boolean collides(CollisionMap m) {
        for(MapComponent m1 : components) {
            for(MapComponent m2 : m.components) {
                if(m1.isColliding(m2)) { return true; }
            }
        }
        return false;
    }
    
    public void shift(int x, int y) {
        for(MapComponent mc : components) {
            mc.shift(x, y);
        }
    }
    
    public void rotate(int x, int y, double degrees) {
        for(MapComponent mc : components) {
            mc.rotate(x, y, degrees);
        }
    }
    
    public static CollisionMap createMapFromMapImage(BufferedImage b) {
        int mapCompCount = -1;
        for(int x = 0; x < b.getWidth(); x++) {
            for(int y = 0; y < b.getHeight(); y++) {
                Color c = new Color(b.getRGB(x, y));
                if(c.getBlue() < 1) {
                    if(mapCompCount < c.getRed()) {
                        mapCompCount = c.getRed();
                        System.err.println("new red!" + mapCompCount);
                    }
                }
            }
        }
        
        CollisionMap cm = new CollisionMap();
        cm.components = new MapComponent[mapCompCount + 1];
        for(int i = 0; i <= mapCompCount; i++) {
            cm.components[i] = MapComponent.createMapComponentFromMap(b, i);
        }
        
        return cm;
    }
}