package game;

import java.util.ArrayList;

/**
 * Class for modelling the board for the Rolit game.
 * 
 * @author Max Messerich en Joeri Kock
 */

public class Board {

	// Constants -------------------------------------------------------------
	
	/*@
	 * private invariant VECTORS.length == 8;			
	 */
	/**
	 * The vectors used for checking all directions from a desired field on the
	 * board.
	 */
	public static final int[][] VECTORS = {{1, 0}, {-1, 0}, {0, 1}, 
		{0, -1}, {1, 1}, {-1, 1}, {-1, -1}, {1, -1}};

	/**
	 * The width of the field.
	 */
	public static final int FIELD_WIDTH = 8;

	/**
	 * The height of the field.
	 */
	public static final int FIELD_HEIGHT = 8;
	
	// Instance variables ----------------------------------------------------

	/*@
     *	private invariant fields.length == FIELD_WIDTH * FIELD_HEIGHT;
     *	invariant 	(\forall int i; 0 <= i & i < FIELD_WIDTH;
     *					(\forall int j; 0 <= i & i < FIELD_HEIGHT;
     *   					getField(i, j) == 0 || getField(i,j) == 1 ||
     *   					getField(i, j) == 2 || getField(i,j) == 3 ||
     *   					getField(i,j) == -1));
	 */
	/**
	 * Two-dimensional array for the fields on the board.
	 */
	private int[][] field;

	/*@
	 * private invariant newBall == true || newBall == false;
	 * private invariant modified == true || modified == false;
	 */
	/**
	 * Booleans for telling if a player has a new ball or modified a ball.
	 */
	private boolean newBall, modified;

	/*@
	 * private invariant newBallX >= 0 && newBallX < FIELD_WIDTH;
	 * private invariant newBallY >= 0 && newBallY < FIELD_HEIGHT;
	 * private invariant newBallColor >= 0 && newBallColor < 3;
	 */
	/**
	 * X and Y coordinates and the colour of a new ball.
	 */
	private int newBallX, newBallY, newBallColor;

	/*@
	 * private invariant currentPlayer >= 0 && currentPlayer < 4;
	 */
	/**
	 * Variable indicating the current player.
	 */
	private int currentPlayer;

	/**
	 * The changes represented in a field. A list of X and Y coordinates.
	 */
	private ArrayList<int[]> changes;

	/**
	 * Lists indicating the modified balls and the valid moves a certain player
	 * can make.
	 */
	ArrayList<int[]> modifiedBalls, validMovesForNextPlayer;

	/**
	 * A list containing all players.
	 */
	private ArrayList<Player> players;

	// Constructors -------------------------------------------------------
	
	/*@
	 * ensures 	this.newBall == false && this.modified == false &&
	 * 			getField(3, 3) == 0 && getField(3, 4) == 1 &&
	 * 			getField(4, 4) == 2 && getField(4, 3) == 3;
	 */
	/**
	 * Create the board and reset it to its initial state.
	 */
	public Board() {
		this.field = new int[FIELD_WIDTH][FIELD_HEIGHT];
		this.newBall = false;
		this.modified = false;
		this.reset();

	}

	// Queries ------------------------------------------------------------
	
	/*@
	 * requires playerDate != null;
	 */
	/**
	 * Sets the board up for a new game.
	 * 
	 * @param players
	 *            Array of Player instances that will participate in the game.
	 */
	public void newGame(ArrayList<Player> playerData) {
		this.players = playerData;
	}

	/*@
	 * ensures changes != null && modifiedBalls != null;
	 * ensures	getField(3, 3) == 0 && getField(3, 4) == 1 &&
	 * 			getField(4, 4) == 2 && getField(4, 3) == 3;
	 * 
	 * loop_invariant int i, i >= 0 && i <= FIELD_WIDTH;
	 * loop_invariant \forall (int j, j >= 0 && j < i;
	 * 					getfield(j) == -1;
	 */
	/**
	 * Resets the board to the initial Rolit state.
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
	 * Method for retrieving the color of the current player.
	 * 
	 * @return Returns the colorID(0,1,2,3) of the current player.
	 */
	public int currentPlayerColor() {
		return players.get(currentPlayer).getID();
	}

