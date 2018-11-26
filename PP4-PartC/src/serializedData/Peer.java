package serializedData;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Objects;

public class Peer implements Comparable<Peer> {
    private int userId, rank;
    private InetAddress address;
    private HashMap<String, Integer> files;

    public Peer(int u, int r, InetAddress a, HashMap<String, Integer> f) {
        this.userId = u;
        this.rank = r;
        this.address = a;
        this.files = f;
    }

    public int getUserID() { return this.userId; }
    public void setUserID(int n) { this.userId = n; }

    public int getRank() { return this.rank; }
    public void setRank(int r) { this.rank = r; }
    public void decrementRank() { this.rank++; }

    public InetAddress getAddress() { return this.address; }
    public void setAddress(InetAddress i) { this.address = i; }

    public HashMap<String, Integer> getFiles() { return this.files; }
    public void setFiles(HashMap<String, Integer> f) { this.files = f; }
    public void addFile(String f, int port) { this.files.put(f, port); }
    public void removeFile(String f) { this.files.remove(f); }
    public int getPort(String f) { return this.files.get(f); }

    public boolean equals(Peer other) {
        return this.userId == other.getUserID() &&
               this.address.getHostAddress().equals(other.getAddress().getHostAddress()); 
    }

    public int compareTo(Peer other) {
        return Integer.compare(this.rank, other.rank);
    }

    @Override
    public int hashCode() { return Objects.hash(this.userId, this.address, this.rank); }

    public String toString() {
        return "[" + this.userId + " : " + this.address.getHostAddress() + " : " + this.rank + "]";
    }
}