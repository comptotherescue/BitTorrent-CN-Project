/**
 * 
 */
package Peer;

import java.util.HashMap;
import java.util.Map;

import Message.MessageHandler;

/**
 * @author sonal
 *
 */
public class PeerStat {

	/**
	 * 
	 */
	
	private HashMap<Integer,Integer> peerStatMap;
	private static PeerStat peerStat;
	
	public static synchronized PeerStat getInstance() {
		if (peerStat == null) {
			peerStat = new PeerStat();
		}
		return peerStat;
	}
	
	public PeerStat() {
		peerStatMap = new HashMap<Integer,Integer>();
	}
	
	public synchronized void addStat(int index){
		int count = peerStatMap.get(index);
		peerStatMap.put(index,count+1);	
	}

	public synchronized void printStat(){
		for(Map.Entry<Integer, Integer> entry : peerStatMap.entrySet()) {
			System.out.println("PID:"+entry.getValue()+":No. of pieces recieved:"+entry.getValue());
		}
	}

}