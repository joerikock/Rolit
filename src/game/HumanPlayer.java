package game;

/**
 * Class for maintaining a human player in Rolit.
 * 
 * @author Max Messerich en Joeri Kock
 */

public class HumanPlayer extends Player {

	// Instance variables ------------------------------------------------------

	/**
	 * The BoardGUI object.
	 */
	BoardGUI gui;

	/**
	 * Create a human player.
	 * 
	 * @param name
	 *            the name of the player.
	 * @param color
	 *            the colour of the player.
	 * @param gui
	 *            the BoardGUI the player uses.
	 */
	public HumanPlayer(String name, int color, BoardGUI gui) {
		super(name, color);
		this.gui = gui;
	}

	/**
	 * Method for checking whether the player has a possible move to make.
	 * 
	 * @return true if the player has a selected field, false if he has not.
	 */
	public boolean hasMove() {
		return gui.hasSelectedField();
	}

	/**
	 * Method for determining the move for the human player.
	 * 
	 * @return an array with all possible moves for the player.
	 */
	public int[] determineMove(Board b) {
		if (gui.hasSelectedField()) {
			return gui.getSelectedField();
		}
		return null;
	}
}
