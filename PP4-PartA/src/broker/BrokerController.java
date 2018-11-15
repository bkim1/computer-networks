package broker;

import java.util.ArrayList;
import java.util.HashMap;

import serializedData.PeerInfo;
import serializedData.Action;

public class BrokerController {
    private HashMap<String, ArrayList<PeerInfo>> peers;

    public BrokerController() {
        this.peers = new HashMap<>();
    }

    public synchronized PeerInfo searchFiles(PeerInfo p) {
        if (this.peers.containsKey(p.getFileName())) {
            PeerInfo returnData = this.peers.get(p.getFileName()).get(0);
            returnData.setAction(Action.SEARCH_OK);
            System.out.println("Successfully found file!");
            return returnData;
        }
        return new PeerInfo(p, Action.SEARCH_FAIL);
    }

    public synchronized PeerInfo registerFile(PeerInfo p) {
        ArrayList<PeerInfo> list;
        if (this.peers.containsKey(p.getFileName())) {
            list = this.peers.get(p.getFileName());
        }
        else {
            list = new ArrayList<>();
        }
        list.add(p);
        this.peers.put(p.getFileName(), list);
        System.out.println("Successfully registered file!");
        System.out.println(this.peers);
        return new PeerInfo(p, Action.REG_OK);
    }

    public synchronized PeerInfo unregisterFile(PeerInfo p) {
        if (this.peers.containsKey(p.getFileName())) {
            ArrayList<PeerInfo> list = this.peers.get(p.getFileName());

            for (PeerInfo info : list) {
                if (p.isEqual(info)) {
                    list.remove(info);
                    break;
                }
            }
            if (list.size() == 0) { this.peers.remove(p.getFileName()); }
            else { this.peers.put(p.getFileName(), list); }

            System.out.println("Successfully unregistered file!");
            return new PeerInfo(p, Action.UNREG_OK);
        }
        return new PeerInfo(p, Action.UNREG_FAIL);
    }
}