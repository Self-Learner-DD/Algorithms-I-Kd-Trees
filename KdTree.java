import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree
{
    private Node root;
    
    private static class Node
    {
        Point2D p;
        RectHV rect;
        int size;
        Node lb;
        Node rt;
           
        public Node(Point2D p, RectHV rect, int size)
        { 
            this.p = p; this.rect = rect; this.size = size;
        }
    }
    
    public boolean isEmpty()
    { return root == null; }
    
    public int size()
    { return size(root); }
    
    private int size(Node x)
    { 
        if (x == null) return 0;
        else return x.size;
    }
    
    public void insert(Point2D p)
    { 
        assertPoint(p);
        if (root == null) root = new Node(p, new RectHV(0, 0, 1, 1), 1);
        else 
        {
            double xmin = root.rect.xmin();
            double xmax = root.rect.xmax();
            double ymin = root.rect.ymin();
            double ymax = root.rect.ymax();
            root = insertV(root, p, xmin, ymin, xmax, ymax);
        }
    }

    private Node insertV(Node x, Point2D p, double xmin, double ymin, double xmax, double ymax)//use argument of rect instead of rect to avoid multiple call of rect.xmin()...
    {
        if (x == null) return new Node(p, new RectHV(xmin, ymin, xmax, ymax), 1);
        if (x.p.equals(p)) return x;
        int cmp = Point2D.X_ORDER.compare(p, x.p);
        if (cmp < 0) x.lb = insertH(x.lb, p, xmin, ymin, x.p.x(), ymax);
        else x.rt = insertH(x.rt, p, x.p.x(), ymin, xmax, ymax);
        x.size = 1 + size(x.lb) + size(x.rt);
        return x;
    }
    
    private Node insertH(Node x, Point2D p, double xmin, double ymin, double xmax, double ymax)
    {
        if (x == null) return new Node(p, new RectHV(xmin, ymin, xmax, ymax), 1);
        if (x.p.equals(p)) return x;
        int cmp = Point2D.Y_ORDER.compare(p, x.p);
        if (cmp < 0) x.lb = insertV(x.lb, p, xmin, ymin, xmax, x.p.y());
        else x.rt = insertV(x.rt, p, xmin, x.p.y(), xmax, ymax);
        x.size = 1 + size(x.lb) + size(x.rt);
        return x;
    }
    
    public boolean contains(Point2D p)
    { 
        assertPoint(p);
        return contains(root, p, true);
    }
    
    private boolean contains(Node x, Point2D p, boolean vert)
    {
        if (x == null) return false;
        if (x.p.equals(p)) return true;
        int cmp;
        if (vert) cmp = Point2D.X_ORDER.compare(p, x.p);
        else cmp = Point2D.Y_ORDER.compare(p, x.p);
        if (cmp < 0) return contains(x.lb, p, !vert);
        else return contains(x.rt, p, !vert);
    }
    
    public void draw()
    {
        if (isEmpty()) return;
        draw(root, true);
    }
    
    private void draw(Node x, boolean vert)
    {
        if (x.lb != null) draw(x.lb, !vert);
        if (x.rt != null) draw(x.rt, !vert);
        
        x.p.draw();
        
        double xmin, ymin, xmax, ymax;
        if (vert)
        {
            StdDraw.setPenColor(StdDraw.RED);
            xmin = x.p.x();
            xmax = x.p.x();
            ymin = x.rect.ymin();
            ymax = x.rect.ymax();
        }
        else
        {
            StdDraw.setPenColor(StdDraw.BLUE);
            xmin = x.rect.xmin();
            xmax = x.rect.xmax();
            ymin = x.p.y();
            ymax = x.p.y();
        }
        StdDraw.line(xmin, ymin, xmax, ymax);
    }
    
    public Iterable<Point2D> range(RectHV rect) 
    {
        assertRect(rect);
        Queue<Point2D> queue = new Queue<Point2D>();
        range(root, rect, queue);
        return queue;
    }
    
    private void range(Node x, RectHV rect, Queue<Point2D> queue)
    {
        if (x == null) return;
        if (!rect.intersects(x.rect)) return;
        if (rect.contains(x.p)) queue.enqueue(x.p);
        range(x.lb, rect, queue);
        range(x.rt, rect, queue);
    }
    
    public Point2D nearest(Point2D p) 
    {
        assertPoint(p);
        if (isEmpty()) return null;
        Point2D min = root.p;
        return nearest(root, p, min, true);   
    }
    
    private Point2D nearest(Node x, Point2D p, Point2D min, boolean vert)
    {
        if (x == null) return min;
        if (x.p.equals(p)) return x.p;
        if (x.p.distanceSquaredTo(p) < p.distanceSquaredTo(min)) { min = x.p; }
        int cmp;
        if (vert) cmp = Point2D.X_ORDER.compare(p, x.p);
        else cmp = Point2D.Y_ORDER.compare(p, x.p);
        
        if (cmp < 0) 
        {
            min = nearest(x.lb, p, min, !vert);
            if (x.rt != null && min.distanceSquaredTo(p) > x.rt.rect.distanceSquaredTo(p))
                min = nearest(x.rt, p, min, !vert);
        }
        else
        {
            min = nearest(x.rt, p, min, !vert);
            if(x.lb != null && min.distanceSquaredTo(p) > x.lb.rect.distanceSquaredTo(p))
                min = nearest(x.lb, p, min, !vert);
        }
        return min;
    }
    
    private void assertPoint(Point2D p)
    { if ( p == null) throw new IllegalArgumentException("Point cannot be null"); }
    
    private void assertRect(RectHV rect)
    { if ( rect == null) throw new IllegalArgumentException("Rect cannot be null"); }
}
                
        
        
        
        
    