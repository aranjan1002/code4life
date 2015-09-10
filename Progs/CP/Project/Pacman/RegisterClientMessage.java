package edu.cp.project.server;

import java.util.regex.Pattern;

class RegisterClientMessage {
    RegisterClientMessage(String msg) {
	if (Pattern.matches(REG_MSG, msg) == false) {
	    throw new RuntimeException("Not reg msg from client: " + msg);
	}
	else {
	    String[] msgSplit = msg.split(" ");
	    clientName = msgSplit[1];
	    if (msgSplit[2].equals("G")) {
		clientType = CLIENT_TYPE.GHOST;
	    } else {
		clientType = CLIENT_TYPE.PACMEN;
	    }
	}
    }

    public String getClientName() {
	return clientName;
    }

    CLIENT_TYPE getClientType() {
    	return clientType;
    }

    String clientName = null;
    CLIENT_TYPE clientType = null;
    public static final String REG_MSG = "REG [a-zA-Z]* [GP]";
    public enum CLIENT_TYPE {PACMEN, GHOST};
}