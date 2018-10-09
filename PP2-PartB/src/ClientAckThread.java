import java.io.IOException;

public class ClientAckThread implements Runnable {
    private ClientController controller;
    private ClientNetworkUtils cnu;

    public ClientAckThread(ClientController c, ClientNetworkUtils nu) {
        this.controller = c;
        this.cnu = nu;
    }

    public void run() {
        Packet ack = null;
        while(true) {
            try{
                ack = this.cnu.getSerializedPacket();
            } catch(IOException|ClassNotFoundException e) {
                System.err.println(e);
            }
            System.out.println("GOT AN ACK!!!");
            System.out.println(ack.isEndOfFile());
            this.controller.checkAck(ack);
            if (ack.isEndOfFile()) { break; }
        }
        System.out.println("Finished up with ClientACKThread");
    }
}