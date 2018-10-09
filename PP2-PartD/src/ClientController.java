import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class ClientController {
    private static final int WINDOW_SIZE = 5;
    private ArrayList<Packet> window;
    private BufferedReader fileReader;
    private int expectedAck;
    private boolean canSend;
    private boolean isEnd;
    private boolean resend;

    public ClientController(String f) {
        this.expectedAck = -1;
        this.canSend = true;
        this.isEnd = false;
        this.resend = false;

        this.window = new ArrayList<>(WINDOW_SIZE);
        for (int i = 0; i < WINDOW_SIZE; i++) {
            this.window.add(null);
        }

        try {
            this.fileReader = new BufferedReader(new FileReader(f));
            // setupWindow();
            String line = this.fileReader.readLine();
            System.out.println("First line: " + line);
            this.window.set(0, new Packet(line, 0, false));
        } catch (FileNotFoundException e) {
            System.err.println("Could not open file");
            System.exit(0);
        } catch (IOException e) {}
        System.out.println("WINDOW: " + this.window);
    }

    public synchronized void resendPacket() {
        this.canSend = true;
        this.resend = true;
        notifyAll();
    }

    public synchronized void checkAck(Packet ack) {
        System.out.println("Checking ACK now...");
        if (this.expectedAck == -1) { this.expectedAck++; }
        System.out.println("Expecting: " + this.expectedAck + " Got: " + ack.getSequenceNumber());

        if (ack.isEndOfFile()) {
            System.out.println("Done with the file! Closing it out!");
            this.isEnd = true;
            try {
                this.fileReader.close();
            } catch (IOException e) {}
        }
        else if (ack.getSequenceNumber() == this.expectedAck) {
            this.canSend = true;
            try {
                if (!setNextPacket()) {
                    System.out.println("Done with the file! Closing it out!");
                    this.fileReader.close();
                    this.isEnd = true;
                }
            } catch (IOException e) { }
            System.out.println("Looks good! Notifying the other thread");
        }
        else if (ack.getSequenceNumber() > this.expectedAck ||
                ack.getSequenceNumber() == (this.expectedAck + 1) % WINDOW_SIZE) {
            System.out.println("HIT THE SKIP!");
            this.expectedAck = ack.getSequenceNumber();
            this.canSend = true;
            try {
                if (!setNextPacket(this.expectedAck)) {
                    System.out.println("Done with the file! Closing it out!");
                    this.fileReader.close();
                    this.isEnd = true;
                }
            } catch (IOException e) {   }
        }
        else {
            System.out.println("Wrong ACK... Signalling to resend");
            this.resend = true;
            this.canSend = true;
        }
        notifyAll();
    }

    public synchronized Packet getNextPacket() throws InterruptedException {
        if (!this.canSend) { wait(); }

        if (this.isEnd) {
            System.out.println("It's the end... Sending the last packet!");
            return new Packet(null, this.expectedAck, true);
        }

        if (this.resend) {
            System.out.println("Resending the packet!");
            this.resend = false;
            this.canSend = false;
            if (this.expectedAck == -1) { this.expectedAck++; }

            return this.window.get(this.expectedAck);
        }

        System.out.println("Allowed to send packet!");
        this.canSend = false;
        Packet toSend = this.window.get((this.expectedAck + 1) % WINDOW_SIZE);

        // "Fail" to send packet
        Random rand = new Random();
        if (rand.nextInt(100) + 1 < 25) { 
            System.out.println("FAILED TO SEND!");
            this.expectedAck = (this.expectedAck + 1) % WINDOW_SIZE;
            return null; 
        }

        System.out.println("Returning packet: " + toSend.toString());
        this.expectedAck = (this.expectedAck + 1) % WINDOW_SIZE;
        return toSend;
    }

    private synchronized boolean setNextPacket() throws IOException {
        String line = this.fileReader.readLine();
        int seqNum = (this.expectedAck + 1) % WINDOW_SIZE;
        if (line == null) {
            this.window.set(seqNum, new Packet(null, 0, true));
            return false;
        }
        this.window.set(seqNum, new Packet(line, seqNum, false));
        return true;
    }

    private synchronized boolean setNextPacket(int seqNum) throws IOException {
        String line = this.fileReader.readLine();
        if (line == null) {
            this.window.set(seqNum, new Packet(null, 0, true));
            return false;
        }
        this.window.set(seqNum, new Packet(line, seqNum, false));
        return true;
    }
}