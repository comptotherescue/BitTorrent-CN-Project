package Common;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Handshake {
	 private static final String HANDSHAKE_HEADER = "0000000000P2PFILESHARINGPROJ";
	 private static String message = "";
	 private static final int messageSize = 32;
	
	public static synchronized boolean verify(byte[] msg, int pId) {
			String recvdMsg = new String(msg);
			return recvdMsg.indexOf(pId) != -1 && recvdMsg.contains(HANDSHAKE_HEADER);
		}

	public static synchronized int getPid(byte[] msg) {
		byte[] remotePId = Arrays.copyOfRange(msg, msg.length - 4, msg.length);
		return Integer.parseInt(new String(remotePId));
	}
		
	public static synchronized byte[] getMessage() {
		// TODO Auto-generated method stub
		byte[] handShanke = new byte[messageSize];
		ByteBuffer bb = ByteBuffer.wrap(message.getBytes());
		bb.get(handShanke);
		return handShanke;
	}
	private static synchronized void init(String id) {
		message += HANDSHAKE_HEADER + id;
	}

	public static synchronized void setID(int Id) {
		init(id);
	}
	
	public static synchronized int getRemotePId(byte[] byt) {
		int too = byt.length;
		int fro = too - 4;
		byte[] bytes = Arrays.copyOfRange(byt, fro, too);
		String strtmp = new String(bytes, StandardCharsets.UTF_8);
		return Integer.parseInt(strtmp);
	}

}
