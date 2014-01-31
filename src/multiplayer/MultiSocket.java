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
import java.util.Iterator;
import java.util.Set;

/**
 * Server for Rolit.
 * 
 * @author Max Messerich and Joeri Kock
 * 
 */
public class MultiSocket implements Runnable {

	private static class Session implements Runnable {
		/*
		 * ArrayList containing the names of all players.
		 */
		private ArrayList<String> playerNames;
		/*
		 * ArrayList containing all player instances.
		 */
		private ArrayList<Player> players;
		/**
		 * 
		 */

		private ArrayList<String> rematchRequests;
		/*
		 * Board for each running game on the server.
		 */
		private Board board;
		/*
		 * Boolean defining whether the session currently in a game or not.
		 */
		private boolean gameRunning;
		/*
		 * Boolean defining whether a new ball has been placed on the board.
		 */
		private boolean newBall;
		/*
		 * Boolean defining the status of the the thread running the session.
		 */
		private boolean active;
		/*
		 * Boolean defining whether the client has been asked whether he wants
		 * to play again.
		 */
		private boolean sendQuery;

		/*
		 * Integer defining how many players can join this session.
		 */
		public int playerCount;

		/**
		 * A session is a thread that is started when a player request a game
		 * session but the server can not find a fitting one.
		 * 
		 * @param playerCount
		 *            Number of players that are supported by this session.
		 * 
		 * @param clientName
		 *            Name of the client requesting the session.
		 */
		public Session(int thePlayerCount, String theClientName) {

			players = new ArrayList<Player>();
			playerNames = new ArrayList<String>();
			playerNames.add(theClientName);
			board = new Board();
			this.playerCount = thePlayerCount;
			this.rematchRequests = new ArrayList<String>();
			System.out.println("New session created by " + theClientName);
			active = true;
		}

		/**
		 * 
		 * @return State of the session thread. False means that the the thread
		 *         can be terminated.
		 */
		public boolean active() {
			return active;
		}

		/**
		 * 
		 * @return The number of players supported by this session.
		 */
		public int getPlayerCount() {
			return playerCount;
		}

		/**
		 * 
		 * @return true if there are no spaces left in the session
		 */
		public boolean isFull() {
			return playerNames.size() == playerCount;
		}

		/**
		 * 
		 * @return ArrayList containing all player instances of this sdession.
		 */
		private ArrayList<String> getPlayers() {
			return playerNames;
		}

		/**
		 * 
		 * @return True if the session is currently in game. If it is not, a new
		 *         Player can join.
		 */
		public boolean isInGame() {
			return gameRunning;
		}


		/**
		 * Ends the current game and stops the session thread.
		 */
		public void gameOver(String player) {
			System.out.println("Player left or game over");
			String[] args = {player};
			sendToPlayers("gameOver", args);
			active = false;
		}

		private void sendRematchQuery() {
			String[] args = {"restart"};
			sendToPlayers("gameOver", args);
		}

		/**
		 * Sends a message to all clients that are players in this session.
		 * 
		 * @param message
		 *            should follow the protocol
		 * @param args
		 *            String[] containing valid arguments needed for the message
		 */
		public void sendToPlayers(String message, String[] args) {
			for (int i = 0; i < playerNames.size(); i++) {
				if (clients.get(playerNames.get(i)) != null) {
					MultiSocket sock = socketList.get(clients.get(playerNames
							.get(i)));
					if (sock != null) {
						sock.sendMessage(message, args);
					}
				}

			}
		}

		/**
		 * Tries to add a new client to the session.
		 * 
		 * @param name
		 *            name of the client
		 * @return true if the player joined the session
		 */
		public synchronized boolean join(String name) {
			if (!gameRunning && playerNames.size() < playerCount) {
				playerNames.add(name);
				System.out.println(name
						+ " joined session. Current player count: "
						+ playerNames.size() + "( of " + playerCount + ")");
				if (isFull()) {
					this.startGame();
				}
				this.gameRunning = this.isFull();
				System.out.println("gameRunning : " + gameRunning);
				return true;
			}
			return false;
		}

		/**
		 * Tries to execute the move x, y with the color of the player with the
		 * given name. If the execution of the move fails, the client is kicked
		 * from the game.
		 * 
		 * @param x
		 * @param y
		 * @param player
		 */
		public void makeMove(int x, int y, String player) {
			for (int i = 0; i < playerNames.size(); i++) {
				if (player.equals(playerNames.get(i))) {
					if (board.currentPlayer().getName().equals(player)) {

						if (!board.tryMove(x, y, board.currentPlayerColor())) {
							// kick player
							System.out.println("Player "
									+ board.currentPlayer().getName()
									.equals(player)
									+ " send an invalid move");


						} else {
							System.out.println("SESSION: NEW BALL AT" + x
									+ " , " + y);
							sendQuery = true;
							board.update();

							if (board.modified()) {
								newBall = true;

							}
							if (board.finished()) {
								sendQuery = true;
							}
						}

					}
				}
			}
		}

		/**
		 * Starts a new game in the session.
		 */
		public void startGame() {
			board = new Board();
			System.out.println("Session starting a new Game");
			players.clear();
			for (int i = 0; i < playerNames.size(); i++) {
				players.add(new NetworkPlayer(playerNames.get(i), i));
			}
			board.newGame(players);

			gameRunning = true;
			sendToPlayers("newGame",
					playerNames.toArray(new String[playerNames.size()]));
			socketList
			.get(clients.get(board.currentPlayer()
					.getName())).sendMessage(
							"yourTurn", null);
		}
		/**
		 * Checks whether all players in the session requested a rematch and starts
		 * a new game if they did.
		 * @param name
		 */
		public void requestRematch(String name) {
			if (!this.rematchRequests.contains(name)) {
				System.out.println(name + " wants to play again.");
				System.out.println(this.rematchRequests.size() + " | " + this.players.size());
				this.rematchRequests.add(name);

				if (this.rematchRequests.size() == this.players.size()) {
					this.startGame();
				}
			}
		}

