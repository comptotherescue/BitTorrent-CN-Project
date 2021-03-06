package Common;

import java.io.File;

public class Constants {
	//Log messages
 public static String LOG_TCP_CREATE_CONNECTION                          = " makes a connection to Peer ";
 public static String LOG_TCP_RECEIVE_CONNECTION                         = " is connected from Peer ";
 public static String LOG_CHANGE_OF_PREFERREDNEIGHBORS                   = " has the preferred neighbores ";
 public static String LOG_CHANGE_OPTIMISTICALLY_UNCHOKED_NEIGHBOR        = " has the optimistically unchoked neighbor ";
 public static String LOG_CHOKING                                        = " is choked by ";
 public static String LOG_UNCHOKING                                      = " is unchoked by ";
 public static String LOG_RECEIVE_HAVE_MESSAGE                           = " received HAVE message from ";
 public static String LOG_RECEIVE_INTERESTED_MESSAGE                     = " received INTERESTED message from ";
 public static String LOG_RECEIVE_NOT_INTERESTED_MESSAGE                 = " received NOT INTERESTED message from ";
 public static String LOG_DOWNLOAD_PEICE                                 = " has downloaded the piece ";
 public static String LOG_DOWNLOAD_COMPLETE                              = " has downloaded the complete file \n";
 
 //Message types
 public static enum Type {CHOKE, UNCHOKE, INTERESTED, NOTINTERESTED, HAVE, BITFIELD, REQUEST, PIECE, HANDSHAKE;}

 //Constants for config file
 public static String FileName                                                 = "FileName";
 public static String NumofPreferredNeighbors                                  = "NumberOfPreferredNeighbors";
 public static String UnchokingInterval                                        = "UnchokingInterval";
 public static String OptimUnchokingInterval                                   = "OptimisticUnchokingInterval";
 public static String FileSize                                                 = "FileSize";
 public static String PieceSize                                                = "PieceSize";
 public static final String ConfigPath                                         = System.getProperty("user.dir") + File.separatorChar+ "src/Resources/Common.cfg";
 public static final String PeerInfoPath                                       = System.getProperty("user.dir") + File.separatorChar+ "src/Resources/PeerInfo.cfg";
 public static final String COMMON_PROPERTIES_CREATED_FILE_PATH                = System.getProperty("user.dir") + File.separatorChar + "/project/peer_";
 public static final String COMMON_PROPERTIES_FILE_PATH                        = System.getProperty("user.dir") + File.separatorChar + "src/Resources/"; 
}
