package game;

import java.util.ArrayList;

/**
 * Class for a greedy player. A GreedyPlayer checks all the possible moves 
 * it can make, and chooses the one where he can capture the most balls. If
 * a field in the corner of the field is available to make a move on, that
 * field is selected.
 * 
 * @author Max Messerich en Joeri Kock
 */

public class GreedyPlayer extends Player {

	/**
	 * Creating the GreedyPlayer by using the super-constructor of Player.
	 * 
	 * @param color
	 *            the colour the player needs to be assigned to.
	 */
	public GreedyPlayer (int color) {
		super("Greedy Player", color);
	}

	/**
	 * The determineMove method of the GreedyPlayer. This method returns a move
	 * which gets him the most fields. If a corner field is available, that 
	 * field is returned.
	 */
	public int[] determineMove(Board b) {
		ArrayList<int[]> possibleMoves = b.getValidMoveList();
		int numberOfFields = -1;
		int[] result = {-1, -1};
		for (int i = 0; i < possibleMoves.size(); i++) {
			if (possibleMoves.get(i)[0] == 0 && possibleMoves.get(i)[1] == 0) {
				result[0] = 0;
				result[1] = 0;
			} else if (possibleMoves.get(i)[0] == 0 && possibleMoves.get(i)[1] == 7) {
				result[0] = 0;
				result[1] = 7;
			} else if (possibleMoves.get(i)[0] == 7 && possibleMoves.get(i)[1] == 0) {
				result[0] = 7;
				result[1] = 0;
			} else if (possibleMoves.get(i)[0] == 7 && possibleMoves.get(i)[1] == 7) {
				result[0] = 7;
				result[1] = 7;
			} else {
				Board copy = b.deepCopy();
				copy.tryMove(possibleMoves.get(i)[0], 
						possibleMoves.get(i)[1], this.getID());
				if (copy.occupiedFields(this.getID()) > numberOfFields) {
					result[0] = possibleMoves.get(i)[0];
					result[1] = possibleMoves.get(i)[1];
				}
			}
		}
		return result;
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
