package Logger;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.*;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import Common.Constants;
//Aditya
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
	public static void writeLog(int rPeerId, int pieceIndex, int sizeOfDownloadedPiece,String MessageType) {  // Download Piece Message
		 logger.info(": Peer " + peerId + MessageType + pieceIndex + " from " + rPeerId + "." + "Size of pieces downloaded piece is "+sizeOfDownloadedPiece+"\n");
	}
	 public static void writeLog(int rPeerId, int pieceIndex,String Messagetype) {   // Received Have message
	        logger.info(": Peer " + peerId + Messagetype + rPeerId + " for the piece " + pieceIndex + "\n");
	    }
	public static void writeLog(String MessageType) {  //Download Complete message
		  logger.info(": Peer " + peerId + MessageType);
	}
}
