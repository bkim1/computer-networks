package object;

import java.io.Serializable;

public class Room implements Serializable {
    private static final long serialVersionUID = 2L;

    private String name, ip;
    private int port;

    public Room(String name, String ip, int port) {
        this.name = name;
        this.ip = ip;
        this.port = port;
    }

    public Room() {

    }

    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return this.ip; }
    public void setAddress(String ip) { this.ip = ip; }

    public int getPort() { return this.port; }
    public void setPort(int port) { this.port = port; }

    public boolean equals(Room otherRoom) {
        return this.name.equals(otherRoom.getName()) &&
               this.ip.equals(otherRoom.getAddress()) &&
               this.port == otherRoom.port;
    }

    public String toString() { 
        return "[Room: " + this.name + " @ " + this.ip + ":" + this.port + "]";
    }
}