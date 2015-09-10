package networks.project;

import java.io.*;
import java.net.*;

public class ClientInterface {
    public static void main(String[] args) 
	throws IOException, NetworkProjectException {
	InputStreamReader ir = new InputStreamReader(System.in);
	BufferedReader br = new BufferedReader(ir);
	String cmd = new String();
	
	// System.out.println("Starting server socket");
	do {
	    try {
		System.out.println("Commands:");
		System.out.println("---------------------");
		System.out.println("Server <IP addr> <port number>");
		System.out.println("Insert <name> <IP address> <port number>");
		System.out.println("Delete <name> " + 
				   "[<IP address>] [<port number>]");
		System.out.println("Find <wild_name> <wild_IP>"); 
		System.out.println("Link <servername>");
		System.out.println("Unlink <servername>");
		System.out.println("Register<client_name> <port umber>");
		System.out.println("Unregister<client_name>");
		System.out.println("List<client_name_list><server_name_list>");
		System.out.println("Kill");
		System.out.println("Test");
		System.out.println("Quit");
		System.out.print("Please enter a command: ");
		cmd = br.readLine();
		execute(cmd);
	    } catch (NetworkProjectException e) {
		System.out.println(e.getMessage());
		// e.printStackTrace();
	    } catch (SocketException e) {
		System.out.println(e.getMessage());
		// e.printStackTrace();
	    } catch (UnknownHostException e) {
		System.out.println(e.getMessage());
		// e.printStackTrace();
	    }
	} while(!cmd.equals("Quit"));
    }

    static void execute(String cmd) 
	throws NetworkProjectException, SocketException, UnknownHostException, 
	       IOException {
	if (cmd.startsWith("Server")) {
	    /*if (isClientInit) {
		throw new NetworkProjectException("connection var" +
						  "iables already " +
						  "initialized.");
	    }
	    else { */
		ServerCmd srvCmd = new ServerCmd(cmd);
		int port = srvCmd.getPort();
		client = new UDPClient(srvCmd.getIpAddr(),
				       port);
		isClientInit = true;
		// }
	}
	else if (cmd.startsWith("Insert")) {
	    InsertDelCmd insCmd = new InsertDelCmd(cmd);
	    if (!isClientInit) {
		throw new NetworkProjectException("connection var" +
						  "iables not " +
						  "initialized.");
	    }
	    else { 
		client.insert(insCmd.getName(),
			      insCmd.getIpAddr(),
			      insCmd.getPort());
	    }
	}
	else if (cmd.startsWith("Delete")) {
	    InsertDelCmd delCmd = new InsertDelCmd(cmd);
	    if (!isClientInit) {
                throw new NetworkProjectException("connection var" +
                                                  "iables not " +
                                                  "initialized.");
            }
            else {
		String name = delCmd.getName();
		if (!delCmd.shortCmd) {
		    client.delete(delCmd.getName(),
				  delCmd.getIpAddr(),
				  delCmd.getPort());
		}
		else {
		    client.delete(name);
		}
            }
        }
	else if (cmd.startsWith("Find")) {
	    if (!isClientInit) {
                throw new NetworkProjectException("connection var" +
                                                  "iables not " +
                                                  "initialized.");
            }
            else {
		FindCmd findCmd = new FindCmd(cmd);
		client.find(findCmd.getName(), findCmd.getWildCardIp());
	    }
	}
	else if (cmd.equals("Kill")) {
	    if (!isClientInit) {
                throw new NetworkProjectException("connection var" +
                                                  "iables not " +
                                                  "initialized.");
            }
            else {
		client.kill();
		isClientInit = false;
	    }
	}
	else if (cmd.equals("Test")) {
	    if (!isClientInit) {
                throw new NetworkProjectException("connection var" +
                                                  "iables not " +
                                                  "initialized.");
            }
            else {
		client.test();
	    }
	}
	else if (cmd.equals("Quit")) {
	    client.close();
	}
	else if (cmd.startsWith("Link")) {
	    if (!isClientInit) {
		throw new NetworkProjectException("connection var" +
                                                  "iables not " +
                                                  "initialized.");
            }
	    else {
		LinkUnlinkCmd linkCmd = new LinkUnlinkCmd(cmd);
		client.link(linkCmd.getServerName());
	    }
	}
	else if (cmd.startsWith("Unlink")) {
            if (!isClientInit) {
                throw new NetworkProjectException("connection var" +
                                                  "iables not " +
                                                  "initialized.");
            }
            else {
                LinkUnlinkCmd unlinkCmd = new LinkUnlinkCmd(cmd);
                client.unlink(unlinkCmd.getServerName());
            }
        }
	else if (cmd.startsWith("Register")) {
	    if (!isClientInit) {
		throw new NetworkProjectException("connection var" +
                                                  "iables not " +
                                                  "initialized.");
	    }
	    else {
		RegisterCmd regCmd = new RegisterCmd(cmd);
		client.register(regCmd.getClientName(), regCmd.getPort());
	    }
	}
	else if (cmd.startsWith("Unregister")) {
            if (!isClientInit) {
                throw new NetworkProjectException("connection var" +
                                                  "iables not " +
                                                  "initialized.");
            }
            else {
                UnregisterCmd regCmd = new UnregisterCmd(cmd);
                client.unregister(regCmd.getClientName());
            }
        }
	else if (cmd.startsWith("List")) {
	    if (!isClientInit) {
                throw new NetworkProjectException("connection var" +
                                                  "iables not " +
                                                  "initialized.");
            }
            else {
                ListSendCmd listCmd = new ListSendCmd(cmd);
                client.list(listCmd.getList());
            }

	}
	else {
	    throw new NetworkProjectException("Invalid command");
	}
    }
    
