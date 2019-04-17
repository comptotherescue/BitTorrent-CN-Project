/**
 * 
 */
package Peer;

import java.util.HashMap;
import java.util.Map;

import Logger.GenerateLog;
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
		int count = peerStatMap.getOrDefault(index, 0);
		if(peerStatMap.containsKey(index)) {
			peerStatMap.put(index,count+1);
		} else {
			peerStatMap.put(index,count);
		}	
	}

	public synchronized void printStat(){
		for(Map.Entry<Integer, Integer> entry : peerStatMap.entrySet()) {
			GenerateLog.writeLog(":Received From PID:"+entry.getKey()+":No. of pieces recieved:"+entry.getValue());
		}
	}

}
