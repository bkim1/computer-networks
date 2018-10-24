import java.io.Serializable;

public class GameData implements Serializable {
    private static final long serialVersionUID = 1L;

    private String playerName;
    private int[][] board;
    private GameMove move; 
    private boolean isEnd;

    public GameData(GameData gd) {
        this.playerName = gd.getPlayer();
        this.board = gd.getBoard();
        this.move = gd.getMove();
        this.isEnd = gd.isEnd;
    }

    public GameData(String p, int[][] b, GameMove m, boolean e) {
        this.playerName = p;
        this.board = b;
        this.move = m;
        this.isEnd = e;
    }

    public String getPlayer() { return this.playerName; }
    public void setPlayer(String p) { this.playerName = p; }

    public int[][] getBoard() { return this.board; }
    public void setBoard(int[][] b) { this.board = b; }

    public GameMove getMove() { return this.move; }
    public void setMove(GameMove m) { this.move = m; }

    public boolean isEnd() { return this.isEnd; }
    public void setEnd(boolean b) { this.isEnd = b; }

    public String toString() {
        String move = this.move.toString();
        String board = GameUtils.printBoard(this.board);
        
        String dataRep = String.format("Player: %s%nMove: %s%n%n%s", this.playerName, move, board);

        return dataRep;
    }
}
