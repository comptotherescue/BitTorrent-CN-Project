package Common;

import java.net.Socket;
import java.util.BitSet;

public class Connection {
	Upload upload;
	Download download;
	SharedData sharedData;
	double bytesDownloaded;
	Socket peerSocket;
	String peerID;
	boolean choked;
	private ConnectionHandler connectionHandler = ConnectionHandler.getInstance();
	
	public Connection(Socket socket, String peerId) {
		// TODO Auto-generated constructor stub
	}

	public Connection(Socket socket) {
		// TODO Auto-generated constructor stub
	}

	public BitSet getPeerBitSet() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getBytesDownloaded() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setDownloadedbytes(int i) {
		// TODO Auto-generated method stub
		
	}

	public boolean hasFile() {
		// TODO Auto-generated method stub
		return false;
	}

	public void getRemotePeerId() {
		// TODO Auto-generated method stub
		
	}

	public void addInterestedConnection() {
		// TODO Auto-generated method stub
		
	}

	public void addBytesDownloaded(int length) {
		// TODO Auto-generated method stub
		
	}

	public void tellAllNeighbors(int pieceIndex) {
		// TODO Auto-generated method stub
		
	}

	public void addAllConnections() {
		// TODO Auto-generated method stub
		
	}

	public void setPeerId(String remotePeerId) {
		// TODO Auto-generated method stub
		
	}

	public void removeRequestedPiece() {
		// TODO Auto-generated method stub
		
	}

}
