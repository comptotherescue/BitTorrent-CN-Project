import java.util.concurrent.LinkedBlockingQueue;

import Common.Connection;
import Common.Constants;
import Message.MessageHandler;

public class BroadCastingThread extends Thread {
	private LinkedBlockingQueue<Object[]> queue;
	private MessageHandler messageManager;
	private Connection conn;
	private Constants.Type messageType;
	private int pieceIndex;
	private static BroadCastingThread broadcaster;

	private BroadCastingThread() {
		queue = new LinkedBlockingQueue<>();
		messageManager = MessageHandler.getInstance();
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
			queue.put(data);
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
			int messageLength = messageManager.getMessageLength(messageType, pieceIndex);
			byte[] payload = messageManager.getMessagePayload(messageType, pieceIndex);
			conn.sendMessage(messageLength, payload);
			System.out.println("Broadcaster: Sending " + messageType + " to peer " + conn.getRemotePeerId());

		}
	}

	private Object[] retrieveMessage() {
		Object[] data = null;
		try {
			data = queue.take();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}

}