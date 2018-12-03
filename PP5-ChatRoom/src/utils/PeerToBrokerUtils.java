package utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import object.BrokerPacket;

public class PeerToBrokerUtils {
    private static final int PACKET_MAX = 1024; // Maximum size of echo datagram packet payload

    byte[] recvBuf = new byte[PACKET_MAX];
    byte[] sendBuf = new byte[PACKET_MAX];
    DatagramPacket receivePacket, sendpacket;
    BrokerPacket data;
    DatagramSocket clientSocket, serverSocket, socket;
    InetAddress serverAddress;
    int serverPort;


    public PeerToBrokerUtils(InetAddress serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    public void createClientSocket() throws SocketException {
        clientSocket = new DatagramSocket();
    }
    
    private void setReceivePacket() throws SocketException { //create a UDP packet to receive frome a server
        receivePacket = new DatagramPacket(recvBuf, recvBuf.length);
    }
    
    public void setSendPacket() throws SocketException { //create a UDP packet to send to a server
        sendpacket = new DatagramPacket(sendBuf, sendBuf.length, serverAddress, serverPort); 
    }

    public void clientPrint(){
        System.out.println("Handling client at " + receivePacket.getAddress().getHostAddress() + " on port " + receivePacket.getPort());
    }
    
    public BrokerPacket getSerializedPacket() throws IOException, ClassNotFoundException {
        setReceivePacket();
        int countTimeouts = 1;
        clientSocket.setSoTimeout(500);   // set the timeout in millisecounds.
        while(true) {        // receive data until timeout
            try {
                System.out.println("\nWaiting to receive message...");
                clientSocket.receive(receivePacket); // Receive packet from client
                System.out.println("Message received\n");
                recvBuf = receivePacket.getData();
                ByteArrayInputStream byteStream2 = new ByteArrayInputStream(recvBuf);
                ObjectInputStream is = new ObjectInputStream(new BufferedInputStream(byteStream2));

                data = (BrokerPacket) is.readObject();
                // Object obj = is.readObject();
                // System.out.println(obj.getClass() + ": " + obj);
                is.close();
                return data;
            }
            catch (SocketTimeoutException e) {
                // timeout exception.
                System.out.println("Timeout reached!!! " + e);
                // controller.resendPacket();
                if (countTimeouts == 5) {
                    clientSocket.close();
                    System.out.println("5 Timeouts reached!!! Broker can't be reached");
                    return null;
                }
                countTimeouts++; 	
            }
        }
    }

    public void sendSerializedPacket(BrokerPacket data) throws IOException, ClassNotFoundException {	
        //System.out.println("Client name: " + data.getName() + " echo: " + data.getData());
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream(PACKET_MAX); //create a byte array big enough to hold serialized object
        ObjectOutputStream os = new ObjectOutputStream(new BufferedOutputStream(byteStream)); //wrap the byte stream with an Object stream
        os.flush();
        os.writeObject(data); //now write the data to the object output stream (still have not sent the packet)
        os.flush();
        //retrieves byte array
        byte[] sendBuf = byteStream.toByteArray();  //get a byte array from the serialized object	
        DatagramPacket sendPacket = new DatagramPacket(sendBuf, sendBuf.length, serverAddress, serverPort); //fill in the datagram packet
        clientSocket.send(sendPacket); // Send the object
        os.close();
    }
    
    public void closeSocket(){
        clientSocket.close();
    }
}
