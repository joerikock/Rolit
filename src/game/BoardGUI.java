package game;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class BoardGUI {
	public static float BALL_SIZE = 600 / 8;

	private Board board;
	private BallRenderer[][] balls;
	private ArrayList<int[]> boardChanges;
	private float x, y;
	private boolean animationInProgress, newSelectedField;
	int[] newBall,selectedField;
	int w, h;
	public BoardGUI() {
		this.w = Board.FIELD_WIDTH;
		this.h = Board.FIELD_HEIGHT;
		this.balls = new BallRenderer[w][h];
		this.selectedField = null;

	}
	public void setBoard(Board b){
		this.board = b;
		
		init();
		setUpBalls();
	}
	public BoardGUI(int w, int h) {
		this.w = w;
		this.h = h;
		this.balls = new BallRenderer[w][h];
		init();

	}
	public boolean isField(int x, int y){
		return (x>=0&&x<w&&y>=0&&y<h);
	}
	public void setFieldColor(int x, int y, float r, float g, float b,
			float alpha) {
		float[] color = { r, g, b, alpha };

		balls[x][y].setRenderColor(color);
	}
/**
 * Sets up the ball renderes
 */
	private void init(){
		
		for (int x = 0; x < this.w; x++) {
			for (int y = 0; y < this.h; y++) {
				this.balls[x][y] = new BallRenderer(x * BALL_SIZE, y
						* BALL_SIZE, BALL_SIZE);

			}
		}
		this.x = 0;
		this.y = 0;
		animationInProgress = false;
	}

	public void setUpBalls() {
		for (int x = 0; x < this.w; x++) {
			for (int y = 0; y < this.h; y++) {
				if (board.getField(x, y) != -1) {
					balls[x][y].changeColorTo(board.getField(x, y));
				}
			}
		}
	}

	// Sets the coordinates of the left-down edge of the board
	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
		System.out.println("BoardGUI OFFSET: " + this.x + ", " + this.y);
	}

	/**
	 * Updates the Rendered Board, when the Board itself is updated and handles
	 * the interaction of the user like hovering the mouse over a ball and
	 * clicking it.
	 * 
	 * @param mouseX
	 * @param mouseY
	 * @param mouseClicked
	 */
	public void update(float mouseX, float mouseY, boolean mouseClicked) {
		// Set all mouseOver to false for all Balls, reset selectedField
//		System.out.println("------------BOARDGUI update start");
		this.newSelectedField = false;
		for (int x = 0; x < Board.FIELD_WIDTH; x++) {
			for (int y = 0; y < Board.FIELD_HEIGHT; y++) {
				balls[x][y].setMouseOver(false);
			}
		}

		/**
		 * If the board has been modified, fetch the ArrayList of new modified
		 * fields in boardChanges and tell the new Ball to change its color
		 * 
		 */

		if (board.modified()) {
			
			boardChanges = new ArrayList<int[]>();
			boardChanges.addAll(board.getChanges());

			newBall = boardChanges.remove(0);

			this.balls[newBall[0]][newBall[1]].changeColorTo(newBall[2]);

			animationInProgress = true;

		}

		// When the animation is not done yet, animated
		// first ball has been fully blended in, the rest of the
		// changes is executed
		if (animationInProgress) {

			if (this.balls[newBall[0]][newBall[1]].animationDone()) {
				System.out.println("SET :" + boardChanges.size());
				for (int i = 0; i < boardChanges.size(); i++) {
					this.balls[boardChanges.get(i)[0]][boardChanges.get(i)[1]]
							.changeColorTo(boardChanges.get(i)[2]);
				}
				animationInProgress = false;
			}

		} else {
			// Deal with user input
			int selectedFieldX = (int) ((mouseX - x) / BALL_SIZE);
			int selectedFieldY = (int) ((mouseY - y) / BALL_SIZE);
			if (board.boundTest(selectedFieldX, selectedFieldY)) {
				balls[selectedFieldX][selectedFieldY].setMouseOver(true);
			}
			if (mouseClicked) {
				// System.out.println("MOUSE: " + mouseX + ", " + mouseY);
				// System.out.println("Testing Field at " + selectedFieldX +
				// ", "
				// + selectedFieldY);
				this.selectedField = new int[2];
				this.selectedField[0] = selectedFieldX;
				this.selectedField[1] = selectedFieldY;
				this.newSelectedField = true;
				
			}
		}
//		System.out.println("------------BOARDGUI update DONE");
	}
	public boolean hasSelectedField(){
		return this.newSelectedField;
	}
	public int[] getSelectedField(){
		return this.selectedField;
	}
	public boolean animationDone(){
		return !this.animationInProgress;
	}
	// TODO: MouseOver reset should not be reset in rendering!
	public void draw(ShapeRenderer shapes) {
		// shapes.begin(ShapeType.Filled);
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				this.balls[x][y].draw(this.x, this.y, shapes);
				this.balls[x][y].setMouseOver(false);
			}
		}
		// shapes.end();
	}

	public void batchDraw(SpriteBatch batch) {
		batch.begin();
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				this.balls[x][y].batchDraw(this.x, this.y, batch);
			}
		}
		batch.end();
	}

}
