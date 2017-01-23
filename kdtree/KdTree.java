import edu.princeton.cs.algs4.Point2D; 
import edu.princeton.cs.algs4.StdDraw; 
import edu.princeton.cs.algs4.RectHV; 
import java.util.List;
import java.util.ArrayList;

public class KdTree {
    
    private static class Node {
        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree
        
        public Node(Point2D pt) {
            this.p = pt;
        }
    }
    
    private Node root;
    private int size;
    
    public KdTree()   {                            
        // construct an empty set of points 
        // root = new Node(null);
        // size = 0;
    }
    
    public boolean isEmpty()  {                    
        // is the set empty? 
        return root == null;
    }
    
    public int size() {                        
        // number of points in the set 
        return size;
    }
    
    public void insert(Point2D p) {             
    // add the point to the set (if it is not already in the set)
        if (p == null) throw new java.lang.NullPointerException("cannot insert null point");
        root = insert(root, p, null, 0, 0);
    }
    
    private Node insert(Node n, Point2D p, Node parent, int level, int side) {
                
        if (n == null) { 
            Node newNode = new Node(p);
            if (side == 0) {
                newNode.rect = new RectHV(0., 0., 1., 1.);
            }
            else {
                if ((level % 2) != 0) { // horizontal line, we'll change x and keep y of parent
                    if (side == -1) { // minx of parent to x of parent
                        newNode.rect = new RectHV(parent.rect.xmin(), parent.rect.ymin(), parent.p.x(), parent.rect.ymax());
                    }
                    else { // x of parent to maxx of parent 
                        newNode.rect = new RectHV(parent.p.x(), parent.rect.ymin(), parent.rect.xmax(), parent.rect.ymax());
                    }   
                }
                else { // vertical line, we'll change y and keep x of parent
                    if (side == -1) { // miny of parent to y of parent
                        newNode.rect = new RectHV(parent.rect.xmin(), parent.rect.ymin(), parent.rect.xmax(), parent.p.y());
                    }
                    else { // y of parent to maxy of parent 
                        newNode.rect = new RectHV(parent.rect.xmin(), parent.p.y(), parent.rect.xmax(), parent.rect.ymax());
                    }   
                }
            }
            size += 1;
            return newNode;
        }
        
        if (p == null) throw new java.lang.NullPointerException("cannot insert null point");
        
        double cmp = 0.;
        if ((level % 2) == 0) cmp = p.x() - n.p.x();
        else cmp = p.y() - n.p.y();
        
        if (cmp < 0) n.lb = insert(n.lb, p, n, level + 1, -1); 
        else if (cmp > 0) n.rt = insert(n.rt, p, n, level + 1, 1);
        else if (cmp == 0 && !p.equals(n.p)) n.rt = insert(n.rt, p, n, level + 1, 1);
        return n;
    }
    
    
    private Node get(Point2D p) {
        return get(root, p, 0);
    }
    
    private Node get(Node n, Point2D p, int level) {
        if (n == null) return null;
        
        double cmp = 0.;
        if ((level % 2) == 0) cmp = p.x() - n.p.x();
        else cmp = p.y() - n.p.y();
        
        if (cmp < 0) return get(n.lb, p, level + 1);
        if (cmp >= 0 && !p.equals(n.p)) return get(n.rt, p, level + 1);
        else return n;
    }
    
    public boolean contains(Point2D p)   {         
        if (p == null) {
            throw new java.lang.NullPointerException("cannot check if contain null point");
        }

        // does the set contain point p? 
        return get(p) != null;
    }
    
    public void draw() { 
        draw(root, 0);
    }
    
