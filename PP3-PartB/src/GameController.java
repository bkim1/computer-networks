

public class GameController {
    private Thread player1, player2;
    private boolean p1Turn;
    private GameData controlData;
    private GameUtils gu;

    public GameController() {
        this.controlData = new GameData();
        this.gu = new GameUtils();
        this.p1Turn = true;
    }

    public synchronized void setMove(GameData data) {
        this.controlData = data;
        System.out.println(Thread.currentThread().getName() + ": Set the following data:\n" + data.toString() + "\n");
        if (gu.checkForWin(this.controlData)) {
            this.controlData.gameOver();
        }
        else if (this.controlData.getNumberOfMoves() == 9) {
            this.controlData.tieGame();
        }
        this.p1Turn = !this.p1Turn;
        notify();
    }

    public synchronized GameData getGameData(boolean p1) throws InterruptedException {
        if ((p1 && !this.p1Turn) || (!p1 && this.p1Turn)) { 
            System.out.println(Thread.currentThread().getName() + ": waiting");
            wait();
        }
        if ((p1 && this.p1Turn) || (!p1 && !this.p1Turn)) {
            if (this.controlData.isEnd()) {
                boolean p1Odd = this.controlData.isP1Odd();
                if (p1) { this.controlData.setP1Odd(p1Odd); }
                else { this.controlData.setP1Odd(!p1Odd); }
            }
            return this.controlData;
        }
        return null;
    }

    public synchronized boolean startGame() throws InterruptedException {
        if (this.player1 == null || this.player2 == null) {
            wait();
        }
        if (Thread.currentThread() == this.player1) {
            notify();
            return true;
        }
        notify();
        return false;
    }

    public void setThreads(Thread t1, Thread t2) {
        this.player1 = t1;
        this.player2 = t2;
    }
}