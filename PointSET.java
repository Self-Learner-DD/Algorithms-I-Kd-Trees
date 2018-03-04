import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.SET;

public class PointSET 
{
    private SET<Point2D> set;
    
    public PointSET()                               // construct an empty set of points 
    { set = new SET<Point2D>(); }
    
    public boolean isEmpty()                      // is the set empty? 
    { return set.size() == 0; }
    
    public               int size()                         // number of points in the set 
    { return set.size(); }
    
    public void insert(Point2D p)              // add the point to the set (if it is not already in the set)
    { 
        assertPoint(p);
        set.add(p); 
    }
    
    public boolean contains(Point2D p)            // does the set contain point p? 
    { 
        assertPoint(p);
        return set.contains(p);
    }
    
    public void draw()                         // draw all points to standard draw 
    { 
        for (Point2D p : set)
        p.draw();
    }
    public Iterable<Point2D> range(RectHV rect)             // all points that are inside the rectangle (or on the boundary) 
    {
        assertRect(rect);
        Queue<Point2D> inset = new Queue<Point2D>();
        for (Point2D p : set)
        {
            if (rect.contains(p)) inset.enqueue(p);
        }
        return inset;
    }
    
    public Point2D nearest(Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty 
    {
        assertPoint(p);
        if (set.isEmpty()) return null;
        double smallD = Double.POSITIVE_INFINITY;
        Point2D nearest = p;
        for (Point2D q : set)
        {
            double distance = p.distanceSquaredTo(q);
            if (distance < smallD) { smallD = distance; nearest = q; }
        }
        return nearest;
    }
    
    private void assertPoint(Point2D p)
    { if ( p == null) throw new IllegalArgumentException("Point cannot be null"); }
    
    private void assertRect(RectHV rect)
    { if ( rect == null) throw new IllegalArgumentException("Rect cannot be null"); }
        
}