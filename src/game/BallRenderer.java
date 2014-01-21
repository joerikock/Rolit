package game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Class for rendering the ball and its animation. The colour of the 
 * ball has been modelled as an integer, which can take the values 
 * of 0, 1, 2 and 3.
 * 
 * @author Max Messerich en Joeri Kock
 *
 */

public class BallRenderer {
	
	// Instance variables ----------------------------------------------
	
	/**
	 * This array marks the colour codes, which are {R, G, B}.
	 */
	public static final float[][] PLAYER_COLORS = { { 1, 0, 0 }, { 0, 1, 0 },
			{ 0, 0, 1 }, { 1, 1, 0 } };
	
	/**
	 * Marks how transparent the balls should be.
	 */
	public static final float MAX_ALPHA = .95f;
	
	/**
	 * Creates a new texture image that is rendered over the coloured rectangles
	 * that represent the balls on the field.
	 */
	public static final Texture OVERLAY = new Texture(
			Gdx.files.internal("assets/data/overlay.png"));
	
	/**
	 * Instance variable nessecary for the mouse hover.
	 */
	public static final float[] MOUSE_OVER = { 1, 1, 1 };
	
	/**
	 * The width of the rectangle for rendering and the alpha value for rendering
	 * the colours and the animation.
	 */
	private float x, y, width, alpha;
	
	/**
	 * Array of variables nessecary for rendering the different colours.
	 */
	private float[] renderColor, ballColor, newBallColor;
	
	/**
	 * Instance variable nessecary for the mouse hover.
	 */
	private boolean changing, mouseOver;
	
	/**
	 * Variable that decides whether the alpha value is increased or decreased.
	 */
	private int animationStep;
	
	/**
	 * Integer representing the ID of the colour.
	 */
	private int colorID;
	
	/**
	 * Renders a new ball and sets the colorID to -1, meaning it is empty.
	 * 
	 * @param x
	 * 			the x-coordinate of the ball.
	 * @param y
	 * 			the y-coordinate of the ball.
	 * @param width
	 * 			the width of the field.
	 */
	public BallRenderer(float x, float y, float width) {
		this.x = x;
		this.y = y;
		this.width = width;
		ballColor = new float[3];
		renderColor = new float[3];
		this.colorID = -1;
	}

	/**
	 * 
	 * @return true if the field is empty, false if there is a ball.
	 */
	public boolean isClear() {
		return (colorID == -1);
	}

	/**
	 * Sets the new width of the rectangles. Nessecary when resizing the frame.
	 * 
	 * @param width
	 * 			The width of the rectangles.
	 */
	public void setWidth(float width) {
		this.width = width;
	}
	
	/**
	 * Method determining if the animation of the rectangle is done.
	 * 
	 * @return true if the animation is done, false if it is still animating.
	 */
	public boolean animationDone(){;
		return (!changing&&alpha>=MAX_ALPHA);
	}
	
	/**
	 * Change the colour of the rectangle to a different ID.
	 * 
	 * @param colorID
	 * 			the ID of the colour you want to change the rectangle to.
	 */
	public void changeColorTo(int colorID) {
		if (isClear()) {
			alpha = 0;
			this.colorID = colorID;
			this.ballColor = PLAYER_COLORS[colorID];
			animationStep = 2;
		} else {
			animationStep = 1;
		}
		this.newBallColor = PLAYER_COLORS[colorID];

		this.changing = true;
	}

	/**
	 * Sets the mouseOver equal to the instance variable mouseOver.
	 * 
	 * @param mouseOver
	 * 			true if the mouse is on top of the rectangle, false if it is not.
	 */
	public void setMouseOver(boolean mouseOver) {
		this.mouseOver = mouseOver;
	}
	
	/**
	 * Sets the render colour of the rectangle.
	 * 
	 * @param color
	 * 			the colour the rectangle should be rendered to.
	 */
	public void setRenderColor(float[] color){
		this.renderColor = color;
		this.alpha = color[3];
	}
	
	/**
	 * Render the rectangle on the screen.
	 * 
	 * @param offSetX
	 * 			Defines the left-down corner of the board.
	 * @param offSetY
	 * 			Defines the up-right corner of the board.
	 * @param shapes
	 * 			The ShapeRenderer rendering the rectangle.
	 */
	public void draw(float offSetX, float offSetY, ShapeRenderer shapes) {
		
		if (changing) {
			if (animationStep == 1) {
				if (alpha > 0) {
					alpha = alpha - .015f - alpha/20;
				} else {
					alpha = 0;
					animationStep = 2;
				}
			}
			if (animationStep == 2) {
				if (alpha < MAX_ALPHA) {
					this.ballColor = newBallColor;
					this.alpha = alpha + 0.05f;
				} else {
					changing = false;
					this.alpha = MAX_ALPHA;
				}
			}
		}		
		if (this.isClear()) {
			if (mouseOver) {
				this.renderColor = MOUSE_OVER;
				alpha = .8f;
			} else {
				if (alpha>0) {
					alpha=alpha -.02f;
				}
			}
		} else {
			this.renderColor = this.ballColor;
		}
		shapes.setColor(renderColor[0], renderColor[1], renderColor[2], alpha);
		shapes.rect(offSetX+x, offSetY+y, width, width);		
	}

	/**
	 * Renders the same as the draw-method, but this is used for 
	 * rendering textures.
	 * 
	 * @param offSetX
	 * 			Defines the left-down corner of the board.
	 * @param offSetY
	 * 			Defines the up-right corner of the board.
	 * @param batch
	 * 			The SpriteBatch rendering the rectangle.
	 * 			
	 */
	public void batchDraw(float offSetX, float offSetY, SpriteBatch batch) {
		batch.draw(OVERLAY, offSetX+x, offSetY+y);
	}
}
