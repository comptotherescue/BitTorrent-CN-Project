
package Common;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.LinkedBlockingQueue;

public class Upload implements Runnable{

	
	protected LinkedBlockingQueue<Integer> uploadLenQue;
	protected LinkedBlockingQueue<byte[]> uploadPayQue;
	private Socket skt;
	private DataOutputStream outStream;
	private boolean isAlive;
	// This is for Client thread initialization
		public Upload(Socket skt, String id, SharedData data) {
			init(skt, data);
		}

		// This is for Server thread initialization
		public Upload(Socket skt, SharedData data) {
			init(skt, data);
		}

		@Override
		public void run() {
			while (isAlive) {
				try {
					int msgLen = uploadLenQue.take();
					outStream.writeInt(msgLen);
					outStream.flush();
					byte[] payload = uploadPayQue.take();
					outStream.write(payload);
					outStream.flush();
				} catch (SocketException e) {
					isAlive = false;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		public void addMessage(int length, byte[] payload) {
			try {
				uploadLenQue.put(length);
				uploadPayQue.put(payload);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		private void init(Socket clientSocket, SharedData data) {
			uploadPayQue = new LinkedBlockingQueue<>();
			uploadLenQue = new LinkedBlockingQueue<>();
			isAlive = true;
			this.skt = clientSocket;
			// sharedData = data;
			try {
				outStream = new DataOutputStream(skt.getOutputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	

}
