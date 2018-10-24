

public class GameUtils {

    public int[][] initializeBoard() {
        int[][] board = new int[3][3];

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                board[i][j] = 0;
            }
        }
        return board;
    }

    public boolean validMove(int[][] board, GameMove move) {
        return board[move.getRow()][move.getCol()] == 0;
    }

    public boolean checkForWin(int[][] board, GameMove move) {
        // If true --> Need to check diagonal, horizontal, and vertical
        // Means that its in one of the corners
        if ((move.getRow() == 0 || move.getRow() == board.length - 1)
                && (move.getCol() == 0 || move.getCol() == board.length - 1)) {
            
            return checkHorizontal(board, move) || checkVertical(board, move) ||
                checkDiagonal(board, move, false);
        }
        // Move was placed in center of board
        else if (Math.floor(board.length / 2) == move.getRow() &&
                Math.floor(board.length / 2) == move.getCol()) {
            
            return checkHorizontal(board, move) || checkVertical(board, move) ||
                checkDiagonal(board, move, true);
        }
        // Only have to check vertically and horizontally
        else {
            return checkHorizontal(board, move) || checkVertical(board, move);
        }
    }

    private boolean checkHorizontal(int[][] board, GameMove move) {
        int sum = 0;
        for (int i = 0; i < board.length; i++) {
            sum += board[move.getRow()][i];
        }
        if (sum == 15) { return true; }
        return false;
    }

    private boolean checkDiagonal(int[][] board, GameMove move, boolean isMid) {
        if (isMid || (move.getCol() == 0 && move.getRow() == 0) ||
                (move.getCol() == board.length -1 && move.getRow() == board.length -1)) {
            int sum = 0;
            for (int i = 0; i < board.length; i++) {
                sum += board[i][i];
            }
            if (sum == 15) { return true; }
        }
        if (isMid || (move.getCol() == 0 && move.getRow() == board.length -1) ||
                (move.getCol() == board.length -1 && move.getRow() == 0)) {
            int sum = 0;
            for (int i = 0, j = board.length - 1; i < board.length && j >= 0; i++, j--) {
                sum += board[i][j];
            }
            if (sum == 15) { return true; }
        }
        return false;
    }

    private boolean checkVertical(int[][] board, GameMove move) {
        int sum = 0;
        for (int i = 0; i < board.length; i++) {
            sum += board[i][move.getCol()];
        }
        if (sum == 15) { return true; }
        return false;
    }

    public static String printBoard(int[][] board) {
        String boardRep = "";

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (j == board.length -1) {
                    boardRep += " " + board[i][j];
                }
                else {
                    boardRep += board[i][j] + " | ";
                }
            }
            if (i != board.length - 1) {
                boardRep += "\n---------";
            }
        }
        return boardRep;
    }
}