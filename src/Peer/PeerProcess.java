
package Peer;
import Config.*;
import java.util.List;

import Common.Server;
import Common.SharedFile;
import Common.Client;
import Common.Constants;
import Common.Handshake;
import Config.PeerInfo;

public class PeerProcess {
	private static int Pid;
	public static int getId() {
		return Pid;
	}
	public static void main(String[] args) {
		    Pid = 1001;
	        //Pid = Integer.parseInt(args[0]);
	        CommonInfo info = new CommonInfo();
	        info.loadCommonConfig();
	        PeerInfo peers = new PeerInfo(Constants.PeerInfoPath);
	        Handshake.setID(Pid);
	        if (PeerInfo.getPeer(Pid).hasSharedFile()) {
				SharedFile.getInstance().splitFile();
			}
	        Peer host = new Peer();
	        //PeerInfo peerInfo = new PeerInfo("PeerInfo.cfg");
	        //Peer peer = peerInfo.getPeer(Pid);

	      /*  //Start connection listener
	        Server server = new Server(peer);
	        Thread serverThread = new Thread(server);
	        serverThread.start();

	        List<Peer> peers = peerInfo.getAllPeers();

	        for(Peer neighbor : peers) {
	            if(neighbor.getId() != Pid) {
	                System.out.println("Opening connection between " + neighbor.getId() + " and " + Pid);
	                Client client = new Client(peer, neighbor);
	                client.connect();
	            }
	        }*/
	}

}
