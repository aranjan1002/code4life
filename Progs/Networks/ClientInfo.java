package networks.project;

import java.net.*;

class ClientInfo {
    public String clientName;
    public int portNumber;
    public InetAddress addr;

    public ClientInfo(String clName, InetAddress addr, int port) {
	clientName = clName;
	port = portNumber;
    }
}