    static class InsertDelCmd {
	InsertDelCmd(String cmd) 
	    throws NetworkProjectException, UnknownHostException {
	    String[] cmdSplit = cmd.split(" ");
	    int l = cmdSplit.length;
	    // System.out.println(cmdSplit[0] + cmdSplit.length);
	    if ((new String("Delete").equals(cmdSplit[0]) && 
		 l != 4 && 
		 l != 2) ||
		(new String("Insert").equals(cmdSplit[0]) &&
		 l != 4)) {
		throw new NetworkProjectException("Invalid syntax");
            }
	    name = cmdSplit[1];
	    if (name.length() > 80) {
		throw new NetworkProjectException("name should be at most" +
						  " 80 characters long");
	    }
	    if (l == 4) {
		ipAddr = InetAddress.getByName(cmdSplit[2]);
		port = Integer.parseInt(cmdSplit[3]);
		if (port < 1024 || port > 65535) {
		    throw new NetworkProjectException("Invalid port..." +
						      "should be between" +
						      "1024 and 65535");
		}
	    }
	    else {
		shortCmd = true;
	    }
	}
	
	String getName() {
	    return name;
	}

	InetAddress getIpAddr() {
	    return ipAddr;
	}

	int getPort() {
	    return port;
	}

	String name;
	int port;
	InetAddress ipAddr;
	
	public boolean shortCmd = false;
    }
    
    static class ServerCmd {
	ServerCmd(String cmd) 
	    throws NetworkProjectException, UnknownHostException {
	    String[] cmdSplit = cmd.split(" ");
	    if (cmdSplit.length != 3) {
		throw new NetworkProjectException("Invalid syntax");
	    }
	    ipAddr = InetAddress.getByName(cmdSplit[1]);
	    port = Integer.parseInt(cmdSplit[2]);
	}
	
	InetAddress getIpAddr() {
	    return ipAddr;
	}
	
	int getPort() {
	    return port;
	}
	
	InetAddress ipAddr;
	int port;
    }

    static class FindCmd {
	FindCmd(String cmd) 
	    throws NetworkProjectException {
	    String[] cmdSplit = cmd.split(" ");
	    int l = cmdSplit.length;
	    if (l != 2 && l != 3) {
		throw new NetworkProjectException("Invalid Syntax");
	    }
	    name = cmdSplit[1];
	    if (l == 3) {
		wcIp = cmdSplit[2];
	    }
	}
	
	String getName() {
	    return name;
	}

	String getWildCardIp() {
	    return wcIp;
	}

	String name, wcIp = "";
    }
    
    static class LinkUnlinkCmd {
	LinkUnlinkCmd(String cmd) 
	throws NetworkProjectException {
	    String[] cmdSplit = cmd.split(" ");
	    int l =  cmdSplit.length;
	    if (l != 2) {
		throw new NetworkProjectException("Invalid Syntax");
	    }
	    servName = cmdSplit[1];
	}
	
	String getServerName() {
	    return servName;
	}
	String servName;
    }

    static class RegisterCmd {
	RegisterCmd(String cmd) 
	throws NetworkProjectException {
	    String[] cmdSplit = cmd.split(" ");
	    if (cmdSplit.length != 3) {
		throw new NetworkProjectException("Invalid Syntax");
	    }
	    clientName = cmdSplit[1];
	    port = Integer.parseInt(cmdSplit[2]);
	}

	String getClientName() {
	    return clientName;
	}

	int getPort() {
	    return port;
	}

	String clientName;
	int port;
    }
    
    static class UnregisterCmd {
	UnregisterCmd(String cmd)
	    throws NetworkProjectException {
            String[] cmdSplit = cmd.split(" ");
            if (cmdSplit.length != 2) {
                throw new NetworkProjectException("Invalid Syntax");
            }
            clientName = cmdSplit[1];
	}

        String getClientName() {
            return clientName;
        }

        String clientName;
    }

    static class ListSendCmd {
	ListSendCmd(String cmd)
	    throws NetworkProjectException {
            String[] cmdSplit = cmd.split(" ");
            if (cmdSplit.length != 3) {
                throw new NetworkProjectException("Invalid Syntax");
            }
            list = cmdSplit[1] + cmdSplit[2];
        }
	
	String getList() {
	    return list;
	}
	String list = new String();
    }

    static boolean isClientInit = false;
    static UDPClient client;
}		
	    