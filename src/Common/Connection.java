package Common;


import java.io.IOException;
import java.net.Socket;
import java.util.BitSet;

import Logger.GenerateLog;

public class Connection {
	Upload upload;
	Download download;
	SharedData sharedData;
	double bytesDownloaded;
	Socket peerSocket;
	int remotePid;
	boolean choked;
	private ConnectionHandler connectionHandler = ConnectionHandler.getInstance();
	
	public Connection(Socket socket, int peerId) {
		// TODO Auto-generated constructor stub
		this.peerSocket = socket;
		sharedData = new SharedData(this);
		upload = new Upload(socket, peerId, sharedData);
		download = new Download(socket, peerId, sharedData);
		createThreads(upload, download);
		GenerateLog.writeLog(peerId,Constants.LOG_TCP_CREATE_CONNECTION);
		sharedData.sendHandshake();
		sharedData.setUpload(upload);
		sharedData.start();
	}

	public Connection(Socket socket) {
		// TODO Auto-generated constructor stub
		this.peerSocket = socket;
		sharedData = new SharedData(this);
		upload = new Upload(socket, sharedData);
		download = new Download(socket, sharedData);
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

	public synchronized BitSet getPeerBitSet() {
		// TODO Auto-generated method stub
		return sharedData.getPeerBitSet();
	}

	protected Upload getUpload() {
		return upload;
	}
	
	public synchronized boolean isChoked() {
		return choked;
	}
	
	protected synchronized void addRequestedPiece(int pieceIndex) {
		SharedFile.getInstance().addRequestedPiece(this, pieceIndex);
	}
	
	public double getBytesDownloaded() {
		// TODO Auto-generated method stub
		return bytesDownloaded;
	}

	public synchronized void setDownloadedbytes(int i) {
		// TODO Auto-generated method stub
		bytesDownloaded = i;
		
	}

	public synchronized boolean hasFile() {
		// TODO Auto-generated method stub
		return sharedData.hasFile();
	}

	public synchronized int getRemotePeerId() {
		 return remotePid;
		// TODO Auto-generated method stub	
	}

	public synchronized void addInterestedConnection() {
		// TODO Auto-generated method stub
		connectionHandler.addInterestedConnection(remotePid, this);
	}

	
	public synchronized void addNotInterestedConnection() {
		connectionHandler.addNotInterestedConnection(remotePid, this);
	}
	
	public synchronized void addBytesDownloaded(long length) {
		// TODO Auto-generated method stub
		bytesDownloaded += length;
		
	}

	public synchronized void tellAllNeighbors(int pieceIndex) {
		// TODO Auto-generated method stub
		connectionHandler.tellAllNeighbors(pieceIndex);
	}

	public synchronized void addAllConnections() {
		// TODO Auto-generated method stub
		connectionHandler.addAllConnections(this);
	}

	public void setPeerId(int remotePeerId) {
		// TODO Auto-generated method stub
		remotePid = remotePeerId;
	}

	public synchronized void removeRequestedPiece() {
		// TODO Auto-generated method stub
		SharedFile.getInstance().removeRequestedPiece(this);
	}

	public void close() {
		// TODO Auto-generated method stub
		try {
			peerSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void receiveMessage() {
		download.receiveMessage();
	}

	public synchronized void sendMessage(int messageLength, byte[] payload) {
		// TODO Auto-generated method stub
		upload.addMessage(messageLength, payload);
	}

}
