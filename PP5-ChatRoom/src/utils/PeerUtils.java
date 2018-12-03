package utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;

public class PeerUtils{
	private MulticastSocket socket;
	private int port;
	private int ttl = 64; /* time to live */
	private InetAddress group; 
	private String username;
	
	public PeerUtils(InetAddress ipAddress, int port, String username) throws Exception {
		this.port = port;
		/* instantiate a MulticastSocket */
		this.socket = new MulticastSocket(port);
		/* set the time to live */
		this.socket.setTimeToLive(ttl);
		this.group = ipAddress;
		this.username = username;
	}
	
	public void joinGroup() throws Exception {
		this.socket.joinGroup(this.group);
    }
    
	public void leaveGroup() throws Exception {
		this.socket.leaveGroup(this.group);
		this.socket.close();
	}

	public String readFromKeyboard() throws Exception {
		BufferedReader stdin; /* input from keyboard */
		String sendString; /* string to be sent */
		stdin = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter text: ");
		sendString = stdin.readLine();
		return sendString;
	}
	
	public void sendToSocket(String msg) throws Exception{
		/* remember to convert keyboard input (in msg) to bytes */
		String userMsg = this.username + ": " + msg;
		DatagramPacket sendPacket = new DatagramPacket(userMsg.getBytes(), userMsg.length(), this.group, this.port);
		this.socket.send(sendPacket);
	}
	
	public String readFromSocket() throws Exception{
		String socketString = null; /* string from socket */
		// get their responses!
		//byte[] buf is a byte array from the socket
        byte[] buf = new byte[1000];
        DatagramPacket recv = new DatagramPacket(buf, buf.length);
        this.socket.receive(recv);
        socketString = new String(recv.getData(), 0, recv.getLength());
		return 	socketString;	
    }
    
	public void sendToTerminal(String msg) throws Exception{
		// System.out.println("Multicast text: " + msg + "\n");
		System.out.println(msg);
	}
}
