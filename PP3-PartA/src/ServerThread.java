import java.io.*;
import java.net.*;
import java.util.*;

public class ServerThread implements Runnable {
	Socket socket;
	InputStream recvStream;
	OutputStream sendStream;

	public ServerThread(Socket clntSock) throws IOException {
		this.socket = clntSock;
		this.sendStream = clntSock.getOutputStream();
		this.recvStream = clntSock.getInputStream();
	}

	public void run() {
        GameUtils gu = new GameUtils();
        GameData toReceive, toSend;
        ArrayList<Integer> availableMoves;
        boolean won;

        System.out.println(Thread.currentThread().getName() + " is running...");
        try {
            while (true) {
                toReceive = getData();

                if (toReceive == null) { break; }
                if (toReceive.isEnd()) {
                    if (toReceive.isTie()) { System.out.println("It's a tie..."); }
                    else { System.out.println("Welp we lost..."); }
                    System.out.println("Waiting on the player to play again...");
                    continue;
                }
                System.out.println("Game Data:\n" + toReceive.toString());

                if (toReceive.isP1Turn()) { availableMoves = toReceive.getP1Moves(); }
                else { availableMoves = toReceive.getP2Moves(); }
                toSend = gu.applyMove(toReceive, getRandomMove(availableMoves));
                while (toSend == null) {
                    toSend = gu.applyMove(toReceive, getRandomMove(availableMoves));
                }
                
                won = gu.checkForWin(toSend);
                if (won || toSend.isTie()) {
                    if (won && toSend.isTie()) { toSend.setTie(false); }
                    toSend.gameOver();
                }

                sendData(toSend);
            }
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }

        System.out.println("Closing out server!");
        try {
			this.socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    private GameMove getRandomMove(ArrayList<Integer> availableMoves) {
        Random r = new Random();
        int row = r.nextInt(3);
        int col = r.nextInt(3);
        int move = availableMoves.get(r.nextInt(availableMoves.size()));
        System.out.println("Trying: (" + row + ", " + col + ", " + move + ")");
        return new GameMove(row, col, move);
    }
    
    private GameData getData() throws IOException, ClassNotFoundException {
    	GameData toReceive = null;
        ObjectInputStream ois = new ObjectInputStream(this.recvStream);  
        toReceive = (GameData) ois.readObject();

        return toReceive;
    }

    private void sendData(GameData gd) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(this.sendStream);  
        oos.writeObject(gd);
    }
}
