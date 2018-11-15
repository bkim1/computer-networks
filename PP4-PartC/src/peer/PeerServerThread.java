package peer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import serializedData.Action;
import serializedData.PeerInfo;
import utils.PeerToBrokerUtils;

public class PeerServerThread implements Runnable {
	private int port;
	private String fileName;

	public PeerServerThread(int p, String f) {
		this.port = p;
		this.fileName = f;
	}

    public void run() {
    	ServerSocket serverSocket = null;

		try {
			serverSocket = new ServerSocket(this.port);

			System.out.println(Thread.currentThread().getName() + ": Peer file transfer is up and running.....");
			try {
				while (!Thread.currentThread().isInterrupted()) {
					System.out.println(Thread.currentThread().getName() + ": Waiting for someone to want the file...");
					Socket clntSock = serverSocket.accept();  //accept the incoming call, and pass the NEW socket to the thread
					PeerSendThread sendThread = new PeerSendThread(clntSock, this.fileName);
					Thread T = new Thread(sendThread);
					T.start();
				}
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			serverSocket.close();
		} catch (IOException e) {
			System.err.println("Could not listen on port: " + this.port);
			return;
		}
	}
	
}