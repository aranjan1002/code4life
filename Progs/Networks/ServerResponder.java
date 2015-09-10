package networks.project;

import java.io.*;

class ServerResponder implements Runnable {
    public void run() {
        try {
            int cmd;
            while (true) {
                while (UDPServer.needToSendPktsToServer() == false) {
		    //System.out.println("nothing to send to server");
		}
		System.out.println("Sending a packet to server");
                UDPServer.
                    server2ServerSocket
		    .send(UDPServer.getPacketToSendToServer());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
