package fettuccine.sprite;

import fettuccine.geom.Rectangle;
import fettuccine.geom.Vector2;

public class CollisionMap {
    public static final class MapComponent {
        public static final byte MODE_INTERSECT = 0;
        public static final byte MODE_NOT_INTERSECT = 1;
        
        Rectangle bounds;
        
        Vector2[] data;
        byte mode;
        
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
                if(v.y < bounds.bottom()) { bounds.setBottom(v.y); }
            }
        }
        
        public void calculateBounds(Vector2 point) {
            if(point.x < bounds.x) { bounds.x = point.x; }
            if(point.y < bounds.y) { bounds.y = point.y; }
            if(point.x > bounds.right()) { bounds.setRight(point.x); }
            if(point.y < bounds.bottom()) { bounds.setBottom(point.y); }
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
            if(bounds.intersects(m.bounds)) {
                for(Vector2 v : m.data) {
                    if(this.intersects(v)) { return true; }
                }
                return false;
            }
            else { return false; } //If the bounds do not intersect, neither do the polygons
        }
        
        public boolean isColliding(MapComponent m) {
            switch(mode) {
                case MODE_INTERSECT:
                    return intersects(m);
                case MODE_NOT_INTERSECT:
                    return !intersects(m);
                default:
                    return intersects(m);
            }
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
    
    public static void test() {
        //MapComponent mc1 = new MapComponent();
        MapComponent mc1 = new MapComponent(new Vector2[]{ new Vector2(0, 0), new Vector2(0, 10), new Vector2(10, 10), new Vector2(10, 0)});
        MapComponent mc2 = new MapComponent(new Vector2[]{ new Vector2(5, 5), new Vector2(5, 15), new Vector2(15, 15), new Vector2(15, 0)});
        System.err.println(mc1.intersects(mc2));
    }
}