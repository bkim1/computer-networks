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

public class ServerNetworkUtils {
    private static final int ECHOMAX = 512; // Maximum size of game datagram packet payload

    byte[] recvBuf = new byte[ECHOMAX];
    DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);
    DatagramPacket receivePacket, sendPacket;
    GameData data;
    DatagramSocket clientSocket, socket;
    InetAddress serverAddress;
    int serverPort;

    public ServerNetworkUtils(int serverPort) {
        this.serverPort = serverPort;
    }
    
    public void createServerSocket() throws SocketException {
        this.socket = new DatagramSocket(serverPort);;
    }

    public void setReceivePacket() throws SocketException {
        this.receivePacket = new DatagramPacket(recvBuf, recvBuf.length);
    }
    
    public void serverPrint(){
        System.out.println("Handling client at " + this.receivePacket.getAddress().getHostAddress() + " on port " + receivePacket.getPort());
    }

    public GameData getSerializedGameData() throws IOException, ClassNotFoundException {
        socket.receive(this.receivePacket); // Receive packet from client
        this.recvBuf = this.receivePacket.getData();
        ByteArrayInputStream byteStream = new ByteArrayInputStream(this.recvBuf);
        ObjectInputStream is = new ObjectInputStream(new BufferedInputStream(byteStream));
        this.data = (GameData) is.readObject();
        //System.out.println("Client name: " + data.getName() + " Got: " + data.getData());
        is.close();
        return this.data;
    }
    
    public void sendSerializedGameData(GameData data) throws IOException, ClassNotFoundException {	
        //System.out.println("Client name: " + data.getName() + " echo: " + data.getData());
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream(ECHOMAX); //create a byte array big enough to hold serialized object
        ObjectOutputStream os = new ObjectOutputStream(new BufferedOutputStream(byteStream)); //wrap the byte stream with an Object stream
        os.flush();
        os.writeObject(data); //now write the data to the object output stream (still have not sent the packet)
        os.flush();
        //retrieves byte array
        byte[] sendBuf = byteStream.toByteArray();  //get a byte array from the serialized object	
        this.clientSocket = new DatagramSocket(); //create a new socket to send out the object
        this.sendPacket = new DatagramPacket(sendBuf, sendBuf.length, this.receivePacket.getAddress(), this.receivePacket.getPort()); //fill in the datagram packet
        clientSocket.send(sendPacket); // Send the object
        os.close();
        this.clientSocket.close();
    }

    public void closeSocket() {
        this.socket.close();
    }
}
