package game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * The class for the animated background in the menu. There is a red square
 * moving horizontally, and after some time it goes bouncing around the frame.
 * 
 * @author Max Messerich en Joeri Kock.
 */

public class AnimatedBackGround {
	// Instance variables ------------------------------------------

	/**
	 * The BoardGUI.
	 */
	BoardGUI bg;

	/**
	 * Some integers that are nessecary for the animation to work.
	 */
	int count, maxCount, speedCount, speed, x, y, xChange, yChange;

	/**
	 * Creates a new AnimatedBackGround.
	 * 
	 * @param speed
	 *            the speed of the moving animation.
	 * @param delay
	 *            this determines how long the animation should move
	 *            horizontally.
	 */
	public AnimatedBackGround(int initSpeed, int delay) {
		this.bg = new BoardGUI(
				(int) (Gdx.graphics.getWidth() / BoardGUI.BALL_SIZE) + 1,
				(int) (Gdx.graphics.getHeight() / BoardGUI.BALL_SIZE) + 1);
		this.count = 0;
		this.speedCount = 0;
		this.speed = initSpeed;
		this.maxCount = delay;
		x = 5;
		y = 5;
		xChange = 1;
		yChange = 0;
	}

	/**
	 * The method performing the actual animation. When the square hits the
	 * edge, the xChange and/or yChange is reversed.
	 */
	public void update() {
		if (count == maxCount) {

			xChange = Tools.randomDir();
			yChange = Tools.randomDir();

			count = 0;
		} else {
			count++;
		}
		if (speedCount == speed) {
			if (!bg.isField(x + xChange, y)) {
				xChange *= -1;
			}
			if (!bg.isField(x, y + yChange)) {
				yChange *= -1;
			}

			x += xChange;
			y += yChange;
			// System.out.println(x + ", " + y + " :: " + xChange + ", " +
			// yChange);
			// bg.setFieldColor(x, y, 0, 0, 1, .3f);
			speedCount = 0;
		} else {
			speedCount++;
		}
		bg.setFieldColor(x, y, .9f, .2f, .2f, .9f);

	}

	/**
	 * Draws the shape on the screen.
	 * 
	 * @param shapes
	 *            A ShapeRenderer nessecary for creating the shape.
	 */
	public void shapesDraw(ShapeRenderer shapes) {
		bg.draw(shapes);
	}

	/**
	 * Draws the texture of the red square.
	 * 
	 * @param batch
	 *            SpriteBatch nessecary for the on-screen rendering of the
	 *            image.
	 */
	public void batchDraw(SpriteBatch batch) {
		bg.batchDraw(batch);
	}
}
