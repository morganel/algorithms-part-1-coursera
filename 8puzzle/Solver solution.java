import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.MinPQ;
// import edu.princeton.cs.algs4.Deque;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Deque;

public class Solver {
    
    private Node solutionNode;
    private Node twinSolutionNode;
    
    private class Node implements Comparable<Node> {
        private Board board;
        private int priority;
        private int d_manhattan;
        private int d_moves;
        private Node previousNode;
        
        
        public Node(Board board, Node previousNode) {
            this.board = board;
            this.d_manhattan = board.manhattan();
            this.previousNode = previousNode;
            if (previousNode == null) {
                this.d_moves = 0;
            } else {
                this.d_moves = previousNode.d_moves + 1;
            }
            
            this.priority = this.d_manhattan + this.d_moves;
        }
        
        public int compareTo(Node that) {
            if (this.priority > that.priority) return 1;
            if (this.priority < that.priority) return -1;
            return 0;
        }
        
    }
    
    public Solver(Board initial) {
        // find a solution to the initial board (using the A* algorithm)
        if (initial.isGoal()) {
            solutionNode = new Node(initial, null);
        }
        else {
            MinPQ<Node> PQ = new MinPQ<Node>();
            MinPQ<Node> twinPQ = new MinPQ<Node>();
            Node node = new Node(initial, null);
            Node twinNode = new Node(initial.twin(), null);
            PQ.insert(node);
            twinPQ.insert(twinNode);
            while (true) {
                // 
                twinSolutionNode = makeStep(twinPQ);
                if (twinSolutionNode.board.isGoal()) {
                    solutionNode = null;
                    return;
                }
                
                solutionNode = makeStep(PQ);   
                if (solutionNode.board.isGoal()) {
                    return;
                }               
            }           
        }       
    }
    
    private Node makeStep(MinPQ<Node> minPQ) {
        Node minNode = minPQ.delMin();
        addNeighbors(minNode, minPQ);
        return minNode;
    }
    
    private void addNeighbors(Node node, MinPQ<Node> minPQ) {
        for (Board neighbor: node.board.neighbors()) {
            if (node.previousNode == null || !neighbor.equals(node.previousNode.board)) {
                minPQ.insert(new Node(neighbor, node));
            }          
        }
    }
    
    public boolean isSolvable() {
        // is the initial board solvable?
        return solutionNode != null;
    }
    
    public int moves() {
        // min number of moves to solve initial board; -1 if no solution
        if (isSolvable()) return solutionNode.d_moves;
        return -1;
    }
    
    public Iterable<Board> solution() {
        // sequence of boards in a shortest solution; null if no solution           
        if (!isSolvable()) return null;
        Deque<Board> solutionBoards = new ArrayDeque<Board>();

        Node n = solutionNode;
        while (n != null) {
            solutionBoards.push(n.board);
            //StdOut.println(n.board);
            n = n.previousNode;
        }
        return solutionBoards;
    }
    
    public static void main(String[] args) {
        
        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
            blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        
        // solve the puzzle
        Solver solver = new Solver(initial);
        
        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution()) {
                StdOut.println(board);
            }
        }
    }
    
}