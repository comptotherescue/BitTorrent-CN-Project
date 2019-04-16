package Config;
import java.io.*;
import java.util.*;

import Common.ConfigurationException;
import Common.Constants;
import Common.NetworkInfo;
import Peer.PeerStat;

public class PeerInfo {

	  private static HashMap<Integer, NetworkInfo> peerMap = new LinkedHashMap<>();
	  public static void loadPeerInfoConfig() {
		    
	        int id = 1;
	        Scanner sc = null;
	    	ConfigurationException configExp = new ConfigurationException(Constants.PeerInfoPath);
	        try {
				sc = new Scanner(new File(Constants.PeerInfoPath));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			while (sc.hasNextLine()) {
				String str[] = sc.nextLine().split(" ");
				NetworkInfo network = new NetworkInfo();
				network.setNumber(id++);
				network.setPeerId(Integer.parseInt(str[0]));
				PeerStat.getInstance().addStat(network.getPeerId());
				network.setHostName(str[1]);
				network.setPort(Integer.parseInt(str[2]));
				network.setHasSharedFile(str[3].equals("1") ? true : false);
				peerMap.put(Integer.parseInt(str[0]), network);
			}
			PeerStat.getInstance().printStat();
			sc.close();    
	    }

	    public static NetworkInfo getPeer(int id) {
	        return peerMap.get(id);
	    }

	    public static HashMap<Integer, NetworkInfo> getAllPeers() {
	        return peerMap;
	    }

	    public static int numberOfPeers() {
			return peerMap.size();
		}

	  

}
