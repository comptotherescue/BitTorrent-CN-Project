package Common;

public class Handshake {
	 private static final String HANDSHAKE_HEADER = "P2PFILESHARINGPROJ";
	public static byte[] getMessage(int peerId) {
		// TODO Auto-generated method stub
		 byte[] message = new byte[32];
	        byte[] handshakeHeaderBytes = HANDSHAKE_HEADER.getBytes();
	        int handshakeHeaderLength = handshakeHeaderBytes.length;
	        System.arraycopy(handshakeHeaderBytes, 0, message, 0, handshakeHeaderLength);
	        for(int i = handshakeHeaderLength; i < handshakeHeaderLength + 10; i++) {
	            message[i] = 0;
	        }
	        char[] peerIdArr = (peerId + "").toCharArray();
	        for(int i = 0; i < 4; i++) {
	            message[handshakeHeaderLength + 10 + i] = (byte) peerIdArr[i];
	        }

	        return message;
	}
	public static byte[] getMessage() {
		// TODO Auto-generated method stub
		return null;
	}

}
