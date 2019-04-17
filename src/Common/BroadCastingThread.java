package Common;

import java.util.concurrent.LinkedBlockingQueue;

import Message.MessageHandler;

public class BroadCastingThread extends Thread {
	
	private Connection conn;
	private MessageHandler messageHandler;
	private LinkedBlockingQueue<Object[]> broadCastQueue;
	private static BroadCastingThread broadcaster;
	private Constants.Type messageType;
	private int pieceIndex;
	
	private BroadCastingThread() {
		broadCastQueue = new LinkedBlockingQueue<>();
		messageHandler = MessageHandler.getInstance();
		conn = null;
		messageType = null;
		pieceIndex = Integer.MIN_VALUE;
	}
	
	@Override
	public void run() {
		while (true) {
			Object[] data = retrieveMessage();
			conn = (Connection) data[0];
			messageType = (Constants.Type)data[1];
			pieceIndex = (int) data[2];
			System.out.println(
					"Broadcaster: Building " + messageType + pieceIndex + " to peer " + conn.getRemotePeerId());
			int messageLength = messageHandler.getMessageLength(messageType, pieceIndex);
			byte[] payload =  messageHandler.getMessagePayload(messageType, pieceIndex);
			conn.sendMessage(messageLength, payload);
			System.out.println("Broadcaster: Sending " + messageType + " to peer " + conn.getRemotePeerId());

		}
	}
	
	private Object[] retrieveMessage() {
		Object[] data = null;
		try {
			data = broadCastQueue.take();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}

	protected static synchronized BroadCastingThread getInstance() {
		if (broadcaster == null) {
			broadcaster = new BroadCastingThread();
			broadcaster.start();
		}
		return broadcaster;
	}

	protected synchronized void addMessage(Object[] data) {
		try {
			broadCastQueue.put(data);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	

}