package game;

import java.util.ArrayList;

import menuItems.Menu;
import menuItems.MenuManager;
import multiplayer.Client;
import rollitMenus.IngameMenu;
import rollitMenus.LoginMenu;
import rollitMenus.MainMenu;
import rollitMenus.NewGameMenu;
import rollitMenus.OnlineGameMenu;
import menuItems.TextOutputField;

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

	// Constants --------------------------------------------------------

	/**
	 * Creating a constant tools, used for generating random booleans, integers,
	 * doubles and floats.
	 */
//	public static final Tools TOOLS = new Tools();

	// Instance variables -----------------------------------------------

	/*@
	 * private invariant menus != null;
	 */
	/**
	 * Creates a MenuManager.
	 */
	private MenuManager menus;

	/*@
	 * private invariant login != null;
	 */
	/**
	 * Creates a LoginMenu.
	 */
	private LoginMenu login;

	/*@
	 * private invariant mainMenu != null;
	 */
	/**
	 * Creates a main menu.
	 */
	private MainMenu mainMenu;

	/*@
	 * private invariant inGameMenu != null;
	 */
	/**
	 * Creates an in-game menu.
	 */
	private IngameMenu inGameMenu;

	/*@
	 * private invariant newGameMenu != null;
	 */
	/**
	 * Menu for selecting players.
	 */
	private NewGameMenu newGameMenu;
	/*@
	 * private invariant onlineGameMenu != null;
	 */
	/**
	 * Menu for selecting players.
	 */
	private OnlineGameMenu onlineGameMenu;

	/**
	 * Client for playing online. (can be null, if offline)
	 */
	private Client client;
	
	/*@
	 * private invariant boardPainter != null;
	 */
	/**
	 * Creates a BoardGUI object.
	 */
	private BoardGUI boardPainter;

	/*@
	 * private invariant bg != null;
	 */
	/**
	 * Creates an animated background for the login- and the main menu.
	 */
	private AnimatedBackGround bg;

	/*@
	 * private invariant board != null;
	 */
	/**
	 * Creates a board.
	 */
	private Board board;

	/*@
	 * private invariant gameActive == true || gameActive == false;
	 */
	/**
	 * Creates a boolean defining whether the game is currently active.
	 */
	private boolean gameActive;

	/*@
	 * private invariant players != null;
	 */
	/**
	 * Creates an array for the players.
	 */
	private Player[] players;

	/*@
	 * private invariant showHints == true || showHints == false;
	 */
	/**
	 * Boolean deciding whether hints are displayed during the game.
	 */
	private boolean showHints;

	// Constructors --------------------------------------------------------

	/**
	 * Create a new game.
	 */
	public Game() {
		this.menus = new MenuManager();
		login = new LoginMenu(menus);
		mainMenu = new MainMenu(menus);
		inGameMenu = new IngameMenu(menus);
		newGameMenu = new NewGameMenu(menus);
		onlineGameMenu = new OnlineGameMenu(menus);
		menus.addMenu(inGameMenu);
		menus.addMenu(mainMenu);
		menus.addMenu(newGameMenu);
		menus.addMenu(onlineGameMenu);
		menus.addMenu(login);
		boardPainter = new BoardGUI();
		boardPainter.setPosition(300, 0);
		bg = new AnimatedBackGround(3, 200);
		players = new Player[4];
	}

	// Queries -------------------------------------------------------------

	/*@
	 * ensures \result = this.board;
	 */
	/**
	 * Method for retrieving the board.
	 * 
	 * @return the board the current game is using.
	 */
	public Board getBoard() {
		return board;
	}

	/*@
	 * requires mouseDown == true || mouseDown == false;
	 */
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
	public void update(float x, float y, boolean mouseDown, char input) {
		gameActive = false;
		Menu active = menus.getActiveMenu();

		menus.update(x, y, mouseDown, input);
		if (active == inGameMenu) {
			gameActive = true;
			if (active.lastClickedElement() == "Back") {
				System.out.println("Going back to the main menu");
				menus.setActiveMenu(mainMenu);
			}
			this.updateGame(x, y, mouseDown);

		} else {
			bg.update();
		}
		if (active == onlineGameMenu) {
			this.updateOnlineGameMenu(active);
		}
		if (active == mainMenu) {
			if (active.lastClickedElement() == "Exit") {
				System.exit(0);
			}
			if (active.lastClickedElement() == "New Game") {
				menus.setActiveMenu(newGameMenu);
			}
			if (active.lastClickedElement() == "Play Online") {
				menus.setActiveMenu(onlineGameMenu);
			}
			
		}
		if (active == newGameMenu) {
			updateNewGameMenu(active);
			if (active.lastClickedElement() == "Back") {
				menus.setActiveMenu(mainMenu);
			}
		}
		if (active == login) {
			if (active.lastClickedElement() == "Login") {
				System.out.println("Loggin in with: " + login.getUser() + ", "
						+ login.getPassword());
				menus.setActiveMenu(mainMenu);
				try{
					client = new Client(login.getUser(), login.getPassword(),1235, "localHost", board, boardPainter);
				}catch(Exception e){
					System.out.println("Login Failed");
				}
				if (client != null) {
					Thread clientThread = new Thread(client);
					clientThread.start();
				}
			}
		}
	}

	private void updateGame(float x, float y, boolean mouseDown) {
		boardPainter.update(x, y, mouseDown, showHints);
		gameActive = true;
		if (boardPainter.animationDone()) {
			if (!board.finished()) {
				if (board.currentPlayer().hasMove()) {
					board.currentPlayer().makeMove(board);
				}
			}
		}
		board.update();
	}
	private void updateOnlineGameMenu(Menu active) {
		if (active.lastClickedElement() == "Back") {
			menus.setActiveMenu(mainMenu);
		}
		if (active.lastClickedElement() == "Connect") {
			
			client.requestGame(2);
		}
		if(client.inGame()){
			menus.setActiveMenu(inGameMenu);
			board = client.getBoard();
			boardPainter.setBoard(board);
		}
	}
	private void updateInGameMenu(Menu active) {
		TextOutputField field;
		field = (TextOutputField) (active.getElement("redScore"));
//		field.setText();
	}
	private void updateNewGameMenu(Menu active) {
		if (active.lastClickedElement() == "Start") {
			if (active.getSelectedChild("Show hints") == "On") {
				System.out.println("Hints activated");
				showHints = true;
			} else if (active.getSelectedChild("Show hints") == "Off") {
				System.out.println("Hints deactivated");
				showHints = false;
			}
			/**
			 * Fetch players & hints
			 */
			String[] playerColors = {"Red", "Green", "Blue", "Yellow"};
			for (int i = 0; i < 4; i++) {

				if (active.getSelectedChild(playerColors[i]).equals(
						"Human Player")) {

					players[i] = new HumanPlayer("Max", i, boardPainter);
				}
				if (active.getSelectedChild(playerColors[i])
						.equals("Simple AI")) {

					players[i] = new DumbPlayer(i);
				}
				if (active.getSelectedChild(playerColors[i]).equals("Smart AI")) {

					players[i] = new SmartPlayer(i);
				}
				if (active.getSelectedChild(playerColors[i]).equals("No Player")) {
					players[i] = null;
				}
			}
			ArrayList<Player> playerList = new ArrayList<Player>();
			for (int i = 0; i < 4; i++) {
				if (players[i] != null) {
					playerList.add(players[i]);
				}
			}
			if (playerList.size() > 1) {
				board = new Board();
				board.newGame(playerList);
				boardPainter.setBoard(board);
				boardPainter.setUpBalls();
				menus.setActiveMenu(inGameMenu);
			}
		}
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
