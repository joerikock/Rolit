package game;

import java.util.ArrayList;

public class Board {
	public static final int[][] VECTORS = { { 1, 0 }, { -1, 0 }, { 0, 1 },
			{ 0, -1 }, { 1, 1 }, { -1, 1 }, { -1, -1 }, { 1, -1 } };
	public static final int FIELD_WIDTH = 8;
	public static final int FIELD_HEIGHT = 8;
	private int[][] field;
	private boolean newBall, modified;
	private int newBallX, newBallY, newBallColor, playerCount, currentPlayer;
	private ArrayList<int[]> changes;
	ArrayList<int[]> modifiedBalls, validMovesForNextPlayer;
	private ArrayList<Player> players;

	public Board() {
		this.field = new int[FIELD_WIDTH][FIELD_HEIGHT];
		this.newBall = false;
		this.modified = false;
		this.reset();
		
	}
	/**
	 * Sets the board up for a new game. 
	 * @param players Array of Player instances that will participate in the round.
	 */
	public void newGame(ArrayList<Player> playerData){
		this.players = playerData;
	}
	/**
	 * Resets the board to the standart rollit board.
	 */
	private void reset() {
		changes = new ArrayList<int[]>();
		modifiedBalls = new ArrayList<int[]>();
		this.currentPlayer = 0;
		for (int w = 0; w < FIELD_WIDTH; w++) {
			for (int h = 0; h < FIELD_HEIGHT; h++) {
				this.field[w][h] = -1;
			}
		}
		field[3][3] = 0;
		field[3][4] = 1;
		field[4][4] = 2;
		field[4][3] = 3;
	}
	
	/**
	 * 
	 * @return Returns the colorID(0,1,2,3) of the current player.
	 */
	public int currentPlayerColor() {
		return players.get(currentPlayer).getID();
	}

	/**
	 * 
	 * @return The instance of the current player.
	 */
	public Player currentPlayer() {
		return players.get(currentPlayer);
	}

	/**
	 * Next Players turn.
	 */
	private void nextPlayer() {
		currentPlayer++;
		if (currentPlayer == players.size()) {
			currentPlayer = 0;
		}
	}

