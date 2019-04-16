
package Common;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Timer;
import java.util.TimerTask;

import Config.*;
import Logger.GenerateLog;

public class ConnectionHandler {
	private static ConnectionHandler connectionHandler;
	private HashSet<Connection> allConnections;
	private HashSet<Connection> notInterested;
	private PriorityQueue<Connection> preferredNeighbors;
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
		notInterested = new HashSet<>();
		preferredNeighbors = new PriorityQueue<>(k + 1,
				(a, b) -> (int) a.getBytesDownloaded() - (int) b.getBytesDownloaded());
		broadcaster = BroadCastingThread.getInstance();
		commonFile = CommonFile.getInstance();
		allConnections = new HashSet<>();
		monitor();
	}

	// TODO: Stop timer task p when a peer has the entire file now choose neighbors randomly
	private void monitor() {
		new Timer().scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				if (peersWithFullFile.size() == n - 1 && commonFile.isCompleteFile()) {
					System.exit(0);
				}
				if (preferredNeighbors.size() > 1) {
					Connection conn = preferredNeighbors.poll();
					conn.setDownloadedbytes(0);
					ArrayList<Integer> preferredNeighborsList = new ArrayList<Integer>();
					broadcaster.addMessage(new Object[] { conn, Constants.Type.CHOKE, Integer.MIN_VALUE });
					conn.choked = true;
					 System.out.println("Choking:" + conn.getRemotePeerId());
					 for(Connection curConnection: allConnections) {
						 if(!notInterested.contains(curConnection) && !preferredNeighbors.contains(curConnection) && preferredNeighbors.size() < CommonInfo.getNumberOfPreferredNeighbors()) {
							 preferredNeighbors.add(curConnection);
							 if(curConnection.isChoked()) {
								 broadcaster.addMessage(new Object[] { curConnection, Constants.Type.UNCHOKE, Integer.MIN_VALUE });
								 curConnection.choked = false;
							 }
						 } else if(!notInterested.contains(curConnection) && preferredNeighbors.size() == CommonInfo.getNumberOfPreferredNeighbors() && curConnection.getBytesDownloaded() > preferredNeighbors.peek().getBytesDownloaded() ) {
							 Connection c = preferredNeighbors.poll();
							 broadcaster.addMessage(new Object[] { c, Constants.Type.CHOKE, Integer.MIN_VALUE });
							 preferredNeighbors.add(curConnection);
							 if(curConnection.isChoked()) {
								 broadcaster.addMessage(new Object[] { curConnection, Constants.Type.UNCHOKE, Integer.MIN_VALUE });
								 curConnection.choked = false;
							 }
						 }
						 
					 }
				}
			}
		}, new Date(), p * 1000);

		new Timer().scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				List<Connection> array = new ArrayList<>(allConnections);
				Collections.shuffle(array);
				for (Connection conn : array) {
					if (!notInterested.contains(conn) && !preferredNeighbors.contains(conn) && !conn.hasFile()) {
						 broadcaster.addMessage(new Object[] { conn, Constants.Type.UNCHOKE, Integer.MIN_VALUE });
						 GenerateLog.writeLog(conn.getRemotePeerId(),Constants.LOG_CHANGE_OPTIMISTICALLY_UNCHOKED_NEIGHBOR);
						 if(preferredNeighbors.size() < CommonInfo.getNumberOfPreferredNeighbors()) {
							 preferredNeighbors.add(conn);
						 }
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
			broadcaster.addMessage(new Object[] { conn, Constants.Type.HAVE, pieceIndex });
		}
	}

	
	 /* If preferredNeighbors < k, send unchoke & add to preferredNeighbors
	    Otherwise, remove from not interested & add to interested
	 */
	public synchronized void addInterestedConnection(int peerID, Connection connection) {
		if (preferredNeighbors.size() <= k && !preferredNeighbors.contains(connection)) {
			connection.setDownloadedbytes(0);
			preferredNeighbors.add(connection);
			broadcaster.addMessage(new Object[] { connection, Constants.Type.UNCHOKE, Integer.MIN_VALUE });
		}
		notInterested.remove(connection);
	}

	/*
	 * Remove from interested & add to not interested.
	 */
	public synchronized void addNotInterestedConnection(int peerID, Connection connection) {
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

	public synchronized  PriorityQueue<Connection> getPreferredNeighbors() {
		return preferredNeighbors;
	}
	public void addToPeersWithFullFile(int str) {
		peersWithFullFile.add(str);
	}
	
}


	