package Common;

import java.util.BitSet;

import Config.CommonInfo;
import Message.Message;

public class BitField extends Message {

	private static BitField bitfield;
	private SharedFile sharedFile;

	private BitField() {
		init();
	}

	private void init() {
		b_type = 5;
		b_payload = new byte[CommonInfo.getNumberOfPieces() + 1];
		b_content = new byte[CommonInfo.getNumberOfPieces()];
		sharedFile = SharedFile.getInstance();
		b_payload[0] = b_type;
		BitSet filePieces = sharedFile.getFilePieces();
		for (int i = 0; i < CommonInfo.getNumberOfPieces(); i++) {
			if (filePieces.get(i)) {
				// content[i] = 1;
				b_payload[i + 1] = 1;
			}
		}
	}

	public synchronized static BitField getInstance() {
		if (bitfield == null) {
			bitfield = new BitField();
		}
		return bitfield;
	}

	@Override
	public
	synchronized int getMessageLength() {
		init();
		return b_payload.length;
	}

	@Override
	public
	synchronized byte[] getPayload() {
		return b_payload;
	}

}
