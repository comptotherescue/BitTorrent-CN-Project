package Common;

public class NetworkInfo {
	
	private int num;
	private int Pid;
	private String hName;
	private int port;
	private boolean hasSFile;

	public int getPeerId() {
		return Pid;
	}

	public int getNumber() {
		return num;
	}

	public void setNumber(int number) {
		this.num = number;
	}

	public void setPeerId(int peerId) {
		this.Pid = peerId;
	}

	public String getHostName() {
		return hName;
	}

	public void setHostName(String hostName) {
		this.hName = hostName;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public boolean hasSharedFile() {
		return hasSFile;
	}

	public void setHasSharedFile(boolean hasSharedFile) {
		this.hasSFile = hasSharedFile;
	}

	@Override
	public String toString() {
		return "Peer [peerId=" + Pid + ", hostName=" + hName + ", port=" + port + ", hasSharedFile="
				+ hasSFile + "]";
	}
}
