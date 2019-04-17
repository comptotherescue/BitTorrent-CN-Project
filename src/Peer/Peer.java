package Peer;

import Common.ConnectionHandler;
import Common.NetworkInfo;
import Config.PeerInfo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Peer {

	private static Peer host = new Peer();
	private NetworkInfo network;
	ConnectionHandler connectionHandler;
	public static boolean allPeersReceivedFiles = false;

	public Peer() {
		network = PeerInfo.getPeer(PeerProcess.getId());
		connectionHandler = ConnectionHandler.getInstance();
	}

	//Server socket creation
	public void listenForConnections() throws IOException {

		ServerSocket socket = null;
		try {
			socket = new ServerSocket(network.getPort());
			// TODO: End connection when all peers have received files
			while (false == allPeersReceivedFiles) {
				Socket peerSocket = socket.accept();
				connectionHandler.createConnection(peerSocket);
			}
		} catch (Exception e) {
			System.out.println("Closed exception");
		} finally {
			socket.close();
		}
	}

	//Client socket creation
		private void createConnection(NetworkInfo peerInfo) {
			int peerPort = peerInfo.getPort();
			String peerHost = peerInfo.getHostName();
			try {
				Socket clientSocket = new Socket(peerHost, peerPort);
				connectionHandler.createConnection(clientSocket, peerInfo.getPeerId());
				Thread.sleep(300);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		public static Peer getInstance() {
			return host;
		}

		public boolean hasFile() {
			return network.hasSharedFile();
		}
		
		public NetworkInfo getNetwork() {
			return network;
		}

		public void setNetwork(NetworkInfo network) {
			this.network = network;
		}
		
		public void createTCPConnections() {
		HashMap<Integer, NetworkInfo> map = PeerInfo.getAllPeers();
		int myNumber = network.getNumber();
		for (Integer peerId : map.keySet()) {
			NetworkInfo peerInfo = map.get(peerId);
			if (peerInfo.getNumber() < myNumber) {
				new Thread() {
					@Override
					public void run() {
						createConnection(peerInfo);
					}
				}.start();
			}
		}
	}
}