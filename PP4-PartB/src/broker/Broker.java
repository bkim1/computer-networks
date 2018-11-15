package broker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import utils.BrokerNetworkUtils;
import serializedData.PeerInfo;

public class Broker {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        int port;

        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }
        else {
            // Get Network Initialization Info
            BufferedReader fromKeyboard = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Enter the Port #: ");
            String portInput = fromKeyboard.readLine();
            port = Integer.parseInt(portInput.replaceAll("\\s+", ""));
        }

        // Initialize Server
        BrokerNetworkUtils bnu = new BrokerNetworkUtils(port);
        bnu.createServerSocket();
        bnu.setReceivePacket();
        System.out.println("Server is waiting on port #" + port);

        // Setup shared memory controller
        BrokerController bc = new BrokerController();

        PeerInfo rcvInfo;
        while (true) {
            rcvInfo = bnu.getSerializedPeerInfo();
            System.out.println("Received: \n" + rcvInfo);
            BrokerThread bt = new BrokerThread(bc, bnu.getSenderPacket(), rcvInfo);
            Thread t = new Thread(bt);
            t.start();
        }
    }
}