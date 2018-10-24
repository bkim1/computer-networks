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
        GameData rcvData;

        try {
            while (true) {
                rcvData = getData();

                System.out.println("Game Data:\n" + rcvData.toString());

                
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    private GameData getData() throws ClassNotFoundException {
        InputStream is = this.socket.getInputStream();  
        ObjectInputStream ois = new ObjectInputStream(is);  
        toReceive = (GameData) ois.readObject();

        return toReceive;
    }

    private void sendData(GameData gd) {
        OutputStream os = this.socket.getOutputStream();  
        ObjectOutputStream oos = new ObjectOutputStream(os);  
        oos.writeObject(gd);
    }
}
