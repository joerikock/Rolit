package network;

import game.Game;

import java.io.*;
import java.net.*;
import java.util.*;

public class ClientHandler extends Thread {
	private Game game;
	private String name;
	private Integer partySize;
	private Party party;

	private Server server;
	private BufferedReader in;
	private BufferedWriter out;

	/**
	 * Construeert een ClientHandler object. Initialiseert de beide Data
	 * streams.
	 * 
	 * @require server != null && sock != null
	 */
	public ClientHandler(Server server, Socket sock) throws IOException {
		this.server = server;
		this.in = new BufferedReader(new InputStreamReader(
				sock.getInputStream()));
		this.out = new BufferedWriter(new OutputStreamWriter(
				sock.getOutputStream()));
		this.name = null;
		this.partySize = 0;
		this.party = null;
		this.game = null;
	}

	public void run() {
		boolean running = true;
		try {
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

	/**
	 * Geeft de naam van de speler behorende bij deze client terug
	 */
	public String getPlayerName() {
		return name;
	}

	/**
	 * Geeft als er nog geen spel gestart is het gewenste aantal spelers terug
	 * en anders het aantal spelers die meedoen aan dit spel
	 */
	public Integer getPartySize() {
		return partySize;
	}

	/**
	 * @return true als de client verbonden is
	 */
	public boolean isConnected() {
		return (name != null);
	}

	/**
	 * @return true als deze client aan een spel meedoet
	 */
	public boolean isInGame() {
		return (game != null);
	}

	/**
	 * Stel het spel in waar deze client aan mee gaat doen.
	 * 
	 * @param game
	 *            spel waar deze client aan mee gaat doen
	 */
	public void setGame(Game game) {
		this.game = game;
	}

	/**
	 * Geeft het spel waar deze client aan meedoet terug
	 */
	public Game getGame() {
		return game;
	}

	/**
	 * Stel de groep van clients in waarmee deze client samen een Game gaat
	 * spelen
	 * 
	 * @param party
	 *            lijst van clients voor hetzelfde spel
	 */
	public void setParty(Party party) {
		this.party = party;
	}

	/**
	 * Geeft de groep spelers terug die hetzelfde spel spelen
	 */
	public Party getParty() {
		return party;
	}

	/**
	 * Leest commando's uit de meegegeven input en voert deze uit.
	 * 
	 * @param lineIn
	 *            regel van de invoer
	 */
	private void readCommand(String lineIn) {
		try {
			String[] splitLine = lineIn.split("\\s");
			String command = splitLine[0].toLowerCase();
			List<String> args = Arrays.asList(splitLine).subList(1,
					splitLine.length);

			if (command.equals("connect") && !isConnected()) {
				connect(args.get(0));
			} else if (command.equals("join") && isConnected() && !isInGame()) {
				joinGame(args.get(0));
			} else if (command.equals("domove") && isConnected() && isInGame()) {
				makeMove(args.get(0));
			} else if (command.equals("chat")) {
				chat(args.get(0));
			}
		} catch (IndexOutOfBoundsException e) {
			server.addMessage("[*Tekort aan argumenten genegeerd*]");
		} catch (Exception e) {
			server.addMessage("[!*Oeps, er ging iets fout: "
					+ e.getMessage().toString() + "*]");
		}
	}

	/**
	 * Stuurt een commando naar de Client.
	 * 
	 * @param cmd
	 *            Commando dat verstuurd wordt
	 * @param arg
	 *            Het met het commando meegestuurde argument
	 */
	public void sendCommand(String cmd, String arg) {
		String[] args = { arg };
		sendCommand(cmd, args);
	}

	/**
	 * Stuurt een commando naar de Client.
	 * 
	 * @param cmd
	 *            Commando dat verstuurd wordt
	 * @param args
	 *            De met het commando meegestuurde argumenten
	 */
	public void sendCommand(String cmd, String[] args) {
		String send = cmd.toLowerCase();
		for (int i = 0; i < args.length; i++)
			send += " " + args[i];
		send += "\n";

		try {
			out.write(send);
			out.flush();
		} catch (IOException e) {
			disconnect();
		}
	}

	public void connect(String arg0) {
		String name = arg0;
		while (!server.nameAvailable(name))
			name += (int) (Math.random() * 10);
		this.name = name;
		server.addMessage("[" + name + " heeft verbinding gemaakt]");
		sendCommand("ackconnect", name);
	}

	public void joinGame(String arg0) {
		try {
			partySize = Integer.parseInt(arg0);
			if (partySize > 1 && partySize <= 4) {
				server.addMessage("[" + name + " wil meedoen aan een spel met "
						+ partySize + " spelers]");
				if (server.tryStartGame(partySize, this)) {
<<<<<<< HEAD
					String[] turnArgs = { game.getCurrentPlayer().getName() };
=======
					String[] turnArgs = {game.getBoard().currentPlayer().getName()};
>>>>>>> 2458e483fbfe7d3a621c37dc5005adf81ff2a1f9
					server.addMessage("[" + turnArgs[0] + " is aan de beurt]");
					party.broadcast("turn", turnArgs);
				}
			} else
				partySize = 0;
		} catch (NumberFormatException e) {
			partySize = 0;
			server.addMessage("[*Verkeerd aantal spelers genegeerd*]");
		}
	}

	public void makeMove(String arg0) {
		Integer move = Integer.parseInt(arg0);
		if (game.getBoard().isTile(move)
				&& game.getCurrentPlayer().getName().equals(name)
				&& game.getBoard().isValidMove(move,
						game.getPlayers().getByName(name).getColor())) {
		if (game.getBoard().isTile(move) &&
				game.getBoard().currentPlayer().getName().equals(name) &&
				game.getBoard().isValidMove(move, game.getPlayers().getByName(name).getColor())) {
			game.makeMove(move);
			String[] moveArgs = { name, move.toString() };
			server.addMessage("[" + name + " doet zet " + moveArgs[1] + "]");
			party.broadcast("movedone", moveArgs);
			if (game.getBoard().isFull()) {
				String result = "[Het spel is afgelopen met de volgende stand:]\n";
				String[] punten = new String[partySize];
				for (int i = 0; i < partySize; i++) {
					punten[i] = new Integer(game.getBoard().countTiles(
							game.getPlayers().get(i).getColor())).toString();
					result += game.getPlayers().get(i).getName() + ": "
							+ punten[i] + "\n";
				}
				server.addMessage(result);
				party.broadcast("endgame", punten);
				party.reset();
			} else {
				String[] turnArgs = { game.getCurrentPlayer().getName() };
				String[] turnArgs = {game.getBoard().currentPlayer().getName()};
				server.addMessage("[" + turnArgs[0] + " is aan de beurt]");
				party.broadcast("turn", turnArgs);
			}
		} else {
			game.kickPlayer(name);
			party.removeHandler(this);

			String[] kickArgs = { name };
			party.broadcast("kick", kickArgs);
		}
	}

	public void chat(String arg0) {
		String[] chatArgs = { name, arg0 };
		party.broadcast("message", chatArgs);
	}

	public void reset() {
		game = null;
		party = null;
		partySize = 0;
	}

	public void disconnect() {
		server.removeHandler(this);
		if (party != null) {
			party.removeHandler(this);
			if (isInGame()) {
				game.kickPlayer(name);
				String[] kickArgs = { name };
				party.broadcast("kick", kickArgs);
			}
		}
	}
}
