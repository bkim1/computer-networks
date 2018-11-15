package broker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import serializedData.*;

public class BrokerController {
    private Map<String, PriorityQueue<Peer>> registeredFiles;
    private Map<Integer, Peer> peers;

    public BrokerController() {
        this.registeredFiles = new HashMap<>();
        this.peers = new HashMap<>();
    }

    public synchronized PeerInfo searchFiles(PeerInfo p) {
        System.out.println("Peers: " + this.peers);
        if (!this.peers.containsKey(p.getUserID()) ||
                (this.peers.get(p.getUserID()).getFiles().size() < 2)) {
            return new PeerInfo(p, Result.SEARCH_NO_FREE_RIDE);
        }

        if (this.registeredFiles.containsKey(p.getFileName())) {
            Peer peer = this.registeredFiles.get(p.getFileName()).peek();
            PeerInfo returnData = this.constructPeerInfo(peer, p.getFileName(), Result.SEARCH_OK);
            System.out.println("Successfully found file!");
            return returnData;
        }
        return new PeerInfo(p, Result.SEARCH_FAIL);
    }

    public synchronized PeerInfo registerFile(PeerInfo p) {
        PriorityQueue<Peer> pq;
        if (this.registeredFiles.containsKey(p.getFileName())) {
            pq = this.registeredFiles.get(p.getFileName());
        }
        else {
            pq = new PriorityQueue<>();
        }
        Peer peer;
        if (this.peers.containsKey(p.getUserID())) {
            peer = this.peers.get(p.getUserID());
        }
        else {
            peer = new Peer(p.getUserID(), 10, p.getPort(), p.getIP(), new ArrayList<>());
        }
        peer.addFile(p.getFileName());
        pq.add(peer);

        this.peers.put(p.getUserID(), peer);
        this.registeredFiles.put(p.getFileName(), pq);
        System.out.println("Successfully registered file!");
        System.out.println(this.registeredFiles);

        return this.constructPeerInfo(peer, p.getFileName(), Result.REG_OK);
    }

    public synchronized PeerInfo unregisterFile(PeerInfo p) {
        if (this.peers.containsKey(p.getUserID())) {
            PriorityQueue<Peer> pq = this.registeredFiles.get(p.getFileName());
            Peer peer = this.peers.get(p.getUserID());
            for (Peer otherPeer : pq) {
                if (peer.equals(otherPeer)) {
                    pq.remove(otherPeer);
                    break;
                }
            }
            if (pq.size() == 0) { this.registeredFiles.remove(p.getFileName()); }
            else { this.registeredFiles.put(p.getFileName(), pq); }

            System.out.println("Successfully unregistered file!");

            peer.removeFile(p.getFileName());
            this.peers.put(p.getUserID(), peer);

            return this.constructPeerInfo(peer, p.getFileName(), Result.UNREG_OK);
        }
        return new PeerInfo(p, Result.UNREG_FAIL);
    }

    public synchronized PeerInfo adjustRanking(PeerInfo p) {
        if (this.peers.containsKey(p.getPeerID())) {
            Peer peer = this.peers.get(p.getPeerID());
            peer.decrementRank();
            this.peers.put(p.getPeerID(), peer);
            return this.constructPeerInfo(peer, Result.ADJUST_RANK_OK);
        }
        return new PeerInfo(p, Result.ADJUST_RANK_FAIL);
    }

    private PeerInfo constructPeerInfo(Peer p, Result r) {
        PeerInfo returnData = new PeerInfo();
        returnData.setIP(p.getAddress());
        returnData.setPeerID(p.getUserID());
        returnData.setPort(p.getPort());
        returnData.setResult(r);

        if (r == Result.REG_OK || r == Result.UNREG_OK) {
            returnData.setUserID(p.getUserID());
        }

        return returnData;
    }

    private PeerInfo constructPeerInfo(Peer p, String fileName, Result r) {
        PeerInfo returnData = new PeerInfo();
        returnData.setIP(p.getAddress());
        returnData.setPeerID(p.getUserID());
        returnData.setPort(p.getPort());
        returnData.setFileName(fileName);
        returnData.setResult(r);

        if (r == Result.REG_OK || r == Result.UNREG_OK) {
            returnData.setUserID(p.getUserID());
        }

        return returnData;
    }
}