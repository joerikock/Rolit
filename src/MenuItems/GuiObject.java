package MenuItems;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.me.mygdxgame.RollIt;

//TODO: generalize drawing of elements
public class GuiObject implements GuiElement {
	public static final float R = 0;
	public static final float G = 0;
	public static final float B = 0;
	public static final float MOUSEOVER_R = .7f;
	public static final float MOUSEOVER_G = .7f;
	public static final float MOUSEOVER_B = .7f;
	static final float mouseOverEffectSpeed = .5f;
	boolean mouseOver, selected, clicked;
	float x, y, px, py, width, height, alpha, heightProgress;
	Menu parent;
	String name;

	public GuiObject(String name) {
		this.name = name;
	}

	public void setParent(Menu parent) {
		this.parent = parent;
	}

	public String getName() {
		return name;
	}

	public void setDimensions(float x, float y, float width, float height) {
		System.out.println(x + ". " + y);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public void setParentPosition(float x, float y) {
		px = x;
		py = y;
	}

	public void setParentAlpha(float a) {

		alpha = a;
	}

	@Override
	/**
	 * Updates the Object and returns true, if the the mouse is over the elemnt and is clicked
	 */
	public void update(float x, float y, boolean mouseDown) {
		// when the mouse has been pressed, the object can no longer be selected
		// (apart from the situation in which the is clicked, when it is
		// allready selected)
		// difference between clicked and selected: clicked is only true, when
		// the element has been clicked. selected is true while the element has
		// been clicked and
		// the mousebutton has not been pressed again
		clicked = false;
		mouseOver = (x > this.x && x < this.x + this.width && y > this.y && y < this.y
				+ this.height);
		if (mouseDown && mouseOver) {
			clicked = true;
			selected = true;
		}
		if (mouseDown && !mouseOver) {
			selected = false;
		}

	}

	public float realX() {
		return px + x;
	}

	public float realY() {
		if (this.mouseOver) {
			if (this.heightProgress < height) {
				this.heightProgress += Button.mouseOverEffectSpeed;
			} else {
				this.heightProgress = height;
			}
		} else {
			if (this.heightProgress > 0) {
				this.heightProgress -= Button.mouseOverEffectSpeed;
			} else {
				this.heightProgress = 0;
			}
		}
		return py + y;
	}

	public void draw(ShapeRenderer shapes, SpriteBatch batch) {
		// TODO Auto-generated method stub
		System.out.println(this.name + " draw called. ALPHA: " + this.alpha);

		// shapes.begin(ShapeType.Filled);

		// System.out.println(alpha);

		// shapes.end();
		// this.drawExtras(shapes, batch);
	}

	@Override
	public boolean focused() {
		// TODO Auto-generated method stub
		return mouseOver;
	}

	@Override
	public boolean selected() {
		return selected;
	}

	@Override
	public boolean clicked() {
		return clicked;
	}

	@Override
	public void drawExtras(ShapeRenderer shapes, SpriteBatch batch) {
		System.out.println("drawExtras called  within Object" + this.name);
		batch.begin();
		// System.out.println(this.text);
		RollIt.FONT.setColor(1, 0, 0, 1);
		RollIt.FONT.draw(batch, name, realX(), realY() + height / 2);
		batch.end();
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public void shapesDraw(ShapeRenderer shapes) {
		if (this.mouseOver) {
			if (this.heightProgress < height) {
				this.heightProgress += Button.mouseOverEffectSpeed;
			} else {
				this.heightProgress = height;
			}
		} else {
			if (this.heightProgress > 0) {
				this.heightProgress -= Button.mouseOverEffectSpeed;
			} else {
				this.heightProgress = 0;
			}
		}
		shapes.setColor(GuiObject.R, GuiObject.G, GuiObject.B, alpha);
		shapes.rect(realX(), realY(), width, height);
		if (heightProgress > 0) {
			shapes.setColor(GuiObject.MOUSEOVER_R, GuiObject.MOUSEOVER_G,
					GuiObject.MOUSEOVER_B, alpha);
			shapes.rect(realX(), realY(), width, heightProgress);
		}

	}

	@Override
	public void batchDraw(SpriteBatch batch) {
		// TODO Auto-generated method stub

	}
}
