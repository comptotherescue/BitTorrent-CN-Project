package Config;
import java.io.*;
import java.util.*;

import Common.Constants;
import Peer.Peer;

public class PeerInfo {

	  private static Map<Integer, Peer> peerMap;

	    public static Peer getPeer(int id) {
	        return peerMap.get(id);
	    }

	    public static List<Peer> getAllPeers() {
	        return new LinkedList<>(peerMap.values());
	    }

	    public static int numberOfPeers() {
			return peerMap.size();
		}

	    public PeerInfo(String peerInfoConfigFile) {
	        this.peerMap = new LinkedHashMap<>();
	        Scanner sc = null;
	        try {
				sc = new Scanner(new File(Constants.PeerInfoPath));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			while (sc.hasNextLine()) {
				String str[] = sc.nextLine().split(" ");
				Peer network = new Peer(Integer.parseInt(str[0]),str[1],Integer.parseInt(str[2]),str[3].equals("1") ? true : false);
				peerMap.put(Integer.parseInt(str[0]),network);
			}
			sc.close();    
	    }

}
