
package Peer;
import Config.*;
import java.util.List;

import Common.Server;

import Common.Client;
import Config.PeerInfo;

public class PeerProcess {
	private static int Pid;
	public static void main(String[] args) {
		    Pid = 1100;
	        //Pid = Integer.parseInt(args[0]);
	        CommonInfo info = new CommonInfo();
	        info.loadCommonConfig();
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
