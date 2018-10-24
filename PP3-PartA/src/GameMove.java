import java.io.Serializable;

public class GameMove implements Serializable {
    private static final long serialVersionUID = 1L;

    private int row, col;
    private int move;

    public GameMove(int r, int c, int m) {
        this.row = r;
        this.col = c;
        this.move = m;
    }

    public int getRow() { return this.row; }
    public void setRow(int r) { this.row = r; }

    public int getCol() { return this.col; }
    public void setCol(int c) { this.col = c; }

    public int getMove() { return this.move; }
    public void setMove(int m) { this.move = m; }

    public String toString() { 
        return "(" + this.col + ", " + this.row + ", " + this.move + ")";
    }
}