import java.util.List;
import java.util.ArrayList;
// import org.junit.Assert;

public class Board {
    
    private final int[][] board;
    private final int n;
    
    public Board(int[][] blocks)   {        
        // construct a board from an n-by-n array of blocks
        // (where blocks[i][j] = block in row i, column j)
        n = blocks.length;
        board = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                board[i][j] = blocks[i][j];
            }
        }
    }
    
    public int dimension()  {               // board dimension n
        return n;
    }
    
    private int goalCellValue(int i, int j) {
        // returns what should be the number in each cell
        if (i == n-1 && j == n-1) {
            return 0;
        }
        else {
            return i*n + j + 1;
        }
    }
    
    private int valueToCell(int value, int coordinate) {
        if (coordinate == 0) { // x-axis
            return (value - 1) / n;
        }
        return (value - 1) % n;
    }
    
    public int hamming() {                  // number of blocks out of place
        int d = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                // if (board[i][j] != goalCellValue(i,j)) {
                //    d++;
                // }
                if (board[i][j] > 0 && (i != valueToCell(board[i][j], 0) || j != valueToCell(board[i][j], 1))) {
                    d++;
                }
            }
        }
        return d;
    }
    
    public int manhattan()  {               // sum of Manhattan distances between blocks and goal
        int d = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] > 0) {
                    d += Math.abs(i - valueToCell(board[i][j], 0));
                    d += Math.abs(j - valueToCell(board[i][j], 1));
                }
            }
        }
        return d;
    }
    
    public boolean isGoal()  {              // is this board the goal board?
        return this.hamming() == 0;
    }
    
    public Board twin()   {                 // a board that is obtained by exchanging any pair of blocks
        int[][] twinBoard = new int[n][n];
        int izero;
        int jzero;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                twinBoard[i][j] = board[i][j];
                if (board[i][j] == 0) {
                    izero = i;
                    jzero = j;
                }
            }
        }
        // find 2 indices that are non-zero. there can only be 1 zero.
        if (board[0][0] != 0) {
            if (board[0][1] != 0) {
                twinBoard[0][1] = board[0][0];
                twinBoard[0][0] = board[0][1];
            }
            else {
                twinBoard[1][0] = board[0][0];
                twinBoard[0][0] = board[1][0];
            }
        }
        else {
            twinBoard[1][0] = board[0][1];
            twinBoard[0][1] = board[1][0];
        }
        Board tBoard = new Board(twinBoard);
        return tBoard;
    }
    
    public boolean equals(Object y)   {     // does this board equal y?
        if (y == null) {
            return false;
        }
        if (y.getClass() == this.getClass()) {
            // System.out.println(y.getClass() == this.getClass());
            y = (Board) y;
        } 
        else {
            return false;
        }

        // check the dimensions
        Board boardy = (Board) y;
        if (n != boardy.n) {
            return false;
        }
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] != boardy.board[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
    
    private void exch(int[][] b, int i1, int j1, int i2, int j2) {
        int v = b[i2][j2];
        b[i2][j2] = b[i1][j1];
        b[i1][j1] = v;
    }
    
    public Iterable<Board> neighbors() {    // all neighboring boards
        
        List<Board> neighbors = new ArrayList<Board>();
        
        // look for the zero
        int izero = 0;
        int jzero = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] == 0) {
                    izero = i;
                    jzero = j;
                    break;
                }
            }
        }
        
        // check if it has a neighbor to the top
        int[][] copyBoard = new int[n][n];
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                copyBoard[i][j] = board[i][j];
            }
        }
        
        if (izero > 0) {
            exch(copyBoard, izero, jzero, izero - 1, jzero);
            Board neighborBoard = new Board(copyBoard);
            neighbors.add(neighborBoard);
            exch(copyBoard, izero - 1, jzero, izero, jzero);
        }
        
        if (jzero > 0) {
            exch(copyBoard, izero, jzero, izero, jzero - 1);
            Board neighborBoard = new Board(copyBoard);
            neighbors.add(neighborBoard);
            exch(copyBoard, izero, jzero - 1, izero, jzero);
        }
        
        if (izero < n - 1) {
            exch(copyBoard, izero, jzero, izero + 1, jzero);
            Board neighborBoard = new Board(copyBoard);
            neighbors.add(neighborBoard);
            exch(copyBoard, izero+1, jzero, izero, jzero);
        }
        
        if (jzero < n - 1) {
            exch(copyBoard, izero, jzero, izero, jzero + 1);
            Board neighborBoard = new Board(copyBoard);
            neighbors.add(neighborBoard);
            exch(copyBoard, izero, jzero + 1, izero, jzero);
        }

        return neighbors;
        
        /*
        return new Iterable<Board>() {
            public Iterator<Board> iterator() {
                return new Iterator<Board>() {
                    private int position;
                    public boolean hasNext() {
                        return false;
                    }
                    public Board next() {
                        return null;
                    }
                }
            }
        }
        */
        
        
    }
    
    public String toString()  {             // string representation of this board (in the output format specified below)
        String s = "";
        s += Integer.toString(n);
        s += "\n";
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s += (Integer.toString(board[i][j]) + " ");
            }
            s += "\n";
        }
        return s;
    }

    public static void main(String[] args) { // unit tests (not graded)
        int[][] blocks = new int[3][3];
        blocks[0][0] = 1;
        blocks[0][1] = 4;
        blocks[0][2] = 6;
        blocks[1][0] = 2;
        blocks[1][1] = 0;
        blocks[1][2] = 3;
        blocks[2][0] = 8;
        blocks[2][1] = 7;
        blocks[2][2] = 5;
        Board b = new Board(blocks);
        System.out.println(b.toString());
        
        // Assert.assertSame(b.dimension(), 3);
        
        Board twinB = b.twin();
        System.out.println(twinB.toString());
        
        Board twinTwinB = twinB.twin();
        // System.out.println(b.equals(twinB));
        // Assert.assertFalse(b.equals(1));
        // Assert.assertFalse(b.equals(twinB));
        // Assert.assertTrue(b.equals(twinTwinB));  
        // Assert.assertFalse(b.isGoal());
        
        // System.out.println(b.equals(null));
        
        int[][] goalBlocks = new int[2][2];
        goalBlocks[0][0] = 1;
        goalBlocks[0][1] = 2;
        goalBlocks[1][0] = 3;
        goalBlocks[1][1] = 0;
        Board bGoal = new Board(goalBlocks);
        // Assert.assertTrue(bGoal.isGoal());    
        
        System.out.println(bGoal.toString());
        Iterable<Board> neighbors = bGoal.neighbors();
        for (Board ng: neighbors) {
            System.out.println(ng.toString());
        }      
    }
}