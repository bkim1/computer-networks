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

import serializedData.PeerInfo;

public class BrokerNetworkUtils {
    private static final int ECHOMAX = 512; // Maximum size of game datagram packet payload

    byte[] recvBuf = new byte[ECHOMAX];
    DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);
    DatagramPacket receivePacket, sendPacket;
    PeerInfo data;
    DatagramSocket clientSocket, socket;
    InetAddress serverAddress;
    int serverPort;

    public BrokerNetworkUtils(int serverPort) {
        this.serverPort = serverPort;
    }
    
    public void createServerSocket() throws SocketException {
        this.socket = new DatagramSocket(serverPort);;
    }

    public void setReceivePacket() throws SocketException {
        this.receivePacket = new DatagramPacket(recvBuf, recvBuf.length);
    }

    public PeerInfo getSerializedPeerInfo() throws IOException, ClassNotFoundException {
        socket.receive(this.receivePacket);
        this.recvBuf = this.receivePacket.getData();

        ByteArrayInputStream byteStream = new ByteArrayInputStream(this.recvBuf);
        ObjectInputStream is = new ObjectInputStream(new BufferedInputStream(byteStream));

        this.data = (PeerInfo) is.readObject();
        is.close();
        return this.data;
    }
    
    public void sendSerializedPeerInfo(PeerInfo data) throws IOException, ClassNotFoundException {	
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream(ECHOMAX);
        ObjectOutputStream os = new ObjectOutputStream(new BufferedOutputStream(byteStream));

        os.flush();
        os.writeObject(data);
        os.flush();

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