package networks.project;

import java.io.*;
import java.net.*;
import java.util.*;

class UDPServer {
    public static void main (String[] args)
	throws IOException {
	readConfigFile();
	readFromSerFile();
	System.out.print("Starting server on port: " + portNum + " for clients ");
	System.out.println("and on port: " + portNumForSrv + " for servers");
	serverSocket = new DatagramSocket(portNum);
	server2ServerSocket = new DatagramSocket(portNumForSrv);
	Thread clientListener = new Thread(new ClientListener());
	Thread clientResponder = new Thread(new ClientResponder());
	Thread serverToserverListener = new Thread(new ServerListener());
	Thread serverToserverResponder = new Thread(new ServerResponder());
	clientListener.start();
	clientResponder.start();
	serverToserverListener.start();
	serverToserverResponder.start();
	try {
	    clientListener.join();
	    clientResponder.join();
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}
    }
    
    private static void serializeRecords() 
	throws IOException {
	FileOutputStream fos = new FileOutputStream(serFile);
	ObjectOutputStream oos = new ObjectOutputStream(fos);
	oos.writeObject(recList);
	oos.close();
	System.out.println("Records persisted");
    }

    @SuppressWarnings("unchecked")
	private static void readFromSerFile() 
	throws IOException {
	File file = new File(serFile);
	if (file.exists()) {
	    FileInputStream fis = new FileInputStream(file);
	    ObjectInputStream ois = new ObjectInputStream(fis);
	    try {
		recList = (HashMap<String, Record>) ois.readObject();
	    } catch (ClassNotFoundException e) {
		e.printStackTrace();
		throw new IOException("Unable to read from persistant file");
	    }
	    ois.close();
	    System.out.println("Records read from file");
	}
    }
    
    private static void readConfigFile() 
	throws IOException {
	InputStreamReader ir = 
	    new InputStreamReader(
				  new FileInputStream(CONFIGFILE)
				  );
	BufferedReader br = new BufferedReader(ir);
	portNum = Integer.parseInt(br.readLine());
	portNumForSrv = Integer.parseInt(br.readLine());
	serFile = br.readLine();
	br.close();
    }

    /*private static byte extractCmd() {
      System.out.println("data length: " + rcvData.length);
      String data = new String(rcvData).trim();
      System.out.println("Data: " + data);
      int l = data.length();
      byte cmd = rcvData[l];
      System.out.println("cmd: " + cmd);
      rcvData[l] = 0;
      return cmd;
      }
    */

    public static synchronized boolean isOnList(String srvname) {
	return recList.containsKey(srvname);
    }

    public static synchronized Record getFromList(String srvname) {
	return recList.get(srvname);
    }

    public static synchronized void removeFromList(String srvname) {
	recList.remove(srvname);
    }

    public static synchronized Iterator getListIterator() {
	return recList.entrySet().iterator();
    }

    public static synchronized void addToList(String name, Record record) {
	recList.put(name, record);
    }

    public static synchronized void sendToClient(DatagramPacket pkt) {
	pktsToClientsList.offer(pkt);
    }

    public static synchronized void sendToServer(DatagramPacket pkt) {
	pktsToServersList.offer(pkt);
    }

    public static synchronized DatagramPacket getPacketToSendToClient() {
	return pktsToClientsList.remove();
    }

    public static synchronized DatagramPacket getPacketToSendToServer() {
        return pktsToServersList.remove();
    }

    public static void kill() 
	throws IOException {
	serializeRecords();
	System.exit(1);
	// clientListener.stop();
	// clientResponder.stop();
    }

    public static synchronized boolean needToSendPktsToClient() {
	return (pktsToClientsList.size() > 0);
    }

    public static synchronized boolean needToSendPktsToServer() {
        return (pktsToServersList.size() > 0);
    }

    public static synchronized InetAddress getMySrvAddress() {
	return server2ServerSocket.getInetAddress();
    }

    public static synchronized boolean isClientRegistered(String name) {
	return clientMap.containsKey(name);
    }

    public static synchronized void registerClient(String name, 
						   InetAddress addr,
						   int port) {
	clientMap.put(name, new ClientInfo(name, addr, port));
    }

    public static synchronized void unregisterClient(String name) {
        clientMap.remove(name);
    }

    public static synchronized ArrayList<Record> getConnectedServers() {
	ArrayList<Record> connectedServersList 
	    = new ArrayList<Record>();
	Iterator it = recList.entrySet().iterator();
	while (it.hasNext()) {
	    Map.Entry pairs = (Map.Entry) it.next();
	    Record val = (Record) pairs.getValue();
	    if (val.isConnected == true) {
		connectedServersList.add(val);
            }
	}
	return connectedServersList;
    }

    public static synchronized ArrayList<String> getRegisteredClientNames() {
	ArrayList<String> registeredClientsList = new ArrayList<String>();
        Iterator it = clientMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            ClientInfo val = (ClientInfo) pairs.getValue();
            registeredClientsList.add(val.clientName);
        }
        return registeredClientsList;
    }

    boolean shouldRespondToClient = false, shouldRespondToServer = false;
    static DatagramSocket serverSocket, server2ServerSocket;
    static Map<String, Record> recList = new HashMap<String, Record>();
    static Map<String, ClientInfo> clientMap = new HashMap<String, ClientInfo>();
    static Queue<DatagramPacket> pktsToClientsList = new LinkedList<DatagramPacket>();
    static Queue<DatagramPacket> pktsToServersList = new LinkedList<DatagramPacket>();
    static Record rec = null;
    static byte[] rcvData = new byte[1024];
    static byte[] rcvDataFrmSrv = new byte[1024];
    static DatagramPacket rcvPkt, rcvPktFrmSrv;
    static DatagramPacket packetToSendToClient;
    static String sendData, sendDataToSrv;
    static int portNum, portNumForSrv;

    private static String serFile; // file to store persistant records
    private static Thread clientListener;
    private static Thread clientResponder;
    
    private final static String CONFIGFILE = "server.conf";
}