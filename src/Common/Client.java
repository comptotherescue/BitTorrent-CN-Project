/**
 * 
 */
package Common;

import java.io.*;
import java.net.Socket;

import Message.MessageHandler;
import Peer.Peer;

/**
 * @author sonal
 *
 */
public class Client {
	 private Peer peer;
	    private Peer remotePeer;
	    private ObjectOutputStream outputStream;

	    public Client(Peer peer, Peer remotePeer) {
	        this.peer = peer;
	        this.remotePeer = remotePeer;
	    }

	    public void connect() {
	        try {
	            Socket socket = new Socket(remotePeer.getIpAddress(), remotePeer.getPortNumber());
	            peer.addSocket(socket);
	            outputStream = new ObjectOutputStream(socket.getOutputStream());
	            outputStream.flush();
	            peer.setOutputStream(outputStream);
	            MessageController messageController = new MessageController(socket, peer, outputStream);
	            Thread thread = new Thread(messageController);
	            thread.start();
	            byte[] message = Handshake.getMessage();
	            peer.send(outputStream, message);
	        } catch(IOException ioException) {
	           // System.out.println("IOException while connecting to server " + remotePeer.getId());
	            ioException.printStackTrace();
	        }
	    }
}
