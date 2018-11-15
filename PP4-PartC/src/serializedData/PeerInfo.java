package serializedData;

import java.io.Serializable;
import java.net.InetAddress;


public class PeerInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private InetAddress ip;
    private int port, userId;
    private String fileName;
    private Action action;
    private Result result;
    // private Peer peer;

    public PeerInfo(InetAddress i, int p, String f, Action a, Result r, int u) {
        this.ip = i;
        this.port = p;
        this.fileName = f;
        this.action = a;
        this.result = r;
        this.userId = u;
        // this.peer = peer;
    }

    public PeerInfo(PeerInfo p, Action a) {
        this.ip = p.getIP();
        this.port = p.getPort();
        this.fileName = p.getFileName();
        this.result = p.getResult();
        this.userId = p.getUserID();
        // this.peer = p.getPeer();
        this.action = a;
    }

    public PeerInfo(PeerInfo p, Result r) {
        this.ip = p.getIP();
        this.port = p.getPort();
        this.fileName = p.getFileName();
        this.action = p.getAction();
        // this.peer = p.getPeer();
        this.userId = p.getUserID();
        this.result = r;
    }

    public PeerInfo(PeerInfo p) {
        this.ip = p.getIP();
        this.port = p.getPort();
        this.fileName = p.getFileName();
        this.action = p.getAction();
        this.result = p.getResult();
        // this.peer = p.getPeer();
        this.userId = p.getUserID();
    }

    public PeerInfo(Action a) {
        this.ip = null;
        this.fileName = null;
        this.port = 0;
        this.action = a;
    }

    public Action getAction() { return this.action; }
    public void setAction(Action a) { this.action = a; }

    public Result getResult() { return this.result; }
    public void setResult(Result r) { this.result = r; }

    public InetAddress getIP() { return this.ip; }
    public void setIP(InetAddress i) { this.ip = i; }

    public int getPort() { return this.port; }
    public void setPort(int p) { this.port = p; }

    public String getFileName() { return this.fileName; }
    public void setFileName(String f) { this.fileName = f; }

    // public Peer getPeer() { return this.peer; }
    // public void setPeer(Peer p) { this.peer = p; }

    public int getUserID() { return this.userId; }
    public void setUserID(int u) { this.userId = u; }

    public boolean equals(PeerInfo other) {
        boolean ipEqual = this.ip.getHostAddress().equals(other.getIP().getHostAddress());
        boolean portEqual = this.port == other.getPort();
        boolean fileEqual = this.fileName.equals(other.getFileName());
        // boolean peerEqual = this.peer.equals(other.getPeer());
        boolean userIdEqual = this.userId == other.getUserID();

        return ipEqual && portEqual && fileEqual && userIdEqual;
    }

    public String toString() {
        return "IP: " + this.ip + "\n" +
               "Port: " + this.port + "\n" +
               "File: " + this.fileName + "\n" +
               "Action: " + this.action + "\n" +
               "Result: " + this.result + "\n" +
               "userId: " + this.userId;
    }
}
