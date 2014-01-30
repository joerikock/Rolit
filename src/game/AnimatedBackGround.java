package game;

import java.util.ArrayList;

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
	private class Mover {
		private int x, y, speed, delay, count, xdir, ydir;
		private float r, g, b;

		public Mover(int x, int y, int speed, int delay, float r, float g,
				float b) {
			this.x = x;
			this.y = y;
			this.speed = speed;
			this.delay = delay;
			this.r = r;
			this.g = g;
			this.b = b;
			count = 0;
			xdir = Tools.randomDir();
			ydir = Tools.randomDir();
		}

		public void update(int maxX, int maxY) {
			count++;
			if (count == delay) {
				if (x + xdir < 0 || x + xdir >= maxX) {
					xdir *= -1;
				}
				if (y + ydir < 0 || y + ydir >= maxY) {
					ydir *= -1;
				}
				x += xdir;
				y += ydir;
				if (x >= maxX) {
					x = maxX;
				}
				if (y >= maxY) {
					y = maxY - 1;
				}
				count = 0;
			}

			bg.setFieldColor(x, y, r, g, b, .2f);
		}
	}

	// Instance variables ------------------------------------------

	/**
	 * The BoardGUI.
	 */
	private BoardGUI bg;

	/**
	 * ArrayList of Movers that animate the background
	 */
	private ArrayList<Mover> movers;

	/**
	 * 
	 */
	private int w, h;

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
		w = (int) (Gdx.graphics.getWidth() / (BoardGUI.BALL_SIZE) + 1);
		h = (int) (Gdx.graphics.getHeight() / (BoardGUI.BALL_SIZE));
		this.bg = new BoardGUI(w, h);
		movers = new ArrayList<Mover>();
		movers.add(new Mover(3, 3, 1, 6, 0, 1, 0));
		movers.add(new Mover(7, 4, 1, 6, 1, 1, 0));
		movers.add(new Mover(6, 6, 1, 6, 0, 1, 1));
		movers.add(new Mover(4, 6, 1, 6, 0, 0, 1));
		movers.add(new Mover(3, 2, 1, 6, 0, 1, 0));
		movers.add(new Mover(1, 3, 1, 6, 1, 1, 0));
		movers.add(new Mover(3, 7, 1, 6, 0, 1, 1));
		movers.add(new Mover(5, 6, 1, 6, 0, 0, 1));
	}

	/**
	 * The method performing the actual animation. When the square hits the
	 * edge, the xChange and/or yChange is reversed.
	 */
	public void update() {
		for (Mover mov : movers) {
			mov.update(w, h);
		}
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
