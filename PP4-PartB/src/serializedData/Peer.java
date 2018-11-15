package serializedData;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.Objects;

public class Peer implements Serializable {
    private static final long serialVersionUID = 1L;

    private String username;
    private InetAddress address;

    public Peer(String n, InetAddress a) {
        this.username = n;
        this.address = a;
    }

    public String getUsername() { return this.username; }
    public void setUsername(String n) { this.username = n; }

    public InetAddress getAddress() { return this.address; }
    public void setAddress(InetAddress i) { this.address = i; }

    public boolean equals(Peer other) {
        return this.username.equals(other.getUsername()) &&
               this.address.getHostAddress().equals(other.getAddress().getHostAddress()); 
    }

    @Override
    public int hashCode() { return Objects.hash(this.username, this.address); }

    public String toString() {
        return "[" + this.username + " : " + this.address.getHostAddress();
    }
}