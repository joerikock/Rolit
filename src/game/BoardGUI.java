package game;

import java.util.ArrayList;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Class for creating a GUI for the Board of Rolit.
 * 
 * @author Max Messerich en Joeri Kock
 * 
 */

public class BoardGUI {

	// Constants ------------------------------------------------------------

	/**
	 * The size of the ball.
	 */
	public static final float BALL_SIZE = 75;

	// Instance variables -----------------------------------------------------

	/*@
	 * private invariant board != null;
	 */
	/**
	 * The board.
	 */
	private Board board;

	/*@
	 * private invariant balls != null;
	 */
	/**
	 * The balls.
	 */
	private BallRenderer[][] balls;

	/*@
	 * private invariant boardChanges != null;
	 */
	/**
	 * List that keeps up the changes made to the board.
	 */
	private ArrayList<int[]> boardChanges;

	/*@
	 * private invariant choices != null;
	 */
	/**
	 * List that keeps the possible Moves for the current player.
	 */
	private ArrayList<int[]> choices;

	/*@
	 * private invariant offSetX != null && offSetY != null;
	 */
	/**
	 * The x and y coordinates for the balls.
	 */
	private float offSetX, offSetY;

	/*@
	 * private invariant animationInProgress != null && changinsBallInit != null
	 * && newSelectedField != null && choicesInit != null;
	 */
	/**
	 * Booleans used for the animation.
	 */
	private boolean animationInProgress, changinsBallInit, 
	newSelectedField, choicesInit;

	/*@
	 * private invariant newBall != null;
	 */
	/**
	 * Array neccesary for the animation of a new ball.
	 */
	private int[] newBall;

	/*@
	 * private invariant selectedField != null;
	 */
	/**
	 * Array neccesary for which field/ball is currently selected.
	 */
	private int[] selectedField;

	/*@
	 * private invariant w != null && h != null;
	 */
	/**
	 * The number of balls in the x and y direction.
	 */
	private int w, h;

	// Constructors -------------------------------------------------------------

	/**
	 * Creates a new BoardGUI.
	 */
	public BoardGUI() {
		this.w = Board.FIELD_WIDTH;
		this.h = Board.FIELD_HEIGHT;
		this.balls = new BallRenderer[w][h];
		this.selectedField = null;
	}

	/*@
	 * ensures width == this.w && height == this.h;
	 */
	/**
	 * Creates a new BoardGUI using a defined width and height.
	 * 
	 * @param width
	 *            the desired width of the board.
	 * @param height
	 *            the desired height of the board.
	 */
	public BoardGUI(int width, int height) {
		this.w = width;
		this.h = height;
		this.balls = new BallRenderer[w][h];
		init();
	}

	// Queries ------------------------------------------------------------------

	/*@
	 * requires b != null;
	 */
	/**
	 * Sets the board to the initial position.
	 * 
	 * @param b
	 *            the board that needs to be set.
	 */
	public void setBoard(Board b) {
		this.board = b;
		init();
		setUpBalls();
	}

	/*@
	 * ensures \result == (x >= 0 && x < this.w && y >= 0 && y < this.h);
	 */
	/**
	 * Method for determining if a field exists on the board.
	 * 
	 * @param x
	 *            the x-coordinate of the field.
	 * @param y
	 *            the y-coordinate of the field.
	 * @return true if the field exists, false if it does not exist.
	 */
	public boolean isField(int x, int y) {
		return x >= 0 && x < w && y >= 0 && y < h;
	}

	/*@
	 * requires x >= 0 && x < this.w && y >= 0 && y < this.h;
	 */
	/**
	 * Method for setting a field to a new color.
	 * 
	 * @param x
	 *            the x-coordinate of the field.
	 * @param y
	 *            the y-coordinate of the field.
	 * @param r
	 *            float used for the color ID: red.
	 * @param g
	 *            float used for the color ID: green.
	 * @param b
	 *            float used for the color ID: blue.
	 * @param alpha
	 *            variable used for the animation. For further information see
	 *            the class BallRenderer.
	 */
	public void setFieldColor(int x, int y, float r, float g, float b,
			float alpha) {
		float[] color = {r, g, b, alpha};
		balls[x][y].setRenderColor(color);
	}

