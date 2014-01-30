package game;

import java.util.ArrayList;

/**
 * Class for a smart player. A SmartPlayer checks all the possible moves it can
 * make, and chooses the one where he can capture the most balls. If a field in
 * the corner of the field is available to make a move on, that field is
 * selected.
 * 
 * @author Max Messerich en Joeri Kock
 */

public class SmartPlayer extends Player {

	/**
	 * Creating the SmartPlayer by using the super-constructor of Player.
	 * 
	 * @param color
	 *            the colour the player needs to be assigned to.
	 */
	public SmartPlayer() {
		super("Smart Player");
	}

	/**
	 * The determineMove method of the SmartPlayer. This method returns a move
	 * which gets him the most fields. If a corner field is available, that
	 * field is returned.
	 */
	public int[] determineMove(Board b) {
		ArrayList<int[]> possibleMoves = b.getValidMoveList();
		for (int[] elem : possibleMoves) {
			System.out.print("(" + elem[0] + ", " + elem[1] + "), ");
		}
		int numberOfFields = -1;
		int fieldCounter = -1;
		int[] result = {-1, -1};
		for (int i = 0; i < possibleMoves.size(); i++) {
			if (possibleMoves.get(i)[0] == 0 && possibleMoves.get(i)[1] == 0) {
				result[0] = 0;
				result[1] = 0;
				return result;
			} else if (possibleMoves.get(i)[0] == 0
					&& possibleMoves.get(i)[1] == 7) {
				result[0] = 0;
				result[1] = 7;
				return result;
			} else if (possibleMoves.get(i)[0] == 7
					&& possibleMoves.get(i)[1] == 0) {
				result[0] = 7;
				result[1] = 0;
				return result;
			} else if (possibleMoves.get(i)[0] == 7
					&& possibleMoves.get(i)[1] == 7) {
				result[0] = 7;
				result[1] = 7;
				return result;
			}
		}
		for (int i = 0; i < possibleMoves.size(); i++) {
			fieldCounter = 0;
			ArrayList<int[]> resultingChanges = b.resultingChangesForMove(
					possibleMoves.get(i)[0], possibleMoves.get(i)[1],
					this.getID());
			for (int j = 0; j < resultingChanges.size(); j++) {
				fieldCounter += resultingChanges.get(j)[1];
			}
			if (fieldCounter > numberOfFields) {
				numberOfFields = fieldCounter;
				result[0] = possibleMoves.get(i)[0];
				result[1] = possibleMoves.get(i)[1];
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
