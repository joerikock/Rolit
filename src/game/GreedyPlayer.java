package game;

import java.util.ArrayList;

/**
 * Class for a greedy player. A GreedyPlayer checks all the possible moves 
 * it can make, and chooses the one where he can capture the most balls. If
 * a field in the corner of the field is available to make a move on, that
 * field is selected.
 * 
 * @author Max Messerich en Joeri Kock
 *
 */

public class GreedyPlayer extends Player {

	/**
	 * Creating the GreedyPlayer by using the super-constructor of Player.
	 * 
	 * @param color
	 *            the colour the player needs to be assigned to.
	 */
	public GreedyPlayer (int color) {
		super("Greedy player", color);
	}

	/**
	 * Method returning an array of all possible moves vor the DumbPlayer. The
	 * moves chosen by this player are completely random, as long as it is a
	 * legal move to make.
	 */
	public int[] determineMove(Board b) {
		ArrayList<int[]> possibleMoves = b.getValidMoveList();
		for (int i = 0; i < possibleMoves.size(); i++) {
			Board copy;
		}
		int choice = Tools.randomInt(0, possibleMoves.size() - 1);
		if (choice == possibleMoves.size()) {
			return possibleMoves.get(choice - 1);
		}
		return possibleMoves.get(choice);
	}

	/**
	 * Method for checking if the player has a possible move to make. This is
	 * always true.
	 * 
	 * @return true if the player has a possible move to make. This is always
	 *         the case.
	 */
	public boolean hasMove() {
		return true;
	}
	
	
}
