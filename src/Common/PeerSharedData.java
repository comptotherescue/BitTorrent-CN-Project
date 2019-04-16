/**
 * 
 */
package Common;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Calendar;
import java.util.concurrent.LinkedBlockingQueue;

import Common.Constants.Type;
import Config.CommonInfo;
import Logger.GenerateLog;
import Peer.PeerProcess;
import Peer.PeerStat;
import Message.MessageHandler;

/**
 * @author sonal
 *
 */
public class PeerSharedData extends Thread{
	private volatile boolean bitfieldSent;
	private BitSet peerBitset;
	private int remotePeerId;
	private Connection conn;
	private volatile boolean uploadHandshake;
	private static boolean isComplete;
	private volatile boolean isHandshakeDownloaded;
	private CommonFile commonFile;
	private BroadCastingThread broadcaster;
	private boolean peerHasFile;
	int i = 0;
	private LinkedBlockingQueue<byte[]> payloadQueue;
	private boolean isAlive;
	UploadPiece uploadPiece;
	
	public PeerSharedData(Connection connection) {
		conn = connection;
		payloadQueue = new LinkedBlockingQueue<>();
		isAlive = true;
		isComplete = false;
		commonFile = CommonFile.getInstance();
		broadcaster = BroadCastingThread.getInstance();
		peerBitset = new BitSet(CommonInfo.getNumberOfPieces());
	}

	public void setUpload(UploadPiece value) {
		uploadPiece = value;
		if (getUploadHandshake()) {
			broadcaster.addMessage(new Object[] { conn, Constants.Type.HANDSHAKE, Integer.MIN_VALUE });
		}
	}

