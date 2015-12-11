package fettuccine.geom;

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
    
    public final void calculateBounds() {
        System.err.println("---- " + points.length);
        bounds = new Rectangle(points[0], new Vector2(0));
        for(Vector2 v : points) {
            System.err.println("height: " + bounds.height);
            if(v.x < bounds.x) { bounds.setLeft(v.x); }
            if(v.y < bounds.y) { bounds.setTop(v.y); }
            if(v.x > bounds.right()) { bounds.setRight(v.x); }
            if(v.y > bounds.bottom()) { bounds.setBottom(v.y); }
        }
        
    }
    
    public void shift(float dx, float dy) {
        for(Vector2 v : points) {
            v.shift(dx, dy);
        }
        bounds.shift(dx, dy);
    }
    
    public void rotate(float ax, float ay, double degrees) {
        for(Vector2 v : points) {
            v.rotate(ax, ay, degrees);
        }
        calculateBounds();
    }
    
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
    
    public boolean intersects(Polygon test) {        
        if(this.bounds.intersects(test.bounds)) {
            System.err.println("bounds intersect!");
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
            /*float m1, i1, m2, i2;
            int gi1, li1, gi2, li2;
            int spi, spj;
            for(int i = 0; i < points.length; i++) {
                System.err.println("i: " + i);
                
                spi = (i + 1 == points.length) ? 0 : i + 1;
                if(points[i].x < points[spi].x) {
                    li1 = i;
                    gi1 = spi;
                }
                else {
                    li1 = spi;
                    gi1 = i;
                }
                //Vertical line: special case
                if(points[li1].x == points[gi1].x) {                    
                    for(int j = 0; j < test.points.length - 1; j++) {
                        spj = (j + 1 == test.points.length) ? 0 : j + 1;
                        if(test.points[j].x < test.points[spj].x) {
                            li2 = j;
                            gi2 = spj;
                        }
                        else {
                            li2 = spj;
                            gi2 = j;
                        }
                        //Two vertical lines: very special case
                        if(test.points[li2].x == test.points[gi2].x) {
                            //Check to see if vertical lines are in same position
                            if(points[li1] == test.points[li2]) {
                                float ly, gy;
                                if(test.points[li2].y < test.points[gi2].y) {
                                    ly = test.points[li2].y;
                                    gy = test.points[gi2].y;
                                }
                                else {
                                    ly = test.points[gi2].y;
                                    gy = test.points[li2].y;
                                }
                                if((points[li1].y >= ly && points[li1].y <= gy) || (points[gi1].y >= ly && points[gi1].y <= gy)) { return true; }
                            }
                            //Skip rest of loop iteration
                            continue;
                        }
                        m2 = (test.points[gi2].y - test.points[li2].y) / (test.points[gi2].x - test.points[li2].x);
                        i2 = test.points[li2].y - (m2 * test.points[li2].x);
                        
                        float tx = points[li1].x;
                        float ty = (m2 * tx) + i2;
                        if(tx >= test.points[li2].x && tx <= test.points[gi2].x && ty >= test.points[li2].y && ty <= test.points[gi2].y) { return true; }
                    }
                    //Skip rest of loop iteration
                    continue;
                }
                m1 = (points[gi1].y - points[li1].y) / (points[gi1].x - points[li1].x);
                i1 = points[li1].y - (m1 * points[li1].x);
                
                for(int j = 0; j < test.points.length - 1; j++) {
                    spj = (j + 1 == test.points.length) ? 0 : j + 1;
                    if(test.points[j].x < test.points[spj].x) {
                        li2 = j;
                        gi2 = spj;
                    }
                    else {
                        li2 = spj;
                        gi2 = j;
                    }
                    //Vertical lines: special case
                    System.err.println("Vertical line: " + (test.points[li2].x == test.points[gi2].x));
                    if(test.points[li2].x == test.points[gi2].x) {
                        float tx = test.points[li2].x;
                        if(tx >= points[li1].x && tx <= points[gi1].x) { return true; }
                        //Skip rest of loop iteration
                        continue;
                    }
                    m2 = (test.points[gi2].y - test.points[li2].y) / (test.points[gi2].x - test.points[li2].x);
                    i2 = test.points[li2].y - (m2 * test.points[li2].x);
                    
                    float tx = (i2 - i1) / (m1 - m2);
                    float ty = (m1 * tx) + i1;
                    System.err.println("1- first point: " + points[li1].x + " " + points[li1].y);
                    System.err.println("1- second point: " + points[gi1].x + " " + points[gi1].y);
                    System.err.println("2- first point: " + test.points[li2].x + " " + test.points[li2].y);
                    System.err.println("2- second point: " + test.points[gi2].x + " " + test.points[gi2].y);
                    System.err.println(tx >= test.points[li2].x);
                    System.err.println(tx <= test.points[gi2].x);
                    System.err.println("condition: " + ((tx >= points[li1].x) && (tx <= points[gi1].x) && (tx >= test.points[li2].x) && (tx <= test.points[gi2].x)
                    && (ty >= points[li1].y) && (ty <= points[gi1].y) && (ty >= test.points[li2].y) && (ty <= test.points[gi2].y)));
                    System.err.println("Comparing to: " + tx + " " + ty);
                    if((tx >= points[li1].x) && (tx <= points[gi1].x) && (tx >= test.points[li2].x) && (tx <= test.points[gi2].x)
                    && (ty >= points[li1].y) && (ty <= points[gi1].y) && (ty >= test.points[li2].y) && (ty <= test.points[gi2].y)) { return true; }
                }
            }*/
        
    }
    
    public void printBounds() {
        System.err.println("Begin polygon dump");
        System.err.println(bounds.width + " x " + bounds.height + " @ (" + bounds.x + ", " + bounds.y + ")");
        System.err.println();
        for(Vector2 v : points) {
            System.err.println(v.x + " " + v.y);
        }
        System.err.println("---");
    }
}