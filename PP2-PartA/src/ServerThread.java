import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ServerThread implements Runnable {
    Socket socket;
    InputStream recvStream;
    OutputStream sendStream;
    String request;
    String quote;

    // protected DatagramSocket socket = null;
    protected BufferedReader in = null;
    protected boolean moreQuotes = true;

    public ServerThread(Socket clntSock) throws IOException {
        this.socket = clntSock;
        this.recvStream = clntSock.getInputStream();
        this.sendStream = clntSock.getOutputStream();
    }

    public void run() {

    }
}