package network;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server extends Thread implements MessageUI {
	private int port;
	private List<ClientHandler> threads;

	/** Construeert een nieuw Server-object. */
	public Server(int port) {
		this.port = port;
		this.threads = new ArrayList<ClientHandler>();
	}

	/**
	 * Luistert op de port van deze Server of er Clients zijn die een verbinding
	 * willen maken. Voor elke nieuwe socketverbinding wordt een nieuw
	 * ClientHandler thread opgestart die de verdere communicatie met de Client
	 * afhandelt.
	 */
	public void run() {
		boolean running = true;
		ServerSocket server = null;
		try {
			server = new ServerSocket(port);
			addMessage("[Server is gestart met poort " + port + "]");
			while (running) {
				Socket socket = server.accept();
				ClientHandler client = new ClientHandler(this, socket);
				addHandler(client);
				client.start();
			}
		} catch (IOException e) {
			running = false;
			addMessage("[Server is gestopt]");
		}
	}

	/**
	 * 
	 * @require (2 <= partySize <= 4)
	 * @param partySize
	 * @param lastClient
	 * @return true als het gelukt is een Game te beginnen.
	 */
	public boolean tryStartGame(int partySize, ClientHandler lastClient) {
		Party candidates = new Party();
		candidates.add(lastClient);
		for (ClientHandler client : threads) {
			if ((client.getPartySize() == partySize)
					&& (client.getGame() == null)
					&& !(candidates.contains(client)))
				candidates.add(client);
			if (candidates.size() == partySize)
				break;
		}
		if (candidates.size() == partySize) {
			candidates.startGame();
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	public boolean nameAvailable(String name) {
		boolean available = true;
		for (ClientHandler client : threads) {
			if (client.isConnected() && client.getPlayerName().equals(name)) {
				available = false;
				break;
			}
		}
		return available;
	}

	public void addMessage(String msg) {
		System.out.println(msg);
	}

	/**
	 * Voegt een ClientHandler aan de collectie van ClientHandlers toe.
	 * 
	 * @param handler
	 *            ClientHandler die wordt toegevoegd
	 */
	public void addHandler(ClientHandler handler) {
		threads.add(handler);
	}

	/**
	 * Verwijdert een ClientHandler uit de collectie van ClientHandlers.
	 * 
	 * @param handler
	 *            ClientHandler die verwijderd wordt
	 */
	public void removeHandler(ClientHandler handler) {
		if (threads.contains(handler))
			threads.remove(handler);
	}
}
