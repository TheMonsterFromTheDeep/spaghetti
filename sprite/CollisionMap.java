package fettuccine.sprite;

import fettuccine.geom.Polygon;
import fettuccine.geom.Rectangle;
import fettuccine.geom.Vector2;
import java.awt.Color;
import java.awt.image.BufferedImage;

public class CollisionMap {
    /*public static final class MapComponent {       
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
        
        public MapComponent(MapComponent mc) {
            this.data = new Vector2[mc.data.length];
            for(int i = 0; i < data.length; i++) {
                this.data[i] = new Vector2(mc.data[i]);
            }
            this.bounds = new Rectangle(mc.bounds);
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
        
        public void shift(float x, float y) {
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
        
        public MapComponent createInstance() {
            return new MapComponent(this);
        }
    }*/
    
    private static Polygon createMapComponentFromMap(BufferedImage b, int red) {
        Vector2[] data;

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

        data = new Vector2[pointCount + 1];

        for(int x = 0; x < b.getWidth(); x++) {
            for(int y = 0; y < b.getHeight(); y++) {
                Color c = new Color(b.getRGB(x, y));
                if(c.getBlue() < 1 && c.getRed() == red) {
                    data[c.getGreen()] = new Vector2(x, y);
                }
            }
        }

        return new Polygon(data);
    }
    
    Polygon[] components;
    /** The anchor stores the upper-left corner of the collision map in its default state. */
    Vector2 anchor;
    /** The center stores the center of the CollisionMap. */
    Vector2 center;
    
    public CollisionMap() {
        anchor = new Vector2(0);
        center = new Vector2(0);
    }
    
    public CollisionMap(CollisionMap cm) {
        components = new Polygon[cm.components.length];
        for(int i = 0; i < components.length; i++) {
            components[i] = cm.components[i].getCopy();
        }
        anchor = new Vector2(cm.anchor);
        center = new Vector2(0);
    }
    
    public boolean collides(CollisionMap m) {
        for(Polygon p1 : components) {
            for(Polygon p2 : m.components) {
                if(p1.intersects(p2)) { return true; }
            }
        }
        return false;
    }
    
    public void setAnchor(float x, float y) {
        float dx = x - anchor.x;
        float dy = y - anchor.y;
        for(Polygon p : components) {
            p.shift(dx, dy);
        }
        anchor = new Vector2(dx, dy);
    }
    
    public void shift(int x, int y) {
        for(Polygon p : components) {
            p.shift(x, y);
        }
        anchor.shift(x, y);
        center.shift(x, y);
    }
    
    public void rotate(double degrees) {
        for(Polygon p : components) {
            p.rotate(center.x, center.y, degrees);
        }
        anchor.rotate(center.x, center.y, degrees);
    }
    
    public CollisionMap createInstance() {
       return new CollisionMap(this);
    }
    
    public static CollisionMap createMapFromMapImage(BufferedImage b) {
        int mapCompCount = -1;
        for(int x = 0; x < b.getWidth(); x++) {
            for(int y = 0; y < b.getHeight(); y++) {
                Color c = new Color(b.getRGB(x, y));
                if(c.getBlue() < 1) {
                    if(mapCompCount < c.getRed()) {
                        mapCompCount = c.getRed();
                    }
                }
            }
        }
        
        CollisionMap cm = new CollisionMap();
        cm.components = new Polygon[mapCompCount + 1];
        for(int i = 0; i <= mapCompCount; i++) {
            cm.components[i] = createMapComponentFromMap(b, i);
        }
        
        return cm;
    }
    
    public void printBounds() {
        components[0].printBounds();
    }
}