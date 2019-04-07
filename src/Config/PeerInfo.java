package Config;
import java.io.*;
import java.util.*;

import Common.Constants;
import Common.NetworkInfo;
import Peer.Peer;

public class PeerInfo {

	  private static HashMap<Integer, NetworkInfo> peerMap;

	    public static NetworkInfo getPeer(int id) {
	        return peerMap.get(id);
	    }

	    public static HashMap<Integer, NetworkInfo> getAllPeers() {
	        return peerMap;
	    }

	    public static int numberOfPeers() {
			return peerMap.size();
		}

	    public PeerInfo(String peerInfoConfigFile) {
	        this.peerMap = new LinkedHashMap<>();
	        int id = 1;
	        Scanner sc = null;
	        try {
				sc = new Scanner(new File(Constants.PeerInfoPath));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			while (sc.hasNextLine()) {
				String str[] = sc.nextLine().split(" ");
				//Peer network = new Peer(Integer.parseInt(str[0]),str[1],Integer.parseInt(str[2]),str[3].equals("1") ? true : false);
				NetworkInfo network = new NetworkInfo();
				network.setNumber(id++);
				network.setPeerId(Integer.parseInt(str[0]));
				network.setHostName(str[1]);
				network.setPort(Integer.parseInt(str[2]));
				network.setHasSharedFile(str[3].equals("1") ? true : false);
				peerMap.put(Integer.parseInt(str[0]), network);
			}
			sc.close();    
	    }

}
