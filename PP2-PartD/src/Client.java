import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;

public class Client {
    public static void main(String[] args) throws IOException {
        BufferedReader fromKeyboard = new BufferedReader(new InputStreamReader(System.in));
        InetAddress serverAddress;
        int port;
        
        if (args.length != 0) {
        	serverAddress = InetAddress.getByName(args[0]);
        	port = Integer.parseInt(args[1]);
        }
        else {
        	// Get connection info
        	System.out.println("Enter IP Address: ");
        	String ip = fromKeyboard.readLine();
        	serverAddress = InetAddress.getByName(ip);
        	
        	System.out.println("Enter Port #: ");
        	port = Integer.parseInt(fromKeyboard.readLine());
        }
        ClientNetworkUtils cnu = new ClientNetworkUtils(serverAddress, port);        	
        cnu.createClientSocket();
        
        // Setup file to read from
        System.out.println("Enter the file name: ");
        String file = fromKeyboard.readLine();

        ClientController controller = new ClientController(file);
        ClientAckThread ackThread = new ClientAckThread(controller, cnu);
        ClientSendThread sendThread = new ClientSendThread(controller, cnu);
        cnu.setController(controller);

        Thread t0 = new Thread(ackThread, "ACK Thread");
        Thread t1 = new Thread(sendThread, "SEND Thread");

        t0.start();
        t1.start();
    }
}
