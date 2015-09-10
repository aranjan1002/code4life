package networks.project;

import java.io.*;
import java.net.*;

class ServerResponder implements Runnable {
    public void run() {
        try {
            int cmd;
            while (true) {
                while (UDPServer.needToSendPktsToServer() == false) {
		    //System.out.println("nothing to send to server");
		}
		DatagramPacket pkt = UDPServer.getPacketToSendToServer();
		System.out.println("Sending to server packet:" +
				 new String(pkt.getData()).toString() +
				 "\nServer: " + pkt.getAddress() +
				 "\nPort: " + pkt.getPort());

                UDPServer.
                    server2ServerSocket
		    .send(pkt);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
