package object;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.Base64;

public class BrokerPacket implements Serializable {
    private static final long serialVersionUID = 1L;

    private String username, password, sessionID;
    private Action action;
    private Result result;
    private List<Room> rooms;

    public BrokerPacket(BrokerPacket packet, List<Room> rooms, Result result) {
        this.username = packet.getUser();
        this.password = packet.getPassword();
        this.action = packet.getAction();

        this.result = result;
        this.rooms = rooms;
    }

    public BrokerPacket(BrokerPacket packet, Action action) {
        this.username = packet.getUser();
        this.password = packet.getPassword();
        this.sessionID = packet.getSessionID();
        this.result = packet.getResult();
        this.rooms = packet.getRooms();
        
        this.action = action;
    }
    
    public BrokerPacket(BrokerPacket packet, Result result) {
        this.username = packet.getUser();
        this.password = packet.getPassword();
        this.sessionID = packet.getSessionID();
        this.action = packet.getAction();
        this.rooms = packet.getRooms();

        this.result = result;
    }

    public BrokerPacket() {
        this.username = "";
        this.password = "";
        this.sessionID = "";
        this.action = Action.UNKWN;
        this.result = Result.UNKWN;
        this.rooms = new ArrayList<>();
    }

    public String getUser() { return this.username; }
    public void setUser(String username) { this.username = username; }

    public String getPassword() { return this.password; }
    public void setPassword(String password) {
        this.password = Base64.getEncoder().encodeToString(password.getBytes());
    }

    public String getSessionID() { return this.sessionID; }
    public void setSessionID(String sessionID) { this.sessionID = sessionID; }

    public Action getAction() { return this.action; }
    public void setAction(Action action) { this.action = action; }

    public Result getResult() { return this.result; }
    public void setResult(Result result) { this.result = result; }

    public List<Room> getRooms() { return this.rooms; }
    public void setRooms(List<Room> rooms) { this.rooms = rooms; }
    public void addRoom(Room room) { this.rooms.add(room); }


    public String toString() {
        return "[" + this.username + " : " + this.action + " : " + this.result + "]";
    }
}