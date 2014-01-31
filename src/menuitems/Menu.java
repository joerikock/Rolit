package menuitems;

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
	/**
	 * ArrayList that saves the
	 */
	// private ArrayList<GuiObject> menuObjects;
	// private ArrayList<GuiObject> childrenObjects;
	private ArrayList<Integer> buttonIndexList, textInputFieldIndexList;
	private int lastClickedElementIndex;
	MenuManager manager;
	private String name;

	/**
	 * Creates a new Menu with button, textfields and god know what. The update
	 * function returns the id of the selected element
	 */
	public Menu(String name) {
		lastClickedElementIndex = -1;
		this.isParent = new HashMap<Integer, Boolean>();
		this.guiObjects = new ArrayList<GuiObject>();
		this.buttonIndexList = new ArrayList<Integer>();
		this.textInputFieldIndexList = new ArrayList<Integer>();
		this.name = name;
	}

	/**
	 * Sets the manager. Builds relationship between the menuManager and the
	 * menu.
	 * 
	 * @param manager
	 */
	public void setManager(MenuManager manager) {
		this.manager = manager;
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
		}
		if (obj instanceof TextInputField) {

			textInputFieldIndexList.add(guiObjects.size() - 1);
		}
	}

	/**
	 * Resets the menu to its unused state.
	 */
	public void activate() {
		lastClickedElementIndex = -1;
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

	/**
	 * Adds a new GuiObject to the menu and marks is as a child of another
	 * element.
	 * 
	 * @param obj
	 * @param parent
	 */
	public void addChildGuiObjectTo(GuiObject obj, GuiObject parent) {
		addInternal(obj);
		isParent.put(guiObjects.size() - 1, false);

	}

	/**
	 * Calls the shapesDraw function for all GuiObject of the menu.
	 * 
	 * @param shapes
	 */
	public void shapesDraw(ShapeRenderer shapes) {

		for (int i = 0; i < guiObjects.size(); i++) {
			// shapes.begin(ShapeType.Filled);
			guiObjects.get(i).shapesDraw(shapes);
			// shapes.end();
		}
	}

	/**
	 * Calls the batchDraw function for all GUIObejcts of the menu.
	 * 
	 * @param batch
	 */
	public void batchDraw(SpriteBatch batch) {

		for (int i = 0; i < guiObjects.size(); i++) {
			// System.out.println();
			guiObjects.get(i).batchDraw(batch);
			guiObjects.get(i).drawOverLay(batch);
		}
	}

	/**
	 * Sets the position of the menu relative to the down-left cornor.
	 * 
	 * @param x
	 * @param y
	 */
	public void setPosition(float x, float y) {
		for (int i = 0; i < guiObjects.size(); i++) {
			guiObjects.get(i).setParentPosition(x, y);
		}
	}

	/**
	 * Sets the alpha value of all GuiObjects.
	 * 
	 * @param a
	 */
	public void setAlpha(float a) {
		for (int i = 0; i < guiObjects.size(); i++) {
//			System.out.println(guiObjects.get(i).getName());
			guiObjects.get(i).setAlpha(a);
		}
	}

	/**
	 * Updates all GuiObjects and their children of the menu with userInput.
	 * Updates which elements have been selected.
	 * 
	 * @param x
	 * @param y
	 * @param mouseDown
	 * @param input
	 */
	public void update(float x, float y, boolean mouseDown, char input) {
		// Reset lastClickedElement
		this.lastClickedElementIndex = -1;
		// Update all guiObjects with their needed data
		// System.out.println("MENU UPDATE. SELECTED ITEMS: ");
		for (int i = 0; i < guiObjects.size(); i++) {
			guiObjects.get(i).update(x, y, mouseDown);

			if (guiObjects.get(i) instanceof TextInput) {
				TextInput t = (TextInput) guiObjects.get(i);
				t.updateKeys(input);
			}
			// If the currrent GuiObject x is of the menu itself, and is
			// clicked, the currently selected element
			// is x
			if (isParent.get(i)) {
				if (guiObjects.get(i).clicked()) {
					System.out
							.println("parent +" + guiObjects.get(i).getName());
					this.lastClickedElementIndex = i;
				}
			}
		}
	
	}

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

	/**
	 * Returns the name of the selected child of the GuiObject given as
	 * argument. Returns null if the the GuiObject does not exist or does not
	 * have children.
	 * 
	 * @param parentName
	 * @return
	 */
	public String getSelectedChild(String parentName) {
		GuiObject temp = getElement(parentName);
		if (temp != null) {
			// System.out.println(temp.getName()+", "+temp.getChildren().size());
			if (temp.getSelectedChild() != null) {
				return temp.getSelectedChild();
			}

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
	public GuiObject getElement(String name) {
		for (int i = 0; i < this.guiObjects.size(); i++) {
			if (guiObjects.get(i).getName().equals(name)) {
				return guiObjects.get(i);
			}
		}
		return null;
	}

	/**
	 * Return the name of the menu.
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

}
