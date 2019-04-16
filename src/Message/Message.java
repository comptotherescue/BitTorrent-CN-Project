package Message;

import java.nio.ByteBuffer;

public abstract class Message {
	protected byte b_type;
	protected ByteBuffer bb_bufferBytes;
	protected byte[] b_content;
	protected byte[] b_messageLength = new byte[4];
	protected byte[] b_payload;


	abstract protected int getMessageLength();

	abstract protected byte[] getPayload();

}
