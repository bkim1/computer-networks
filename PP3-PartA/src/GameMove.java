import java.io.Serializable;

public class GameMove implements Serializable {
    private static final long serialVersionUID = 1L;
    private int row, col;
    private String move;

    public GameMove(int r, int c, String m) {
        this.row = r;
        this.col = c;
        this.move = m;
    }

    public void applyMove(ArrayList<ArrayList<String>> board) {
        ArrayList<String> boardCol = board.get(this.col);
        boardCol.set(this.row, this.move);
    }

    public String toString() { 
        return "(" + this.col + ", " + this.row + ", " + this.move;
    }
}