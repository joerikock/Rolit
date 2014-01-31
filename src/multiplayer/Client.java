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
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Client implements Runnable {
	private Socket socket;
	private String name;
	private boolean inGame;
	private boolean currentPlayer;
	private BufferedReader bufferedReader;
	private ClientListener listener;
	private Board clientBoard;
	private Thread listenerThread;
	private String errorMessage;
	/**
	 * Defines the state in which the client is. 0 = not logged in. 1 = trying
	 * to log in. 2 = logged in.
	 */
	private int loginStatus;
	private static boolean running = true;
	/*
	 * Boolean defining whether the client started a new game on his boarc
	 */
	private boolean newGame;
	public Client() {

	}
	public boolean connect(int port, String serverName) {
		InetAddress ip = null;
		System.out.println("Client loggin started");
		try {
			ip = InetAddress.getByName(serverName);
		} catch (UnknownHostException e1) {
			this.errorMessage = "Server " + serverName + " not found on port " +port;
			System.out.println("Server " + serverName + " not found on port "
					+ port);
			e1.printStackTrace();
		}
		socket = null;
		try {
			socket = new Socket(ip, port);
		} catch (IOException e) {
			this.errorMessage = "Could not connect to server.";
			System.out.println(e.getMessage());
		}
		if (socket != null) {
			bufferedReader = null;
			try {
				bufferedReader = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));
			} catch (IOException e) {
				e.printStackTrace();
			}
			/**
			 * Start a new Thread for listening for incoming messages
			 */
			listener = new ClientListener(bufferedReader, this);
			listenerThread = new Thread(listener);
			listenerThread.start();
			System.out.println(socket);
		}
		System.out.println("Client loggin done");
		return (socket!=null);
	}

	public void login(String name) {
		this.name = name;
		String[] a = { name };
		sendMessage("login", a);
		loginStatus = 1;
	}
	public void rematch(){
		sendMessage("join",null);
	}
	public void close() {
		if(socket!=null){
			sendMessage("logOut", null);
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			loginStatus = 0;
		}
		running = false;

	}

	@Override
	public void run() {
		while (running) {
			if (this.inGame) {
				if (this.currentPlayer) {
//					System.out.println(this.name + " is current player");
					if (clientBoard.currentPlayer().hasMove()) {
						int[] position = clientBoard.currentPlayer()
								.determineMove(clientBoard);
						if (position != null) {
							String[] args = { position[0] + "",
									position[1] + "" };
							this.sendMessage("move", args);
						}
					}

				}
			}
		}

	}

	public void sendMessage(String command, String[] args) {
		if (socket != null) {
			PrintWriter printWriter = null;
			try {
				printWriter = new PrintWriter(new OutputStreamWriter(
						socket.getOutputStream()));
			} catch (IOException e) {
				e.printStackTrace();
			}
			String message = command;
			if (args != null) {
				for (int i = 0; i < args.length; i++) {
					message += " " + args[i];
				}
			}
			System.out.println("NEW COMMAND SEND TO SERVER: " + message);
			printWriter.println(message);
			printWriter.flush();

		}
	}

	private static class ClientListener implements Runnable {

		private BufferedReader bufferedReader;
		private Client client;

		public ClientListener(BufferedReader bufferedReader, Client client) {
			this.bufferedReader = bufferedReader;
			this.client = client;
		}

		@Override
		public void run() {
			while (running) {
				System.out.println("");
				System.out
						.println("Client waiting for new input from the server");
				String message = null;
				try {
					message = bufferedReader.readLine();
				} catch (IOException e) {
					client.errorMessage = "Connection to the server lost";
					System.out.println("Connection to the server lost");
					// e.printStackTrace();
					client.loginStatus = 0;
					client.inGame = false;
					break;

				}
				if (message != null) {
					String[] messageParts = message.split(" ");
					System.out.println("SERVER MESSAGE : "+ message);
					if (messageParts.length > 0) {
						if (messageParts[0].equals("newGame")) {
							ArrayList<String> names = new ArrayList<String>();
							for (int i = 1; i < messageParts.length; i++) {
								names.add(messageParts[i]);
							}
							client.startGame(names);
						}
					}
					if (client.inGame()) {
						if (messageParts[0].equals("yourTurn")) {
							client.setActive();
						}
						if (messageParts[0].equals("update")
								&& messageParts.length == 3) {
							int x = Integer.parseInt(messageParts[1]);
							int y = Integer.parseInt(messageParts[2]);
							client.getBoard().tryMove(x, y,
									client.getBoard().currentPlayerColor());
						}
						if (messageParts[0].equals("gameOver")) {
							
							if(messageParts.length==1){
								client.errorMessage = "The server shut down";
								client.leaveGame();
							}else{
								if(messageParts[1].equals("restart")){
										System.out.println("Server asking for a rematch");
										client.errorMessage = "Play again?";
								}else{
									client.errorMessage = "A player left the game. Going back to the main menu.";
									client.leaveGame();
								}
								
							}
							
							
						}
					}
					if (messageParts.length == 2) {
						if (messageParts[0].equals("loginAck")) {
							if (messageParts[1].equals("welcome")) {
								// Login worked
								client.errorMessage = "You are now logged in!";
								client.loginComplete();
							}
							if (messageParts[1].equals("incorrect")) {
								client.errorMessage = "Login failed. Choose a different name";
								// Login failed
							}
						}

					}

				}
			}
		}

	};
	public String getMessage(){
		return this.errorMessage;
	}
	public void messageFetched(){
		this.errorMessage = null;
	}
	public int getLoginState() {
		return loginStatus;
	}
	public boolean staredNewGame(){
		return this.newGame;
	}
	public void newGameFetched(){
		this.newGame = false;
	}
	private void loginComplete() {
		loginStatus = 2;
	}

	public void requestGame(int playerCount) {
		System.out.println("requestGame called");
		String[] c = { String.valueOf(playerCount) };
		sendMessage("join", c);
	}

	public void startGame(ArrayList<String> players) {
		ArrayList<Player> playerList = new ArrayList<Player>();
		// playerList.remove(name);
		// playerList.add(new HumanPlayer(name, 0, boardGui));
		for (int i = 0; i < players.size(); i++) {
			Player p = new NetworkPlayer(players.get(i), i + 1);
			playerList.add(p);
		}
		this.newGame = true;
		System.out.println(" - -");
		System.out.println("RESETTING BOARD AND STARTING A NEW GAME");
		System.out.println("- -");
		clientBoard = new Board();
		this.clientBoard.newGame(playerList);
		inGame = true;

	}

	private void setActive() {
		currentPlayer = true;
	}

	public boolean inGame() {
		return inGame;
	}

	public Board getBoard() {
		return this.clientBoard;
	}

	public boolean isCurrentPlayer() {
		return this.currentPlayer;
	}

	public void leaveGame() {
		inGame = false;
		sendMessage("disjoin", null);
	}

	public void makeMove(int x, int y) {
		if (currentPlayer) {
			if (clientBoard.validateMove(x, y)) {
				String[] args = { x + "", y + "" };
				this.sendMessage("move", args);
				currentPlayer = false;
			}

		}
	}

}
