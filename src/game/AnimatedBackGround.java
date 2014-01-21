package game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class AnimatedBackGround {
	BoardGUI bg;
	int count, maxCount, speedCount, speed, x, y, xChange, yChange;

	public AnimatedBackGround(int speed, int delay) {
		this.bg = new BoardGUI(
				(int) (Gdx.graphics.getWidth() / BoardGUI.BALL_SIZE)+1,
				(int) (Gdx.graphics.getHeight() / BoardGUI.BALL_SIZE)+1);
		this.count = 0;
		this.speedCount = 0;
		this.speed = speed;
		this.maxCount = delay;
		x = 5;
		y = 5;
		xChange = 1;
		yChange = 0;
	}

	public void update() {
		if (count == maxCount) {

			xChange = Tools.randomDir();
			yChange = Tools.randomDir();

			count = 0;
		} else {
			count++;
		}
		if(speedCount==speed){
			if (!bg.isField(x + xChange, y)) {
				xChange *= -1;
			}
			if (!bg.isField(x, y + yChange)) {
				yChange *= -1;
			}

			x += xChange;
			y += yChange;
//			System.out.println(x + ", " + y + " :: " + xChange + ", " + yChange);
//			bg.setFieldColor(x, y, 0, 0, 1, .3f);
			speedCount= 0;
		}else{
			speedCount++;
		}
		bg.setFieldColor(x, y,.9f,.2f,.2f, .9f);


	}

	public void shapesDraw(ShapeRenderer shapes) {
		bg.draw(shapes);
	}

	public void batchDraw(SpriteBatch batch) {
		bg.batchDraw(batch);
	}
}