	/**
	 * Method for retrieving the current player.
	 * 
	 * @return The instance of the current player.
	 */
	public Player currentPlayer() {
		return players.get(currentPlayer);
	}

	/**
	 * Passing the turn to the next player.
	 */
	private void nextPlayer() {
		currentPlayer++;
		if (currentPlayer == players.size()) {
			currentPlayer = 0;
		}
	}

	/**
	 * Method for retrieving the colour of a given field.
	 * 
	 * @param x
	 *            the X-coordinate of the field.
	 * @param y
	 *            the Y-coordinate of the field.
	 * @return Returns the ID of the color occupying the field at x, y. If the
	 *         field is empty or out of bounds, -1 is returned.
	 */
	public int getField(int x, int y) {
		if (boundTest(x, y)) {
			return field[x][y];
		} else {
			return -1;
		}
	}

	/**
	 * Test if a given field is existing on the field or not.
	 * 
	 * @param x
	 *            the X-coordinate of the field.
	 * @param y
	 *            the Y-coordinate of the field.
	 * @return True if the position x, y is not out of bounds.
	 */
	public boolean boundTest(int x, int y) {
		return (x >= 0 && x < FIELD_WIDTH && y >= 0 && y < FIELD_HEIGHT);
	}

	/**
	 * Method for checking whether a given move MUST be made, meaning no other
	 * moves are possible.
	 * 
	 * @param x
	 *            the X-coordinate of the field.
	 * @param y
	 *            the Y-coordinate of the field.
	 * @return Returns true if the move x,y has to be made.
	 */
	private boolean forcedMove(int x, int y) {
		if (this.resultingChangesForMove(x, y, currentPlayerColor()).size() > 0) {
			return true;
		}
		return false;

	}

	/**
	 * Generates a list of {x, y} coordinates containing all the possible valid
	 * moves.
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
	 * Generates a list of {x, y} coordinates containing all the possible valid
	 * moves.
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
	 * Checks whether a given field x, y has neighbours.
	 * 
	 * @param x
	 *            the X-coordinate of the field.
	 * @param y
	 *            the Y-coordinate of the field.
	 * @return true if the field has neighbours.
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
	 * Method for checking if the game has ended.
	 * 
	 * @return true if no additional balls can be added to the board
	 */
	public boolean finished() {
		boolean result = true;
		for (int x = 0; x < Board.FIELD_WIDTH; x++) {
			for (int y = 0; y < Board.FIELD_HEIGHT; y++) {
				if (this.getField(x, y) == -1) {
					result = false;
				}
			}
		}
		return result;
	}

	/**
	 * Method for returning the winner of the game.
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
	 * Method for checking if a certain move is valid.
	 * 
	 * @param x
	 *            the X-coordinate of the move.
	 * @param y
	 *            the Y-coordinate of the move.
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
	 *            the player.
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
	 * Method for checking if the board has been modified since last move.
	 * 
	 * @return True if the board has been modified last loop (=> new Ball
	 *         placed)
	 */
	public boolean modified() {
		return this.modified;
	}

	/**
	 * Method for retrieving a list of the recent changes in the board.
	 * 
	 * @return A list of the most recent changes of the ball. The list contains
	 *         arrays that represent x,y-positions of the a ball and its color.
	 */
	public ArrayList<int[]> getChanges() {
		return this.modifiedBalls;
	}

	/**
	 * Method that at first makes a given move on the board, then checks in all
	 * directions of VECTORS and makes the changes when a ball is "captured".
	 * 
	 * @param x
	 *            the X-coordinate of the move.
	 * @param y
	 *            the Y-coordinate of the move.
	 * @param color
	 *            the colour to change the field into.
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

	/**
	 * Method for updating the field.
	 */
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

	/**
	 * Method for printing the current field on the console.
	 */
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
