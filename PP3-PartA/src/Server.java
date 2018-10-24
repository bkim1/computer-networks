import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	
    public static void main(String[] args) throws IOException {
    	ServerSocket serverSocket = null;
		boolean listening = true;
		try {
			serverSocket = new ServerSocket(5000);
		} catch (IOException e) {
			System.err.println("Could not listen on port: 5000.");
			System.exit(-1);
		}
        System.out.println("Tic-Tac-Toe Server is up and running.....");
		while (listening)
		{
			Socket clntSock = serverSocket.accept();  //accept the incoming call, and pass the NEW socket to the thread
			ServerThread serverThread = new ServerThread(clntSock);
			Thread T = new Thread(serverThread);
			T.start();
		}
		serverSocket.close();
    }
}
