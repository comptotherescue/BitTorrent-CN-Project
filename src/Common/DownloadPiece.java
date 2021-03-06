package Common;

import java.io.DataInputStream;
import java.net.SocketException;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;

public class DownloadPiece implements Runnable{
	
	private DataInputStream in;
	private SharedData sharedData;
	private Socket skt;
	private boolean isAlive;
	
		public DownloadPiece(Socket skt, SharedData data) {
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
			} catch (SocketException e) {
				isAlive = false;
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
				//isAlive = false;
				e.printStackTrace();
			}
			return len;
		}
		
		protected void receiveMessage() {
			while (isAlive()) {
				int messageLength = Integer.MIN_VALUE;
				messageLength = receiveMsgLen();
				if (!isAlive()) {
					continue;
				}
				byte[] payL = new byte[messageLength];
				receiveMsgPayL(payL);
				sharedData.addPayload(payL);
				//System.out.println("Receive finished");
			}
		}
}
