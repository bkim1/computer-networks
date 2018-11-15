package peer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;


public class PeerSendThread implements Runnable {
    private Socket peerSocket;
    private String fileName;
    private OutputStream outputStream;

    public PeerSendThread(Socket s, String f) throws IOException {
        this.peerSocket = s;
        this.fileName = f;
        this.outputStream = s.getOutputStream();
    }

    public void run() {
        File f = new File(this.fileName);   
        System.out.println(Thread.currentThread().getName() + ": Got request for " + this.fileName);

        try {
            Scanner fileReader = new Scanner(f);
            String line;

            System.out.println(Thread.currentThread().getName() + ": Starting file transfer for " +
                               this.peerSocket.getInetAddress().toString() +
                               ":" + this.peerSocket.getPort());
            
            int numLines = 0;
            while (fileReader.hasNext()) {
                line = fileReader.nextLine() + "\n";
                byte[] buf = line.getBytes();

                this.outputStream.write(buf, 0, buf.length);
                this.outputStream.flush();
                numLines++;
            }

            System.out.println(Thread.currentThread().getName() + ": " + numLines + " lines sent");
            fileReader.close();
            this.peerSocket.close();
        } catch (FileNotFoundException e) {
            System.out.println(Thread.currentThread().getName() + ": File does not exist");
            byte[] sendBuff = new byte[100];
            sendBuff = "File doesn't exist\n".getBytes();
            try {
                this.outputStream.write(sendBuff, 0, sendBuff.length);
                this.outputStream.flush();
            } catch (IOException err) { 
                err.printStackTrace();
            }

            return;
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
}