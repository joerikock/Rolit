package multiplayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class MultiSocket implements Runnable {
	private Socket socket;
	private static HashMap<String, Integer> clients = new HashMap<String, Integer>();
	private static ArrayList<MultiSocket> socketList = new ArrayList<MultiSocket>();
	private static ArrayList<String> messages = new ArrayList<String>();

	private static final String user = "Max";
	private static final String[][] users = {{"max", "hallo"},{"gollum","hallo"},{"lord","hallo"},{"dr.cool","hallo"}};
	private boolean isClient;
	private int index;
	public MultiSocket(Socket sock) {
		this.socket = sock;
		socketList.add(this);
		this.index = socketList.size()-1;

	}

	public static void addClient(Socket socket) {
		// Send request for userNam

	}
	public static void logout(String name){
		if(clients.containsKey(name)){
			socketList.get(clients.get(name)).close();
			socketList.remove(clients.get(name));
			
			}
	}
	private void close(){
		
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void closeAll(ServerSocket serverSocket) {
		try {
			serverSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		for (int i = 0; i < socketList.size(); i++) {
//			// TODO: Sent message to socket that its being shut down.
//			try {
//				socketList.get(i).close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}

	}
	/**
	 * Login
	 * @param name
	 * @param password
	 */
	private boolean validateUser(String name, String password){
		for(int i=0; i<users.length;i++){
			if(users[i][0].equals(name)){
				if(users[i][1].equals(password)){
					System.out.println("ValidUser!");
					return true;
				}
			}
		}
		return false;
	}
	public static String[] getClientMessage(String name){
		if(clients.containsKey(name)){
			return (socketList.get(clients.get(name))).getMessage();
		}
		return null;
	}
	/**
	 * Fetches the message written into a socket.
	 * 
	 * @param socket
	 * @return
	 */
	private String[] getMessage() {
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
		} catch (IOException e) {
			System.out.println("Failed to get InputStream from socket.");
			e.printStackTrace();
		}
		char[] buffer = new char[200];
		int charCount = 0;
		try {
			charCount = bufferedReader.read(buffer, 0, 200);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String message = null;
		if (charCount > 0) {
			message = new String(buffer, 0, charCount);
		}
		// TODO: check whether client and message are valid.
		String[] messageParts = message.split(" ");
		String[] returnMessage = new String[messageParts.length];
		
		return messageParts;
	}
	public static Set<String> getClients(){
		return clients.keySet();
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			if(!isClient){
				String[] s = getMessage();
//				System.out.println("ICH BIN HIER!");
				if (s.length == 3) {
					System.out.println("Correct msg_length");
					for (int a = 0; a < s.length; a++) {
						System.out.println(a + " : " + s[a]);
					}
					if (s[0].equals("login")) {
						System.out.println("Trying to login");
						if(validateUser(s[1],s[2])){
							isClient = true;
							clients.put(s[1], index);
							System.out.println("CLIENTSIZE: "+ clients.size());
							sendMessage(socket, "login"+"Ack");
						}
					}
				}
			}else{
//				sendMessage(socket, "Hallo");
			}

		}
	}



	private void sendMessage(Socket socket, String message) {
		PrintWriter printWriter = null;
		try {
			printWriter = new PrintWriter(new OutputStreamWriter(
					socket.getOutputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		printWriter.print(message);
		printWriter.flush();
	}

	/**
	 * Runnable to listen for new Sockets;
	 */
	public static class Listener implements Runnable {
		ServerSocket ssocket = null;

		public void setServerSocket(ServerSocket serverSocket) {
			ssocket = serverSocket;
		}

		@Override
		public void run() {

			while (true) {

				System.out.println("listening");
				Socket socket = null;
				if (ssocket.isClosed()) {
					System.out.println("socketClosed");
					break;
				}
				try {
					socket = ssocket.accept();
				} catch (IOException e) {
					System.out.println("Failed to connect to socket!");
					e.printStackTrace();

				}
				System.out.println("Socket added!");

				if(socketList.size()==3){
					System.out.println("ASD");
					closeAll(ssocket);
					System.exit(0);
				}
				
				MultiSocket newSocket = new MultiSocket(socket);
				Thread thread = new Thread(newSocket);
				thread.start();

			}

		}

	};

	/**
	 * Gets the next message that has not been fetched yet and removes it from
	 * the list of unfetched messages.
	 * 
	 * @return
	 */
	public static String getNextMessage() {
		if (messages.size() > 0) {
			return messages.remove(messages.size() - 1);
		} else {
			return null;
		}

	}

	/**
	 * Starts a new Server
	 */
	public static void init() {
		int port = 1235;
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			System.out.println("Port " + port + " allready in use.");
			e.printStackTrace();
		}
		// MultiSocket newSocket = new MultiSocket(serverSocket);

		Listener listener = new Listener();
		listener.setServerSocket(serverSocket);
		Thread listenerThread = new Thread((Runnable) (listener));
		listenerThread.start();
	}

	/**
	 * Main loop for testing init()
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		int port = 1235;
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			System.out.println("Port " + port + " allready in use.");
			e.printStackTrace();
		}

//		Thread thread = new Thread(newSocket);
//		thread.start();
		Listener listener = new Listener();
		listener.setServerSocket(serverSocket);
		Thread listenerThread = new Thread((Runnable) (listener));
		listenerThread.start();

	}

}
