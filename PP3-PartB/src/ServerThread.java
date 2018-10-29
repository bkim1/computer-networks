import java.io.*;
import java.net.*;

public class ServerThread implements Runnable {
	private Socket socket;
	private InputStream recvStream;
	private OutputStream sendStream;
    private GameController controller;

	public ServerThread(Socket clntSock, GameController c) throws IOException {
		this.socket = clntSock;
		this.sendStream = clntSock.getOutputStream();
        this.recvStream = clntSock.getInputStream();
        this.controller = c;
	}

	public void run() {
        GameData toReceive, toSend;
        boolean isPlayer1 = false;

        System.out.println(Thread.currentThread().getName() + " is running...");

        System.out.println("Waiting for other player...");
        try {
            isPlayer1 = this.controller.startGame();
        } catch (InterruptedException e) { }
        System.out.println(Thread.currentThread().getName() + ": Found the other player!");

        try {
            // Start of the game
            System.out.println(Thread.currentThread().getName() + ": Waiting for game data");
            while ((toSend = this.controller.getGameData(isPlayer1)) == null) {
                System.out.println(Thread.currentThread().getName() + ": sleeping!");
                Thread.sleep(5000); 
            }
            System.out.println(Thread.currentThread().getName() + ": Got the game data!\n" + toSend.toString());
            sendData(toSend);
            
            System.out.println(Thread.currentThread().getName() + ": Sent game data!");
            while (true) {
                System.out.println("\n" + Thread.currentThread().getName() + ": Waiting on client to get data");
                toReceive = getPlayerData();
                System.out.println(Thread.currentThread().getName() + ": received data\n" + toReceive);
                
                if (toReceive == null) { break; }

                this.controller.setMove(toReceive);

                if (toReceive.isEnd()) { break; }
                toSend = this.controller.getGameData(isPlayer1);
                sendData(toSend);
            }
        } catch (ClassNotFoundException | IOException | InterruptedException e) {
            System.err.println(Thread.currentThread().getName() + ": Error!");
            e.printStackTrace();
        }

        System.out.println("Closing out game!");
        try {
			this.socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    private GameData getPlayerData() throws IOException, ClassNotFoundException {
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
