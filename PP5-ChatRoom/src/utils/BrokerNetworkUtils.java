package utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import object.BrokerPacket;

public class BrokerNetworkUtils {
    private static final int PACKET_MAX = 512; // Maximum size of game datagram packet payload

    byte[] recvBuf = new byte[PACKET_MAX];
    DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);
    DatagramPacket receivePacket, sendPacket;
    BrokerPacket data;
    DatagramSocket clientSocket, socket;
    InetAddress serverAddress;
    int serverPort;

    public BrokerNetworkUtils(int serverPort) {
        this.serverPort = serverPort;
    }
    
    public void createServerSocket() throws SocketException {
        this.socket = new DatagramSocket(serverPort);
    }

    public DatagramPacket getSenderPacket() { return this.receivePacket; }

    public void setReceivePacket() throws SocketException {
        this.receivePacket = new DatagramPacket(recvBuf, recvBuf.length);
    }

    public BrokerPacket getSerializedBrokerPacket() throws IOException, ClassNotFoundException {
        socket.receive(this.receivePacket);
        this.recvBuf = this.receivePacket.getData();

        ByteArrayInputStream byteStream = new ByteArrayInputStream(this.recvBuf);
        ObjectInputStream is = new ObjectInputStream(new BufferedInputStream(byteStream));

        this.data = (BrokerPacket) is.readObject();
        is.close();
        return this.data;
    }

    public void sendSerializedBrokerPacket(BrokerPacket data, DatagramPacket packet) throws IOException {
        System.out.println("Attempting to send the data back!");
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream(PACKET_MAX);
        ObjectOutputStream os = new ObjectOutputStream(new BufferedOutputStream(byteStream));

        os.flush();
        os.writeObject(data);
        os.flush();

        System.out.println("Wrote the data to the stream");
        //retrieves byte array
        System.out.println(byteStream.toString());
        byte[] sendBuf = byteStream.toByteArray();

        DatagramSocket clientSocket = new DatagramSocket();
        System.out.println("Client IP: " + packet.getAddress() + "\n" +
                           "Client Port: " + packet.getPort());
        DatagramPacket sendPacket = new DatagramPacket(sendBuf, sendBuf.length, packet.getAddress(), packet.getPort());
        clientSocket.send(sendPacket); // Send the object
        os.close();
        clientSocket.close();
    }
    
    public void sendSerializedBrokerPacket(BrokerPacket data) throws IOException, ClassNotFoundException {	
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream(PACKET_MAX);
        ObjectOutputStream os = new ObjectOutputStream(new BufferedOutputStream(byteStream));

        os.flush();
        // os.writeObject(data);
        os.writeObject(data.getRooms());
        // os.flush();

        //retrieves byte array
        byte[] sendBuf = byteStream.toByteArray();
        this.clientSocket = new DatagramSocket();
        this.sendPacket = new DatagramPacket(sendBuf, sendBuf.length, this.receivePacket.getAddress(), this.receivePacket.getPort()); //fill in the datagram packet
        clientSocket.send(sendPacket); // Send the object
        os.close();
        this.clientSocket.close();
    }

    public void closeSocket() {
        this.socket.close();
    }
}