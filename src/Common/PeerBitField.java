package Common;

import java.nio.ByteBuffer;
import java.util.BitSet;

import Config.CommonInfo;

public class PeerBitField {

	private static PeerBitField peerBitfield;
	private CommonFile commonFile;
	protected byte b_type;
	protected ByteBuffer bb_bufferBytes;
	protected byte[] b_content;
	protected byte[] b_messageLength = new byte[4];
	protected byte[] b_payload;


	private PeerBitField() {
		init();
	}

	private void init() {
		b_type = 5;
		b_payload = new byte[CommonInfo.getNumberOfPieces() + 1];
		b_content = new byte[CommonInfo.getNumberOfPieces()];
		commonFile = CommonFile.getInstance();
		b_payload[0] = b_type;
		BitSet filePieces = commonFile.getFilePieces();
		for (int i = 0; i < CommonInfo.getNumberOfPieces(); i++) {
			if (filePieces.get(i)) {
				b_payload[i + 1] = 1;
			}
		}
	}

	public synchronized static PeerBitField getInstance() {
		if (peerBitfield == null) {
			peerBitfield = new PeerBitField();
		}
		return peerBitfield;
	}
	
	public synchronized byte[] getPayload() {
		return b_payload;
	}
	
	public synchronized int getMessageLength() {
		init();
		return b_payload.length;
	}


	

}