	@Override
	public void run() {
		while (isAlive) {
			try {
				byte[] p = payloadQueue.take();
				processPayload(p);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void addPayload(byte[] payload) {
		try {
			payloadQueue.put(payload);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public synchronized BitSet getPeerBitSet() {
		return peerBitset;
	}

	public synchronized void sendHandshake() {
		setUploadHandshake();
	}

	public synchronized void setUploadHandshake() {
		uploadHandshake = true;
	}

	public synchronized boolean getUploadHandshake() {
		return uploadHandshake;
	}

	public void updatePeerId(int peerId) {
		remotePeerId = peerId;
	}

	public synchronized int getRemotePeerId() {
		return remotePeerId;
	}

	public synchronized void setRemotePeerId(int remotePeerId) {
		this.remotePeerId = remotePeerId;
	}

	public synchronized void setBitfieldSent() {
		bitfieldSent = true;
	}

	public synchronized void setPeerBitset(byte[] payload) {
		for (int i = 1; i < payload.length; i++) {
			// System.out.print(payload[i]);
			if (payload[i] == 1) {
				peerBitset.set(i - 1);
			}
		}
		if (peerBitset.cardinality() == CommonInfo.getNumberOfPieces()) {
			peerHasFile = true;
			ConnectionHandler.getInstance().addToPeersWithFullFile(remotePeerId);
		}
	}

	public synchronized void updatePeerBitset(int index) {
		peerBitset.set(index);
		
		if (peerBitset.cardinality() == CommonInfo.getNumberOfPieces()) {
			ConnectionHandler.getInstance().addToPeersWithFullFile(remotePeerId);
			peerHasFile = true;
		}
	}

	protected void processPayload(byte[] payload) {
		Type messageType = getMessageType(payload[0]);
		Type responseMessageType = null;
		int pieceIndex = Integer.MIN_VALUE;
		System.out.println("Received message: " + messageType);
		GenerateLog.writeLog("Received message: " + messageType);
		switch (messageType) {
		case CHOKE:
			// clear requested pieces of this connection
			GenerateLog.writeLog(conn.getRemotePeerId(),Constants.LOG_CHOKING);
			conn.removeRequestedPiece();
			responseMessageType = null;
			break;
		case UNCHOKE:
			// respond with request
			GenerateLog.writeLog(conn.getRemotePeerId(),Constants.LOG_UNCHOKING);
			responseMessageType = Type.REQUEST;
			pieceIndex = commonFile.getRequestPieceIndex(conn);
			break;
		case INTERESTED:
			// add to interested connections
			GenerateLog.writeLog(conn.getRemotePeerId(),Constants.LOG_RECEIVE_INTERESTED_MESSAGE);
			conn.addInterestedConnection();
			responseMessageType = null;
			break;
		case NOTINTERESTED:
			// add to not interested connections
			GenerateLog.writeLog(conn.getRemotePeerId(),Constants.LOG_RECEIVE_NOT_INTERESTED_MESSAGE);
			conn.addNotInterestedConnection();
			responseMessageType = null;
			break;
		case HAVE:
			// update peer bitset
			// send interested/not interested
			pieceIndex = ByteBuffer.wrap(payload, 1, 4).getInt();
			GenerateLog.writeLog(conn.getRemotePeerId(),pieceIndex,Constants.LOG_RECEIVE_HAVE_MESSAGE);
			updatePeerBitset(pieceIndex);
			responseMessageType = getInterestedNotInterested();
			break;
		case BITFIELD:
			// update peer bitset
			// send interested/not interested
			setPeerBitset(payload);
			responseMessageType = getInterestedNotInterested();
			break;
		case REQUEST:
			// send requested piece
			responseMessageType = Type.PIECE;
			byte[] content = new byte[4];
			System.arraycopy(payload, 1, content, 0, 4);
			pieceIndex = ByteBuffer.wrap(content).getInt();
			 System.out.println(pieceIndex);
			 if(!ConnectionHandler.getInstance().getPreferredNeighbors().contains(getRemotePeerId())) {
				 conn.addInterestedConnection();
			 }
			if (pieceIndex == Integer.MIN_VALUE) {
				responseMessageType = null;
				checkIfPeerHasCompleteFile();
			}
			break;
		case PIECE:
			pieceIndex = ByteBuffer.wrap(payload, 1, 4).getInt();
			conn.addBytesDownloaded(payload.length);
			commonFile.setPiece(Arrays.copyOfRange(payload, 1, payload.length));
			GenerateLog.writeLog(conn.getRemotePeerId(),pieceIndex, commonFile.getReceivedFileSize(),Constants.LOG_DOWNLOAD_PEICE);
			PeerStat.getInstance().addStat(conn.getRemotePeerId());
			responseMessageType = Type.REQUEST;
			conn.tellAllNeighbors(pieceIndex);
			pieceIndex = commonFile.getRequestPieceIndex(conn);
			if (pieceIndex == Integer.MIN_VALUE) {
				if(!isComplete) {
					GenerateLog.writeLog(Constants.LOG_DOWNLOAD_COMPLETE);
					PeerStat.getInstance().printStat();
					isComplete = !isComplete;
				}
				commonFile.writeToFile(PeerProcess.getId());
				messageType = null;
				isAlive = false;
				responseMessageType = null;
			}
			break;
		case HANDSHAKE:
			remotePeerId = Handshake.getRemotePId(payload);
			conn.setPeerId(remotePeerId);
			conn.addAllConnections();
			//System.out.println("Handshake: " + responseMessageType);
			if (!getUploadHandshake()) {
				setUploadHandshake();
				GenerateLog.writeLog(remotePeerId,Constants.LOG_TCP_CREATE_CONNECTION);
			 	broadcaster.addMessage(new Object[] { conn, Constants.Type.HANDSHAKE, Integer.MIN_VALUE });
				System.out.println("Added " + messageType + " to broadcaster");
			}
			if (commonFile.hasAnyPieces()) {
				responseMessageType = Type.BITFIELD;
			}
			System.out.println("Response Message Type: " + responseMessageType);
			break;
		}

		if (null != responseMessageType) {
			System.out.println("Shared data if: Added " + responseMessageType + " to broadcaster");
			broadcaster.addMessage(new Object[] { conn, responseMessageType, pieceIndex });
		}
	}

	private boolean isInterested() {
		for (int i = 0; i < CommonInfo.getNumberOfPieces(); i++) {
			if (peerBitset.get(i) && !commonFile.isPieceAvailable(i)) {
				return true;
			}
		}
		return false;
	}

	public boolean hasFile() {
		return peerHasFile;
	}

	private Type getInterestedNotInterested() {
		if (isInterested()) {
			return Type.INTERESTED;
		}
		return Type.NOTINTERESTED;
	}

	private Type getMessageType(byte type) {
		MessageHandler messageHandler= MessageHandler.getInstance();
		if (!isHandshakeDownloaded()) {
			setHandshakeDownloaded();
			return Type.HANDSHAKE;
		}
		return messageHandler.getType(type);
	}

	private boolean isHandshakeDownloaded() {
		return isHandshakeDownloaded;
	}

	private void setHandshakeDownloaded() {
		isHandshakeDownloaded = true;
	}
	
	private synchronized void checkIfPeerHasCompleteFile() {
		if (peerBitset.cardinality() == CommonInfo.getNumberOfPieces()) {
			ConnectionHandler.getInstance().addToPeersWithFullFile(remotePeerId);
			peerHasFile = true;
		}
	} 
	public String getTime() {
		return Calendar.getInstance().getTime() + ": ";
	}

}
