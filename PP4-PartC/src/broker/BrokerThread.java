package broker;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;

import serializedData.*;

public class BrokerThread implements Runnable {
    private static final int PACKET_MAX = 512;

    private PeerInfo data;
    private BrokerController controller;
    private InetAddress clientAddress;
    private int clientPort;

    public BrokerThread(BrokerController b, DatagramPacket n, PeerInfo p) {
        this.controller = b;
        this.data = p;
        this.clientPort = n.getPort();
        this.clientAddress = n.getAddress();
    }

    public void run() {
        System.out.println("\nBrokerThread spawned!");
        PeerInfo returnData;

        if (this.data.getUserID() == -1) {
            Random r = new Random();
            this.data.setUserID(r.nextInt(50000) + 1);
        }

        switch(this.data.getAction()) {
            case SEARCH:
                returnData = this.controller.searchFiles(this.data);
                break;

            case REG:
                returnData = this.controller.registerFile(this.data);
                break;

            case UNREG:
                returnData = this.controller.unregisterFile(this.data);
                break;

            default:
                returnData = new PeerInfo(this.data, Result.UNKWN);
                break;
        }
        try {
            System.out.println("Processed! Returning: \n" + returnData + "\n");
            this.sendSerializedPeerInfo(returnData);
            System.out.println("Sent the data!");
        } catch (IOException | ClassNotFoundException e) { 
            e.printStackTrace();
        }
        System.out.println("Thread closing out now!\n");
    }

    private void sendSerializedPeerInfo(PeerInfo data) throws IOException, ClassNotFoundException {	
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream(PACKET_MAX);
        ObjectOutputStream os = new ObjectOutputStream(new BufferedOutputStream(byteStream));

        os.flush();
        os.writeObject(data);
        os.flush();

        //retrieves byte array
        byte[] sendBuf = byteStream.toByteArray();
        DatagramSocket clientSocket = new DatagramSocket();
        DatagramPacket sendPacket = new DatagramPacket(sendBuf, sendBuf.length, this.clientAddress, this.clientPort);
        
        clientSocket.send(sendPacket); // Send the object
        
        os.close();
        clientSocket.close();
    }
}