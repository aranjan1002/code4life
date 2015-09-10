public final class Server implements Runnable {
    List<ClientInfo> pacManClients;
    List<ClientInfo> ghostClients;
	public void run() {
		try {
			serverSocket = new ServerSocket(6789);
			System.out.println("Server socked started");

			BufferedReader in = null;
			// listen to registration messages from clients 
			registerWithClients();
			// create and start a thread for each client
			for (ClientInfo clientInfo : pacMenClients) {
				new Thread(new ClientListener(clientInfo)).start();
			}

			for (ClientInfo clientInfo : ghostClients) {
				new Thread(new ClientListener(clientInfo)).start();
			}
		} catch (IOException e) {
		    e.printStackTrace();
		} finally {
		    serverSocket.close();
		}
	}

	static synchronized void moveClientOnBoardAndBroadcast(ClientInfo 
			clientInfo,
			//ClientMessageReader.MOVE_DIR movDir) {
			String movDir) {
		String boardState = board.move(clientInfo, movDir);
		if (boardState != null) {
			broadcast(boardState);
		}
	}

	private static synchronized void broadcast(String boardState) {
		System.out.println(boardState + " sending to client");
		for (ClientInfo clientInfo : pacMenClients) {
			clientInfo.outToClient.println(boardState);
		}
		for (ClientInfo clientInfo : ghostClients) {
			clientInfo.outToClient.println(boardState);
		}
	}

	private ClientInfo getClientRegistrationInfo(Socket clientSocket) {
		try {
			InputStreamReader isr =
					new InputStreamReader(clientSocket.getInputStream());
			BufferedReader inFromClient = new BufferedReader(isr);
			String clientMsg = inFromClient.readLine();
			RegisterClientMessage registerClientMsg = 
					new RegisterClientMessage(clientMsg);
			PrintWriter outToClient = 
					new PrintWriter(clientSocket.getOutputStream(), true);
			String clientName = registerClientMsg.getClientName();
			ClientInfo clientInfo = new ClientInfo(clientName, 
					inFromClient,
					outToClient,
					registerClientMsg.
					getClientType());
			return clientInfo;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}

	}

	static List<ClientInfo> pacMenClients = new ArrayList<ClientInfo>();
	static List<ClientInfo> ghostClients = new ArrayList<ClientInfo>();
	ServerSocket serverSocket = null;
	final int numClient = 2;
	ClientMessageReader clientMsgReader = new ClientMessageReader();
	static PacMenBoard2 board;
	Socket clientSocket;
}
