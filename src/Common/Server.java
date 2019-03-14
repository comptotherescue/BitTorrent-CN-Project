/**
 * 
 */
package Common;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import Peer.Peer;

/**
 * @author sonal
 *
 */
public class Server implements Runnable{
	/**
	 * 
	 */
	 private Peer peer;
	    private ObjectOutputStream outputStream;

	    public Server(Peer peer) {
	        this.peer = peer;
	    }

	    public void run() {
	        int portNumber = peer.getPortNumber();
	        ServerSocket listener = null;
	        try {
	            listener = new ServerSocket(portNumber);
	            while(true) {
	                Socket socket = listener.accept();
	                peer.addSocket(socket);
	                outputStream = new ObjectOutputStream(socket.getOutputStream());
	                outputStream.flush();
	                peer.setOutputStream(outputStream);
	                MessageController messageController = new MessageController(socket, peer, outputStream);
	                Thread thread = new Thread(messageController);
	                thread.start();
	                byte[] message = Handshake.getMessage(peer.getId());
	                System.out.println("Sending message " + new String(message));
	                peer.send(outputStream, message);
	            }
	        } catch(IOException ioException) {
	            System.out.println("IOException while opening socket at port number " + portNumber + " for peer " + peer.getId());
	            ioException.printStackTrace();
	        } finally {
	            if(listener != null) {
	                try {
	                    listener.close();
	                } catch (IOException ioException1) {
	                    System.out.println("IOException while closing listener socket at " + portNumber + " for peer " + peer.getId());
	                    ioException1.printStackTrace();
	                }
	            }
	        }
	    }
}
