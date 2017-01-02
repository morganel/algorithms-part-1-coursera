import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    
    private final double[] trialsStat; 
    private final int gridSize;
    
    public PercolationStats(int n, int trials) {   // perform trials independent experiments on an n-by-n grid
        if (n <= 0 || trials <= 0) {
            throw new java.lang.IllegalArgumentException("PercolationStats constructor received n <= 0.");
        }
        
        gridSize = n;
        trialsStat = new double[trials];
        int o = 0;
        for (int t = 0; t < trials; t++) {
            // performs 1 trial
            Percolation p = new Percolation(n);

            while (!p.percolates()) {
                // System.out.println(p.percolates());
                int siteToOpen = StdRandom.uniform(n*n);
                
                // System.out.println(siteToOpen);
                if (!p.isOpen(index2rowGrid(siteToOpen), index2colGrid(siteToOpen))) {
                    o++;
                    p.open(index2rowGrid(siteToOpen), index2colGrid(siteToOpen));
                }
            }
            trialsStat[t] = p.numberOfOpenSites(); // number of open sites
        }
        // System.out.println("o:" + o + "/ new o:" + (double) o / (n*n*trials));
               
    }
    
    private int index2rowGrid(int idx) {
        return idx / gridSize + 1;
    }
    
    private int index2colGrid(int idx) {
        return idx % gridSize + 1;
    }
    
    public double mean()  { 
        // System.out.println("mean print:" + (float) StdStats.mean(trialsStat) / (gridSize*gridSize));
        double d = StdStats.mean(trialsStat) / (gridSize*gridSize);
        return d; 
    }
    
    public double stddev() {                       // sample standard deviation of percolation threshold
        // System.out.println((float) StdStats.stddev(trialsStat) / (gridSize*gridSize));
        double d = StdStats.stddev(trialsStat) / (gridSize*gridSize);
        return d;
    }
        
    public double confidenceLo() {                 // low  endpoint of 95% confidence interval
        return mean()- 1.96*stddev()/Math.sqrt(trialsStat.length);
    }
    
    public double confidenceHi() {                 // high endpoint of 95% confidence interval
        return mean() + 1.96*stddev()/Math.sqrt(trialsStat.length);
    }
    
    public static void main(String[] args) {       // test client (described below)
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]); 
        // int n = 20;
        // int trials = 10;
        PercolationStats ps = new PercolationStats(n, trials);
        System.out.println("Mean = " + ps.mean());
        System.out.println("Std = " + ps.stddev());
        System.out.println("95% confidence interval = " + ps.confidenceLo() + "," + ps.confidenceHi());
        
    }
}