	/*@
	 * loop_invariant 	int i, i >= 0 && i < this.w;
	 * loop_invariant 	\forall (int j, j >= 0 && j < i;
	 * loop_invariant	int k, k >= 0 && k < this.h;
	 * loop_invariant	\forall (int l, l >= 0 && l < k;
	 * 					this.balls[j][l] != null;
	 */
	/**
	 * Sets up the initial state for the balls.
	 */
	private void init() {
		for (int x = 0; x < this.w; x++) {
			for (int y = 0; y < this.h; y++) {
				this.balls[x][y] = new BallRenderer(x * BALL_SIZE, y
						* BALL_SIZE, BALL_SIZE);
			}
		}
		animationInProgress = false;
	}

	/*@
	 * loop_invariant 	int i, i >= 0 && i < this.w;
	 * loop_invariant 	\forall (int j, j >= 0 && j < i;
	 * loop_invariant	int k, k >= 0 && k < this.h;
	 * loop_invariant	\forall (int l, l >= 0 && l < k;
	 * 					this.balls[j][l] == \old(this.balls[j][l]).reset();
	 */
	/**
	 * Puts the balls rendered in the correct initial position.
	 */
	public void setUpBalls() {
		for (int x = 0; x < this.w; x++) {
			for (int y = 0; y < this.h; y++) {
				balls[x][y].reset();
				if (board.getField(x, y) != -1) {
					balls[x][y].changeColorTo(board.getField(x, y));
				}
			}
		}
	}

	/*@
	 * requires x != null && y != null;
	 * ensures this.offSetX = x && this.offSetY = y;
	 */
	/**
	 * Sets the coordinates of the left-down edge of the board.
	 * 
	 * @param x
	 *            the x-coordinate.
	 * @param y
	 *            the y-coordinate.
	 */
	public void setPosition(float x, float y) {
		this.offSetX = x;
		this.offSetY = y;
	}

	/*@
	 * requires mouseClicked == true || mouseClicked == false;
	 * requires hint == true || hint == false;
	 */
	/**
	 * Updates the Rendered Board when the Board is updated and handles the
	 * interaction of the user like hovering the mouse over a ball and clicking
	 * it. Shows possible moves for the current Player.
	 * 
	 * @param mouseX
	 *            the x-coordinate of the mouse.
	 * @param mouseY
	 *            the y-coordinate of the mouse.
	 * @param mouseClicked
	 *            determines whether the mouse is clicked or not.
	 */
	public void update(float mouseX, float mouseY, boolean mouseClicked, boolean hint) {
		this.newSelectedField = false;
		for (int x = 0; x < Board.FIELD_WIDTH; x++) {
			for (int y = 0; y < Board.FIELD_HEIGHT; y++) {
				balls[x][y].setMouseOver(false);
			}
		}
		/**
		 * Whenever the board has been updated (or no ball has been placed yet)
		 * the list of possible moves for the current player is fetched and the
		 * the possible choices are indicated in rendering.
		 */
		if (hint && !choicesInit) {
			this.choices = board.getValidMoveList();
			for (int i = 0; i < choices.size(); i++) {
				balls[choices.get(i)[0]][choices.get(i)[1]].isChoice(board
						.currentPlayerColor());
			}
			choicesInit = true;

		}
		/**
		 * If the board has been modified, fetch the ArrayList of new modified
		 * fields in boardChanges and tell the new Ball to change its color.
		 * choiceInit is set to false so the suggestions are updated next loop.
		 * 
		 */
		if (board.modified()) {

			boardChanges = new ArrayList<int[]>();
			boardChanges.addAll(board.getChanges());

			newBall = boardChanges.remove(0);

			this.balls[newBall[0]][newBall[1]].changeColorTo(newBall[2]);
			if (hint) {
				for (int i = 0; i < choices.size(); i++) {
					balls[choices.get(i)[0]][choices.get(i)[1]].notChoice();
				}
			}
			animationInProgress = boardChanges.size() > 0;
			this.changinsBallInit = false;
			this.choicesInit = false;
		}
		/**
		 * Only if the last animation has been finished, the user can select a
		 * new field.
		 **/
		if (animationInProgress) {
			if (this.balls[newBall[0]][newBall[1]].animationDone()) {
				if (!changinsBallInit) {
					for (int i = 0; i < boardChanges.size(); i++) {
						this.balls[boardChanges.get(i)[0]][boardChanges.get(i)[1]]
								.changeColorTo(boardChanges.get(i)[2]);
					}
					changinsBallInit = true;
				}
				if (this.balls[boardChanges.get(0)[0]][boardChanges.get(0)[1]]
						.animationDone()) {
					animationInProgress = false;
					choicesInit = false;
				}

			}

		} else {
			int selectedFieldX = (int) ((mouseX - offSetX) / BALL_SIZE);
			int selectedFieldY = (int) ((mouseY - offSetY) / BALL_SIZE);
			if (board.boundTest(selectedFieldX, selectedFieldY)) {
				balls[selectedFieldX][selectedFieldY].setMouseOver(true);
			}
			if (mouseClicked) {
				this.selectedField = new int[2];
				this.selectedField[0] = selectedFieldX;
				this.selectedField[1] = selectedFieldY;
				this.newSelectedField = true;

			}
		}
	}

