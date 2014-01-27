package network;

import game.Game;
import game.Player;

import java.net.*;
import java.util.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

import javax.swing.JButton;

import rolit.gui.ClientGUI;
import rolit.network.protocol.ClientToServer;

public class Client extends Thread implements ActionListener {
	private Game game;
	private String name;
	private boolean AI;
	private Integer playerCount;

	private ClientGUI gui;
<<<<<<< HEAD
=======
	private BoardGUI gui;
>>>>>>> ee6e528334977e4c51046311bd6a91ee973a5b56

	private Socket sock;
	private BufferedReader in;
	private BufferedWriter out;

	public Client(InetAddress host, Integer port, String name, boolean AI,
			int playerCount) throws IOException {
		this.sock = new Socket(host, port);
		this.in = new BufferedReader(new InputStreamReader(
				sock.getInputStream()));
		this.out = new BufferedWriter(new OutputStreamWriter(
				sock.getOutputStream()));
<<<<<<< HEAD
=======
	public Client(InetAddress host, Integer port, String name, boolean AI, int playerCount) throws IOException {
		this.sock = new Socket(host, port);
		this.in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		this.out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
>>>>>>> ee6e528334977e4c51046311bd6a91ee973a5b56
		this.name = name;
		this.AI = AI;
		this.playerCount = new Integer(playerCount);
		this.gui = null;
	}

	public void run() {
		boolean running = true;
		try {
			sendCommand("connect", name);
			while (running) {
				String lineIn = in.readLine();
				if (lineIn != null)
					readCommand(lineIn);
			}
		} catch (IOException e) {
			running = false;
			disconnect();
		}
	}

	private void readCommand(String lineIn) {
		String[] splitLine = lineIn.toLowerCase().split("\\s");
		String command = splitLine[0];
		List<String> args = Arrays.asList(splitLine).subList(1,
				splitLine.length);
<<<<<<< HEAD
=======
		List<String> args = Arrays.asList(splitLine).subList(1, splitLine.length);
>>>>>>> ee6e528334977e4c51046311bd6a91ee973a5b56

		if (command.equals("ackconnect")) {
			name = args.get(0);
			sendCommand("join", playerCount.toString());
		} else if (command.equals("startgame")) {
			Player p1, p2, p3, p4;
<<<<<<< HEAD
<<<<<<< HEAD
			
			p1 = new Player(args.get(0), 0);
=======

			p1 = new Player(args.get(0), BColor.RED);
>>>>>>> ui
=======
			p1 = new Player(args.get(0), BColor.RED);
			p1 = new Player(args.get(0), 0);
>>>>>>> ee6e528334977e4c51046311bd6a91ee973a5b56
			if (args.size() == 2) {
				p2 = new Player(args.get(1), BColor.GREEN);
				game = new Game(p1, p2);
			} else {
				p2 = new Player(args.get(1), BColor.YELLOW);
				p3 = new Player(args.get(2), BColor.GREEN);
				if (args.size() == 3) {
					game = new Game(p1, p2, p3);
				} else {
					p4 = new Player(args.get(3), BColor.BLUE);
					game = new Game(p1, p2, p3, p4);
				}
			}
			if (gui == null)
				gui = new ClientGUI(this);
			game.addObserver(gui);
			if (AI)
				game.addObserver(new AIController(this, game.getPlayers()
						.getByName(name)));
			game.startPlaying();
		} else if (command.equals("turn")) {

		} else if (command.equals("movedone")) {
			game.makeMove(Integer.parseInt(args.get(1)));
			if (game.getBoard().isFull())
				game = null;
		} else if (command.equals("endgame")) {
			game.deleteObservers();
		} else if (command.equals("kick")) {
			game.kickPlayer(args.get(0));
			if (game.getPlayers().size() < 2)
				sendCommand("join", playerCount.toString());
		} else if (command.equals("message")) {

		}

	}

	/**
	 * Stuurt een bericht naar de Server.
	 * 
	 * @param cmd
	 *            Commando dat verstuurd wordt
	 * @param arg
	 *            De met het commando meegestuurde argument
<<<<<<< HEAD
=======
	 * @param cmd Commando dat verstuurd wordt
	 * @param arg De met het commando meegestuurde argument
>>>>>>> ee6e528334977e4c51046311bd6a91ee973a5b56
	 */
	public void sendCommand(String cmd, String arg) {
		String[] args = { arg };
		sendCommand(cmd, args);
	}

	/**
	 * Stuurt een bericht naar de Server.
	 * 
	 * @param cmd
	 *            Commando dat verstuurd wordt
	 * @param args
	 *            De met het commando meegestuurde argumenten
<<<<<<< HEAD
=======
	 * @param cmd Commando dat verstuurd wordt
	 * @param args De met het commando meegestuurde argumenten
>>>>>>> ee6e528334977e4c51046311bd6a91ee973a5b56
	 */
	public void sendCommand(String cmd, String[] args) {
		String send = cmd;
		for (int i = 0; i < args.length; i++)
			send += " " + args[i];
		send += "\n";

		try {
			out.write(send);
			out.flush();
		} catch (IOException e) {
			System.err.println(e.toString());
		}
	}

	/** Sluit de socketverbinding van deze client. */
	public void disconnect() {
		try {
			in.close();
			out.close();
			sock.close();
		} catch (IOException e) {
			System.err.println(e.toString());
		}
	}

	public boolean isAIControlled() {
		return AI;
	}

	public void doMove(int move) {
		sendCommand("domove", Integer.toString(move));
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof JButton) {
			JButton button = (JButton) e.getSource();
			if (!AI && button.getName().equals("move") && game != null) {
<<<<<<< HEAD
<<<<<<< HEAD
				int move = Integer.valueOf(button.getActionCommand()).intValue();
				if (name.equals(game.getCurrent()).getName()) && (move >= 0 && move < Board.DIM * Board.DIM) && 
						game.getBoard().isValidMove(move, game.getCurrentPlayer().getColor())) {
		        	sendCommand("domove", Integer.toString(move));
=======
=======
>>>>>>> ee6e528334977e4c51046311bd6a91ee973a5b56
				int move = Integer.valueOf(button.getActionCommand())
						.intValue();
				if (name.equals(game.getCurrentPlayer().getName())
						&& (move >= 0 && move < Board.DIM * Board.DIM)
						&& game.getBoard().isValidMove(move,
								game.getCurrentPlayer().getColor())) {
<<<<<<< HEAD
=======
				int move = Integer.valueOf(button.getActionCommand()).intValue();
				if (name.equals(game.getCurrent()).getName()) && (move >= 0 && move < Board.DIM * Board.DIM) && 
				game.getBoard().isValidMove(move, game.getCurrentPlayer().getColor())) {
>>>>>>> ee6e528334977e4c51046311bd6a91ee973a5b56
					sendCommand("domove", Integer.toString(move));
>>>>>>> ui
				}
			} else if (button.getName().equals("reset")) {
				sendCommand("join", playerCount.toString());
			}
		}
	}

}