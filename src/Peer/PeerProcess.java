
package Peer;
import Config.*;
import Logger.GenerateLog;

import java.io.IOException;
import java.util.List;

import Common.SharedFile;
import Common.Constants;
import Common.Handshake;

public class PeerProcess {
	private static int Pid;
	public static int getId() {
		return Pid;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		    Pid = Integer.parseInt(args[0]);
		    try {
				GenerateLog G = new GenerateLog(Pid);
			} catch (SecurityException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        CommonInfo info = new CommonInfo();
	        CommonInfo.loadCommonConfig();
	        PeerInfo peers = new PeerInfo(Constants.PeerInfoPath);
	        Handshake.setID(Pid);
	        if (PeerInfo.getPeer(Pid).hasSharedFile()) {
				SharedFile.getInstance().splitFile();
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
