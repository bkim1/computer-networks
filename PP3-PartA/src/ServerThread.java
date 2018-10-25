import java.io.*;
import java.net.*;
import java.util.*;

public class ServerThread implements Runnable {
	Socket socket;
	InputStream recvStream;
	OutputStream sendStream;
	String request;

	// protected DatagramSocket socket = null;
	protected BufferedReader in = null;
	protected boolean moreQuotes = true;

	public ServerThread(Socket clntSock) throws IOException {
		this.socket = clntSock;
		this.recvStream = clntSock.getInputStream();
		this.sendStream = clntSock.getOutputStream();
	}

	public void run() {
        GameUtils gu = new GameUtils();
        GameData toReceive, toSend;

        System.out.println(Thread.currentThread().getName() + " is running...");
        try {
            while (true) {
                toReceive = getData();

                System.out.println("Game Data:\n" + toReceive.toString());

                toSend = new GameData(toReceive);
                toSend.incrementMoves();

                sendData(toSend);
                break;
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
    
    private GameData getData() throws IOException, ClassNotFoundException {
    	GameData toReceive = null;
        InputStream is = this.socket.getInputStream();  
        ObjectInputStream ois = new ObjectInputStream(is);  
        toReceive = (GameData) ois.readObject();

        return toReceive;
    }

    private void sendData(GameData gd) throws IOException {
        OutputStream os = this.socket.getOutputStream();  
        ObjectOutputStream oos = new ObjectOutputStream(os);  
        oos.writeObject(gd);
    }
}
