package Common;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import Config.CommonInfo;
import Peer.PeerProcess;

public class CommonFile extends Thread{
	private volatile HashMap<Connection, Integer> requestedPiecesMap;
	private static ConcurrentHashMap<Integer, byte[]> filemap;
	private static FileChannel writeFileChannel;
	private LinkedBlockingQueue<byte[]> fileQueue;
	private static CommonFile commonFile;
	private volatile static BitSet filePieces;
	private CommonFile() {
		fileQueue = new LinkedBlockingQueue<>();
		requestedPiecesMap = new HashMap<>();
	}
	
	static {
		filemap = new ConcurrentHashMap<Integer, byte[]>();
		filePieces = new BitSet(CommonInfo.getNumberOfPieces());
		try {
			File createdFile = new File(Constants.COMMON_PROPERTIES_CREATED_FILE_PATH + PeerProcess.getId() + File.separatorChar + CommonInfo.gettargetFileName());
			createdFile.getParentFile().mkdirs(); // Will create parent directories if not exists
			createdFile.createNewFile();
			writeFileChannel = FileChannel.open(createdFile.toPath(), StandardOpenOption.WRITE);
		} catch (IOException e) {
			System.out.println("Failed to create new file while receiving the file from host peer");
			e.printStackTrace();
		}
	}

	public void splitFile() {
		File filePtr = new File(Constants.COMMON_PROPERTIES_FILE_PATH + CommonInfo.gettargetFileName());
		FileInputStream fis = null;
		DataInputStream dis = null;
		int fileSize = (int) CommonInfo.getSharedFileSize();
		int numberOfPieces = CommonInfo.getNumberOfPieces();
		// System.out.println("Filesize: " + fileSize);
		try {
			fis = new FileInputStream(filePtr);
			dis = new DataInputStream(fis);
			int pieceSize = CommonInfo.getSharedFilePieceSize();
			int pieceIndex = 0;
			// TODO: will fileInputStream always read pieceSize amount of data?
			try {
				for (int i = 0; i < CommonInfo.getNumberOfPieces(); i++) {
					pieceSize = i != numberOfPieces - 1 ? CommonInfo.getSharedFilePieceSize()
							: fileSize % CommonInfo.getSharedFilePieceSize();
					byte[] piece = new byte[pieceSize];
					// System.out.println("Reading piece of size: " + pieceSize);
					dis.readFully(piece);
					filemap.put(pieceIndex, piece);
					filePieces.set(pieceIndex++);
				}
			} catch (IOException fileReadError) {
				fileReadError.printStackTrace();
				System.out.println("Error while splitting file");
			}

		} catch (FileNotFoundException e) {
			System.out.println("Error reading common.cfg file");
			e.printStackTrace();
		} finally {
			try {
				fis.close();
				dis.close();
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Error while closing fileinputstream after reading file");
			}
		}
	}

	public synchronized byte[] getPiece(int index) {
		return filemap.get(index);
	}

	@Override
	public void run() {
		while (true) {
			try {
				byte[] payload = fileQueue.take();
				int pieceIndex = ByteBuffer.wrap(payload, 0, 4).getInt();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public synchronized void setPiece(byte[] payload) {
		filePieces.set(ByteBuffer.wrap(payload, 0, 4).getInt());
		filemap.put(ByteBuffer.wrap(payload, 0, 4).getInt(), Arrays.copyOfRange(payload, 4, payload.length));
		try {
			fileQueue.put(payload);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public synchronized void writeToFile(int peerId) {
		String filename = Constants.COMMON_PROPERTIES_CREATED_FILE_PATH + peerId + File.separatorChar
				+ CommonInfo.gettargetFileName();
		System.out.println(filename);
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(filename);
			for (int i = 0; i < filemap.size(); i++) {
				try {
					fos.write(filemap.get(i));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public synchronized boolean isPieceAvailable(int index) {
		return filePieces.get(index);
	}

	public synchronized boolean isCompleteFile() {
		return filePieces.cardinality() == CommonInfo.getNumberOfPieces();
	}

	public synchronized int getReceivedFileSize() {
		return filePieces.cardinality();
	}

	protected synchronized int getRequestPieceIndex(Connection conn) {
		if (isCompleteFile()) {
			return Integer.MIN_VALUE;
		}
		BitSet peerBitset = conn.getPeerBitSet();
		int numberOfPieces = CommonInfo.getNumberOfPieces();
		BitSet peerClone = (BitSet) peerBitset.clone();
		BitSet myClone = (BitSet) filePieces.clone();
		peerClone.andNot(myClone);
		if (peerClone.cardinality() == 0) {
			return Integer.MIN_VALUE;
		}
		myClone.flip(0, numberOfPieces);
		myClone.and(peerClone);
		System.out.println(peerClone + " " + myClone);
		int[] missingPieces = myClone.stream().toArray();
		return missingPieces[new Random().nextInt(missingPieces.length)];
	}

	protected BitSet getFilePieces() {
		return filePieces;
	}

	protected synchronized boolean hasAnyPieces() {
		return filePieces.nextSetBit(0) != -1;
	}

	public synchronized void addRequestedPiece(Connection connection, int pieceIndex) {
		requestedPiecesMap.put(connection, pieceIndex);

	}

	public synchronized void removeRequestedPiece(Connection connection) {
		requestedPiecesMap.remove(connection);
	}
	public static CommonFile getInstance() {
		// TODO Auto-generated method stub
		if (commonFile == null) {
			commonFile = new CommonFile();
			commonFile.start();
		}
		return commonFile;
	}

	

}
