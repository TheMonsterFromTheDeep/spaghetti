package fettuccine.geom;

/**
 * A Polygon simply represents a collection of 2D points in 2D space, and contains
 * methods for manipulating and testing them.
 * 
 * @author TheMonsterFromTheDeep
 */
public class Polygon {
    Vector2[] points;
    Rectangle bounds;
    
    public Polygon() {
        points = new Vector2[0];
        bounds = new Rectangle(0);
    }
    
    public Polygon(Vector2[] points) {
        this.points = points;
        calculateBounds();
    }
    
    /**
     * Causes the Polygon to re-calculate its bounds. This should be done
     * after any operation that would change the bounds of the Polygon.<br /><br />
     * 
     * Operations can manually change the bounds instead, so that this
     * method does not have to be run.<br /><br />
     * 
     * Bounds are used to determine things like whether Polygons intersect, and as
     * such, bounds should always be consistent with the Polygon's geometry.
     */
    public final void calculateBounds() {
        bounds = new Rectangle(points[0], new Vector2(0));
        for(Vector2 v : points) {
            if(v.x < bounds.x) { bounds.setLeft(v.x); }
            if(v.y < bounds.y) { bounds.setTop(v.y); }
            if(v.x > bounds.right()) { bounds.setRight(v.x); }
            if(v.y > bounds.bottom()) { bounds.setBottom(v.y); }
        }
        
    }
    
    /**
     * Shifts the Polygon by the specified amount. This will also
     * shift the bounds Rectangle by the same amount.
     * 
     * @param dx The distance to shift the Polygon along the x axis
     * @param dy The distance to shift the Polygon along the y axis
     */
    public void shift(float dx, float dy) {
        for(Vector2 v : points) {
            v.shift(dx, dy);
        }
        bounds.shift(dx, dy);
    }
    
    /**
     * Rotates the Polygon by a specified amount around an anchor
     * point.<br /><br />
     * 
     * Bounds are re-calculated during this method call.
     * @param ax The x position of the anchor point.
     * @param ay The y position of the anchor point.
     * @param degrees The amount of degrees to rotate the Polygon.
     */
    public void rotate(float ax, float ay, double degrees) {
        for(Vector2 v : points) {
            v.rotate(ax, ay, degrees);
        }
        calculateBounds();
    }
    
    /**
     * Copies the Polygon - all its points, and its bounds.
     * @return A Polygon with identical data to this one.
     */
    public Polygon getCopy() {
        Polygon copy = new Polygon();
        copy.points = new Vector2[points.length];
        System.arraycopy(points,0,copy.points,0,points.length);
        copy.calculateBounds();
        return copy;
    }
    
    private abstract class IntersectTest {
        /** true if vertical, false otherwise */
        boolean isVertical;
        
        public abstract boolean testLine(IntersectLine l);
        public abstract boolean testVert(IntersectVert v);
    }
    
    private class IntersectLine extends IntersectTest {
        public float sx, sy, ex, ey, m, yi;
        
        public IntersectLine(Vector2 p1, Vector2 p2) {
            isVertical = false;
            
            if(p1.x < p2.x) {
                sx = p1.x;
                ex = p2.x;
                sy = p1.y;
                ey = p2.y;
            }
            else {
                sx = p2.x;
                ex = p1.x;
                sy = p2.y;
                ey = p1.y;
            }
            m = (ey - sy) / (ex - sx);
            yi = sy - (m * sx);
            if(ey < sy) {
                float tmp = sy;
                sy = ey;
                ey = tmp;
            }
        }
        
        @Override
        public boolean testLine(IntersectLine test) {
            float tx = (test.yi - this.yi) / (this.m - test.m);
            float ty = (m * tx) + yi;
            return (sx <= tx && ex >= tx && sy <= ty && ey >= ty && test.sx <= tx && test.ex >= tx && test.sy <= ty && test.ey >= ty);
        }

        @Override
        public boolean testVert(IntersectVert test) {           
            if(test.x >= sx && test.x <= ex) {
                float ty = (m * test.x) + yi;
                return (sy <= ty && ey >= ty && test.sy <= ty && test.ey >= ty);
            }
            return false;
        }
        
    }
    
    private class IntersectVert extends IntersectTest {

        public float x, sy, ey;
        
        public IntersectVert(Vector2 p1, Vector2 p2) {
            isVertical = true;
            
            x = p1.x;
            if(p1.y < p2.y) {
                sy = p1.y;
                ey = p2.y;
            }
            else {
                sy = p2.y;
                ey = p1.y;
            }
        }
        
        @Override
        public boolean testLine(IntersectLine test) {
            if(x >= test.sx && x <= test.ex) {
                float ty = (test.m * x) + test.yi;
                return (sy <= ty && ey >= ty && test.sy <= ty && test.ey >= ty);
            }
            return false;
        }

        @Override
        public boolean testVert(IntersectVert test) {
            if(test.x == this.x) {
                return !(test.sy > this.ey || this.sy > test.ey);
            }
            return false;
        }
        
    }
    
    /**
     * Tests whether the Polygon contains a specific point.
     * @param v The point to test for.
     * @return Whether the Polygon contains the point.
     */
    public boolean contains(Vector2 v) {
            //Thanks to http://stackoverflow.com/questions/217578/how-can-i-determine-whether-a-2d-point-is-within-a-polygon#answer-2922778
            int i, j;
            boolean c = false;
            for (i = 0, j = points.length - 1; i < points.length; j = i++) {
              if ( ((points[i].y > v.y) != (points[j].y > v.y)) &&
                (v.x < (points[j].x-points[i].x) * (v.y-points[i].y) / (points[j].y-points[i].y) + points[i].x) )
                { c = !c; }
            }
            return c;
        }
    
    /**
     * Calculates whether this Polygon intersects the specified Polygon to test.<br /><br />
     * 
     * The Polygons count as intersecting if any of the edges intersect, or if one Polygon is
     * contained completely by the other Polygon.
     * @param test The Polygon to test intersection with.
     * @return Whether the Polygons intersect (as described above).
     */
    public boolean intersects(Polygon test) {        
        if(this.bounds.intersects(test.bounds)) {
            IntersectTest[] thisTest = new IntersectTest[points.length];
            IntersectTest[] otherTest = new IntersectTest[test.points.length];
            
            int ti;
            
            for(int i = 0; i < points.length; i++) {
                ti = (i + 1 == points.length) ? 0 : i + 1;
                if(Math.abs(points[i].x - points[ti].x) < 0.01) { 
                    thisTest[i] = new IntersectVert(points[i], points[ti]); 
                }
                else {
                    thisTest[i] = new IntersectLine(points[i], points[ti]); 
                }
            }
            for(int i = 0; i < test.points.length; i++) {
                ti = (i + 1 == test.points.length) ? 0 : i + 1;
                if(Math.abs(test.points[i].x - test.points[ti].x) < 0.01) { 
                    otherTest[i] = new IntersectVert(test.points[i], test.points[ti]); 
                }
                else {
                    otherTest[i] = new IntersectLine(test.points[i], test.points[ti]); 
                }
            }
            
            for(IntersectTest it1 : thisTest) {
                for(IntersectTest it2 : otherTest) {
                    if(it2.isVertical) {
                        if(it1.testVert((IntersectVert)it2)) { return true; }
                    }
                    else {
                        if(it1.testLine((IntersectLine)it2)) { return true; }
                    }
                }
            }
            
            for(Vector2 v : test.points) {
                if(this.contains(v)) { return true; }
            }
            for(Vector2 v : points) {
                if(test.contains(v)) { return true; }
            }
        }
        return false;        
    }
}