package network;

import java.util.*;
import rolit.game.*;

/**
 * Party. Klasse die bestaat uit een ArrayList met de clients
 * die samen een Game aan het spelen zijn.
 * @author Max Kerkers & Tijmen Zandbergen
 */
public class Party extends ArrayList<ClientHandler> {
	private static final long serialVersionUID = 6595754933131726796L;

	public void startGame() {
		Game game;
		PlayerList players = new PlayerList();
		players.add(new Player(get(0).getPlayerName(), BColor.RED));
		if (size() == 2) {
			players.add(new Player(get(1).getPlayerName(), BColor.GREEN));
			game = new Game(players.get(0), players.get(1));
		} else {
			players.add(new Player(get(1).getPlayerName(), BColor.YELLOW));
			players.add(new Player(get(2).getPlayerName(), BColor.GREEN));
			if (size() == 3) {
				game = new Game(players.get(0), players.get(1), players.get(2));
			} else {
				players.add(new Player(get(3).getPlayerName(), BColor.BLUE));
				game = new Game(players.get(0), players.get(1), players.get(2), players.get(3));
			}
		}
		for (ClientHandler client: this) {
			client.setParty(this);
			client.setGame(game);
		}
		broadcast("startgame", players.getNames().toArray(new String[players.size()]));
	}
	
	public void reset() {
		for (ClientHandler client: this) {
            client.reset();
        }
		this.clear();
	}
	
	/**
     * Stuurt een bericht via de ClientHandlers in deze ArrayList.
     * @param cmd Commando dat verstuurd wordt
     * @param args De met het commando meegestuurde argumenten
     */
    public void broadcast(String cmd, String[] args) {
    	for (ClientHandler client: this) {
            client.sendCommand(cmd, args);
        }
    }
    
    public void removeHandler(ClientHandler client) {
		if (contains(client))
			remove(client);
		if (size() == 1)
			get(0).setGame(null);
	}
    
}
