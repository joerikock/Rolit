package game;

/**
 * Abstract class for keeping a player in Rolit.
 * 
 * @author Max Messerich en Joeri Kock
 */

public abstract class Player {

	// Instance variables ------------------------------------------------------

	/*
	 * @ private invariant name != null;
	 */
	/**
	 * The name of the player.
	 */
	private String name;

	/*
	 * @ private invariant id != null;
	 */
	/**
	 * The ID of the player.
	 */
	private int id;

	/*
	 * @ private invariant board != null;
	 */
	/**
	 * The board the player is playing on.
	 */
	Board board;

	// Construcors
	// ----------------------------------------------------------------

	/*
	 * @ requires theName != null && theID != null; ensures this.name == theName
	 * && this.id == theID;
	 */
	/**
	 * Create a player.
	 * 
	 * @param name
	 *            the name of the player.
	 * @param id
	 *            the ID of the player.
	 */
	public Player(String theName) {
		this.name = theName;

	}

	// Queries
	// ------------------------------------------------------------------

	/*
	 * @ ensures \result == this.id;
	 */
	/**
	 * Method for setting the id to the instance variable id.
	 * 
	 * @return the id.
	 */
	public void setID(int color) {
		this.id = color;
	}

	public int getID() {
		return id;
	}

	/*
	 * @ ensures \result == this.name;
	 */
	/**
	 * Method for setting the name to the instance variable name.
	 * 
	 * @return the name of the player.
	 */
	public String getName() {
		return name;
	}

	/*
	 * @ requires b != null; ensures this.board == b;
	 */
	/**
	 * Method for setting the board to the instance variable.
	 * 
	 * @param b
	 *            the board.
	 */
	public void setBoard(Board b) {
		this.board = b;
	}

	/*
	 * @ requires b != null; ensures \result != null;
	 */
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

	/*
	 * @ ensures \result == true || \result == false;
	 */
	/**
	 * Method for checking if the player has a possible move to make. This
	 * method is further implemented in the extentions of the class Player, e.g.
	 * HumanPlayer, DumbPlayer.
	 * 
	 * @return true if the player has a move, false if he has not.
	 */
	public abstract boolean hasMove();

	/*
	 * @ requires b != null;
	 */
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
