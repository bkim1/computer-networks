import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class GameData implements Serializable {
    private static final long serialVersionUID = 1L;

    private int[][] board;
    private GameMove move; 
    private boolean isEnd, p1Turn, p1Odd, tie;
    private int numMoves;
    private ArrayList<Integer> p1Moves, p2Moves;

    public GameData(GameData gd) {
        this.board = gd.getBoard();
        this.move = gd.getMove();
        this.isEnd = gd.isEnd();
        this.p1Turn = gd.isP1Turn();
        this.p1Odd = gd.isP1Odd();
        this.numMoves = gd.getNumberOfMoves();
        this.p1Moves = gd.getP1Moves();
        this.p2Moves = gd.getP2Moves();
    }

    public GameData(int[][] b, GameMove m, boolean e, boolean t, boolean o, int n, ArrayList<Integer> p1, ArrayList<Integer> p2) {
        this.board = b;
        this.move = m;
        this.isEnd = e;
        this.p1Turn = t;
        this.p1Odd = o;
        this.numMoves = n;
        this.p1Moves = p1;
        this.p2Moves = p2;
    }

    public GameData(int[][] b, GameMove m, boolean e, boolean t, boolean o, int n) {
        this.board = b;
        this.move = m;
        this.isEnd = e;
        this.p1Turn = t;
        this.p1Odd = o;
        this.numMoves = n;

        if (this.p1Odd) {
            this.p1Moves = new ArrayList<>(Arrays.asList(1, 3, 5, 7, 9));
            this.p2Moves = new ArrayList<>(Arrays.asList(2, 4, 6, 8));
        }
        else {
            this.p2Moves = new ArrayList<>(Arrays.asList(1, 3, 5, 7, 9));
            this.p1Moves = new ArrayList<>(Arrays.asList(2, 4, 6, 8));
        }
    }

    public GameData() {
        this.board = new int[3][3];
        this.move = null;
        this.isEnd = false;
        this.p1Turn = true;
        this.p1Odd = true;
        this.numMoves = 0;
        this.p1Moves = new ArrayList<>(Arrays.asList(1, 3, 5, 7, 9));
        this.p2Moves = new ArrayList<>(Arrays.asList(2, 4, 6, 8));
    }

    public int[][] getBoard() { return this.board; }
    public void setBoard(int[][] b) { this.board = b; }

    public GameMove getMove() { return this.move; }
    public void setMove(GameMove m) { this.move = m; }

    public boolean isEnd() { return this.isEnd; }
    public void setEnd(boolean b) { this.isEnd = b; }
    public void gameOver() { this.isEnd = true; }

    public boolean isP1Turn() { return this.p1Turn; }
    public void setP1Turn(boolean b) { this.p1Turn = b; }
    public void switchTurn() { this.p1Turn = !this.p1Turn; }

    public boolean isP1Odd() { return this.p1Odd; }
    public void setP1Odd(boolean b) { this.p1Odd = b; }

    public boolean isTie() { return this.tie; }
    public void setTie(boolean b) { this.tie = b; }
    public void tieGame() {
        this.isEnd = true;
        this.tie = true;
    }

    public int getNumberOfMoves() { return this.numMoves; }
    public void setNumberOfMoves(int n) { this.numMoves = n; }
    public void incrementMoves() { this.numMoves++; }

    public ArrayList<Integer> getP1Moves() { return this.p1Moves; }
    public void setP1Moves(ArrayList<Integer> m) { this.p1Moves = m; }
    public void removeP1Move(int i) {
        int index = this.p1Moves.indexOf(i);
        this.p1Moves.remove(index); 
    }
    
    public ArrayList<Integer> getP2Moves() { return this.p2Moves; }
    public void setP2Moves(ArrayList<Integer> m) { this.p2Moves = m; }
    public void removeP2Move(int i) { 
        int index = this.p2Moves.indexOf(i);
        this.p2Moves.remove(index); 
    }
    
    public String toString() {
        String rep = "";

        if (this.move != null) { rep += "Move: " + this.move.toString() + "\n"; }
        rep += "Number of Moves: " + this.numMoves + "\n\n"
               + GameUtils.printBoard(this.board) + "\n";
        return rep;
    }
}
