package serializedData;

import java.io.Serializable;
import java.net.InetAddress;


public class PeerInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private InetAddress ip;
    private int port, userId, peerId;
    private String fileName;
    private Action action;
    private Result result;

    public PeerInfo(InetAddress i, int p, String f, Action a, Result r, int u, int pId) {
        this.ip = i;
        this.port = p;
        this.fileName = f;
        this.action = a;
        this.result = r;
        this.userId = u;
        this.peerId = pId;
    }

    public PeerInfo(PeerInfo p, Action a) {
        this.ip = p.getIP();
        this.port = p.getPort();
        this.fileName = p.getFileName();
        this.result = p.getResult();
        this.userId = p.getUserID();
        this.peerId = p.getPeerID();
        this.action = a;
    }

    public PeerInfo(PeerInfo p, Result r) {
        this.ip = p.getIP();
        this.port = p.getPort();
        this.fileName = p.getFileName();
        this.action = p.getAction();
        this.userId = p.getUserID();
        this.peerId = p.getPeerID();
        this.result = r;
    }

    public PeerInfo(PeerInfo p) {
        this.ip = p.getIP();
        this.port = p.getPort();
        this.fileName = p.getFileName();
        this.action = p.getAction();
        this.result = p.getResult();
        this.userId = p.getUserID();
        this.peerId = p.getPeerID();
    }

    public PeerInfo(Action a, int p) {
        this.action = a;
        this.peerId = p;
    }

    public PeerInfo(Action a) {
        this.ip = null;
        this.fileName = null;
        this.port = 0;
        this.action = a;
    }

    public PeerInfo() {}

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

    public int getUserID() { return this.userId; }
    public void setUserID(int u) { this.userId = u; }

    public int getPeerID() { return this.peerId; }
    public void setPeerID(int p) { this.peerId = p; }

    public boolean equals(PeerInfo other) {
        boolean ipEqual = this.ip.getHostAddress().equals(other.getIP().getHostAddress());
        boolean portEqual = this.port == other.getPort();
        boolean fileEqual = this.fileName.equals(other.getFileName());
        boolean userIdEqual = this.userId == other.getUserID();

        return ipEqual && portEqual && fileEqual && userIdEqual;
    }

    public String toString() {
        return "IP: " + this.ip + "\n" +
               "Port: " + this.port + "\n" +
               "File: " + this.fileName + "\n" +
               "Action: " + this.action + "\n" +
               "Result: " + this.result + "\n" +
               "User ID: " + this.userId + "\n" +
               "Peer ID: " + this.peerId + "\n";
    }
}
