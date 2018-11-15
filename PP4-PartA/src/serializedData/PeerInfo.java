package serializedData;

import java.io.Serializable;
import java.net.InetAddress;


public class PeerInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private InetAddress ip;
    private int port;
    private String fileName;
    private Action action;

    public PeerInfo(InetAddress i, int p, String f, Action a) {
        this.ip = i;
        this.port = p;
        this.fileName = f;
        this.action = a;
    }

    public PeerInfo(PeerInfo p, Action a) {
        this.ip = p.getIP();
        this.port = p.getPort();
        this.fileName = p.getFileName();
        this.action = a;
    }

    public PeerInfo(PeerInfo p) {
        this.ip = p.getIP();
        this.port = p.getPort();
        this.fileName = p.getFileName();
        this.action = p.getAction();
    }

    public PeerInfo(Action a) {
        this.ip = null;
        this.fileName = null;
        this.port = 0;
        this.action = a;
    }

    public PeerInfo() {
        this.ip = null; 
        this.fileName = null;
        this.port = 0;
        this.action = null;
    }

    public Action getAction() { return this.action; }
    public void setAction(Action a) { this.action = a; }

    public InetAddress getIP() { return this.ip; }
    public void setIP(InetAddress i) { this.ip = i; }

    public int getPort() { return this.port; }
    public void setPort(int p) { this.port = p; }

    public String getFileName() { return this.fileName; }
    public void setFileName(String f) { this.fileName = f; }

    public boolean isEqual(PeerInfo other) {
        boolean ipEqual = this.ip.getHostAddress().equals(other.getIP().getHostAddress());
        boolean portEqual = this.port == other.getPort();
        boolean fileEqual = this.fileName.equals(other.getFileName());

        return ipEqual && portEqual && fileEqual;
    }

    public String toString() {
        return "IP: " + this.ip + "\n" +
               "Port: " + this.port + "\n" +
               "File: " + this.fileName + "\n" +
               "Action: " + this.action;
    }
}
