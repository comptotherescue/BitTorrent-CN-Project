
package Common;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Timer;
import java.util.TimerTask;

import Config.*;
import Logger.GenerateLog;

public class ConnectionHandler {
	private static ConnectionHandler connectionHandler;
	private HashSet<Connection> allConnections;
	private HashSet<Connection> notInterestedPeers;
	private PriorityQueue<Connection> preferredNeighborsQueue;
	public HashSet<Integer> peersWithFullFile = new HashSet<Integer>();
	
     
	private int k =  CommonInfo.getNumberOfPreferredNeighbors();
	private int m = (int)CommonInfo.getOptimisticUnchokingInterval();
	private int p = (int)CommonInfo.getUnchokingInterval();
	private int n = PeerInfo.numberOfPeers();
	private CommonFile commonFile;
	
	private BroadCastingThread broadcaster;

	public int getPeersWithFile() {
		return peersWithFullFile.size();
	}
	
	private ConnectionHandler() {
		notInterestedPeers = new HashSet<>();
		preferredNeighborsQueue = new PriorityQueue<>(k + 1,
				(a, b) -> (int) a.getBytesDownloaded() - (int) b.getBytesDownloaded());
		broadcaster = BroadCastingThread.getInstance();
		commonFile = CommonFile.getInstance();
		allConnections = new HashSet<>();
		monitor();
	}
	
	public static synchronized ConnectionHandler getInstance() {
		if (connectionHandler == null) {
			connectionHandler = new ConnectionHandler();
		}
		return connectionHandler;
	}
	
	public synchronized void createConnection(Socket socket, int peerId) {
		new Connection(socket, peerId);
	}

	public synchronized void createConnection(Socket socket) {
		new Connection(socket);
	}

	// TODO: Stop timer task p when a peer has the entire file now choose neighbors randomly
	private void monitor() {
		new Timer().scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				if (peersWithFullFile.size() == n - 1 && commonFile.isCompleteFile()) {
					System.exit(0);
				} if (preferredNeighborsQueue.size() >= 1) {
					Connection conn = preferredNeighborsQueue.poll();
					conn.setDownloadedbytes(0);
					ArrayList<Integer> preferredNeighborsList = new ArrayList<Integer>();
					for (Connection connT : preferredNeighborsQueue) {
						preferredNeighborsList.add(connT.remotePid);
						connT.setDownloadedbytes(0);
					}
					broadcaster.addMessage(new Object[] { conn, Constants.Type.CHOKE, Integer.MIN_VALUE });
				GenerateLog.writeLog(preferredNeighborsList, Constants.LOG_CHANGE_OF_PREFERREDNEIGHBORS);
					 System.out.println("Choking:" + conn.getRemotePeerId());
				} else if(preferredNeighborsQueue.size() == 0 && commonFile.isCompleteFile() && allConnections.size()>0)
				{
					System.exit(0);
				}
			}
		}, new Date(), p * 1000);

		new Timer().scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				if (peersWithFullFile.size() == n - 1 && commonFile.isCompleteFile()) {
					System.out.println("Exit in optimistic of choke");
					System.exit(0);
				}
				for (Connection conn : allConnections) {
					if (!notInterestedPeers.contains(conn) && !preferredNeighborsQueue.contains(conn) && !conn.hasFile()) {
					    broadcaster.addMessage(new Object[] { conn, Constants.Type.UNCHOKE, Integer.MIN_VALUE });
					    preferredNeighborsQueue.add(conn);
						GenerateLog.writeLog(conn.getRemotePeerId(),Constants.LOG_CHANGE_OPTIMISTICALLY_UNCHOKED_NEIGHBOR);
					}
				}
			}
		}, new Date(), m * 1000);

	}

	
	protected synchronized void tellAllNeighbors(int pieceIndex) {
		for (Connection conn : allConnections) {
			broadcaster.addMessage(new Object[] { conn, Constants.Type.HAVE, pieceIndex });
		}
	}

	
	 /* If preferredNeighbors < k, send unchoke & add to preferredNeighbors
	    Otherwise, remove from not interested & add to interested
	 */
	public synchronized void addInterestedConnection(int peerID, Connection connection) {
		if (preferredNeighborsQueue.size() <= k && !preferredNeighborsQueue.contains(connection)) {
			connection.setDownloadedbytes(0);
			preferredNeighborsQueue.add(connection);
			broadcaster.addMessage(new Object[] { connection, Constants.Type.UNCHOKE, Integer.MIN_VALUE });
		}
		notInterestedPeers.remove(connection);
	}

	public synchronized void addNotInterestedConnection(int peerID, Connection connection) {
		notInterestedPeers.add(connection);
		preferredNeighborsQueue.remove(connection);
	}

	public synchronized void addAllConnections(Connection connection) {
		allConnections.add(connection);
	}

	public void addToPeersWithFullFile(int str) {
		peersWithFullFile.add(str);
	}
	
}


	