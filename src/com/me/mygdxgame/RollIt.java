package com.me.mygdxgame;

import game.Board;
import game.BoardGUI;
import game.Game;
import rollitMenus.LoginMenu;
import rollitMenus.MainMenu;
import MenuItems.Button;
import MenuItems.MenuManager;
import MenuItems.TextInputField;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.me.mygdxgame.InputHandler.CleanUpException;

public class RollIt implements ApplicationListener, TextInputListener {
	public static BitmapFont FONT;

	private SpriteBatch batch;

	private InputHandler input;
	ShapeRenderer shapes;
	MenuManager menu;
	Button switcher;
	TextInputField text;
	BoardGUI boardPainter;
	Board board;
	Camera camera;
	Game game;
	@Override
	public void create() {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
//		camera = new OrthographicCamera(w, h);
//		camera.position.set(0, 0, 0);
		Texture.setEnforcePotImages(false);
		batch = new SpriteBatch();

		shapes = new ShapeRenderer();
		// add Key Listener for keyboardInput

		FONT = new BitmapFont();
		menu = new MenuManager();
		LoginMenu loginMenu = new LoginMenu(menu);
		MainMenu mainMenu = new MainMenu(menu);

		menu.addMenu(mainMenu);
		menu.addMenu(loginMenu);
//		board = new Board();
		menu.print();
		// board.tryMove(3, 3, 1);
		// board.tryMove(3,4, 2);
		// board.update();
		//
		// board.update();
		game = new Game();


		input = new InputHandler();
	}

	public void dispose() {
		batch.dispose();

	}

	// TODO: Fix alpha blending in menus
	public void render() {
		// System.out.println(camera.position.x+","+camera.position.y);
		// camera.update();
		Gdx.gl.glClearColor(.3f, .3f, .3f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glEnable(GL10.GL_BLEND);
		Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		
		FONT.setColor(0, 0, 0, 1);
		try {
			input.update();
		} catch (CleanUpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// System.out.println(input.getKey());
		if (input.getKey() == 'c') {

			board.tryMove(5, 1, 2);
		}
		if (input.getKey() == 'd') {
			board.print();
		}
		game.update(input.getMouseX(), input.getMouseY(), input.mouseClicked(),input.getKey());

		game.draw(shapes, batch);
		input.cleanUp();

	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void input(String text) {
		// TODO Auto-generated method stub

	}

	@Override
	public void canceled() {
		// TODO Auto-generated method stub

	}

}
