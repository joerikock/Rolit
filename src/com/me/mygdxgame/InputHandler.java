package com.me.mygdxgame;

import java.util.ArrayList;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
/**
 * Fetches input.
 * @author Max Messerich and Joeri Kock
 *
 */
public class InputHandler implements InputProcessor {
	class CleanUpException extends Exception {
		private static final long serialVersionUID = 1L;

		public CleanUpException() {
		}

		public CleanUpException(String message) {
			super(message);
		}
	}

	/**
	 * Responsible for fetching mouse and keyboard input.
	 */
	float mouseX, mouseY,startWidth, startHeight;
	boolean mouseClicked, mouseDown, currentMouseState, 
	keyReleased, keyDown, currentKeyState;
	char typedKey;
	ArrayList<Character> lastC;

	public InputHandler(float standartWidth, float standartHeight) {
		InputMultiplexer im = new InputMultiplexer();
		im.addProcessor(this);
		Gdx.input.setInputProcessor(im);
		this.startHeight = standartHeight;
		this.startWidth = standartWidth;
		mouseDown = false;
	}

	/**
	 * Updates the the position of the mouse and checks whether it has been
	 * clicked. Note: All required data has to be fetched via the given queries
	 * like getKey() or mouseClicked(), because all arguments will be reset to
	 * the starting values at the beginning of each loop.
	 */
	public void update() {
		
		mouseX = Gdx.input.getX() * ( this.startWidth / Gdx.graphics.getWidth());
		mouseY = (Gdx.graphics.getHeight() - Gdx.input.getY())
				* (this.startHeight/Gdx.graphics.getHeight());

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
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		typedKey = character;
		return false;

	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}
