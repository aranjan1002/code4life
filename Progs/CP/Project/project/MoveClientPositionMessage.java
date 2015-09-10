package edu.cp.project;

import java.util.regex.Pattern;

class MoveClientPositionMessage {
	MoveClientPositionMessage(String msg) {
		if (Pattern.matches(ClientMessageReader.MOVE_DIR_MSG, msg) == false) {
			throw new RuntimeException("Not change pos msg from client: " + msg);
		}
		else {
			String[] msgSplit = msg.split(" ");
			clientName = msgSplit[0];
			if (msgSplit[0].equals("U")) {
				movementDirection = ClientMessageReader.MOVE_DIR.UP;
			} else if (msgSplit[0].equals("D")) {
				movementDirection = ClientMessageReader.MOVE_DIR.DOWN;
			} else if (msgSplit[0].equals("L")) {
				movementDirection = ClientMessageReader.MOVE_DIR.LEFT;
			} else {
				movementDirection = ClientMessageReader.MOVE_DIR.RIGHT;
			}
		}
	}

    ClientMessageReader.MOVE_DIR getMovementDirection() {
	return movementDirection;
    }

    String clientName = null;
    ClientMessageReader.MOVE_DIR movementDirection = null;
}