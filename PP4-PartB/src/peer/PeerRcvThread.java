package peer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.time.LocalDateTime;

import serializedData.*;

public class PeerRcvThread implements Runnable {
    private PeerInfo peerInfo;
    private Socket peerSocket;
    private InputStream inputStream;

    public PeerRcvThread(PeerInfo p) throws IOException {
        this.peerInfo = p;
        this.peerSocket = new Socket(this.peerInfo.getIP(), this.peerInfo.getPort());
        this.inputStream = this.peerSocket.getInputStream();
    }

    public void run() {
        try {
            System.out.println(Thread.currentThread().getName() + ": Requesting the file!");
            this.getRequestFile();
            // System.out.println(Thread.currentThread().getName() + ": Got something!");
            // this.sendRequestToFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + ": Finished writing the file! Closing down!");
    }

    private void getRequestFile() throws IOException {
		try {
            int dataSize;
            System.out.println(Thread.currentThread().getName() + ": writing to file\n");
            String fileName = LocalDateTime.now().toString() + "-output.txt";
            File outputFile = new File(fileName);
            outputFile.createNewFile();

            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
            // Wait for something to come
            System.out.println(Thread.currentThread().getName() + ": Waiting for something to come around!");
			while ((dataSize = this.inputStream.available()) == 0) {  }
            
            byte[] recvBuff = new byte[dataSize];
            while ((dataSize = this.inputStream.read(recvBuff, 0, dataSize)) != -1) {
                String reqString = new String(recvBuff, 0, dataSize);

                if (reqString.contains("File doesn't exist")) {
                    System.out.println("File couldn't be found...");
                    writer.close();
                    this.peerSocket.close();
                    return;
                }

                // System.out.println(Thread.currentThread().getName() + ": " + reqString);
                writer.write(reqString);
            }
            writer.close();
            this.peerSocket.close();
		} catch (IOException ex) {
			System.err.println("IOException in getRequest");
        }
    }
}