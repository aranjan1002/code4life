package edu.cp.project;

import java.util.regex.Pattern;

class RegisterClientMessage {
    RegisterClientMessage(String msg) {
	if (Pattern.matches(ClientMessageReader.REG_MSG, msg) == false) {
	    throw new RuntimeException("Not reg msg from client");
	}
	else {
	    String[] msgSplit = msg.split(" ");
	    clientName = msgSplit[1];
	    if (msgSplit[2].equals("G")) {
		clientType = ClientMessageReader.CLIENT_TYPE.GHOST;
	    } else {
		clientType = ClientMessageReader.CLIENT_TYPE.PACMEN;
	    }
	}
    }

    public String getClientName() {
	return clientName;
    }

    ClientMessageReader.CLIENT_TYPE getClientType() {
	return clientType;
    }

    String clientName = null;
    ClientMessageReader.CLIENT_TYPE clientType = null;
}