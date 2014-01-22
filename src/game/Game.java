package game;

import rollitMenus.IngameMenu;
import rollitMenus.LoginMenu;
import rollitMenus.MainMenu;
import MenuItems.MenuManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

/**
 * Class for maintaining the progress of the game Rolit.
 * 
 * @author Max Messerich en Joeri Kock
 * 
 */

public class Game {

	// Instance variables --------------------------------------------------

	/**
	 * Creating a constant tools, used for generating random booleans, integers,
	 * doubles and floats.
	 */
	public static final Tools tools = new Tools();

	/**
	 * Creates a MenuManager.
	 */
	private MenuManager menus;

	/**
	 * Creates a LoginMenu.
	 */
	private LoginMenu login;

	/**
	 * Creates a main menu.
	 */
	private MainMenu mainMenu;

	/**
	 * Creates an in-game menu.
	 */
	private IngameMenu inGameMenu;

	/**
	 * Creates a BoardGUI object.
	 */
	private BoardGUI boardPainter;

	/**
	 * Creates an animated background for the login- and the main menu.
	 */
	private AnimatedBackGround bg;

	/**
	 * Creates a board.
	 */
	private Board board;

	/**
	 * Creates a boolean defining whether the game is currently active.
	 */
	private boolean gameActive;

	/**
	 * Creates 3 players.
	 */
	private Player p1, p2, p3, p4;

	/**
	 * Creates an array for the players.
	 */
	private Player[] players;

	/**
	 * Creates a new game.
	 */
	public Game() {
		this.menus = new MenuManager();
		login = new LoginMenu(menus);
		mainMenu = new MainMenu(menus);
		inGameMenu = new IngameMenu(menus);
		menus.addMenu(mainMenu);
		menus.addMenu(login);
		menus.addMenu(inGameMenu);
		board = new Board();
		boardPainter = new BoardGUI();
		boardPainter.setPosition(300, 0);
		boardPainter.setBoard(board);
		bg = new AnimatedBackGround(3, 200);
//		p1 = new DumbPlayer(0);
//		p2 = new DumbPlayer(1);
		p1 = new HumanPlayer("Dr.Schnappus", 0, boardPainter);
		p2 = new HumanPlayer("ArschGeige200", 1, boardPainter);
		p3 = new HumanPlayer("Pimmel", 2, boardPainter);
		p4 = new HumanPlayer("Joeri", 3, boardPainter);
		players = new Player[4];
		players[0] = p1;
		players[1] = p2;
		players[2] = p3;
		players[3] = p4;
		board.newGame(players);
	}

	/**
	 * Updates the Menus and the game. Passes user input.
	 * 
	 * @param x
	 *            the x-coordinate of the mouse cursor.
	 * @param y
	 *            the y-coordinate of the mouse cursor.
	 * @param mouseDown
	 *            checks whether the left mouse button is pressed or not.
	 * @param input
	 *            last character typed with the keyboard.
	 */
	// TODO: Multithreading???
	public void update(float x, float y, boolean mouseDown, char input) {
		gameActive = false;
		if (menus.getActiveMenu() == inGameMenu) {
			if (menus.getActiveMenu().lastClickedElement() == "Back") {
				System.out.println("Going back to the main menu");
				menus.setActiveMenu(mainMenu);
			}
			this.updateGame(x, y, mouseDown);

		} else {
			bg.update();
		}
		menus.update(x, y, mouseDown, input);
		/**
		 * If a game is not in progress render a dummy field that randomly
		 * changes colors so the menu looks better.
		 */
		if (menus.getActiveMenu() == mainMenu) {
			if (menus.getActiveMenu().lastClickedElement() == "Exit") {
				System.exit(0);
			}
			if (menus.getActiveMenu().lastClickedElement() == "New Game") {
				System.out.println("Starting a new Game");
				menus.setActiveMenu(inGameMenu);
				board.newGame(players);

			}

		}
		if (menus.getActiveMenu() == login) {
			if (menus.getActiveMenu().lastClickedElement() == "Login") {
				System.out.println("Loggin in with: " + login.getUser() + ", "
						+ login.getPassword());
				menus.setActiveMenu(mainMenu);
			}
		}

		// TODO: Add new Menu that opens when "Back" is clicked and give more
		// options
	}

	private void updateGame(float x, float y, boolean mouseDown) {
		boardPainter.update(x, y, mouseDown);

		gameActive = true;
		
		// TODO: Managing game should be done in an extra function
		if (boardPainter.animationDone()) {
//			System.out.println("Animation Done!");
			if (!board.finished()) {
				if (board.currentPlayer().hasMove()) {
					// System.out.
					board.currentPlayer().makeMove(board);
				}
			} else {
				System.out.println("GAME OVER");
			}
		}
		board.update();
	}

	/**
	 * Sets the initial position of the ShapeRenderer.
	 * 
	 * @param shapes
	 *            the shapes to be rendered to their initial state.
	 */
	private void shapeRendererInit(ShapeRenderer shapes) {
		Gdx.graphics.getGL10().glEnable(GL10.GL_BLEND);
		shapes.begin(ShapeType.Filled);
	}

	/**
	 * Method to end the ShapeRenderer.
	 * 
	 * @param shapes
	 *            the shapes to be ended.
	 */
	private void shapeRendererExit(ShapeRenderer shapes) {
		shapes.end();
	}

	/**
	 * Draw the shapes and the batch (for the textures) to the BoardGUI using
	 * the methods provided in the BoardGUI class.
	 * 
	 * @param shapes
	 *            the shapes to be drawn.
	 * @param batch
	 *            the batch used for the textures.
	 */
	public void draw(ShapeRenderer shapes, SpriteBatch batch) {
		if (gameActive) {
			shapeRendererInit(shapes);
			boardPainter.draw(shapes);
			shapeRendererExit(shapes);
			boardPainter.batchDraw(batch);
		} else {
			shapeRendererInit(shapes);
			bg.shapesDraw(shapes);
			shapeRendererExit(shapes);
			bg.batchDraw(batch);
		}
		shapeRendererInit(shapes);
		menus.shapesDraw(shapes);
		shapeRendererExit(shapes);
		menus.batchDraw(batch);
		if (gameActive) {
			boardPainter.batchDraw(batch);
		}
	}
}