	/**
	 * 
	 * 
	 * @param x
	 * @param y
	 * @return Returns the ID of the color occupying the field at x, y. If the
	 *         field is empty or out of bounds, -1 is returned.
	 */
	public int getField(int x, int y) {
		if (boundTest(x, y)) {
			return field[x][y];
		}
		return -1;
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @return True if the position x, y is not out of bounds.
	 */
	public boolean boundTest(int x, int y) {
		return (x >= 0 && x < FIELD_WIDTH && y >= 0 && y < FIELD_HEIGHT);
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @return Returns true if the move x,y has to be made.
	 */
	private boolean forcedMove(int x, int y) {
		if (this.resultingChangesForMove(x, y, currentPlayerColor()).size() > 0) {
			return true;
		}
		return false;

	}

	/**
	 * 
	 * @return A list of all possible moves of the current player.
	 */
	public ArrayList<int[]> getValidMoveList() {
		if (validMovesForNextPlayer == null) {
			this.validMovesForNextPlayer = this.getValidMoves();
		}
		return validMovesForNextPlayer;
	}

	/**
	 * 
	 * @return Creates a list of legal moves for the current player. Is used
	 *         when the game just started or a turn has been made.
	 */
	private ArrayList<int[]> getValidMoves() {
		// Fill freeFields with all free Fields of Board b

		ArrayList<int[]> freeMoves = new ArrayList<int[]>();
		// Add all fields that have neighbours into freeMoves
		for (int x = 0; x < Board.FIELD_WIDTH; x++) {
			for (int y = 0; y < Board.FIELD_HEIGHT; y++) {
				if (getField(x, y) == -1) {
					if (hasNeighbours(x, y)) {
						int[] freeField = { x, y };
						freeMoves.add(freeField);
					}

				}
			}
		}

		ArrayList<int[]> validMoves = new ArrayList<int[]>();
		// Now check which of these moves would change the board
		for (int i = 0; i < freeMoves.size(); i++) {
			if (this.forcedMove(freeMoves.get(i)[0], freeMoves.get(i)[1])) {
				validMoves.add(freeMoves.get(i));
			}
		}

		// If there are no moves that HAVE to be played, return a list of all
		// Fields that have occupied neighbours
		if (validMoves.size() == 0) {
			return freeMoves;
		}
		// otherwise validMove
		return validMoves;
	}

	/**
	 * Checks whether the the field x, y has neighbours.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	private boolean hasNeighbours(int x, int y) {
		if (boundTest(x, y)) {
			for (int[] vec : VECTORS) {
				if (getField(x + vec[0], y + vec[1]) != -1) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 
	 * @return true if no additional balls can be added to the board
	 */
	public boolean finished() {
		// TODO: Not correct. Check whether balls can be played instead for free
		// spaces.
		for (int x = 0; x < Board.FIELD_WIDTH; x++) {
			for (int y = 0; y < Board.FIELD_HEIGHT; y++) {
				if (this.getField(x, y) == -1) {
					
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 
	 * @return Returns the player instance that won the game.
	 */
	public Player getWinner() {
		if (finished()) {
			int[] ballCount = new int[players.size()];
			for (int x = 0; x < Board.FIELD_WIDTH; x++) {
				for (int y = 0; y < Board.FIELD_HEIGHT; y++) {
					ballCount[this.getField(x, y)]++;
				}
			}
			int max = 0;
			int currentBest = 0;
			for (int index = 0; index < players.size(); index++) {
				if (ballCount[index] > max) {
					max = ballCount[index];
					currentBest = index;
				}
			}
			return players.get(currentBest);
		}
		return null;
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @return Returns true if the move x,y is valid.
	 */
	private boolean validateMove(int x, int y) {
		int[] pos = { x, y };
		ArrayList<int[]> temp = getValidMoveList();
		for (int i = 0; i < temp.size(); i++) {
		
			if (temp.get(i)[0] == pos[0] && temp.get(i)[1] == pos[1]) {
				
				return true;
			}

		}
		return false;
	}

	/**
	 * Tries to place a new Ball onto the Board. Places the a ball with the
	 * specified color at the desired position and returns true. If the
	 * operation fails, it returns false.
	 * 
	 * @param x
	 * @param y
	 * @param color
	 * @return true if ball has been placed. false if not possible.
	 */
	// TODO: Check if color code is correct.
	public boolean tryMove(int x, int y, int color) {
		// sets field[x][y] = color if possible and sets newBall to true to
		// force update

		if (validateMove(x, y)) {

			this.newBall = true;
			this.newBallX = x;
			this.newBallY = y;
			this.newBallColor = color;
			field[x][y] = color;

			return true;

		}
		return false;
	}
	
	/**
	 * Count all the fields a given player has.
	 * 
	 * @param id
	 * 			the player.
	 * @return the number of fields the player has.
	 */
	public int occupiedFields(int id) {
    	int result = 0;
    	for (int i = 0; i < FIELD_WIDTH; i++) {
    		for (int j = 0; j < FIELD_HEIGHT; j++) {
    			if (field[i][j] == id) {
    				result += 1;
    			}
    		}
		}
    	return result;
    }

	/**
	 * 
	 * @return True if the board has been modified last loop (=> new Ball
	 *         placed)
	 */
	public boolean modified() {
		return this.modified;
	}

	/**
	 * 
	 * @return A list of the most recent changes of the ball. The list contains
	 *         arrays that represent x,y-positions of the a ball and its color.
	 */
	public ArrayList<int[]> getChanges() {
		return this.modifiedBalls;
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @param color
	 * @return A list of changes that have to be executed, if a ball is placed
	 *         on x,y with the color "color".
	 */
	public ArrayList<int[]> resultingChangesForMove(int x, int y, int color) {
		/**
		 * loop through all possible direction. Advance one step at a time until
		 * a ball of the same color as the new ball is found. save the index of
		 * the vector and the distance between the new ball and the found ball
		 * in changes. sad
		 * */
		/*
		 * create arraylist for needed changes. changes[i][0] = index of vector
		 * (direction) length changes[i][1] = length
		 */

		ArrayList<int[]> requiredChanges = new ArrayList<int[]>();
		int[] newBallData = new int[3];
		newBallData[0] = x;
		newBallData[1] = y;
		newBallData[2] = color;
		// req.add(newBallData);
		// add new Ball to changes
		for (int i = 0; i < VECTORS.length; i++) {
			int distance = 1;
			while (true) {
				int currentX = newBallData[0] + VECTORS[i][0] * distance;
				int currentY = newBallData[1] + VECTORS[i][1] * distance;

				int currentColor = this.getField(currentX, currentY);
				/**
				 * If the the current field is out of bounds or empty stop
				 * updating and add nothing to changes. (If a the same color had
				 * been found, the search would have already been canceled)
				 */
				if (currentColor == -1) {
					break;
				}
				/**
				 * If the current field is the same color as the new ball, add
				 * the index of the current vector and distance to changes.
				 * Cancel search for this vector.
				 */
				if (currentColor == color) {
					if (distance == 1) {
						break;
					}
					int[] newChange = { i, distance };
					requiredChanges.add(newChange);
					break;
				}
				distance++;
			}
		}
		return requiredChanges;
	}

	public void update() {
		/**
		 * If the board has been updated last loop, clear the changes list and
		 * set modified to false. -> You have to fetch the changes before
		 * calling update again!!! (Same as in InputHandler)
		 */
		this.modified = false;
		if (modifiedBalls.size() > 0) {
			// this.modified = false;
			modifiedBalls.clear();
			changes.clear();
		}
		if (newBall) {


			changes = this.resultingChangesForMove(newBallX, newBallY,
					newBallColor);
			for (int i = 0; i < changes.size(); i++) {
				System.out
						.println(changes.get(i)[0] + ", " + changes.get(i)[1]);
			}
			int[] newBallData = { newBallX, newBallY, newBallColor };

			modifiedBalls.add(newBallData);
		
			// Execute the changes
			if (changes.size() > 0) {
				for (int i = 0; i < changes.size(); i++) {
					int currentVectorIndex = changes.get(i)[0];
					int currentDistance = changes.get(i)[1];
					for (int d = 1; d < currentDistance; d++) {
						// overwrite fields

						field[this.newBallX + VECTORS[currentVectorIndex][0]
								* d][this.newBallY
								+ VECTORS[currentVectorIndex][1] * d] = this.newBallColor;
						int[] s = new int[3];
						s[0] = this.newBallX + VECTORS[currentVectorIndex][0]
								* d;
						s[1] = this.newBallY + VECTORS[currentVectorIndex][1]
								* d;
						s[2] = newBallColor;
						modifiedBalls.add(s);
					}
				}
			}
			this.modified = true;
			this.newBall = false;
			// The next players turn
			this.nextPlayer();
			validMovesForNextPlayer = null;
			this.validMovesForNextPlayer = this.getValidMoveList();
		}
	}

	public void print() {
		System.out.println("-------------------------");
		for (int x = 0; x < FIELD_WIDTH; x++) {
			for (int y = 0; y < FIELD_HEIGHT; y++) {
				System.out.print("| " + this.getField(x, y) + " |");
			}
			System.out.println();
		}
	}
}
