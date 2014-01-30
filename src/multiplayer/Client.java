package multiplayer;

import game.Board;
import game.BoardGUI;
import game.HumanPlayer;
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
	private static int count = 0;
	private String name;
	private boolean gotAck;
	private boolean inGame;
	private Player player;
	private boolean sendGameRequest, currentPlayer;
	private int gamePlayerCount;
	private BufferedReader bufferedReader;
	private String lastServerMessage;
	private ClientListener listener;
	private Board clientBoard;
	private Thread listenerThread;
	public Client(String name, String password, int port, String serverName) {
		InetAddress ip = null;

		try {
			ip = InetAddress.getByName(serverName);
		} catch (UnknownHostException e1) {
			System.out.println("Server " + serverName + " not found on port "
					+ port);
			e1.printStackTrace();
		}
		socket = null;
		try {
			socket = new Socket(ip, port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Start a new Thread for listening for incoming messages
		listener = new ClientListener(bufferedReader, this);
		listenerThread = new Thread(listener);
		listenerThread.start();
		count++;
		this.name = name;
		System.out.println("Client init. " + name);
		String[] a = { name, password };
		sendMessage("login", a);
//		this.clientBoard = board;

	}

	public void close() {
		sendMessage("logOut");
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		listenerThread.stop();
		
	}

	@Override
	public void run() {
		while (true) {
			if (this.inGame) {
				if (this.currentPlayer) {
					System.out.println(this.name+" is current player");
					if (clientBoard.currentPlayer().hasMove()) {
						int[] position = clientBoard.currentPlayer()
								.determineMove(clientBoard);
						if (position != null) {
							String[] args = { position[0] + "",
									position[1] + "" };
							this.sendMessage("move", args);
						}
					}
					// clientBoard.currentPlayer().makeMove(clientBoard);

				}
			}
		}

	}

	private void sendMessage(String command) {
		PrintWriter printWriter = null;
		try {
			printWriter = new PrintWriter(new OutputStreamWriter(
					socket.getOutputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		printWriter.println(command);
		printWriter.flush();
	}

	public void sendMessage(String command, String[] args) {
		PrintWriter printWriter = null;
		try {
			printWriter = new PrintWriter(new OutputStreamWriter(
					socket.getOutputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String message = command;
		for (int i = 0; i < args.length; i++) {
			message += " " + args[i];
		}
		System.out.println("NEW COMMAND SEND TO SERVER: " + command);
		printWriter.println(message);
		printWriter.flush();
		// set gotAck to false. only can send next message if the previous one
		// has been ackknwoledged
		gotAck = false;
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
			while (true) {
				System.out.println("Client listening");
				String message = null;
				try {
					message = bufferedReader.readLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (message != null) {
					String[] messageParts = message.split(" ");
					System.out.println("readMessage: " + message);
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
						if(messageParts[0].equals("update")&&messageParts.length==3){
							int x = Integer.parseInt(messageParts[1]);
							int y = Integer.parseInt(messageParts[2]);
							client.getBoard().tryMove(x, y, client.getBoard().currentPlayerColor());
						}
					}
					if (messageParts.length == 2) {
						if (messageParts[0].equals("loginAck")) {
							if (messageParts[1].equals("welcome")) {
								// Login worked
								System.out.println("Loggin worked!!!!!");
							}
							if (messageParts[1].equals("incorrect")) {
								// Login failed
							}
						}

					}

				}
			}
		}

	};

	public void requestGame(int playerCount) {
		System.out.println("requestGame called");
		String[] c = { String.valueOf(playerCount) };
		sendMessage("join", c);
	}

	public void startGame(ArrayList<String> players) {
		inGame = true;
		ArrayList<Player> playerList = new ArrayList<Player>();
//		playerList.remove(name);
//		playerList.add(new HumanPlayer(name, 0, boardGui));
		for (int i = 0; i < players.size(); i++) {
			Player p = new NetworkPlayer(players.get(i), i + 1);
			playerList.add(p);
		}
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
	public boolean isCurrentPlayer(){
		return this.currentPlayer;
	}
	public void makeMove(int x, int y){
		if(currentPlayer){
			if(clientBoard.validateMove(x, y)){
				String[ ] args = {x+"",y+""};
				this.sendMessage("move", args);
				currentPlayer = false;
			}

		}
	}
	public static void main(String[] args) {

		Client client1 = new Client("Dr.Schnappus", "sda", 1235, "localHost");
		Thread client1Thread = new Thread((Runnable) client1);
		client1Thread.start();
		Client client2 = new Client("Horst-Peter-Hararld", "sda", 1235,
				"localHost");
		Thread client2Thread = new Thread((Runnable) client2);
		client2Thread.start();
	}
}
