import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class GenerateLog {
	public static int peerId;
	public static Logger logger;
	public static FileHandler fileHandler;
	public static SimpleFormatter logFormatter;
	
	public GenerateLog(int peerId) throws SecurityException, IOException
	{
		this.peerId = peerId;
		logger = Logger.getLogger("GenerateLog"+GenerateLog.peerId);
		logger.setLevel(Level.INFO);
		fileHandler = new FileHandler("log_peer_"+GenerateLog.peerId+".log");
		logFormatter = new SimpleFormatter();
		fileHandler.setFormatter(logFormatter);
		logger.addHandler(fileHandler);
	}
	
	public static void writeLog(int rPeerId,String MessageType)
	{
		if(MessageType.equals(Constants.LOG_TCP_CREATE_CONNECTION)) {
			 logger.info(": Peer " + peerId + Constants.LOG_TCP_CREATE_CONNECTION + rPeerId + "\n");
		}
		else if(MessageType.equals(Constants.LOG_TCP_RECEIVE_CONNECTION)){
			logger.info(": Peer " + peerId + Constants.LOG_TCP_RECEIVE_CONNECTION + rPeerId + "\n");
		}
		else if(MessageType.equals(Constants.LOG_CHANGE_OPTIMISTICALLY_UNCHOKED_NEIGHBOR)) {
			logger.info(": Peer " + peerId + Constants.LOG_CHANGE_OPTIMISTICALLY_UNCHOKED_NEIGHBOR + rPeerId + "\n");
		}
		else if(MessageType.equals(Constants.LOG_CHOKING)) {
			logger.info(": Peer " + peerId + Constants.LOG_CHOKING + rPeerId + "\n");
		}
		else if(MessageType.equals(Constants.LOG_CHOKING)) {
			logger.info(": Peer " + peerId + Constants.LOG_CHOKING + rPeerId + "\n");
		}
		else if(MessageType.equals(Constants.LOG_RECEIVE_INTERESTED_MESSAGE)) {
			logger.info(": Peer " + peerId + Constants.LOG_RECEIVE_INTERESTED_MESSAGE +rPeerId + "\n");
		}
		else if(MessageType.equals(Constants.LOG_RECEIVE_NOT_INTERESTED_MESSAGE)) {
			logger.info(": Peer " + peerId + Constants.LOG_RECEIVE_NOT_INTERESTED_MESSAGE + rPeerId + "\n");
		}
	}
	public static void writeLog(ArrayList<Integer> preferredNeighbors,String MessageType) {
		
		StringBuffer textLog = new StringBuffer();
        textLog.append(":Peer " + peerId + MessageType);

        for (int i = 0; i < preferredNeighbors.size(); i++)
        {
            if (i != (preferredNeighbors.size() - 1)){
                textLog.append(preferredNeighbors.get(i) + ", ");
            }
            else{
                textLog.append(preferredNeighbors.get(i) + "\n");
            }
        }
        logger.info(textLog.toString());
	}
	public static void writeLog(int rPeerId, int pieceIndex, int numberOfPieces,String MessageType) {  // Download Piece Message
		 logger.info(": Peer " + peerId + MessageType + pieceIndex + " from " + rPeerId + "." + "Now the number of pieces it has is "+numberOfPieces+"\n");
	}
	 public static void writeLog(int rPeerId, int pieceIndex,String Messagetype) {   // Received Have message
	        logger.info(": Peer " + peerId + Messagetype + rPeerId + " for the piece " + pieceIndex + "\n");
	    }
	public static void writeLog(String MessageType) {
		  logger.info(": Peer " + peerId + MessageType);
	}
	public static void main(String[] args) {
		try {
			GenerateLog log = new GenerateLog(1001);
			log.writeLog(1002,Constants.LOG_TCP_CREATE_CONNECTION);
			log.writeLog(1002,Constants.LOG_TCP_RECEIVE_CONNECTION);
			log.writeLog(1002,Constants.LOG_CHANGE_OPTIMISTICALLY_UNCHOKED_NEIGHBOR);
			log.writeLog(1003,Constants.LOG_CHOKING);
			log.writeLog(1003,Constants.LOG_UNCHOKING);
			log.writeLog(1004,Constants.LOG_RECEIVE_INTERESTED_MESSAGE);
			log.writeLog(1005,Constants.LOG_RECEIVE_NOT_INTERESTED_MESSAGE);
			ArrayList<Integer> preferredNeighbors = new ArrayList<>(Arrays.asList(1001, 1002, 1004, 1005));
			log.writeLog(preferredNeighbors, Constants.LOG_CHANGE_OF_PREFERREDNEIGHBORS);
			log.writeLog(1004, 5, 6, Constants.LOG_DOWNLOAD_PEICE);
			log.writeLog(1003, 4, Constants.LOG_RECEIVE_HAVE_MESSAGE);
			log.writeLog(Constants.LOG_DOWNLOAD_COMPLETE);
			
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
