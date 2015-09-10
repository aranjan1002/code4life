package edu.cp.project;

public class ClientListener implements Runnable {
	ClientListener(ClientInfo clInfo) {
		clientInfo = clInfo;
	}

	public void run() {
		try {
			while (true) {
				while(!clientInfo.inFromClient.ready()) {}

				String clientMsg = clientInfo.inFromClient.readLine();
				System.out.println(clientMsg + " " + clientInfo);
//				MoveClientPositionMessage movClientMsg =
//						new MoveClientPositionMessage(clientMsg);
//				ClientMessageReader.MOVE_DIR moveDIR = 
//						movClientMsg.getMovementDirection();
				PacMenServer.moveClientOnBoardAndBroadcast(clientInfo, 
						//moveDIR);
						clientMsg);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());

		}
	}

	ClientInfo clientInfo;

}