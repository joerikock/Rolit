package MenuItems;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

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
	/**
	 * ArrayList in which all guiObjects of the menu will be stored.
	 */
	private ArrayList<GuiObject> guiObjects;
	/**
	 * Saves for each Element whether it is parent or Child.
	 */
	private HashMap<Integer, Boolean> isParent;
	/**
	 * HashMap that handles child and parent Objects.
	 */
	private HashMap<Integer, Integer> childIndexManager;
	/**
	 * ArrayList that saves the
	 */
	// private ArrayList<GuiObject> menuObjects;
	// private ArrayList<GuiObject> childrenObjects;
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
		this.isParent = new HashMap<Integer, Boolean>();

		this.childIndexManager = new HashMap<Integer, Integer>();

		this.guiObjects = new ArrayList<GuiObject>();
		this.buttonIndexList = new ArrayList<Integer>();
		this.textInputFieldIndexList = new ArrayList<Integer>();
		this.name = name;
	}

	public void setManager(MenuManager manager) {
		this.manager = manager;
	}

	public MenuManager getManager() {
		return this.manager;
	}

	public void activate() {
		this.lastClickedElementIndex = -1;
	}

	/**
	 * Adds a specific GuiObject(Button, TextField, etc) to the menu.
	 * 
	 * @param obj
	 */

	private void addInternal(GuiObject obj) {
		guiObjects.add(obj);
		if (obj instanceof Button) {
			buttonIndexList.add(guiObjects.size() - 1);

			System.out.println("Menu.addGuiObject: " + "Button added "
					+ obj.name);
		}
		if (obj instanceof TextInputField) {

			textInputFieldIndexList.add(guiObjects.size() - 1);
		}
	}

	/**
	 * Adds a new GuiObject to the menu.
	 * 
	 * @param obj
	 */
	public void addGuiObject(GuiObject obj) {

		addInternal(obj);
		isParent.put(guiObjects.size() - 1, true);

	}

	public void addChildGuiObjectTo(GuiObject obj, GuiObject parent) {
		addInternal(obj);
		isParent.put(guiObjects.size() - 1, false);
		// Find the index of the parent in indexManager
		for (int i = 0; i < guiObjects.size(); i++) {
			if (guiObjects.get(i) == parent) {
				System.out.println("Parent found at " + i);
				childIndexManager.put(guiObjects.size() - 1, i);
			}

		}

	}

	public void shapesDraw(ShapeRenderer shapes) {

		for (int i = 0; i < guiObjects.size(); i++) {
			// shapes.begin(ShapeType.Filled);
			guiObjects.get(i).shapesDraw(shapes);
			// shapes.end();
		}
	}

	public void batchDraw(SpriteBatch batch) {

		for (int i = 0; i < guiObjects.size(); i++) {
			// System.out.println();
			guiObjects.get(i).batchDraw(batch);
		}
	}

	public void setPosition(float x, float y) {
		for (int i = 0; i < guiObjects.size(); i++) {
			guiObjects.get(i).setParentPosition(x, y);
		}
	}

	public void setAlpha(float a) {
		for (int i = 0; i < guiObjects.size(); i++) {
			// System.out.println(buttons.get(i).toString());
			guiObjects.get(i).setParentAlpha(a);
		}
	}

	/*
	 * Return the index of the element that has been selected by the user
	 */

	public void update(float x, float y, boolean mouseDown, char input) {

		// Update all guiObjects with their needed data
		for (int i = 0; i < guiObjects.size(); i++) {

			guiObjects.get(i).update(x, y, mouseDown);
			guiObjects.get(i).updateMembers();
			if (guiObjects.get(i) instanceof TextInput) {
				TextInput t = (TextInput) guiObjects.get(i);
				t.updateKeys(input);
			}
			// If the currrent GuiObject x is of the menu itself, and is
			// clicked, the currently selected element
			// is x
			if (isParent.get(i)) {
				if (guiObjects.get(i).clicked()) {
					// System.out.println(i);
					this.lastClickedElementIndex = i;
				}
			} else {
				// If the current GuiObject x is a child object of GuiObject y,
				// the
				// currently selected element is y

				if (guiObjects.get(i).clicked()) {

					this.lastClickedElementIndex = this.childIndexManager
							.get(i);
				}
			}

		}
	}

	// public abstract void action();
	/**
	 * Returns the name of the last clicked Element in the currently active menu
	 * 
	 * @return
	 */
	public String lastClickedElement() {

		if (this.lastClickedElementIndex != -1) {
			// System.out.println("CLICKED: "
			// +this.menuObjects.get(lastClickedElementIndex).getName());
			return this.guiObjects.get(lastClickedElementIndex).getName();
		}
		return null;
	}

	/**
	 * 
	 * @return The last clicked GuiObject instances.
	 */
	public GuiObject lastClickedElementInstace() {

		if (this.lastClickedElementIndex != -1) {
			// System.out.println("CLICKED: "
			// +this.menuObjects.get(lastClickedElementIndex).getName());
			return this.guiObjects.get(lastClickedElementIndex);
		}
		return null;
	}

	// public String selectedChild(){
	// return this.guiObjects.get(lastClickedElementIndex).getSelectedChild();
	// }
	public String getSelectedChild(String parentName) {
		GuiObject temp = getElement(parentName);
		if (temp != null) {
			return temp.getSelectedChild();
		}
		return null;
	}

	/**
	 * If there exists a GuiObject with the name "name", the element is
	 * returned. If there is no such GuiObject, null is returned.
	 * 
	 * @param name
	 *            The name of the desired GuiObject.
	 * @return
	 */
	private GuiObject getElement(String name) {
		for (int i = 0; i < this.guiObjects.size(); i++) {
			if (guiObjects.get(i).getName().equals(name)) {
				return guiObjects.get(i);
			}
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
