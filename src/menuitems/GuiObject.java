package menuitems;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public abstract class GuiObject {
	public static final Texture OVERLAY = new Texture(
			Gdx.files.internal("assets/data/guioverlay.png"));
	/**
	 * Defines the color for the mouse over effect.
	 */
	public static final float[] MOUSEOVER_COLOR = {.3f, .3f, .3f};
	/**
	 * Defindes the normal background color.
	 */
	public static final float[] COLOR = {.8f, .2f, .5f};
	/**
	 * Defines the font color.
	 */
	public static final float[] FONT_COLOR = {.9f, .9f, .9f};
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
	private static final float MOUSEOVER_EFFECT_SPEED = 2f;
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
	 * Defines whether the object can be interacted with.
	 */
	protected boolean clickAble;
	/**
	 * Defines whether the object is being rendered.
	 */
	protected boolean render;
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
	public GuiObject(String theName) {
		children = new ArrayList<GuiObject>();
		this.name = theName;
		this.selectAble = true;
		this.clickAble = true;
		this.render = true;
	}

	/**
	 * Sets the parent object. Adds this GuiObject instance as a child to the
	 * GuiObject given in the constructor.
	 * 
	 * @param parentObject
	 */
	public void setParentObject(GuiObject theParentObject) {
		this.isChild = true;
		this.selectAble = false;
		this.parentObject = theParentObject;
		parentObject.addChild(this);
	}

	/**
	 * If choice is false, the object will not be rendered.
	 * 
	 * @param choice
	 */
	public void setRender(boolean choice) {
		this.render = choice;
	}

	/**
	 * Sets selectAble to true or false.
	 * 
	 * @param selectAble
	 */
	public void setSelectAble(boolean isSelectAble) {
		this.selectAble = isSelectAble;
	}

	public void setClickAble(boolean clickeAble) {
		setSelectAble(clickeAble);
		this.clickAble = false;
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
	public void setDimensions(float theX, float theY, float w, float h) {
		this.x = theX;
		this.y = theY;
		this.width = w;
		this.height = h;
	}

	/**
	 * Sets the parent position of the GuiObject. The parent position defines
	 * the x and y distance from the botto,-left cornor of the screen
	 * 
	 * @param x
	 * @param y
	 */
	public void setParentPosition(float theX, float theY) {
		parentX = theX;
		parentY = theY;
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
	public void setSelected(boolean isSelected) {
		this.selected = isSelected;
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
	public void update(float theX, float theY, boolean mouseDown) {

		clicked = false;
		if (clickAble) {
			mouseOver = theX > this.x && theX < this.x + this.width
					&& theY > this.y && theY < this.y + this.height;
		}

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
		if (render) {
			if (selected || mouseOver) {

				if (this.heightProgress < height) {
					this.heightProgress += GuiObject.MOUSEOVER_EFFECT_SPEED;
				} else {
					this.heightProgress = height;
				}
			} else {

				if (this.heightProgress > 0) {
					this.heightProgress -= GuiObject.MOUSEOVER_EFFECT_SPEED;
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
	}

	/**
	 * Abstract method for using the LIBGdx SpriteBatch. Used for rendering
	 * text.
	 * 
	 * @param batch
	 */
	public abstract void batchDraw(SpriteBatch batch);
	
	public void drawOverLay(SpriteBatch batch) {
		if (render) {
			batch.draw(OVERLAY, x, y);
		}
	}
}
