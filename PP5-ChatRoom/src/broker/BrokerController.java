package broker;


import java.util.List;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.ArrayList;
import java.util.Base64;
import java.security.SecureRandom;

import object.*;

public class BrokerController {
    private Map<String, String> peers;
    private List<Room> rooms;
    private Map<String, String> currentPeers;

    public BrokerController() {
        this.peers = new HashMap<>();
        this.rooms = new ArrayList<>();
        this.currentPeers = new HashMap<>();

        this.peers.put("user", Base64.getEncoder().encodeToString("somepassword".getBytes()));
        this.peers.put("bkim", Base64.getEncoder().encodeToString("test".getBytes()));
        this.peers.put("fred", Base64.getEncoder().encodeToString("goober".getBytes()));

        this.setupRooms();
    }

    public synchronized BrokerPacket register(BrokerPacket userPacket) {
        if (this.peers.containsKey(userPacket.getUser())) {
            return new BrokerPacket(userPacket, Result.REG_FAIL);
        }

        BrokerPacket returnData = new BrokerPacket(userPacket, Result.REG_OK);
        returnData.setSessionID(this.generateID());
        returnData.setRooms(this.rooms);

        this.peers.put(userPacket.getUser(), userPacket.getPassword());
        this.currentPeers.put(userPacket.getUser(), returnData.getSessionID());
        return returnData;
    }

    public synchronized BrokerPacket login(BrokerPacket userPacket) {
        if (this.currentPeers.containsKey(userPacket.getUser()) ||
                !this.peers.containsKey(userPacket.getUser()) ||
                !this.peers.get(userPacket.getUser()).equals(userPacket.getPassword())) {
            return new BrokerPacket(userPacket, Result.LOGIN_FAIL);
        }

        BrokerPacket returnData = new BrokerPacket(userPacket, Result.LOGIN_OK);
        returnData.setSessionID(this.generateID());
        returnData.setRooms(this.rooms);

        this.currentPeers.put(userPacket.getUser(), returnData.getSessionID());
        return returnData;
    }

    public synchronized BrokerPacket logout(BrokerPacket userPacket) {
        if (!this.currentPeers.containsKey(userPacket.getUser()) ||
                !userPacket.getSessionID().equals(this.currentPeers.get(userPacket.getUser()))) {
            return new BrokerPacket(userPacket, Result.LOGOUT_FAIL);
        }

        this.currentPeers.remove(userPacket.getUser());
        return new BrokerPacket(userPacket, Result.LOGOUT_OK);
    }

    public synchronized BrokerPacket getRooms(BrokerPacket userPacket) {
        if (!this.currentPeers.containsKey(userPacket.getUser())) {
            return new BrokerPacket(userPacket, Result.UNAUTH);
        }

        BrokerPacket returnPacket = new BrokerPacket(userPacket, Result.GET_ROOMS_OK);
        returnPacket.setRooms(this.rooms);
        return returnPacket;
    }

    private String generateID() {
        Random random = new SecureRandom();
        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lower = upper.toLowerCase(Locale.ROOT);
        String digits = "0123456789";
        String alphanum = upper + lower + digits;
        char[] symbols = alphanum.toCharArray();
        char[] buf = new char[20];

        for (int idx = 0; idx < buf.length; ++idx)
            buf[idx] = symbols[random.nextInt(symbols.length)];
        return new String(buf);
    }

    private void setupRooms() {
        Room room0, room1, room2;

        room0 = new Room("Boston College News", "233.1.2.3", 5555);
        room1 = new Room("CS Department News", "233.1.2.4", 5556);
        room2 = new Room("Red Sox Stink", "233.1.2.5", 5557);
        
        this.rooms.add(room0);
        this.rooms.add(room1);
        this.rooms.add(room2);
    }
}