package Common;

import java.util.concurrent.LinkedBlockingQueue;

import Message.MessageHandler;

public class BroadCastingThread extends Thread {
	
	private MessageHandler messageHandler;
	private Connection conn;
	private Constants.Type messageType;
	private int pieceIndex;
	private LinkedBlockingQueue<Object[]> que;
	private static BroadCastingThread broadcaster;

	private BroadCastingThread() {
		que = new LinkedBlockingQueue<>();
		messageHandler = MessageHandler.getInstance();
		conn = null;
		messageType = null;
		pieceIndex = Integer.MIN_VALUE;
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
			que.put(data);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
			data = que.take();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}

}