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

public class Game {
	public static final Tools tools = new Tools();
	private MenuManager menus;
	LoginMenu login;
	MainMenu mainMenu;
	IngameMenu inGameMenu;
	BoardGUI boardPainter;
	AnimatedBackGround bg;
	Board board;
	boolean gameActive;
	Player p1, p2, p3;
//	HumanPlayer p2;
	Player[] players;
	/**
	 * Runs the all menus and the game
	 */
	public Game() {
		this.menus = new MenuManager();
		login = new LoginMenu(menus);
		mainMenu = new MainMenu(menus);
		inGameMenu = new IngameMenu(menus);
		menus.addMenu(inGameMenu);
		menus.addMenu(mainMenu);
		menus.addMenu(login);
	
		boardPainter = new BoardGUI();
		boardPainter.setPosition(200, 0);
		bg = new AnimatedBackGround(3,200);
//		p1 = new DumbPlayer(0);
	
		p1 = new HumanPlayer("Dr.Schnappus",0, boardPainter);
		p2 = new HumanPlayer("ArschGeige200", 1, boardPainter);
		p3 = new HumanPlayer("Pimmel", 2, boardPainter);
		players = new Player[3];
		players[0] = p1;
		players[1] = p2;
	}
	//Such awesome. much programming. great working. WOW
	/**
	 * Updates the Menus and the game. Feeding user input
	 * @param x
	 * @param y
	 * @param mouseDown
	 * @param input
	 */
	public void update(float x, float y, boolean mouseDown, char input) {
		gameActive = false;
		if(menus.getActiveMenu() == inGameMenu){
			if(menus.getActiveMenu().lastClickedElement() == "Back"){
				System.out.println("Going back to the main menu");
				menus.setActiveMenu(mainMenu);
			}
			boardPainter.update(x, y, mouseDown);
			
			gameActive = true;
			board.update();
			if(boardPainter.animationDone()){
				if(!board.finished()){
					if(board.currentPlayer()==0){
						
						if(p1.hasMove()){
							System.out.println("Game. player 0 making move");
							p1.makeMove(board);
							System.out.println("Game. player 0 done");
						}
						
						
					}
					if(board.currentPlayer()==1){
						if(p2.hasMove()){
							p2.makeMove(board);
						}
						
					}
					if(board.currentPlayer()==2){
						if(p3.hasMove()){
							p3.makeMove(board);
						}
						
					}					
				}else{
					System.out.println("GAME OVER");
				}
				
			}
			
			
			
			
			
			
		}else{
			bg.update();
		}
		menus.update(x, y, mouseDown,input);
		//If a game is not in progress render a dummy field that randomly changes colors or something so the menu looks cool and stuff
		if(menus.getActiveMenu() == mainMenu){
			if(menus.getActiveMenu().lastClickedElement() == "Exit"){
				System.exit(0);
			}
			if(menus.getActiveMenu().lastClickedElement() == "New Game"){
				System.out.println("Starting a new Game");
				menus.setActiveMenu(inGameMenu);
				board = new Board(players);
				boardPainter.setBoard(board);
				
			}
			
		}
		if(menus.getActiveMenu().lastClickedElement()!=null){
			System.out.println(menus.getActiveMenu().lastClickedElement());
		}
		if (menus.getActiveMenu() == login) {
//			
			if (menus.getActiveMenu().lastClickedElement() == "Login") {
				System.out.println("Loggin in with: " +login.getUser()+", "+login.getPassword());
				menus.setActiveMenu(mainMenu);
			}
		}

		//TODO: Add new Menu that opens when "Back" is clicked and give more options

	}
	private void shapeRendererInit(ShapeRenderer shapes){
		Gdx.graphics.getGL10().glEnable(GL10.GL_BLEND); 
		shapes.begin(ShapeType.Filled);
	}
	private void shapeRendererExit(ShapeRenderer shapes){
		shapes.end();
	}
	//TODO: Spop using ShapeRenderer and use Texture instead. Asshole behaviour in rendering.
	public void draw(ShapeRenderer shapes, SpriteBatch batch){
//		board.update();
//		boardPainter.update(input.getMouseX(), input.getMouseY(),
//				input.mouseClicked());

		if(gameActive){
			shapeRendererInit(shapes);
			boardPainter.draw(shapes);
			shapeRendererExit(shapes);
			boardPainter.batchDraw(batch);
		}else{
			shapeRendererInit(shapes);
			bg.shapesDraw(shapes);
			shapeRendererExit(shapes);
			bg.batchDraw(batch);
		}
		shapeRendererInit(shapes);
		menus.shapesDraw(shapes);
		shapeRendererExit(shapes);
//		boardPainter.draw(shapes);
		// Gdx.gl.glDisable(GL10.GL_BLEND);
//		shapes.end();
		// Gdx.gl.glEnable(GL10.GL_BLEND);
//		boardPainter.batchDraw(batch);
		menus.batchDraw(batch);
		if(gameActive){
			boardPainter.batchDraw(batch);
		}else{
//			bg.batchDraw(batch);
//			bg.batchDraw(batch);
		}
//		 menus.draw(shapes, batch);
	}

}
