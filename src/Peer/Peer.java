package Peer;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Peer {
	 private Map<Integer, Peer> peerEntries;
	 public Peer getPeer(int peerId) {
	        return peerEntries.get(peerId);
	 }

	 public List<Peer> getAllPeers() {
	        return new LinkedList<>(peerEntries.values());
	 }
	 
	public Peer() {
		// TODO Auto-generated constructor stub
	}

}
public PeerInfoConfig(String peerInfoConfigFile) {
    this.peerMap = new LinkedHashMap<>();
    try {
        loadPeerInfoConfig(peerInfoConfigFile);
        for(Map.Entry<Integer, Peer> entry : peerMap.entrySet()) {
            entry.getValue().setNeighborMap(new LinkedHashMap<>(peerMap));
        }
    } catch (Exception e) {
        System.out.println("Error initializing PeerInfoConfig");
        e.printStackTrace();
    }
}