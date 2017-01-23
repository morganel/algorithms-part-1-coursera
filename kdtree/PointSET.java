import java.util.TreeSet;
import edu.princeton.cs.algs4.Point2D; 
import edu.princeton.cs.algs4.RectHV; 
import java.util.List;
import java.util.ArrayList;

public class PointSET {
    
    private TreeSet<Point2D> treeSet;
    
    public PointSET()   {                            
        // construct an empty set of points 
        treeSet = new TreeSet<Point2D>();
    }
    
    public boolean isEmpty()  {                    
        // is the set empty? 
        return treeSet.isEmpty();
    }
    
    public int size() {                        
        // number of points in the set 
        return treeSet.size();
    }
    
    public void insert(Point2D p) {             
    // add the point to the set (if it is not already in the set)
        if (p == null) {
            throw new java.lang.NullPointerException("cannot insert null point");
        }
        treeSet.add(p);
    }
    
    public boolean contains(Point2D p)   {         
        if (p == null) {
            throw new java.lang.NullPointerException("cannot check if contain null point");
        }

        // does the set contain point p? 
        return treeSet.contains(p);
    }
    
    public void draw() {                        
        // draw all points to standard draw
        for (Point2D p : treeSet) {
            p.draw();
        }
    }
    
    public Iterable<Point2D> range(RectHV rect)  {           
        // all points that are inside the rectangle 
        List<Point2D> rangeMatches = new ArrayList<Point2D>();
        if (rect == null) {
            throw new java.lang.NullPointerException("cannot get range of null rectangle");
        }
        for (Point2D p : treeSet) {
            if (rect.contains(p)) {
                rangeMatches.add(p);
            }
        }
        return rangeMatches;
    }
    
    public Point2D nearest(Point2D p)  {           
        // a nearest neighbor in the set to point p; null if the set is empty 
        if (p == null) {
            throw new java.lang.NullPointerException("cannot get nearest of null point");
        }
        if (isEmpty()) {
            return null;
        }
        
        double minDist = Double.POSITIVE_INFINITY;
        Point2D nearestNeighbor = treeSet.first();
        for (Point2D ps : treeSet) {
            double d = ps.distanceTo(p);
            if (d < minDist) {
                minDist = d;
                nearestNeighbor = ps;
            }
        }       
        return nearestNeighbor;
    }

    public static void main(String[] args)  {                
        // unit testing of the methods (optional) 
        
        PointSET p = new PointSET();
        System.out.println(p.size());
        Point2D pt = new Point2D(0.5, 0.5);
        System.out.println(p.nearest(pt));
        
    }
}