package com.me.mygdxgame;

import game.Game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GLTexture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class RollIt implements ApplicationListener, TextInputListener {
	public static BitmapFont font;
	private SpriteBatch batch;
	private InputHandler input;
	ShapeRenderer shapes;
	Game game;

	@Override
	public void create() {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		GLTexture.setEnforcePotImages(false);
		batch = new SpriteBatch();
		shapes = new ShapeRenderer();
		// add Key Listener for keyboardInput
		font = new BitmapFont();
		game = new Game();
		input = new InputHandler(w, h);
	}

	@Override
	public void dispose() {
		batch.dispose();
		shapes.dispose();
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(.2f, .2f, .2f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glEnable(GL10.GL_BLEND);
		Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

		font.setColor(0, 0, 0, 1);
		input.update();
		game.update(input.getMouseX(), input.getMouseY(), input.mouseClicked(),
				input.getKey());

		game.draw(shapes, batch);
		input.cleanUp();

	}

	@Override
	public void resize(int width, int height) {
		System.out.println(width + ", " + height);

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
