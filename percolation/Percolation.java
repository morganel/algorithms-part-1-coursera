import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    
    private final WeightedQuickUnionUF uf;
    private final WeightedQuickUnionUF ufVis;
    private final boolean[][] grid;
    private int openSiteCt;
    private final int gridSize;
    
    public Percolation(int n) {
        if (n <= 0) {
            throw new java.lang.IllegalArgumentException("Percolation constructor received n <= 0.");
        }

        // create n-by-n grid, with all sites blocked
        uf = new WeightedQuickUnionUF(n*n + 2);
        ufVis = new WeightedQuickUnionUF(n*n + 2);
        
        for (int i = 1; i <= n; i++) {
            uf.union(0, i);
            ufVis.union(0, i);
        }

        for (int i = 1; i <= n; i++) {
            uf.union(n*(n-1) + i, n*n + 1);
        }
        
        grid = new boolean[n][n];
        openSiteCt = 0;
        gridSize = n;
        
    }
    
    public void open(int row, int col) {   
        if (row > gridSize || col > gridSize || row <= 0 || col <= 0) {
            throw new java.lang.IndexOutOfBoundsException("Cannot open a site");
        }
                
        // open site (row, col) if it is not open already
        if (!isOpen(row, col)) {
            // 1. Open site in grid
            grid[row-1][col-1] = true;
                
            // 2. Connect to neighbors
            // if neighbor is open 
            // make sure neighbor is not a wall
            if (col < gridSize && isOpen(row, col+1)) {
                uf.union(grid2ufCoord(row, col), grid2ufCoord(row, col+1));
                ufVis.union(grid2ufCoord(row, col), grid2ufCoord(row, col+1));
            }
            if (col > 1 && isOpen(row, col-1)) {
                uf.union(grid2ufCoord(row, col), grid2ufCoord(row, col-1));
                ufVis.union(grid2ufCoord(row, col), grid2ufCoord(row, col-1));
            }           
            if (row < gridSize && isOpen(row+1, col)) {
                uf.union(grid2ufCoord(row, col), grid2ufCoord(row+1, col));
                ufVis.union(grid2ufCoord(row, col), grid2ufCoord(row+1, col));
            }            
            if (row > 1 && isOpen(row-1, col)) {
                uf.union(grid2ufCoord(row, col), grid2ufCoord(row-1, col));
                ufVis.union(grid2ufCoord(row, col), grid2ufCoord(row-1, col));
            }            
            // 3. Increment the number of open sites
            openSiteCt++;
        }
    }
    
    private int grid2ufCoord(int row, int col) {
        // converts coordinates in the grid to indices in union find
        return (row-1)*gridSize + col;
    }
    
    public boolean isOpen(int row, int col) {  
        // is site (row, col) open?
        if (row > gridSize || col > gridSize || row <= 0 || col <= 0) {
            throw new java.lang.IndexOutOfBoundsException("isOpen: Cannot check if site is open");
        }
        return grid[row-1][col-1];
    }
    
    public boolean isFull(int row, int col) { 
        if (row > gridSize || col > gridSize || row <= 0 || col <= 0) {
            throw new java.lang.IndexOutOfBoundsException("isFull: Cannot check if site is full");
        }
        // is site (row, col) full?
        // is it connected to the first row i.e. the node on top of it?
        return isOpen(row, col) && ufVis.connected(0, grid2ufCoord(row, col));
    }
    
    public int numberOfOpenSites()  {     // number of open sites
        return openSiteCt;
    }
    
    public boolean percolates() {             // does the system percolate?
        if (gridSize == 1) {
            return isOpen(1, 1);
        } 
        else {
            return uf.connected(0, gridSize*gridSize+1);
        }
    }

    public static void main(String[] args) {  // test client (optional)
        Percolation p = new Percolation(2);
        System.out.println(p.grid[0][0]);
        System.out.println(p.percolates());

        System.out.println("full1: " + p.isFull(1, 1));
        p.open(1, 1);
        
        System.out.println("percolates1: " +p.percolates());
        System.out.println("full2: " +p.isFull(1, 1));
        System.out.println("full3: " +p.isFull(2, 1));

        p.open(2, 1);
        
        System.out.println("percolates2: " +p.percolates());
        System.out.println("full4: " +p.isFull(2, 1));
        
        
        /* for (int i = 1; i <= 4; i++) {
            for (int j = 1; j <= 4; j++) {
                System.out.println(i);
                System.out.println(j);
                p.open(i, j);
            }
        } */        // System.out.println(p.grid[0][0]); 
        System.out.println(p.numberOfOpenSites()); 
        System.out.println(p.gridSize);
        System.out.println(p.percolates());
        System.out.println(p.isFull(1, 1));   
        System.out.println(p.isFull(4, 1));  
    }
    
}

