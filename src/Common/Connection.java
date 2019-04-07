package Common;


import java.net.Socket;
import java.util.BitSet;

import Logger.GenerateLog;
import Peer.Peer;

public class Connection {
	Upload upload;
	Download download;
	SharedData sharedData;
	double bytesDownloaded;
	Socket peerSocket;
	int peerID;
	boolean choked;
	private ConnectionHandler connectionHandler = ConnectionHandler.getInstance();
	
	public Connection(Socket socket, int peerId) {
		// TODO Auto-generated constructor stub
		this.peerSocket = peerSocket;
		sharedData = new SharedData(this);
		upload = new Upload(peerSocket, peerId, sharedData);
		download = new Download(peerSocket, peerId, sharedData);
		createThreads(upload, download);
		GenerateLog.writeLog(peerId,Constants.LOG_TCP_CREATE_CONNECTION);
		sharedData.sendHandshake();
		sharedData.setUpload(upload);
		sharedData.start();
	}

	public Connection(Socket socket) {
		// TODO Auto-generated constructor stub
		this.peerSocket = peerSocket;
		sharedData = new SharedData(this);
		upload = new Upload(peerSocket, sharedData);
		download = new Download(peerSocket, sharedData);
		createThreads(upload, download);
		sharedData.setUpload(upload);
		sharedData.start();
	}
	public void createThreads(Upload upload, Download download) {
		Thread uploadThread = new Thread(upload);
		Thread downloadThread = new Thread(download);
		uploadThread.start();
		downloadThread.start();
	}

	public BitSet getPeerBitSet() {
		// TODO Auto-generated method stub
		return sharedData.getPeerBitSet();
	}

	public int getBytesDownloaded() {
		// TODO Auto-generated method stub
		return 0;
	}

	public synchronized void setDownloadedbytes(int i) {
		// TODO Auto-generated method stub
		bytesDownloaded = i;
		
	}

	public boolean hasFile() {
		// TODO Auto-generated method stub
		return sharedData.hasFile();
	}

	public synchronized int getRemotePeerId() {
		 return peerID;
		// TODO Auto-generated method stub
		
	}

	public synchronized void addInterestedConnection() {
		// TODO Auto-generated method stub
		connectionHandler.addInterestedConnection(peerID, this);
	}

	public void addBytesDownloaded(int length) {
		// TODO Auto-generated method stub

		
	}

	public void tellAllNeighbors(int pieceIndex) {
		// TODO Auto-generated method stub
		
	}

	public void addAllConnections() {
		// TODO Auto-generated method stub
		connectionHandler.addAllConnections(this);
	}

	public void setPeerId(int remotePeerId) {
		// TODO Auto-generated method stub
		
	}

	public void removeRequestedPiece() {
		// TODO Auto-generated method stub
		
	}

	public void close() {
		// TODO Auto-generated method stub
		
	}

	public void sendMessage(int messageLength, byte[] payload) {
		// TODO Auto-generated method stub
		
	}

}
