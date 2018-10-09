import java.io.IOException;

public class ClientSendThread implements Runnable {
    private ClientController controller;
    private ClientNetworkUtils cnu;

    public ClientSendThread(ClientController c, ClientNetworkUtils nu) {
        this.controller = c;
        this.cnu = nu;
    }

    public void run() {
        Packet data = null;
        while(true) {
            try { 
                data = this.controller.getNextPacket();
            } catch (InterruptedException e) { }
            
            if (data == null) { continue; }
            System.out.println("Sending packet: " + data.toString());
            try {
                this.cnu.sendSerializedPacket(data);
                if (data.getData() == null) { break; }
            } catch (IOException|ClassNotFoundException e) {

            }
        }
        System.out.println("Finished with the ClientSendThread");
    }
}