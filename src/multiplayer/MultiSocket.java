package multiplayer;

import game.Board;
import game.NetworkPlayer;
import game.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class MultiSocket implements Runnable {

	private static class Session implements Runnable {
		private ArrayList<String> playerNames;
		private ArrayList<Player> players;

		private Board board;
		boolean gameRunning, newBall, active;
		public int playerCount;

		public Session(int playerCount, String clientName) {

			players = new ArrayList<Player>();
			playerNames = new ArrayList<String>();
			playerNames.add(clientName);
			board = new Board();
			this.playerCount = playerCount;
			System.out.println("New session created by" + clientName);
			active = true;
		}

		public boolean active() {
			return active;
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

		public void gameOver() {
			System.out.println("Player left or game over");
			sendToPlayers("gameOver", null);
			active = false;
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

				return true;
			}
			return false;
		}

		public void makeMove(int x, int y, String player) {
			System.out.println("Session makeMove called :" + x + ", " + y
					+ ", " + player);
			System.out.println("Awaiting color " + board.currentPlayerColor());
			for (int i = 0; i < playerNames.size(); i++) {
				if (player.equals(playerNames.get(i))) {
					System.out.println("Player with name " + player
							+ " found in Game");
					if (board.currentPlayer().getName().equals(player)) {
						System.out.println("Found user is active player");
						if (!board.tryMove(x, y, board.currentPlayerColor())) {
							// kick player
							System.out.println("Move did not work");

						} else {
							System.out.println("SESSION: NEW BALL AT" + x
									+ " , " + y);
						}
						board.update();
						if(board.modified()){
							newBall = true;
						}
					}
				}
			}
			System.out.println("-----------------");
		}

		public void startGame() {
			System.out.println("Session starting a new Game");
			board.newGame(players);
			gameRunning = true;

		}

		@Override
		public void run() {
			while (active) {
				System.out.print("");
				if (gameRunning) {
					if (newBall) {
						System.out.println("WAITING FOR NEW BALL");
						String[] args = {board.getNewBallXPos() + "",
								board.getNewBallYPos() + "" };
						sendToPlayers("update", args);
						socketList.get(
								clients.get(board.currentPlayer().getName()))
								.sendMessage("yourTurn", null);
						newBall = false;
					}
				} else {
					if (playerNames.size() == playerCount) {
						System.out.println("Session full. Starting Game!");
						for (int i = 0; i < playerNames.size(); i++) {
							players.add(new NetworkPlayer(playerNames.get(i), i));
						}
						gameRunning = true;
						startGame();
						sendToPlayers("newGame",
								playerNames.toArray(new String[playerNames
										.size()]));
						socketList.get(
								clients.get(board.currentPlayer().getName()))
								.sendMessage("yourTurn", null);
					}
				}
			}
		}
	}

	private static class SessionHandler {
		private static ArrayList<Session> sessions = new ArrayList<Session>();
		private static HashMap<String, Session> playerSession = new HashMap<String, Session>();

		public SessionHandler() {

		}

		private Session getPlayerSession(String name) {

			for (String s : playerSession.keySet()) {
			}
			return playerSession.get(name);
		}

		/**
		 * Deletes sessions that are no longer active and removes their players
		 * from the list of active player so they can join a new session.
		 */
		public void updateSessions() {

			Iterator<Session> ses = sessions.iterator();
			ArrayList<String> inactivePlayerList = new ArrayList<String>();
			while (ses.hasNext()) {
				Session s = ses.next();
				if (!s.active()) {
					inactivePlayerList.addAll(s.getPlayers());
					System.out.println("Inactive Session shut down");
					ses.remove();
				}

			}
			for (int i = 0; i < inactivePlayerList.size(); i++) {
				playerSession.remove(inactivePlayerList.get(i));
			}
			System.out.println("Remaining active sessions; " + sessions.size());
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
				playerSession.put(playerName, session);
				return session;
			} else {
				for (int i = 0; i < sessions.size(); i++) {
					if (!sessions.get(i).isInGame()
							&& sessions.get(i).getPlayerCount() == playerCount) {
						sessions.get(i).join(playerName);
						System.out.println("SessionManager found session");
						playerSession.put(playerName, sessions.get(i));
						return sessions.get(i);
					}
				}
				Session session = new Session(playerCount, playerName);
				sessions.add(session);
				playerSession.put(playerName, session);
				return session;
			}
		}
	}

	private Socket socket;
	private static HashMap<String, Integer> clients = new HashMap<String, Integer>();
	private static ArrayList<MultiSocket> socketList = new ArrayList<MultiSocket>();
	private static ArrayList<String> messages = new ArrayList<String>();
	private static final SessionHandler sessionHandler = new SessionHandler();
	// private static final String user = "Max";
	// private static final String[][] users = { { "max", "hallo" },
	// { "gollum", "hallo" }, { "lord", "hallo" }, { "dr.cool", "hallo" } };
	private boolean isClient;
	private int index;
	private String clientName;
	private boolean isActive, shutDown;
	private boolean searching;

	public MultiSocket(Socket sock) {
		this.socket = sock;
		socketList.add(this);
		this.index = socketList.size() - 1;
		this.isActive = true;
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
		System.out.println("Shutting down client "+clientName);
		isActive = false;
		shutDown = true;
	}

	public static void closeAll(ServerSocket serverSocket) {
		try {
			serverSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 0; i < socketList.size(); i++) {
			socketList.get(i).close();
		}
	}

	/**
	 * Login
	 * 
	 * @param name
	 * @param password
	 */
	private boolean validateUser(String name) {
		for (String user : clients.keySet()) {
			if (user.equals(name)) {
				return false;
			}
		}
		return true;
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
			this.close();
//			e.printStackTrace();
		}
		if(message!=null){
			String[] messageParts = message.split(" ");
			String[] returnMessage = new String[messageParts.length];
			System.out.println("");
			System.out.println("Server recived message: " + message + " from "
					+ this.clientName);
	
			return messageParts;
		}
		return null;
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
		while (isActive) {
			String[] s = getMessage(bufferedReader);
			String[] args = null;
//			if (s!=null&&s[0]!="logOut") {
//				String[] s = getMessage(bufferedReader);

				// System.out.println("ICH BIN HIER!");
				if (s != null) {
					for (int a = 0; a < s.length; a++) {
						System.out.println(a + " : " + s[a]);
					}
					// If the client is not registered: only login attempt
					// possible
					// if (s[0].equals("logOut") && clients.get(clientName) !=
					// null) {
					// socketList.get(clients.get(clientName)).close();
					// socketList.remove(clients.remove(clientName));
					//
					// } else {
					if (!isClient) {
						if (s.length == 2 && s[0].equals("login")) {
							args = new String[1];
							if (validateUser(s[1])) {
								isClient = true;
								clients.put(s[1], index);
								this.clientName = s[1];
								args[0] = "welcome";
							} else {
								args[0] = "incorrect";
							}
						}

					} else {
						Session session = sessionHandler
								.getPlayerSession(clientName);

						if (!searching && session == null) {
							if (s[0].equals("join") && s.length == 2) {

								if (s[0].equals("join")) {
									// System.out.println("JOIN REQUEST");
									int playerCount = Integer.parseInt(s[1]);
									if (playerCount > 1 && playerCount <= 4) {
										sessionHandler.requestSession(
												Integer.parseInt(s[1]),
												clientName);
									}

								}
							}
						} else {
							if (s[0].equals("move") && s.length == 3) {
								session.makeMove(Integer.parseInt(s[1]),
										Integer.parseInt(s[2]), clientName);
							}
							if (s[0].equals("disjoin")) {
								session.gameOver();
								sessionHandler.updateSessions();
							}

						}
						
					}
					this.sendMessage(s[0] + "Ack", args);
					if (s[0].equals("logOut")) {
							this.close();
					}
				}
			}
//		}
		try {
			Session session = sessionHandler
					.getPlayerSession(clientName);
			if(session!=null){
				session.gameOver();
				sessionHandler.updateSessions();
				
			}
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

		String append = new String();
		if (args != null) {
			for (int index = 0; index < args.length; index++) {
				append += " " + args[index];
			}
		}
		System.out.println("SERVER MESSAGE: Send " + message + "with ARGS: { "
				+ append + " to " + this.clientName);
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
		Thread listenerThread = new Thread((Runnable) listener);
		listenerThread.start();
	}

	/**
	 * Main loop for testing init().
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		int port = Integer.parseInt(args[0]);
		if (port > 1000 && port < 9999) {
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
			Thread listenerThread = new Thread((Runnable) listener);
			listenerThread.start();
		} else {
			System.out.println("Invalid port number");
		}
	}
}
