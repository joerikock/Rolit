package MenuItems;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

/**
 * TODO: Add function to add a GuiElement to the Menu, that makes sure that the
 * Element is updated with corretly(Use same function for Button and TextFields
 * that require different arguments for updating)
 * 
 * @author Schnappus
 * 
 */


public abstract class Menu {
	// private ArrayList<>
	private ArrayList<Button> buttons;
	private ArrayList<GuiObject> menuObjects;
	private ArrayList<Integer> buttonIndexList, textInputFieldIndexList;
	private int lastClickedElementIndex;
	private MenuManager manager;
	private String name;

	/**
	 * Creates a new Menu with button, textfields and god know what. The update
	 * function returns the id of the selected element
	 */
	public Menu(String name) {
		lastClickedElementIndex = -1;
		this.menuObjects = new ArrayList<GuiObject>();
		this.buttonIndexList = new ArrayList<Integer>();
		this.textInputFieldIndexList = new ArrayList<Integer>();
	}

	public void setManager(MenuManager manager) {
		this.manager = manager;
	}

	public MenuManager getManager() {
		return this.manager;
	}
	public void activate(){
		this.lastClickedElementIndex = -1;
	}
	public void addButton(Button button) {
		button.setParent(this);
		this.buttons.add(button);
	}

	/**
	 * Adds a specific GuiObject(Button, TextField, etc) to the menu.
	 * 
	 * @param obj
	 */
	public void addGuiObject(GuiObject obj) {
		obj.setParent(this);
		menuObjects.add(obj);
		int menuObjectsLastIndex = menuObjects.size() - 1;
		// add the index of the new element in menuObjects to the corresponding
		// ArrayList

		if (obj instanceof Button) {
			buttonIndexList.add(menuObjectsLastIndex);
			System.out.println("Menu.addGuiObject: " + "Button added "
					+ obj.name);
		}
		if (obj instanceof TextInputField) {
			textInputFieldIndexList.add(menuObjectsLastIndex);
		}
	}

	/**
	 * calls the draw function of all elements of this menu
	 * 
	 * @param shapes
	 * @param batch
	 */
	public void draw(ShapeRenderer shapes, SpriteBatch batch) {
		// System.out.println("Menu.draw called for "
		// +this.menuObjects.size()+" ");
		for (int i = 0; i < menuObjects.size(); i++) {
			// System.out.print(menuObjects.get(i).getName()+", ");
			// shapes.begin(ShapeType.Filled);
			menuObjects.get(i).draw(shapes, batch);
			menuObjects.get(i).drawExtras(shapes, batch);
			// shapes.end();
			// Gdx.gl10.
		}
	}

	public void shapesDraw(ShapeRenderer shapes) {
		for (int i = 0; i < menuObjects.size(); i++) {
			// System.out.print(menuObjects.get(i).getName()+", ");
			// shapes.begin(ShapeType.Filled);
			menuObjects.get(i).shapesDraw(shapes);
			// shapes.end();
		}
	}

	public void batchDraw(SpriteBatch batch) {

		for (int i = 0; i < menuObjects.size(); i++) {
			// System.out.println();
			menuObjects.get(i).batchDraw(batch);
		}
	}

	public void setPosition(float x, float y) {
		for (int i = 0; i < menuObjects.size(); i++) {
			menuObjects.get(i).setParentPosition(x, y);
		}
	}

	public void setAlpha(float a) {
		for (int i = 0; i < menuObjects.size(); i++) {
			// System.out.println(buttons.get(i).toString());
			menuObjects.get(i).setParentAlpha(a);
		}
	}

	/*
	 * Return the index of the element that has been selected by the user
	 */

	public void update(float x, float y, boolean mouseDown, char input) {
		lastClickedElementIndex = -1;
		for (int i = 0; i < menuObjects.size(); i++) {
			menuObjects.get(i).update(x, y, mouseDown);
			if (menuObjects.get(i).clicked()) {
				// System.out.println(i);
				this.lastClickedElementIndex = i;
			}
		}

		for (int i = 0; i < this.textInputFieldIndexList.size(); i++) {
			TextInputField t = (TextInputField) (this.menuObjects
					.get(textInputFieldIndexList.get(i)));
			t.updateKeys(input);

		}
	}

//	public abstract void action();
/**
 * Returns the name of the last clicked Element in the currently active menu
 * @return
 */
	public String lastClickedElement() {

		if (this.lastClickedElementIndex != -1) {
			// System.out.println("CLICKED: "
			// +this.menuObjects.get(lastClickedElementIndex).getName());
			return this.menuObjects.get(lastClickedElementIndex).getName();
		}
		return null;
	}

	public void print() {
		System.out.println("----------- MENU -------------");
		System.out.println("Buttons: " + this.buttonIndexList.size());
		System.out
				.println("TextFields: " + this.textInputFieldIndexList.size());
		for (int i = 0; i < this.menuObjects.size(); i++) {
			System.out.println(menuObjects.get(i).name);
		}
		System.out.println("------------------------");
	}

	public String getName() {
		return name;
	}

}
