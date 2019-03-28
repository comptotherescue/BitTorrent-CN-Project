package Peer;

import Common.File;
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

public class Peer {
	private int id;
    private String ipAddress;
    private int portNumber;
    private boolean hasFile;
    private int countPieces;
    private boolean[] availableFilePieces;
    private List<Socket> sockets;
    private Map<Integer, Peer> neighborMap;
    private CommonInfo commonInfo;
    private File file;
    private boolean isUnchoked;
    private List<Integer> interestedPeers;
    private List<Integer> unchokedPeers;
    private int optimisticallyUnchokedPeer;
    private Map<Integer, Integer> requestedPieceMap;
    private ObjectOutputStream outputStream;
    private long timerToSelectPreferredNeighbors;
    private long timerToOptimisticallyUnchokeNeighbor;
    private long timeUnchoked;
    private long bytesDownloadedAfterUnchoking;
    private double downloadRate;

    public Peer(int id, String ipAddress, int portNumber, boolean hasFile) {
        this.id = id;
        this.ipAddress = ipAddress;
        this.portNumber = portNumber;
        this.hasFile = hasFile;
        this.sockets = new ArrayList<>();
        this.neighborMap = new HashMap<>();
        this.commonInfo = new CommonInfo();
        this.commonInfo.loadCommonConfig();
        this.file = new File(this.commonInfo, hasFile);
        this.availableFilePieces = new boolean[this.file.getNumberOfPieces()];
        Arrays.fill(availableFilePieces, hasFile);
        this.isUnchoked = false;
        this.interestedPeers = new ArrayList<>();
        this.unchokedPeers = new ArrayList<>();
        this.optimisticallyUnchokedPeer = 0;
        this.requestedPieceMap = new HashMap<>();
        this.timerToSelectPreferredNeighbors = (long)commonInfo.getUnchokingInterval() * 1000;
        this.timerToOptimisticallyUnchokeNeighbor = (long)commonInfo.getOptimisticUnchokingInterval() * 1000;
    }

    public int getId() {
        return id;
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