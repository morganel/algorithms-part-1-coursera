import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.MinPQ;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;

public class Solver {

    private class SearchNode implements Comparable<SearchNode> {
        private Board board;
        private SearchNode parentSearchNode;
        private int manhattan;
        private int moves = 0;
        private int priority;
        
        public SearchNode(Board board, SearchNode parentSearchNode) {
            this.board = board;
            this.parentSearchNode = parentSearchNode;
            this.manhattan = board.manhattan();
            if (parentSearchNode != null) {
                this.moves = parentSearchNode.moves + 1;
            } 
            this.priority = this.moves + this.manhattan;
        }
                
        public int compareTo(SearchNode other) {
            return Integer.valueOf(this.priority).compareTo(Integer.valueOf(other.priority));
        }
    }
    
    private SearchNode solutionNode;
    private SearchNode solutionNodeTwin;
        
    public Solver(Board initial) {          // find a solution to the initial board (using the A* algorithm)
        if (initial == null) {
            throw new java.lang.NullPointerException("Board input to solver cannot be null");
        }
        if (initial.isGoal()) {
            solutionNode = new SearchNode(initial, null);
        } else {
        
            MinPQ<SearchNode> minPQ = new MinPQ<SearchNode>();
            MinPQ<SearchNode> minPQTwin = new MinPQ<SearchNode>();
                        
            SearchNode initialSearchNodeTwin = new SearchNode(initial.twin(), null);
            SearchNode initialSearchNode = new SearchNode(initial, null);
            
            minPQ.insert(initialSearchNode);
            minPQTwin.insert(initialSearchNodeTwin);
            
            while (true) {
                
                SearchNode dequeuedSearchNode = minPQ.delMin();
                // System.out.println(b.toString());
                if (dequeuedSearchNode.board.isGoal()) {
                    // System.out.println("GOAL!");
                    solutionNode = dequeuedSearchNode;
                    return; 
                } 
                else {
                    for (Board ng: dequeuedSearchNode.board.neighbors()) {
                        if (dequeuedSearchNode.parentSearchNode == null || !ng.equals(dequeuedSearchNode.parentSearchNode.board)) {
                            minPQ.insert(new SearchNode(ng, dequeuedSearchNode));
                        }
                    }
                }
                
            //////////////////////////////////
            //////////////////////////////////
            if (!minPQTwin.isEmpty()) {
                dequeuedSearchNode = minPQTwin.delMin();
                // System.out.println(b.toString());
                if (dequeuedSearchNode.board.isGoal()) {
                    // System.out.println("GOAL TWIN!");
                    solutionNode = null;
                    return; 
                } 
                else {
                    for (Board ng: dequeuedSearchNode.board.neighbors()) {
                        if (dequeuedSearchNode.parentSearchNode == null || !ng.equals(dequeuedSearchNode.parentSearchNode.board)) {
                            minPQTwin.insert(new SearchNode(ng, dequeuedSearchNode));
                        }
                    }
                }
            }
            
        }
        }
    }
    
    public boolean isSolvable() {           // is the initial board solvable?
        return solutionNode != null;
    }
    
    public int moves()   {                  // min number of moves to solve initial board; -1 if unsolvable
        if (isSolvable()) {
            return this.solutionNode.moves;
        } 
        else {
            return -1;
        }      
    }
    
    public Iterable<Board> solution() {     // sequence of boards in a shortest solution; null if unsolvable
        if (isSolvable()) {
            List<Board> solution = new ArrayList<Board>();
            SearchNode sn = this.solutionNode;
            while (sn != null) {
                solution.add(sn.board);
                sn = sn.parentSearchNode;
            }
            Collections.reverse(solution);
            return solution;
        } 
        else {
            return null;
        }
    }
    
    public static void main(String[] args) {
        
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
            blocks[i][j] = in.readInt();
        
        /*
        int[][] blocks = new int[2][2];
        blocks[0][0] = 1;
        blocks[0][1] = 2;
        blocks[1][0] = 3;
        blocks[1][1] = 0;
        */
        
        Board initial = new Board(blocks);
        
        // solve the puzzle
        Solver solver = new Solver(initial);
        
        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
    // solve a slider puzzle (given below)
}

