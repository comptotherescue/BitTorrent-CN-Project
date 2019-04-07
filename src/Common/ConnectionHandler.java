/**
 * 
 */
package Common;

/**
 * @author sonal
 *
 */
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
	// private HashMap<String, Connection> interested; // interested but choked
	private HashSet<Connection> notInterested;
	// private ArrayList<String> interestedPeerIds;
	private PriorityQueue<Connection> preferredNeighbors;
	public HashSet<String> peersWithFullFile = new HashSet<String>();
	
     
	private int k =  CommonInfo.getNumberOfPreferredNeighbors();
	private int m = (int)CommonInfo.getOptimisticUnchokingInterval();
	private int p = (int)CommonInfo.getUnchokingInterval();
	private int n = PeerInfo.numberOfPeers();
	private SharedFile sharedFile;
	
//	private BroadcastThread broadcaster;

	public int getPeersWithFile() {
		return peersWithFullFile.size();
	}

	private ConnectionHandler() {
		notInterested = new HashSet<>();
		preferredNeighbors = new PriorityQueue<>(k + 1,
				(a, b) -> (int) a.getBytesDownloaded() - (int) b.getBytesDownloaded());
		//broadcaster = BroadcastThread.getInstance();
		sharedFile = SharedFile.getInstance();
		allConnections = new HashSet<>();
		monitor();
	}

	// TODO: Stop timer task p when a peer has the entire file now choose neighbors randomly
	private void monitor() {
		new Timer().scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				if (peersWithFullFile.size() == n - 1 && sharedFile.isCompleteFile()) {
					System.exit(0);
				}
				if (preferredNeighbors.size() > 1) {
					Connection conn = preferredNeighbors.poll();
					conn.setDownloadedbytes(0);
					ArrayList<Integer> preferredNeighborsList = new ArrayList<Integer>();
					for (Connection connT : preferredNeighbors) {
						preferredNeighborsList.add(connT.peerID);
						connT.setDownloadedbytes(0);
					}
				//	broadcaster.addMessage(new Object[] { conn, Message.Type.CHOKE, Integer.MIN_VALUE });
				GenerateLog.writeLog(preferredNeighborsList, Constants.LOG_CHANGE_OF_PREFERREDNEIGHBORS);
					// System.out.println("Choking:" + conn.getRemotePeerId());
				}
			}
		}, new Date(), p * 1000);

		new Timer().scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				for (Connection conn : allConnections) {
					if (!notInterested.contains(conn) && !preferredNeighbors.contains(conn) && !conn.hasFile()) {
					//	broadcaster.addMessage(new Object[] { conn, Message.Type.UNCHOKE, Integer.MIN_VALUE });
						preferredNeighbors.add(conn);
						GenerateLog.writeLog(conn.getRemotePeerId(),Constants.LOG_CHANGE_OPTIMISTICALLY_UNCHOKED_NEIGHBOR);
					}
				}
			}
		}, new Date(), m * 1000);

	}

	public static synchronized ConnectionHandler getInstance() {
		if (connectionHandler == null) {
			connectionHandler = new ConnectionHandler();
		}
		return connectionHandler;
	}

	protected synchronized void tellAllNeighbors(int pieceIndex) {
		for (Connection conn : allConnections) {
		//	broadcaster.addMessage(new Object[] { conn, Message.Type.HAVE, pieceIndex });
		}
	}

	/*
	 * If preferredNeighbors < k, send unchoke & add to preferredNeighbors
	 * Otherwise, remove from not interested & add to interested
	 */
	public synchronized void addInterestedConnection(int peerID, Connection connection) {
		if (preferredNeighbors.size() <= k && !preferredNeighbors.contains(connection)) {
			connection.setDownloadedbytes(0);
			preferredNeighbors.add(connection);
		//	broadcaster.addMessage(new Object[] { connection, Message.Type.UNCHOKE, Integer.MIN_VALUE });
		}
		notInterested.remove(connection);
	}

	/*
	 * Remove from interested & add to not interested.
	 */
	public synchronized void addNotInterestedConnection(String peerId, Connection connection) {
		notInterested.add(connection);
		preferredNeighbors.remove(connection);
	}

	public synchronized void createConnection(Socket socket, int peerId) {
		new Connection(socket, peerId);
	}

	public synchronized void createConnection(Socket socket) {
		new Connection(socket);
	}

	public String getTime() {
		return Calendar.getInstance().getTime() + ": ";
	}

	public synchronized void addAllConnections(Connection connection) {
		// TODO Auto-generated method stub
		allConnections.add(connection);
	}

	public void addToPeersWithFullFile(String str) {
		peersWithFullFile.add(str);
	}
	
}


	