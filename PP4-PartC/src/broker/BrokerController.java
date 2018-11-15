package broker;

import java.util.ArrayList;
import java.util.HashMap;

import serializedData.*;

public class BrokerController {
    private HashMap<String, ArrayList<PeerInfo>> registeredFiles;
    private HashMap<Integer, Integer> peers;

    public BrokerController() {
        this.registeredFiles = new HashMap<>();
        this.peers = new HashMap<>();
    }

    public synchronized PeerInfo searchFiles(PeerInfo p) {
        if (!this.peers.containsKey(p.getUserID()) ||
                (this.peers.containsKey(p.getUserID()) && this.peers.get(p.getUserID()) < 2)) {
            return new PeerInfo(p, Result.SEARCH_NO_FREE_RIDE);
        }

        if (this.registeredFiles.containsKey(p.getFileName())) {
            PeerInfo returnData = this.registeredFiles.get(p.getFileName()).get(0);
            returnData.setResult(Result.SEARCH_OK);
            System.out.println("Successfully found file!");
            return returnData;
        }
        return new PeerInfo(p, Result.SEARCH_FAIL);
    }

    public synchronized PeerInfo registerFile(PeerInfo p) {
        ArrayList<PeerInfo> list;
        if (this.registeredFiles.containsKey(p.getFileName())) {
            list = this.registeredFiles.get(p.getFileName());
        }
        else {
            list = new ArrayList<>();
        }
        list.add(p);
        this.registeredFiles.put(p.getFileName(), list);
        System.out.println("Successfully registered file!");
        System.out.println(this.registeredFiles);

        if (this.peers.containsKey(p.getUserID())) {
            int count = this.peers.get(p.getUserID());
            this.peers.put(p.getUserID(), ++count);
        }
        else {
            this.peers.put(p.getUserID(), 1);
        }

        return new PeerInfo(p, Result.REG_OK);
    }

    public synchronized PeerInfo unregisterFile(PeerInfo p) {
        if (this.registeredFiles.containsKey(p.getFileName()) && this.peers.containsKey(p.getUserID())) {
            ArrayList<PeerInfo> list = this.registeredFiles.get(p.getFileName());

            for (PeerInfo info : list) {
                if (p.equals(info)) {
                    list.remove(info);
                    break;
                }
            }
            if (list.size() == 0) { this.registeredFiles.remove(p.getFileName()); }
            else { this.registeredFiles.put(p.getFileName(), list); }

            System.out.println("Successfully unregistered file!");

            int count = this.peers.get(p.getUserID());
            this.peers.put(p.getUserID(), --count);

            return new PeerInfo(p, Result.UNREG_OK);
        }
        return new PeerInfo(p, Result.UNREG_FAIL);
    }
}