		/**
		 * Thread for each session.
		 */
		@Override
		public void run() {

			while (active) {
				if (gameRunning) {

					if (!board.finished()) {

						if (newBall) {
							System.out.println("WAITING FOR NEW BALL");
							String[] args = {board.getNewBallXPos() + "",
									board.getNewBallYPos() + ""};
							sendToPlayers("update", args);
							socketList
							.get(clients.get(board.currentPlayer()
									.getName())).sendMessage(
											"yourTurn", null);
							newBall = false;
							sendQuery = true;
						}
					} else {
						if (sendQuery) {
							sendRematchQuery();
							sendQuery = false;
						}
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
					if (sessions.get(i).getPlayerCount() == playerCount
							&& !sessions.get(i).isFull()
							&& !sessions.get(i).isInGame()) {
						sessions.get(i).join(playerName);
						System.out.println("SessionManager found session");
						playerSession.put(playerName, sessions.get(i));
						return sessions.get(i);
					}
				}
				System.out
				.println("Sessions founds but full. Starting new one");
				Session session = new Session(playerCount, playerName);
				sessions.add(session);
				Thread sessionThread = new Thread(session);
				sessionThread.start();
				playerSession.put(playerName, session);
				return session;
			}
		}
	}

	private Socket socket;
	private static HashMap<String, Integer> clients = new HashMap<String, Integer>();
	private static ArrayList<MultiSocket> socketList = new ArrayList<MultiSocket>();
	private static ArrayList<String> messages = new ArrayList<String>();
	private static final SessionHandler SESSIONHANDLER = new SessionHandler();
	private boolean isClient;
	private int index;
	private String clientName;
	private boolean isActive;
	private boolean searching;

	public MultiSocket(Socket sock) {
		this.socket = sock;
		socketList.add(this);
		this.index = socketList.size() - 1;
		this.isActive = true;
	}

	public static void removeClient(String name) {
		socketList.remove(clients.get(name));
		clients.remove(name);
	}

	private void close(Session session) {
		System.out.println("Shutting down client " + clientName);
		if (session != null) {
			session.gameOver(clientName);
		}
		removeClient(clientName);
		isActive = false;
	}

	public static void closeAll(ServerSocket serverSocket) {
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < socketList.size(); i++) {
			socketList.get(i).close(null);
		}
	}

	/**
	 * Login.
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
			this.close(null);
		}
		if (message != null) {
			String[] messageParts = message.split(" ");
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
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		while (isActive) {
			String[] s = getMessage(bufferedReader);
			String[] args = null;
			if (s != null) {
				for (int a = 0; a < s.length; a++) {
					System.out.println(a + " : " + s[a]);
				}
				Session session = SESSIONHANDLER.getPlayerSession(clientName);
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

					if (!searching && session == null) {
						if (s[0].equals("join") && s.length == 2) {

							if (s[0].equals("join")) {
								// System.out.println("JOIN REQUEST");

								int playerCount = Integer.parseInt(s[1]);
								if (playerCount > 1 && playerCount <= 4) {
									SESSIONHANDLER.requestSession(
											Integer.parseInt(s[1]), clientName);
								}

							}
						}
					} else {
						if (s[0].equals("move") && s.length == 3) {
							session.makeMove(Integer.parseInt(s[1]),
									Integer.parseInt(s[2]), clientName);
						}
						if (s[0].equals("disjoin")) {
							session.gameOver(clientName);
							SESSIONHANDLER.updateSessions();
						}
						if (s[0].equals("join")) {
							System.out.println("CLIENT WANTS REMATCH!!!");
							session.requestRematch(clientName);
						}

					}

				}
				this.sendMessage(s[0] + "Ack", args);
				if (s[0].equals("logOut")) {
					this.close(session);
				}
			}
		}
		try {
			Session session = SESSIONHANDLER.getPlayerSession(clientName);
			if (session != null) {
				session.gameOver(null);
				SESSIONHANDLER.updateSessions();
			}
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void sendMessage(String message, String[] args) {
		PrintWriter printWriter = null;
		try {
			printWriter = new PrintWriter(new OutputStreamWriter(
					socket.getOutputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}

		String append = new String();
		if (args != null) {
			for (int i = 0; i < args.length; i++) {
				append += " " + args[i];
			}
		}
		System.out.println("SERVER MESSAGE: Send " + message + "with ARGS: { "
				+ append + " to " + this.clientName);
		printWriter.println(message + append);
		printWriter.flush();
	}

	/**
	 * Runnable to listen for new Sockets.
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
	 * Starts a new Server.
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

		// if (args.length < 1) {
		int port = Integer.parseInt(args[0]);
		if (port > 1000 && port < 9999) {
			ServerSocket serverSocket = null;
			try {
				serverSocket = new ServerSocket(port);
			} catch (IOException e) {
				System.out.println("Port " + port + " allready in use.");
				e.printStackTrace();
			}
			Listener listener = new Listener();
			listener.setServerSocket(serverSocket);
			Thread listenerThread = new Thread((Runnable) listener);
			listenerThread.start();
		} else {
			System.out.println("Invalid port number");
		}
		// }else{
		// System.out.println("Please give a port number in the arguments.");
		// }
	}
}
