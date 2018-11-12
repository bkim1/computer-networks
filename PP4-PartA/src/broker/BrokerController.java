package broker;

import java.util.ArrayList;
import java.util.HashMap;

import serializedData.PeerInfo;

public class BrokerController {
    private HashMap<String, ArrayList<PeerInfo>> peers;

    public BrokerController() {
        this.peers = new HashMap<>();
    }

    public synchronized PeerInfo searchFiles(String file) {
        if (this.peers.containsKey(file)) {
            return this.peers.get(file).get(0);
        }
        return null;
    }

    public synchronized void registerFile(PeerInfo p) {
        ArrayList<PeerInfo> list;
        if (this.peers.containsKey(p.getFileName())) {
            list = this.peers.get(p.getFileName());
        }
        else {
            list = new ArrayList<>();
        }
        list.add(p);
        this.peers.put(p.getFileName(), list);
    }

    public synchronized void unregisterFile(PeerInfo p) {
        if (this.peers.containsKey(p.getFileName())) {
            ArrayList<PeerInfo> list = this.peers.get(p.getFileName());
            list.remove(p);
            this.peers.put(p.getFileName(), list);
        }
    }
}