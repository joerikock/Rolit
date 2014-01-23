package game;

import java.util.ArrayList;

/**
 * A test for the AI in Rolit. Creates 2 AI DumbPlayers that play a game of
 * Rolit against each other using a naive strategy.
 * 
 * @author Max Messerich en Joeri Kock.
 */

public class AITest {

	// -- Instance variable -----------------------------------------

	/**
	 * The board.
	 */
	Board b;

	public static void main(String[] args) {
		/**
		 * Create two DumbPlayers (who are using a naive/random strategy)
		 */
		ArrayList<Player> players = new ArrayList<Player>();
		players.add(new DumbPlayer(0));
		players.add(new DumbPlayer(1));
		Board b = new Board();
		b.newGame(players);
		b.print();
		/**
		 * After every move, print the board again.
		 */
		while (!b.finished()) {
				b.currentPlayer().makeMove(b);
				b.update();
				b.print();
		}
		System.out.println("WINNER IS " + b.getWinner());
	}

}
