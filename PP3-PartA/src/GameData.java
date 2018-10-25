import java.io.Serializable;
import java.util.Arrays;

public class GameData implements Serializable {
    private static final long serialVersionUID = 1L;

    private String playerName;
    private int[][] board;
    private GameMove move; 
    private boolean isEnd, p1Turn, p1Odd;
    private int numMoves;
    private ArrayList<Integer> p1Moves, p2Moves;

    public GameData(GameData gd) {
        this.playerName = gd.getPlayer();
        this.board = gd.getBoard();
        this.move = gd.getMove();
        this.isEnd = gd.isEnd();
        this.p1Turn = gd.isP1Turn();
        this.p1Odd = gd.isP1Odd();
        this.numMoves = gd.getNumberOfMoves();
        this.p1Moves = gd.getP1Moves();
        this.p2Moves = gd.getP2Moves();
    }

    public GameData(String p, int[][] b, GameMove m, boolean e, boolean t, boolean o, int n, ArrayList<Integer> p1, ArrayList<Integer> p2) {
        this.playerName = p;
        this.board = b;
        this.move = m;
        this.isEnd = e;
        this.p1Turn = t;
        this.p1Odd = o;
        this.numMoves = n;
        this.p1Moves = p1;
        this.p2Moves = p2;
    }

    public GameData(String p, int[][] b, GameMove m, boolean e, boolean t, boolean o, int n) {
        this.playerName = p;
        this.board = b;
        this.move = m;
        this.isEnd = e;
        this.p1Turn = t;
        this.p1Odd = o;
        this.numMoves = n;

        this.p1Moves = new ArrayList<>(Arrays.asList(1, 3, 5, 7, 9));
        this.p2Moves = new Arraylist<>(Arrays.asList(2, 4, 6, 8));
    }

    public String getPlayer() { return this.playerName; }
    public void setPlayer(String p) { this.playerName = p; }

    public int[][] getBoard() { return this.board; }
    public void setBoard(int[][] b) { this.board = b; }

    public GameMove getMove() { return this.move; }
    public void setMove(GameMove m) { this.move = m; }

    public boolean isEnd() { return this.isEnd; }
    public void setEnd(boolean b) { this.isEnd = b; }

    public boolean isP1Turn() { return this.p1Turn; }
    public void setP1Turn(boolean b) { this.p1Turn = b; }
    public void switchTurn() { this.p1Turn = !this.p1Turn; }

    public boolean isP1Odd() { return this.p1Odd; }
    public void setP1Odd(boolean b) { this.p1Odd = b; }

    public int getNumberOfMoves() { return this.numMoves; }
    public void setNumberOfMoves(int n) { this.numMoves = n; }
    public void incrementMoves() { this.numMoves++; }

    public ArrayList<Integer> getP1Moves() { return this.p1Moves; }
    public void setP1Moves(ArrayList<Integer> m) { this.p1Moves = m; }
    public void removeP1Move(int i) { this.p1Moves.remove(i); }
    
    public ArrayList<Integer> getP2Moves() { return this.p2Moves; }
    public void setP2Moves(ArrayList<Integer> m) { this.p2Moves = m; }
    public void removeP2Move(int i) { this.p2Moves.remove(i); }
    
    public String toString() {
        return "Player: " + this.playerName + "\n"
                + "Move: " + this.move.toString() + "\n"
                + "Move Number: " + this.numMoves + "\n"
                + GameUtils.printBoard(this.board);
    }
}
