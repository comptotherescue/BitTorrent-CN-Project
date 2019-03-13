package Message;

import java.nio.ByteBuffer;

public abstract class Message {
	private byte b_type;
	private ByteBuffer bb_bufferBytes;
	private byte[] b_content;
	private byte[] b_messageLength = new byte[4];
	private byte[] b_payload;


	abstract protected int getMessageLength();

	abstract protected byte[] getPayload();

}