    private void draw(Node n, int level) {                        
        // draw all points to standard draw
        if (n != null) {
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.05);
            n.p.draw();
            StdDraw.setPenRadius(0.01);
        
            if ((level % 2) == 0) {
                StdDraw.setPenColor(StdDraw.RED); // vertical line
                StdDraw.line(n.p.x(), n.rect.ymin(), n.p.x(), n.rect.ymax());
            }
            else {
                StdDraw.setPenColor(StdDraw.BLUE); // horizontal line
                StdDraw.line(n.rect.xmin(), n.p.y(), n.rect.xmax(), n.p.y());
            }
            draw(n.lb, level + 1);
            draw(n.rt, level + 1);
        }
    }
    
    private void range(Node n, RectHV rect, List<Point2D> rangeMatches) {
        if (rect.contains(n.p)) rangeMatches.add(n.p);
        if (n.rt != null) {
            if (rect.intersects(n.rt.rect)) {
                range(n.rt, rect, rangeMatches);
            }  
        }
        if (n.lb != null) {
            if (rect.intersects(n.lb.rect)) {
                range(n.lb, rect, rangeMatches);
            }  
        }
    }
    
    public Iterable<Point2D> range(RectHV rect)  {           
        // all points that are inside the rectangle 
        List<Point2D> rangeMatches = new ArrayList<Point2D>();
        if (rect == null) {
            throw new java.lang.NullPointerException("cannot get range of null rectangle");
        }
        if (isEmpty()) return new ArrayList<Point2D>();
        range(root, rect, rangeMatches);
        return rangeMatches;
    }
    
    public Point2D nearest(Point2D p)  {           
        // a nearest neighbor in the set to point p; null if the set is empty 
        if (p == null) {
            throw new java.lang.NullPointerException("cannot get nearest of null point");
        }
        
        if (isEmpty()) return null;
        double minDist = Double.POSITIVE_INFINITY;
        
        return nearest(p, root, Double.POSITIVE_INFINITY, root.p, 0);

    }
    
    private Point2D nearest(Point2D p, Node n, double minDist, Point2D nearest, int level) {
        
        // current distance:
        
        if (n == null) return null;
        if (n.rect.distanceTo(p) >= minDist) return null;
        
        Point2D closestPoint = nearest;
        double closestDistance = minDist;
        
        Point2D currentPoint = n.p;
        double d = currentPoint.distanceTo(p);
        if (d < minDist) {
            closestDistance = d;
            closestPoint = currentPoint;
        }
       
        double cmp = 0.;
        if ((level % 2) == 0) cmp = p.x() - n.p.x();
        else cmp = p.y() - n.p.y();

        // System.out.println("cmp:" + cmp);
        
        if (cmp < 0) { 
            currentPoint = nearest(p, n.lb, closestDistance, closestPoint, level + 1);
                   
            if (currentPoint != null) {
                if (currentPoint != closestPoint) {
                    closestDistance = currentPoint.distanceTo(p);
                    closestPoint = currentPoint;
                }
            }
            
            if (n.rt != null && n.rt.rect.distanceTo(p) < closestDistance) {
                currentPoint = nearest(p, n.rt, closestDistance, closestPoint, level + 1);
            }
        }
        
        if (cmp >= 0) { 
            currentPoint = nearest(p, n.rt, closestDistance, closestPoint, level + 1);
            if (currentPoint != null) {
                if (currentPoint != closestPoint) {
                    closestDistance = currentPoint.distanceTo(p);
                    closestPoint = currentPoint;
                }
            }
            if (n.lb != null && n.lb.rect.distanceTo(p) < closestDistance) {
                currentPoint = nearest(p, n.lb, closestDistance, closestPoint, level + 1);
            }
        }
        
        if (currentPoint != null) {
            closestPoint = currentPoint;
        }
        
        return closestPoint;
    }

    public static void main(String[] args)  {                
        // unit testing of the methods (optional) 
        KdTree kdtree = new KdTree();

        double x = 0.1;
        double y = 0.2;
        // StdOut.printf("%8.6f %8.6f\n", x, y);
        Point2D p = new Point2D(x, y);
        kdtree.insert(p);        
        p = new Point2D(0.1, 0.3);
        
        kdtree.insert(p);
        Point2D p2 = new Point2D(0.2, 0.3);
        System.out.println(kdtree.contains(p2));
        System.out.println(kdtree.contains(p));
        
        KdTree kdtree2 = new KdTree();
        System.out.println(kdtree2.contains(p));
        RectHV rect = new RectHV(0., 0., 1., 1.);
        kdtree2.range(rect);
        
    }
}


// checkstyle-algs4 PointSET.java
// checkstyle-algs4 KdTree.java
// findbugs-algs4 PointSET.class
// findbugs-algs4 KdTree.class