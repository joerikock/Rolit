package game;

import java.util.ArrayList;

/**
 * Class for creating a DumbPlayer. This is a AI using the most naive strategy
 * possible, by choosing random fields to be selected.
 * 
 * @author Max Messerich en Joeri Kock
 */

public class DumbPlayer extends Player {

	/**
	 * Creating the DumbPlayer by using the super-constructor of Player.
	 * 
	 * @param color
	 *            the colour the player needs to be assigned to.
	 */
	public DumbPlayer(int color) {
		super("Dumb Player", color);
	}

	/**
	 * Method returning an array a possible move vor the DumbPlayer. The move
	 * chosen by this player is completely random, as long as it is a legal move
	 * to make.
	 */
	@Override
	public int[] determineMove(Board b) {
		ArrayList<int[]> possibleMoves = b.getValidMoveList();
		int choice = Tools.randomInt(0, possibleMoves.size() - 1);
		if (choice == possibleMoves.size()) {
			return possibleMoves.get(choice - 1);
		}
		return possibleMoves.get(choice);
	}

	/**
	 * Method for checking if the player has a possible move to make. This is
	 * always true, for this is a player choosing random fields.
	 * 
	 * @return true if the player has a possible move to make. This is always
	 *         the case.
	 */
	public boolean hasMove() {
		return true;
	}
}
