package serializedData;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Objects;

public class Peer implements Comparable<Peer> {
    private int userId, port, rank;
    private InetAddress address;
    private ArrayList<String> files;
    

    public Peer(int u, int r, int p, InetAddress a, ArrayList<String> f) {
        this.userId = u;
        this.rank = r;
        this.port = p;
        this.address = a;
        this.files = f;
    }

    public int getUserID() { return this.userId; }
    public void setUserID(int n) { this.userId = n; }

    public int getRank() { return this.rank; }
    public void setRank(int r) { this.rank = r; }
    public void decrementRank() { this.rank--; }

    public int getPort() { return this.port; }
    public void setPort(int p) { this.port = p; }

    public InetAddress getAddress() { return this.address; }
    public void setAddress(InetAddress i) { this.address = i; }

    public ArrayList<String> getFiles() { return this.files; }
    public void setFiles(ArrayList<String> f) { this.files = f; }
    public void addFile(String f) { this.files.add(f); }
    public void removeFile(String f) { this.files.remove(f); }

    public boolean equals(Peer other) {
        return this.userId == other.getUserID() &&
               this.address.getHostAddress().equals(other.getAddress().getHostAddress()); 
    }

    public int compareTo(Peer other) {
        return Integer.compare(this.rank, other.rank);
    }

    @Override
    public int hashCode() { return Objects.hash(this.userId, this.port, this.address, this.rank); }

    public String toString() {
        return "[" + this.userId + " : " + this.address.getHostAddress();
    }
}