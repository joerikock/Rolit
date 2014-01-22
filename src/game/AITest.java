package game;

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
		Player p1 = new DumbPlayer(0);
		Player p2 = new DumbPlayer(1);
		Player[] players = { p1, p2 };
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
