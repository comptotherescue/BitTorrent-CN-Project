/**
 * 
 */
package Config;
import java.io.*;
import java.util.*;
import Peer.Peer;
/**
 * @author sonal
 *
 */
public class PeerInfo {

	  private Map<Integer, Peer> peerMap;

	    public Peer getPeer(int id) {
	        return peerMap.get(id);
	    }

	    public List<Peer> getAllPeers() {
	        return new LinkedList<>(peerMap.values());
	    }

	    public PeerInfo(String peerInfoConfigFile) {
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

	    public void loadPeerInfoConfig(String peerInfoConfigFile) {
	        BufferedReader bufferedReader = null;
	        try {
	            bufferedReader = new BufferedReader(new FileReader(peerInfoConfigFile));
	        } catch (FileNotFoundException fileNotFoundException) {
	            System.out.println("Could not find config file " + peerInfoConfigFile);
	            fileNotFoundException.printStackTrace();
	        }
	        if(bufferedReader != null) {
	            String line;
	            try {
	                while((line = bufferedReader.readLine()) != null) {
	                    String[] tokens = line.trim().split("\\s+");
	                    int peerId = Integer.parseInt(tokens[0]);
	                    String peerIpAddress = tokens[1];
	                    int peerPortNumber = Integer.parseInt(tokens[2]);
	                    boolean peerHasFile = (Integer.parseInt(tokens[3]) == 1);
	                    peerMap.put(peerId, new Peer(peerId, peerIpAddress, peerPortNumber, peerHasFile));
	                }
	            } catch (IOException ioException) {
	                System.out.println("IOException while reading config file " + peerInfoConfigFile);
	                ioException.printStackTrace();
	            }
	        }
	    }

}
