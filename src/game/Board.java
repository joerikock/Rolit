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
	private Player[] players;

	public Board(Player[] players) {
		this.field = new int[FIELD_WIDTH][FIELD_HEIGHT];
		this.newBall = false;
		this.modified = false;
		changes = new ArrayList<int[]>();
		modifiedBalls = new ArrayList<int[]>();
		validMovesForNextPlayer = new ArrayList<int[]>();
		this.players = players;
		this.currentPlayer = 0;
		reset();
	}

	private int[][] getData() {
		return field;
	}

	public void reset() {
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

	public int currentPlayer() {
		return currentPlayer;
	}

	private void nextPlayer() {
		currentPlayer++;
		if (currentPlayer == players.length) {
			currentPlayer = 0;
		}
	}

	/**
	 * Returns the int value of the color occuping the field at x, y. If the
	 * field is empty or out of bound, -1 is returned
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public int getField(int x, int y) {
		if (boundTest(x, y)) {
			return field[x][y];
		}
		return -1;
	}

	public boolean boundTest(int x, int y) {
		// System.out.println((x >= 0)+", "+( x < FIELD_WIDTH )+", "+ (y >= 0)
		// +", "+ (y < FIELD_HEIGHT));
		return (x >= 0 && x < FIELD_WIDTH && y >= 0 && y < FIELD_HEIGHT);
	}
	/**
	 * Checks whether playing the move x, y changes the board.
	 * @param x
	 * @param y
	 * @return
	 */
	private boolean forcedMove(int x, int y){
		if(this.resultingChangesForMove(x, y, currentPlayer).size()>0){
			System.out.println("FORCED MOVE: "+x+", "+y);
			return true;
		}
		return false;
			
		
		
	}
	public boolean inValidMoveList(int x, int y){
		int[] pos = {x,y};
		return getValidMoveList().contains(pos);
	}
	public ArrayList<int[]> getValidMoveList(){
		if(validMovesForNextPlayer.size()==0){
			System.out.println("ValidMoves empty. Calling ");
			this.validMovesForNextPlayer = this.getValidMoves();
		}
		return this.validMovesForNextPlayer;
	}
	private ArrayList<int[]> getValidMoves() {
		// Fill freeFields with all free Fields of Board b
		System.out.println("getValidMoves() called");
		ArrayList<int[]> freeMoves = new ArrayList<int[]>();
		//Add all fields that have neighbours into freeMoves
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
		System.out.println("freeMoves: "+freeMoves.size());
		
		ArrayList<int[]> validMoves = new ArrayList<int []>();
		//Now check which of these moves would change the board
		for(int i=0; i<freeMoves.size();i++){
			if(this.forcedMove(freeMoves.get(i)[0],freeMoves.get(i)[1])){
				validMoves.add(freeMoves.get(i));
			}
		}
		//If there are no moves that HAVE to be played, return a list of all Fields that have occupied neighbours
		if(validMoves.size()==0){
			return freeMoves;
		}
		//otherwise validMove
		return validMoves;
	}

	/**
	 * Checks whether the the field x, y has neighbours.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean hasNeighbours(int x, int y) {
		if (boundTest(x, y)) {
			for (int[] vec : VECTORS) {
				if (getField(x + vec[0], y + vec[1]) != -1) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean finished() {
		for (int x = 0; x < Board.FIELD_WIDTH; x++) {
			for (int y = 0; y < Board.FIELD_HEIGHT; y++) {
				if (this.getField(x, y) == -1) {
//					System.out.println(x+" ," +y+" : "+getField(x,y));
					return false;
				}
			}
		}
		return true;
	}

	public int getWinner() {
		if (finished()) {
			int[] ballCount = new int[players.length];
			for (int x = 0; x < Board.FIELD_WIDTH; x++) {
				for (int y = 0; y < Board.FIELD_HEIGHT; y++) {
					ballCount[this.getField(x, y)]++;
				}
			}
			int max = 0;
			int currentBest = 0;
			for(int index = 0; index<players.length;index++){
				if(ballCount[index]>max){
					max = ballCount[index];
					currentBest = index;
				}
			}
			return currentBest;
		}
		return -1;
	}

	public boolean validateMove(int x, int y) {
		return (boundTest(x, y) && getField(x,y)==-1);
//		this.inValidMoveList(x, y)
	}

	private void overwriteField(int x, int y, int color) {
		field[x][y] = color;
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

		if (color == currentPlayer && validateMove(x, y)) {

			this.newBall = true;
			this.newBallX = x;
			this.newBallY = y;
			this.newBallColor = color;
			field[x][y] = color;
			System.out.println("WORKED");
			return true;

		}
		return false;
	}

	public boolean modified() {
		return this.modified;
	}

	public ArrayList<int[]> getChanges() {
//		System.out.println("Board.getChanges()");
		return this.modifiedBalls;
	}

	// TODO: Test whether there are any free fields left
	/**
	 * TODO: changes looping through vectors/distance distance/vector (easier to
	 * get nicer animations, because list would be allready sorted
	 */

	/**
	 * Updates the Board according to the Roll-It-rules: If a new Ball is placed
	 * onto the board, and there is an other ball with the same color already on
	 * the board that lays on a straight line relative to the new ball, all
	 * balls between those two are changed to the color of them.
	 * 
	 * The changes on the board are storred in an ArraList that can be fetched
	 * with getChanges. You have to fetch this list before updating the board
	 * again as the list will be reset each loop.
	 */
	public ArrayList<int[]> resultingChangesForMove(int x, int y, int color){
		/**
		 * loop through all possible direction. Advance one step at a time
		 * until a ball of the same color as the new ball is found. save the
		 * index of the vector and the distance between the new ball and the
		 * found ball in changes.
		 * sad
		 * */
		/*
		 * create arraylist for needed changes. changes[i][0] = index of
		 * vector (direction) length changes[i][1] = length
		 */


		ArrayList<int[]> requiredChanges = new ArrayList<int[]>();
		int[] newBallData = new int[3];
		newBallData[0] = x;
		newBallData[1] = y;
		newBallData[2] = color;
//		req.add(newBallData);
		// add new Ball to changes
		System.out.println(1);
		for (int i = 0; i < VECTORS.length; i++) {
			int distance = 1;
			// System.out.println(2+", "+i);
			while (true) {
				int currentX = newBallData[0] + VECTORS[i][0] * distance;
				int currentY = newBallData[1] + VECTORS[i][1] * distance;
				if(currentX==newBallData[0]&&currentY==newBallData[1]){
					System.out.println("ERROR: ["+ VECTORS[i][0]+", " +VECTORS[i][1]+"] : "+distance +" == ["+currentX+", "+currentY+"] -> ["+newBallData[0]+", "+newBallData[1]+"]");
				}
				int currentColor = this.getField(currentX, currentY);
				/**
				 * If the the current field is out of bounds or empty stop
				 * updating and add nothing to changes. (If a the same color
				 * had been found, the search would have already been
				 * canceled)
				 */
				if (currentColor == -1) {
					break;
				}
				/**
				 * If the current field is the same color as the new ball,
				 * add the index of the current vector and distance to
				 * changes. Cancel search for this vector.
				 */
				if (currentColor == this.newBallColor) {
					int[] newChange = { i, distance };
					requiredChanges.add(newChange);
					break;
				}
				distance++;
			}

		}
		System.out.println("NEW BALL CHANGES: "+requiredChanges.size());
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
			System.out.println("NEW BALL DETECTED!");
		
			changes = this.resultingChangesForMove(newBallX, newBallY, newBallColor);
			for(int i=0; i<changes.size();i++){
				System.out.println(changes.get(i)[0]+", "+changes.get(i)[1]);
			}
			int[] newBallData = {newBallX, newBallY, newBallColor};
	
			modifiedBalls.add(newBallData);
			System.out.println("changes filled: " + changes.size());
			// Execute the changes
			if (changes.size() > 0) {
				System.out.println("changing balls");
				for (int i = 0; i < changes.size(); i++) {
					int currentVectorIndex = changes.get(i)[0];
					int currentDistance = changes.get(i)[1];
					System.out.println("[ " + VECTORS[currentVectorIndex][0]
							+ ", " + VECTORS[currentVectorIndex][1] + " ]: "
							+ currentDistance);
					for (int d = 1; d < currentDistance; d++) {
						// overwrite fields
						// System.out.println(d);
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
			System.out.println("Board.update: ModifiedBalls"
					+ modifiedBalls.size());
			this.modified = true;
			this.newBall = false;
			// The next players turn
			this.nextPlayer();
			this.validMovesForNextPlayer = this.getValidMoves();
		}
	}

	public void print() {
		System.out.println("-------------------------");
		for (int x = 0; x < FIELD_WIDTH; x++) {
			for (int y = 0; y < FIELD_HEIGHT; y++) {
				System.out.print("| " + this.getField(x, y)+" |");
			}
			System.out.println();
		}
	}
}
