package MenuItems;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

//TODO: generalize drawing of elements
public abstract class GuiObject {
	public static final Texture OVERLAY = new Texture(
			Gdx.files.internal("assets/data/guioverlay.png"));
	/**
	 * Defines the color for the mouse over effect
	 */
	public static final float[] MOUSEOVER_COLOR = { .3f, .3f, .3f };
	/**
	 * Defindes the normal background color.
	 */
	public static final float[] COLOR = { .1f, .5f, .5f };
	/**
	 * Defines the font color.
	 */
	public static final float[] FONT_COLOR = { .9f, .9f, .9f };
	/**
	 * Defines the standard height of GuiObjects.
	 */
	public static final float ELEMENT_HEIGHT = 50;
	/**
	 * Defines the standard width of GuiObjects.
	 */
	public static final float ELEMENT_WIDTH = 100;
	/**
	 * Defines the speed of the mouse over effect.
	 */
	private static final float mouseOverEffectSpeed = 1.5f;
	/**
	 * Defines whether the mouse is hovering over the element.
	 */
	boolean mouseOver;
	/**
	 * Defines whether the element is selected (Has been clicked).
	 */
	boolean selected;
	/**
	 * Defines whether the element has been clicked.
	 */
	boolean clicked;
	/**
	 * Defines whether the object is the child object of an other object.
	 */
	boolean isChild;
	/**
	 * Defines whether the obejct is selectable at all. In general all elements
	 * are selectable except for buttons that are not the child of another
	 * object.
	 */
	boolean selectAble;

	/**
	 * Defines the position of the element.
	 */
	float x, y;
	/**
	 * Defines the position of the parent menu.
	 */
	float parentX, parentY;
	/**
	 * Widht and height of the element.
	 */
	float width, height;
	/**
	 * Variables for rendering. alpha is for the alpha of RGBA, heightProgress
	 * defines the height of the mouseOver effect.
	 */
	float alpha, heightProgress;
	/**
	 * Defines the parent object.
	 */
	private GuiObject parentObject;
	/**
	 * List of children objects.
	 */
	private ArrayList<GuiObject> children;
	/**
	 * Defines the parent menu.
	 */
	Menu parent;
	/**
	 * Denfines the name of the object.
	 */
	String name;

	/**
	 * Creates a new GuiObject.
	 * 
	 * @param name
	 */
	public GuiObject(String name) {
		children = new ArrayList<GuiObject>();
		this.name = name;
		this.selectAble = true;
	}

	/**
	 * Sets the parent object. Adds this GuiObject instance as a child to the
	 * GuiObject given in the constructor.
	 * 
	 * @param parentObject
	 */
	public void setParentObject(GuiObject parentObject) {
		this.isChild = true;
		this.selectAble = true;
		this.parentObject = parentObject;
		parentObject.addChild(this);
	}

	/**
	 * Sets selectAble to true or false.
	 * 
	 * @param selectAble
	 */
	public void setSelectAble(boolean selectAble) {
		this.selectAble = selectAble;
	}

	/**
	 * Adds a child to this object.
	 * 
	 * @param child
	 */
	private void addChild(GuiObject child) {
		this.children.add(child);
	}

	/**
	 * Returns a list of all children of this Object.
	 * 
	 * @return
	 */
	private ArrayList<GuiObject> getChildren() {
		return children;
	}

	/**
	 * Returns the instance of the selected child.
	 * 
	 * @return .
	 */
	public String getSelectedChild() {
		for (int i = 0; i < children.size(); i++) {
			if (children.get(i).selected()) {
				return children.get(i).getName();
			}
		}
		return null;
	}

	/**
	 * Return the name of the object.
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the position and the size of the GuiObject.
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public void setDimensions(float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	/**
	 * Sets the parent position of the GuiObject. The parent position defines
	 * the x and y distance from the botto,-left cornor of the screen
	 * 
	 * @param x
	 * @param y
	 */
	public void setParentPosition(float x, float y) {
		parentX = x;
		parentY = y;
	}

	/**
	 * Sets the alpha value of the GuiObject.
	 * 
	 * @param a
	 */
	public void setAlpha(float a) {
		alpha = a;
		if (alpha < 0) {
			alpha = 0;
		}
	}

	/**
	 * Sets the selected to true or false. Important for updating child objects.
	 * 
	 * @param selected
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	/**
	 * * Updates the Object and their children with the mouse input of the user.
	 * The clicked and selected element(s) can be fetch via the menu.
	 * 
	 * @param x
	 *            x-position of the mouse
	 * @param y
	 *            y-position of the mouse
	 * @param mouseDown
	 *            boolean value for the state of the left mouse button.
	 */
	public void update(float x, float y, boolean mouseDown) {

		clicked = false;
		mouseOver = (x > this.x && x < this.x + this.width && y > this.y && y < this.y
				+ this.height);

		if (mouseDown) {
			/**
			 * If the object is a child of an other object and has been clicked,
			 * tell all other children, that they are no longer selected.
			 */
			if (isChild) {
				if (mouseOver) {
					for (int i = 0; i < parentObject.getChildren().size(); i++) {
						parentObject.getChildren().get(i).setSelected(false);
					}
					clicked = true;
					selected = true;
				}
				parentObject.setSelected(true);
			} else {
				if (mouseOver) {
					clicked = true;
					if (selectAble) {
						selected = true;
					}

				} else {

					selected = false;
				}
			}

		}
	}

	/**
	 * Returns whether the GuiObject is selected or not.
	 * 
	 * @return
	 */
	public boolean selected() {
		return selected;
	}

	/**
	 * Returns whether the GuiObject has been clicked last loop.
	 * 
	 * @return
	 */
	public boolean clicked() {
		return clicked;
	}

	/**
	 * Draws the background of the GuiObject. Uses the LIBGdx ShapeRenderer.
	 * 
	 * @param shapes
	 */
	public void shapesDraw(ShapeRenderer shapes) {
		if (selected || mouseOver) {

			if (this.heightProgress < height) {
				this.heightProgress += GuiObject.mouseOverEffectSpeed;
			} else {
				this.heightProgress = height;
			}
		} else {

			if (this.heightProgress > 0) {
				this.heightProgress -= GuiObject.mouseOverEffectSpeed;
			} else {
				this.heightProgress = 0;
			}
		}
		shapes.setColor(COLOR[0], COLOR[1], COLOR[2], alpha);

		shapes.rect(x, y, width, height);
		if (heightProgress > 0) {
			shapes.setColor(MOUSEOVER_COLOR[0], MOUSEOVER_COLOR[1],
					MOUSEOVER_COLOR[2], alpha);
			shapes.rect(x, y, width, heightProgress);
		}

	}

	/**
	 * Abstract method for using the LIBGdx SpriteBatch. Used for rendering
	 * text.
	 * 
	 * @param batch
	 */
	public abstract void batchDraw(SpriteBatch batch);

	public void drawOverLay(SpriteBatch batch) {
		batch.draw(OVERLAY, x, y);
	}
}
