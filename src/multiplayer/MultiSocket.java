package multiplayer;

import game.Board;
import game.NetworkPlayer;
import game.Player;

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

	private static class Session implements Runnable {
		private ArrayList<String> playerNames;
		private ArrayList<Player> players;
		private Board board;
		boolean gameRunning, justStarted;
		public int playerCount;

		public Session(int playerCount, String clientName) {

			players = new ArrayList<Player>();
			playerNames = new ArrayList<String>();
			playerNames.add(clientName);
			board = new Board();
			this.playerCount = playerCount;
			System.out.println("New session created by" + clientName);
		}

		public int getPlayerCount() {
			return playerCount;
		}

		private ArrayList<String> getPlayers() {
			return playerNames;
		}

		public boolean isInGame() {
			return gameRunning;
		}

		public void sendToPlayers(String message, String[] args) {
			for (int i = 0; i < playerNames.size(); i++) {

				socketList.get(clients.get(playerNames.get(i))).sendMessage(
						message, args);
			}
		}

		public boolean join(String name) {
			if (!gameRunning && playerNames.size() < playerCount) {
				playerNames.add(name);
				System.out.println(name + " joined session. "
						+ playerNames.size() + " | " + playerCount);
				if (playerNames.size() == playerCount) {
					System.out.println("Session full. Starting Game!");
					for(int i=0; i<playerNames.size();i++){
						players.add(new NetworkPlayer(playerNames.get(i),i));
					}
					gameRunning = true;
					startGame();
					sendToPlayers("newGame",
							playerNames.toArray(new String[playerNames.size()]));
				}
				return true;
			}
			return false;
		}

		public void makeMove(int x, int y, String player) {
			for (int i = 0; i < playerNames.size(); i++) {
				if (player.equals(playerNames.get(i))) {
					if (board.currentPlayer().getName().equals(player)) {
						if (!board.tryMove(x, y, board.currentPlayerColor())) {
							// kick player
						}
					}
				}
			}
		}

		public void addPlayer(Player player) {
			players.add(player);

		}

		public void startGame() {
		
			board.newGame(players);
			justStarted = true;
			gameRunning = true;
		}

		@Override
		public void run() {
			while (true) {
				if (gameRunning) {
					System.out.println("Session in game");
					if (board.modified() || justStarted) {
						socketList.get(
								clients.get(board.currentPlayer().getName()))
								.sendMessage("yourTurn", null);

					}
				}
			}
		}

		public Player currentPlayer() {
			return board.currentPlayer();
		}
	}

	private static class SessionHandler {
		ArrayList<Session> sessions = new ArrayList<Session>();

		public SessionHandler() {
			this.sessions = new ArrayList<Session>();
		}

		private Session getPlayerSession(String name) {
			for (int i = 0; i < sessions.size(); i++) {
				for (int a = 0; a < sessions.get(i).getPlayers().size(); a++) {
					if (sessions.get(i).getPlayers().get(a).equals(name)) {
						return sessions.get(i);
					}
				}
			}
			return null;
		}

		/**
		 * Creates or finds a new game session for the client.
		 * 
		 * @param playerCount
		 */
		public Session requestSession(int playerCount, String playerName) {

			if (sessions.size() == 0) {
				Session session = new Session(playerCount, playerName);
				sessions.add(session);
				Thread sessionThread = new Thread(session);
				sessionThread.start();
				return session;
			} else {
				for (int i = 0; i < sessions.size(); i++) {
					if (!sessions.get(i).isInGame()
							&& sessions.get(i).getPlayerCount() == playerCount) {
						sessions.get(i).join(playerName);
						System.out.println("SessionManager found session");
						return sessions.get(i);
					}
				}
				Session session = new Session(playerCount, playerName);
				sessions.add(session);
				return session;
			}
		}
	}

	private Socket socket;
	private static HashMap<String, Integer> clients = new HashMap<String, Integer>();
	private static ArrayList<MultiSocket> socketList = new ArrayList<MultiSocket>();
	private static ArrayList<String> messages = new ArrayList<String>();
	private static final SessionHandler sessionHandler = new SessionHandler();
	private static final String user = "Max";
	private static final String[][] users = { { "max", "hallo" },
			{ "gollum", "hallo" }, { "lord", "hallo" }, { "dr.cool", "hallo" } };
	private boolean isClient;
	private int index;
	private String clientName;

	public MultiSocket(Socket sock) {
		this.socket = sock;
		socketList.add(this);
		this.index = socketList.size() - 1;

	}

	private void setClientName(String name) {
		this.clientName = name;
	}

	public static void addClient(Socket socket) {
		// Send request for userNam

	}

	public static void logout(String name) {
		if (clients.containsKey(name)) {
			socketList.get(clients.get(name)).close();
			socketList.remove(clients.get(name));

		}
	}

	private void close() {

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
		// for (int i = 0; i < socketList.size(); i++) {
		// // TODO: Sent message to socket that its being shut down.
		// try {
		// socketList.get(i).close();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }

	}

	/**
	 * Login
	 * 
	 * @param name
	 * @param password
	 */
	private boolean validateUser(String name, String password) {
		for (int i = 0; i < users.length; i++) {
			if (users[i][0].equals(name)) {
				if (users[i][1].equals(password)) {
					System.out.println("ValidUser!");
					return true;
				}
			}
		}
		return false;
	}

	public static String[] getClientMessage(String name) {
		if (clients.containsKey(name)) {
			// return (socketList.get(clients.get(name))).getMessage();
		}
		return null;
	}

	/**
	 * Fetches the message written into a socket.
	 * 
	 * @param socket
	 * @return
	 */
	private String[] getMessage(BufferedReader reader) {

		String message = null;
		try {
			message = reader.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO: check whether client and message are valid.
		String[] messageParts = message.split(" ");
		String[] returnMessage = new String[messageParts.length];
		System.out.println("Server recived message: " + message);
		return messageParts;
	}

	public static Set<String> getClients() {
		return clients.keySet();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while (true) {

			String[] s = getMessage(bufferedReader);
			String[] args = null;
			// System.out.println("ICH BIN HIER!");
			if (s != null) {

				for (int a = 0; a < s.length; a++) {
					System.out.println(a + " : " + s[a]);
				}
				// If the client is not registered: only login attempt possible
				if (!isClient) {
					if (s.length == 3 && s[0].equals("login")) {
						args = new String[1];
						System.out.println("Trying to login");
						if (validateUser(s[1], s[2])) {
							isClient = true;
							clients.put(s[1], index);
							this.clientName = s[1];
							System.out.println("CLIENTSIZE: " + clients.size());
							args = new String[1];
							args[0] = "welcome";
						} else {
							args[0] = "incorrect";
						}
					}

				} else {

					if (s.length == 2 && s[0].equals("join")) {
						Session session = sessionHandler.getPlayerSession(user);
						if (s[0].equals("join")) {
							System.out.println("JOIN REQUEST");
							sessionHandler.requestSession(
									Integer.parseInt(s[1]), clientName);
						}
						if (s[0].equals("move")) {
							session.makeMove(Integer.parseInt(s[1]),
									Integer.parseInt(s[2]), clientName);
						}
					}
				}

				this.sendMessage(s[0], args);
			}
		}
	}

	private void sendMessage(String message, String[] args) {
		PrintWriter printWriter = null;
		try {
			printWriter = new PrintWriter(new OutputStreamWriter(
					socket.getOutputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Send " + message + " to " + this.clientName);
		String append = new String();
		if (args != null) {
			for (int index = 0; index < args.length; index++) {
				append += " " + args[index];
			}
		}
		printWriter.println(message + append);
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

		// Thread thread = new Thread(newSocket);
		// thread.start();
		Listener listener = new Listener();
		listener.setServerSocket(serverSocket);
		Thread listenerThread = new Thread((Runnable) (listener));
		listenerThread.start();

	}

}
