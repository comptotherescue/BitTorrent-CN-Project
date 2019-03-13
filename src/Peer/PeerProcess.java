
/**
 * 
 */
package Peer;
import Config.PeerInfo;
/**
 * @author sonal
 *
 */
public class PeerProcess {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		   if(args.length < 1) {
	            System.out.println("Usage: java peerProcess <peerId>");
	            return;
	        }
	        int peerId = Integer.parseInt(args[0]);
	        PeerInfo peerInfo = new PeerInfo("PeerInfo.cfg");
	        Peer peer = peerInfo.(peerId);

	        //Start connection listener
	        Server server = new Server(peer);
	        Thread serverThread = new Thread(server);
	        serverThread.start();

	        //Manage choking/unchoking
	        peer.startPreferredNeighborSelection();

	        //Manage optimistic unchoking
	        peer.startOptimisticallyUnchokingPeer();

	        List<Peer> peers = peerInfoConfig.getAllPeers();

	        for(Peer neighbor : peers) {
	            if(neighbor.getId() != peerId) {
	                System.out.println("Opening connection between " + neighbor.getId() + " and " + peerId);
	                Client client = new Client(peer, neighbor);
	                client.connect();
	            }
	        }
	}

}
