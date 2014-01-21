package game;

import java.util.ArrayList;

public class RuleEnforcer {
	public static boolean boundTest(int x, int y) {
		return (x >= 0 && x < Board.FIELD_WIDTH && y >= 0 && y < Board.FIELD_HEIGHT);
	}

	public static boolean hasNeighbours(int x, int y, Board b) {
		if (boundTest(x, y)) {
			for (int[] vec : Board.VECTORS) {
				if (b.getField(x + vec[0], y + vec[1]) != -1) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean validateMove(int x, int y, Board b) {
		return (boundTest(x, y) && b.getField(x, y) == -1 && hasNeighbours(x,
				y, b));

	}

	public static ArrayList<int[]> getValidMoves(Board b) {
		// Fill freeFields with all free Fields of Board b
		ArrayList<int[]> validMoves = new ArrayList<int[]>();
		for (int x = 0; x < Board.FIELD_WIDTH; x++) {
			for (int y = 0; y < Board.FIELD_HEIGHT; y++) {
				if (b.getField(x, y) == -1) {
					if (b.hasNeighbours(x, y)) {
						int[] freeField = { x, y };
						validMoves.add(freeField);
					}

				}
			}
		}
		return validMoves;
	}
}
