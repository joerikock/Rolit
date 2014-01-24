package game;

/**
 * Abstract class for keeping a player in Rolit.
 * 
 * @author Max Messerich en Joeri Kock
 */

public abstract class Player {

	// Instance variables ------------------------------------------------------

	/**
	 * The name of the player.
	 */
	private String name;

	/**
	 * The ID of the player.
	 */
	private int id;

	/**
	 * The board the player is playing on.
	 */
	Board board;

	/**
	 * Create a player.
	 * 
	 * @param name
	 *            the name of the player.
	 * @param id
	 *            the ID of the player.
	 */
	public Player(String name, int id) {
		this.name = name;
		this.id = id;
	}

	public int getID() {
		return id;
	}

	/**
	 * Method
	 * 
	 * @param b
	 */
	public void setBoard(Board b) {
		this.board = b;
	}

	/**
	 * Method determining the move for the player. This method is further
	 * implemented in the extentions of the Player class, e.g. HumanPlayer,
	 * DumbPlayer.
	 * 
	 * @param b
	 *            the board the player is playing on.
	 * @return an array of possible moves for the player.
	 */
	public abstract int[] determineMove(Board b);

	/**
	 * Method for checking if the player has a possible move to make. This
	 * method is further implemented in the extentions of the class Player, e.g.
	 * HumanPlayer, DumbPlayer.
	 * 
	 * @return true if the player has a move, false if he has not.
	 */
	public abstract boolean hasMove();

	/**
	 * Method for making a move.
	 * 
	 * @param b
	 *            the board to make the move on.
	 */
	public void makeMove(Board b) {
		int[] move = determineMove(b);
		b.tryMove(move[0], move[1], id);
	}
}
