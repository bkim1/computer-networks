package broker;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import object.BrokerPacket;
import object.Result;

public class BrokerThread implements Runnable {
    private static final int PACKET_MAX = 1024;

    private BrokerPacket data;
    private BrokerController controller;
    private InetAddress clientAddress;
    private int clientPort;

    public BrokerThread(BrokerController b, DatagramPacket n, BrokerPacket p) {
        this.controller = b;
        this.data = p;
        this.clientPort = n.getPort();
        this.clientAddress = n.getAddress();
    }

    public void run() {
        System.out.println("\nBrokerThread spawned!");
        BrokerPacket returnData;

        switch(this.data.getAction()) {
            case REG:
                returnData = this.controller.register(this.data);
                break;
                
            case LOGIN:
                returnData = this.controller.login(this.data);
                break;

            case LOGOUT:
                returnData = this.controller.logout(this.data);
                break;

            case GET_ROOMS:
                returnData = this.controller.getRooms(this.data);
                break;

            default:
                returnData = new BrokerPacket(this.data, Result.UNKWN);
                break;
        }
        try {
            System.out.println("Processed! Returning: \n" + returnData + "\n");
            this.sendSerializedBrokerPacket(returnData);
            System.out.println("Sent the data!");
        } catch (IOException | ClassNotFoundException e) { 
            e.printStackTrace();
        }
        System.out.println("Thread closing out now!\n");
    }

    private void sendSerializedBrokerPacket(BrokerPacket data) throws IOException, ClassNotFoundException {	
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