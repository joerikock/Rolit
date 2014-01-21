package game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class BallRenderer {
	public static final float[][] PLAYER_COLORS = { { 1, 0, 0 }, { 0, 1, 0 },
			{ 0, 0, 1 }, { 1, 1, 0 } };
	public static final float MAX_ALPHA = .95f;
	public static final Texture OVERLAY = new Texture(
			Gdx.files.internal("data/overlay.png"));
	public static final float[] MOUSE_OVER = { 1, 1, 1 };
	private float x, y, width, alpha;
	private float[] renderColor, ballColor, newBallColor;
	private boolean changing, mouseOver;
	private int animationStep;
	private int colorID;
	public BallRenderer(float x, float y, float width) {
		this.x = x;
		this.y = y;
		this.width = width;
		ballColor = new float[3];
		renderColor = new float[3];
		this.colorID = -1;
	}

	public boolean isClear() {
		return (colorID == -1);
	}
	private boolean hasColor(){
		return (alpha!=0);
	}
	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

	public void setWidth(float width) {
		this.width = width;
	}
	public boolean animationDone(){
//		System.out.println("animationDone: "+"[ "+this+x+", "+this.y+" ] "+this.alpha+", "+MAX_ALPHA);
		return (!changing&&alpha>=MAX_ALPHA);
	}
	public void changeColorTo(int colorID) {
		if (isClear()) {
//			System.out.println("Field clear. Setting color to " + colorID);
			alpha = 0;
			this.colorID = colorID;
			this.ballColor = PLAYER_COLORS[colorID];
			animationStep = 2;
		} else {
			animationStep = 1;
		}
		this.newBallColor = PLAYER_COLORS[colorID];

		this.changing = true;
//		System.out.println("BallSet at: "[ " + x + "," + y + " ] : " + alpha);
	}
	
	public void setColor(int colorID) {
		this.ballColor = PLAYER_COLORS[colorID];
		this.alpha = MAX_ALPHA;
	}

	public void setMouseOver(boolean mouseOver) {
//		if(isClear()){
			this.mouseOver = mouseOver;
//		}
		
	}
	public void setRenderColor(float[] color){
//		System.out.println("Setting color of "+x+" , "+y);
		this.renderColor = color;
		this.alpha = color[3];
	}
	public void draw(float offSetX, float offSetY, ShapeRenderer shapes) {
		
		if (changing) {
			// System.out.println(animationStep);
			// Fade out old color
			// System.out.println(animationStep + ", " + alpha + ", " +
			// MAX_ALPHA);
			if (animationStep == 1) {

				// System.out.println(alpha);
				if (alpha > 0) {
					// System.out.println("SUB");
					alpha = alpha - .015f - alpha/20;
				} else {
					alpha = 0;
					animationStep = 2;
				}
				// Fade in new color
			}
			if (animationStep == 2) {
				if (alpha < MAX_ALPHA) {
					// System.out.println("ADD");
					this.ballColor = newBallColor;
					this.alpha = alpha + 0.05f;
				} else {
					changing = false;
					this.alpha = MAX_ALPHA;
				}
			}
			
//			this.mouseOver = false;
		}		
		if(this.isClear()){
			
			if(mouseOver){
				this.renderColor = MOUSE_OVER;
				alpha = .8f;
			}else{
				if(alpha>0){
					alpha=alpha -.02f;
				}
			}
		}else{
//			System.out.println("Appling ballColor");
			this.renderColor = this.ballColor;
		}

//		if (!isClear()) {

			shapes.setColor(renderColor[0], renderColor[1], renderColor[2],
					alpha);
			shapes.rect(offSetX+x, offSetY+y, width, width);
//		}

		
		
	}

	public void batchDraw(float offSetX, float offSetY,SpriteBatch batch) {
		batch.draw(OVERLAY, offSetX+x, offSetY+y);
	}
}
