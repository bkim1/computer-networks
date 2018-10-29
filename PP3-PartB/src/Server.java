import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
	
    public static void main(String[] args) throws IOException {
		ArrayList<Thread> threads = new ArrayList<>();
		ArrayList<GameController> controllers = new ArrayList<>();
    	ServerSocket serverSocket = null;
		boolean listening = true;
		try {
			serverSocket = new ServerSocket(5000);
		} catch (IOException e) {
			System.err.println("Could not listen on port: 5000.");
			System.exit(-1);
		}
		System.out.println("Tic-Tac-Toe Server is up and running.....");
		
		GameController controller = new GameController();
		controllers.add(controller);

		while (listening)
		{
			Socket clntSock = serverSocket.accept();  //accept the incoming call, and pass the NEW socket to the thread
			ServerThread serverThread = new ServerThread(clntSock, controller);
			Thread T = new Thread(serverThread);
			threads.add(T);

			if (threads.size() % 2 == 0) {
				// Set controller to last two threads in list
				controller.setThreads(threads.get(threads.size()-2), threads.get(threads.size()-1));
				controller = new GameController();
			}
			T.start();
		}
		serverSocket.close();
    }
}
