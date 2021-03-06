
package Peer;
import Config.*;
import Logger.GenerateLog;

import java.io.IOException;

import Common.CommonFile;
import Common.Handshake;

public class PeerProcess {
	private static int Pid;
	public static int getId() {
		return Pid;
	}
	/**
	 * @param args
	 * @throws IOException 
	 * @throws SecurityException 
	 */
	public static void main(String[] args) throws SecurityException, IOException {
		    Pid = Integer.parseInt(args[0]);
		    new GenerateLog(Pid);
	        CommonInfo.loadCommonConfig();
	        PeerInfo.loadPeerInfoConfig();
	        Handshake.setID(Pid);
	        if (PeerInfo.getPeer(Pid).hasSharedFile()) {
				CommonFile.getInstance().splitFile();
			}
	        Peer current = Peer.getInstance();
	        current.createTCPConnections();
			try {
				current.listenForConnections();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

}
