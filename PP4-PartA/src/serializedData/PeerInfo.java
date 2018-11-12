package serializedData;

import java.io.Serializable;
import java.net.InetAddress;

public class PeerInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private InetAddress ip;
    private int port;
    private String fileName;

    public PeerInfo(InetAddress i, int p, String f) {
        this.ip = i;
        this.port = p;
        this.fileName = f;
    }

    public PeerInfo() {
        this.ip = null; 
        this.fileName = null;
        this.port = 0;
    }

    public InetAddress getIP() { return this.ip; }
    public void setIP(InetAddress i) { this.ip = i; }

    public int getPort() { return this.port; }
    public void setPort(int p) { this.port = p; }

    public String getFileName() { return this.fileName; }
    public void setFileName(String f) { this.fileName = f; }
}
