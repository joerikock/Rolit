package com.me.mygdxgame;

import java.util.ArrayList;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;

public class InputHandler implements InputProcessor {
	class CleanUpException extends Exception {
		private static final long serialVersionUID = 1L;

		// Parameterless Constructor
		public CleanUpException() {
		}

		// Constructor that accepts a message
		public CleanUpException(String message) {
			super(message);
		}
	}

	/**
	 * Responsible for fetching mouse and keyboard input.
	 */
	float mouseX, mouseY;
	boolean mouseClicked, mouseDown, currentMouseState, 
	keyReleased, keyDown, currentKeyState;
	private boolean clean;
	char typedKey;
	ArrayList<Character> lastC;

	public InputHandler() {
		InputMultiplexer im = new InputMultiplexer();
		im.addProcessor(this);
		Gdx.input.setInputProcessor(im);
		mouseDown = false;
		clean = true;
		// enableRepeatEvents(true);
	}

	//
	/**
	 * Updates the the position of the mouse and checks whether it has been
	 * clicked. Note: All required data has to be fetched via the given queries
	 * like getKey() or mouseClicked(), because all arguments will be reset to
	 * the starting values at the beginning of each loop.
	 * 
	 * @throws CleanUpException
	 */
	public void update() {

		mouseX = Gdx.input.getX() * (900 / Gdx.graphics.getWidth());
		mouseY = (Gdx.graphics.getHeight() - Gdx.input.getY())
				* (600 / Gdx.graphics.getHeight());
		// System.out.println(mouseX+" ,"+(900/Gdx.graphics.getWidth()));
		// System.out.println(mouseY+" , "+(600/Gdx.graphics.getHeight()));
		// System.out.println("");
		// mouseClicked = false;

		if (!Gdx.input.isButtonPressed(0)) {
			if (mouseDown) {

				mouseClicked = true;
				mouseDown = false;
			}
		}
		mouseDown = Gdx.input.isButtonPressed(0);

	}

	public void cleanUp() {
		mouseClicked = false;
		typedKey = '\0';
		clean = true;
	}

	public boolean hasNewKey() {
		return (typedKey != '\0');
	}

	public char getKey() {

		return typedKey;
	}

	/**
	 * Return true when the Mouse was clicked (hold then release)
	 * 
	 * @return
	 */
	public boolean mouseClicked() {
		return mouseClicked;
	}

	/**
	 * Return the x position of the mouse
	 * 
	 * @return
	 */
	public float getMouseX() {
		return this.mouseX;
	}

	/**
	 * returns the y position of the mouse;
	 * 
	 * @return
	 */
	public float getMouseY() {
		return this.mouseY;
	}

	/**
	 * cleanUp needs to be called in order to reset the handler after each loop.
	 * Its the responsibility of the using class to fetch and store the input
	 */

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		// System.out.println(keycode+" FAG");
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		typedKey = character;
		// System.out.println(character+" FAG");
		return false;

	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
}
