package Common;


import java.io.IOException;
import java.net.Socket;
import java.util.BitSet;

import Logger.GenerateLog;

public class Connection {
	UploadPiece uploadPiece;
	DownloadPiece downloadPiece;
	PeerSharedData peerSharedData;
	double bytesDownloaded;
	Socket peerSocket;
	int remotePid;
	boolean choked;
	private ConnectionHandler connectionHandler = ConnectionHandler.getInstance();
	
	public Connection(Socket socket, int peerId) {
		// TODO Auto-generated constructor stub
		this.peerSocket = socket;
		peerSharedData = new PeerSharedData(this);
		uploadPiece = new UploadPiece(socket, peerId, peerSharedData);
		downloadPiece = new DownloadPiece(socket, peerSharedData);
		createThreads(uploadPiece, downloadPiece);
		GenerateLog.writeLog(peerId,Constants.LOG_TCP_CREATE_CONNECTION);
		peerSharedData.sendHandshake();
		peerSharedData.setUpload(uploadPiece);
		peerSharedData.start();
	}

	public Connection(Socket socket) {
		// TODO Auto-generated constructor stub
		this.peerSocket = socket;
		peerSharedData = new PeerSharedData(this);
		uploadPiece = new UploadPiece(socket, peerSharedData);
		downloadPiece = new DownloadPiece(socket, peerSharedData);
		createThreads(uploadPiece, downloadPiece);
		peerSharedData.setUpload(uploadPiece);
		peerSharedData.start();
	}

	public void createThreads(UploadPiece uploadPiece, DownloadPiece downloadPiece) {
		Thread uploadThread = new Thread(uploadPiece);
		Thread downloadThread = new Thread(downloadPiece);
		uploadThread.start();
		downloadThread.start();
	}

	public synchronized BitSet getPeerBitSet() {
		// TODO Auto-generated method stub
		return peerSharedData.getPeerBitSet();
	}

	protected UploadPiece getUpload() {
		return uploadPiece;
	}
	
	public synchronized boolean isChoked() {
		return choked;
	}
	
	protected synchronized void addRequestedPiece(int pieceIndex) {
		CommonFile.getInstance().addRequestedPiece(this, pieceIndex);
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
		return peerSharedData.hasFile();
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
		CommonFile.getInstance().removeRequestedPiece(this);
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
		downloadPiece.receiveMessage();
	}

	public synchronized void sendMessage(int messageLength, byte[] payload) {
		// TODO Auto-generated method stub
		uploadPiece.addMessage(messageLength, payload);
	}

}
