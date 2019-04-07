package Common;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;

public class Download implements Runnable{
	
	private DataInputStream in;
	private SharedData sharedData;
	private Socket skt;
	private boolean isAlive;
	
	    // This is for Client initialization
		public Download(Socket skt, int peerId, SharedData data) {
			init(skt, data);
		}

		private void init(Socket skt, SharedData data) {
			this.skt = skt;
			sharedData = data;
			isAlive = true;
			try {
				in = new DataInputStream(skt.getInputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// This is for Server initialization
		public Download(Socket skt, SharedData data) {
			init(skt, data);
		}
		
		@Override
		public void run() {
			receiveMessage();
		}

		

		private synchronized boolean isAlive() {
			// TODO Auto-generated method stub
			return isAlive;
		}

		

		private void receiveMsgPayL(byte[] payload) {
			receiveRawData(payload);
		}

		private void receiveRawData(byte[] message) {
			try {
				in.readFully(message);
			} catch (EOFException e) {
				System.exit(0);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		private int receiveMsgLen() {
			int len = Integer.MIN_VALUE;
			byte[] msgLen = new byte[4];
			try {
				receiveRawData(msgLen);
				len = ByteBuffer.wrap(msgLen).getInt();
			} catch (Exception e) {
				// isAlive = false;
				e.printStackTrace();
			}
			return len;
		}
		
		protected void receiveMessage() {
			// System.out.println("Receive started");
			while (isAlive()) {
				// System.out.println("Receive started");
				int messageLength = Integer.MIN_VALUE;
				messageLength = receiveMsgLen();
				if (!isAlive()) {
					continue;
				}
				byte[] payL = new byte[messageLength];
				receiveMsgPayL(payL);
				sharedData.addPayload(payL);
				// System.out.println("Receive finished");
			}

		}
}