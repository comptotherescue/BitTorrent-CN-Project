package Peer;

import Common.ConnectionHandler;
import Common.File;
import Common.NetworkInfo;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import Config.CommonInfo;
import Config.PeerInfo;

public class Peer {
	private static Peer host = new Peer();
	private NetworkInfo network;
	ConnectionHandler connectionManager;
	public static boolean allPeersReceivedFiles = false;


    public Peer() {
    	network = PeerInfo.getPeer(PeerProcess.getId());
		connectionManager = ConnectionHandler.getInstance();
    }

    

	public int getPortNumber() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setOutputStream(ObjectOutputStream outputStream2) {
		// TODO Auto-generated method stub
		
	}

	public void addSocket(Socket socket) {
		// TODO Auto-generated method stub
		
	}

	public String getIpAddress() {
		// TODO Auto-generated method stub
		return null;
	}

	public void send(ObjectOutputStream outputStream2, byte[] message) {
		// TODO Auto-generated method stub
		
	}
	

}