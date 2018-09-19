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

public class PlayerNetworkUtils {
    private static final int ECHOMAX = 512; // Maximum size of echo datagram packet payload

    byte[] recvBuf = new byte[ECHOMAX];
    byte[] sendBuf = new byte[ECHOMAX];
    DatagramPacket receivePacket, sendPacket;
    GameData data;
    DatagramSocket clientSocket;
    InetAddress serverAddress;
    int serverPort;


    public PlayerNetworkUtils(InetAddress serverAddress,int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    public void createClientSocket() throws SocketException {
        this.clientSocket = new DatagramSocket();;
    }
    
    private void setReceivePacket() throws SocketException { //create a UDP packet to receive frome a server
        this.receivePacket = new DatagramPacket(recvBuf, recvBuf.length);
    }
    
    public void setSendPacket() throws SocketException { //create a UDP packet to send to a server
        this.sendPacket = new DatagramPacket(this.sendBuf, this.sendBuf.length, this.serverAddress, this.serverPort); 
    }

    public void clientPrint() {
        System.out.println("Handling client at " + this.receivePacket.getAddress().getHostAddress() + " on port " + this.receivePacket.getPort());
    }
    
    public GameData getSerializedGameData() throws IOException, ClassNotFoundException {
        setReceivePacket();
        this.clientSocket.receive(receivePacket); // Receive packet from client
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
        this.sendBuf = byteStream.toByteArray();  //get a byte array from the serialized object	
        this.sendPacket = new DatagramPacket(this.sendBuf, this.sendBuf.length, this.serverAddress, this.serverPort); //fill in the datagram packet
        this.clientSocket.send(sendPacket); // Send the object
        os.close();
    }
    
    public void closeSocket() {
        this.clientSocket.close();
    }
}
