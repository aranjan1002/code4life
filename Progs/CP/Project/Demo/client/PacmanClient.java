package edu.cp.project.client;

@SuppressWarnings("serial")
public class PacmanClient extends Client {
	PacmanClient() {
		super();
		REGMSG = "REG ppp P";
		serverIpAddr = "127.0.0.1";
		clientType = "P";
	}
	
	public static void main(String[] args) {
		new PacmanClient().run();
	}
}