import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;

public class Server {

	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        final int WINDOW_SIZE = 5;
		Packet receivedSerializedData, serializedPacket;
        BufferedReader fromKeyboard = new BufferedReader(new InputStreamReader(System.in));
        String fileName = LocalDateTime.now().toString() + "-output.txt";
        File file = new File(fileName);
        file.createNewFile();
        BufferedWriter fileWriter = new BufferedWriter(new FileWriter(file));

        // Setup Window
        ArrayList<Packet> window = new ArrayList<>(WINDOW_SIZE);
        for (int i = 0; i < WINDOW_SIZE; i++) {
            window.add(null);
        }
        int expectedPacket = 0;
        
        System.out.println("Enter the port number: ");
        int servPort = Integer.parseInt(fromKeyboard.readLine());
        
		ServerNetworkUtils snu = new ServerNetworkUtils(servPort);
		snu.createServerSocket();
		snu.setReceivePacket();
		
		System.out.println("Server is now up and running. Waiting on port # " + servPort);
        
        Random rand = new Random();
        String prevLine = "";

		while (true) { // Run forever, receiving and echoing datagrams from any client
			System.out.println("Waiting to receive something new!");
			receivedSerializedData = snu.getSerializedPacket();
            System.out.println("\nGOT SOMETHING!!!");
            System.out.println(receivedSerializedData);
            snu.serverPrint();
            if (receivedSerializedData.getSequenceNumber() != expectedPacket &&
                   !prevLine.equals(receivedSerializedData.getData())) {
                if (receivedSerializedData.isEndOfFile()) { break; }
                System.out.println("HI");
                continue;
            }
            if (receivedSerializedData.isEndOfFile()) { 
                System.out.println("Got that it's the end! Sending the last ACK");
                snu.sendSerializedPacket(new Packet(null, 0, true));
                break; 
            }
            
            String receivedData = receivedSerializedData.getData();
            if (receivedData == null) { break; }

            System.out.println("Previous Line: " + prevLine);

            window.set(expectedPacket, receivedSerializedData);

            // "Fail" to send ACK 1/4 of the time
            if (rand.nextInt(100) + 1 < 25) { 
                System.out.println("Skipping the ACK!");
                continue; 
            }

            // Setup && Send ACK
			System.out.println("Sending out ACK for seqNum: " + expectedPacket);
            serializedPacket = new Packet("ACK", expectedPacket, false);

            if (!prevLine.equals(receivedData)) {
                System.out.println("Writing line to file: " + receivedData);
                fileWriter.write(receivedData + "\n");
                prevLine = receivedData;
                expectedPacket = (expectedPacket + 1) % WINDOW_SIZE;
            }
			snu.sendSerializedPacket(serializedPacket);
        }
        System.out.println("Done now! Closing out the file!");
        fileWriter.close();
	}
}
