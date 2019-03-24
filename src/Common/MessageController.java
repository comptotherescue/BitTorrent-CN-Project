/**
 * 
 */
package Common;

import java.io.ObjectOutputStream;
import java.net.Socket;

import Peer.Peer;

/**
 * @author sonal
 *
 */
public class MessageController implements Runnable {
	private Socket socket;
    private Peer peer;
    private ObjectOutputStream outputStream;
    private int remotePeerId;

    public MessageController(Socket socket, Peer peer, ObjectOutputStream outputStream) {
        this.socket = socket;
        this.peer = peer;
        this.outputStream = outputStream;
    }
	/**
	 * @param outputStream 
	 * @param peer 
	 * @param socket 
	 * 
	 */
	  public void run() {
		  
	  }
}