	/*@
	 * ensures \result == this.newSelectedField && \result != null;
	 */
	/**
	 * Method for checking if the board has a selected field.
	 * 
	 * @return true if the board has a selected field, false if it does not.
	 */
	public boolean hasSelectedField() {
		return this.newSelectedField;
	}

	/*@
	 * ensures \result == this.selectedField && \result != null;
	 */
	/**
	 * Method returning an array containing all the selected fields on the
	 * board.
	 * 
	 * @return array of all the selected fields on the board.
	 */
	public int[] getSelectedField() {
		return this.selectedField;
	}

	/*@
	 * ensures \result != this.animationInProgress;
	 * ensures \result == true || \result == false;
	 */
	/**
	 * Method that checks if the animation of a rectangle is done.
	 * 
	 * @return true if the animation is done, false if is still in progress.
	 */
	public boolean animationDone() {
		return !this.animationInProgress;
	}

	/*
	 * loop_invariant 	int i, i >= 0 && i < this.w;
	 * loop_invariant 	\forall (int j, j >= 0 && j < i;
	 * loop_invariant	int k, k >= 0 && k < this.h;
	 * loop_invariant	\forall (int l, l >= 0 && l < k;
	 * 					this.balls[j][l] != null;
	 */
	/**
	 * Tells the board to render all the fields into a rectangle.
	 * 
	 * @param shapes
	 *            the shapes that need to be drawn.
	 */
	public void draw(ShapeRenderer shapes) {
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				this.balls[x][y].draw(offSetX, offSetY, shapes);
				this.balls[x][y].setMouseOver(false);
			}
		}
	}

	/*
	 * loop_invariant 	int i, i >= 0 && i < this.w;
	 * loop_invariant 	\forall (int j, j >= 0 && j < i;
	 * loop_invariant	int k, k >= 0 && k < this.h;
	 * loop_invariant	\forall (int l, l >= 0 && l < k;
	 * 					this.balls[j][l] != null;
	 */
	/**
	 * Tells the ball to render the overlay texture.
	 * 
	 * @param batch
	 *            SpriteBatch variable neccesary vor the textures.
	 */
	public void batchDraw(SpriteBatch batch) {
		batch.begin();
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				this.balls[x][y].batchDraw(offSetX, offSetY, batch);
			}
		}
		batch.end();
	}
}
