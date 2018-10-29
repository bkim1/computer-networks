

public class GameUtils {

    public GameData applyMove(GameData data, GameMove move) {
        if (validMove(data, move)) {
            int[][] board = data.getBoard();
            board[move.getRow()][move.getCol()] = move.getMove();
            data.setBoard(board);
            data.setMove(move);
            
            if (data.isP1Turn()) { data.removeP1Move(move.getMove()); }
            else { data.removeP2Move(move.getMove()); }

            data.switchTurn();
            data.incrementMoves();

            if (data.getNumberOfMoves() == 9) {
                data.tieGame(); 
                data.gameOver();
            }

            return data;
        }
        else { return null; }
    }

    public int[][] initializeBoard() {
        return new int[3][3];
    }

    public boolean validMove(GameData data, GameMove move) {
        boolean valid = false;
        if (data.isP1Turn() && !data.isP1Odd()) {
            valid = move.getMove() % 2 == 0;
        }
        else if (data.isP1Turn() && data.isP1Odd()) {
            valid = move.getMove() % 2 != 0;
        }
        else if (data.isP1Odd()) {
            valid = move.getMove() % 2 == 0;
        }
        else {
            valid = move.getMove() % 2 != 0;
        }

        try {
            valid = valid && data.getBoard()[move.getRow()][move.getCol()] == 0;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }

        return valid;
    }

    public boolean checkForWin(GameData gd) {
        boolean winner = false;
        GameMove move = gd.getMove();
        int[][] board = gd.getBoard();

        if (move == null) { return false; }
        // If true --> Need to check diagonal, horizontal, and vertical
        // Means that its in one of the corners
        if ((move.getRow() == 0 || move.getRow() == board.length - 1)
                && (move.getCol() == 0 || move.getCol() == board.length - 1)) {
            
            winner = checkHorizontal(board, move) || checkVertical(board, move) ||
                checkDiagonal(board, move, false);
        }
        // Move was placed in center of board
        else if (Math.floor(board.length / 2) == move.getRow()
                && Math.floor(board.length / 2) == move.getCol()) {
            
            winner = checkHorizontal(board, move) || checkVertical(board, move) ||
                checkDiagonal(board, move, true);
        }
        // Only have to check vertically and horizontally
        else {
            winner = checkHorizontal(board, move) || checkVertical(board, move);
        }
        if (!winner && gd.getNumberOfMoves() == 9) { return false; }
        return winner;
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
        String boardRep = " ";

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (j == board.length -1) {
                    boardRep += board[i][j];
                }
                else {
                    boardRep += board[i][j] + " | ";
                }
            }
            if (i != board.length - 1) {
                boardRep += "\n ----------\n ";
            }
        }
        return boardRep;
    }
}