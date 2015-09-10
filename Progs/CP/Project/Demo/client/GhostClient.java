package edu.cp.project.client;

@SuppressWarnings("serial")
public class GhostClient extends Client {
	GhostClient() {
		super();
		REGMSG = "REG ggg G";
		serverIpAddr = "127.0.0.1";
		clientType = "G";
	}
	
	public static void main(String[] args) {
		new GhostClient().run();
	}
}