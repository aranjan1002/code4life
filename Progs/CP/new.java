public void run() {
    String gameOverMessage = new String();
    while (true) {
	if (pacMenList.size() > 0 && ghostList.size() > 0) {
	    for (COWClient pacman : pacMenList) {
		COWClient.ClientPosition pc; // pc is immutable
		pc = pacman.getClientPosition();
		int px = pc.getX();
		int py = pc.getY();

		for (COWClient ghost : ghostList) {

		    COWClient.ClientPosition gc;
		    gc = ghost.getClientPosition();
		    int gx = gc.getX();
		    int gy = gc.getY();
		    if (Math.abs(gx - px) < PAC_SIZE && Math.abs(gy - py) < PAC_SIZE) {
			gameState = 2;
			gameOverMessage = Client.GHOSTWINS;
		    }
		}
	    }
	    boolean pelletsExist = false;
	    for (int i = 0; i < 13; ++i) {
		for (int j = 0; j < 13; ++j) {
		    if (mazeArray[i][j] == 0)
			pelletsExist = true;
		}
	    }
	    if (!pelletsExist) {
		gameState = 1;
		gameOverMessage = Client.PACMANWINS;
	    }
	    if (gameState != 0) {
		break;
	    }
	}
	drawPanel.repaint();
    }
    Client.sendToServer(gameOverMessage);
}