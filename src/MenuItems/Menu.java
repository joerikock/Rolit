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
	private ArrayList<GuiObject> guiObjects;

	private HashMap<Integer, ArrayList<GuiObject>> indexManager;

	private HashMap<Integer, Integer> childIndexManager;
	private ArrayList<GuiObject> menuObjects;
	private ArrayList<GuiObject> childrenObjects;
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
		this.indexManager = new HashMap<Integer, ArrayList<GuiObject>>();

		this.childIndexManager = new HashMap<Integer,Integer>();

		this.guiObjects = new ArrayList<GuiObject>();
		this.menuObjects = new ArrayList<GuiObject>();
		this.childrenObjects = new ArrayList<GuiObject>();
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
	 * @param obj
	 */

	private void addInternal(GuiObject obj){
		guiObjects.add(obj);
		if (obj instanceof Button) {
			buttonIndexList.add(guiObjects.size()-1);

			System.out.println("Menu.addGuiObject: " + "Button added "
					+ obj.name);
		}
		if (obj instanceof TextInputField) {

			textInputFieldIndexList.add(guiObjects.size() - 1);
		}
	}
	/**
	 * Adds a new GuiObject to the menu.
	 * @param obj
	 */
	public void addGuiObject(GuiObject obj) {

		addInternal(obj);
		indexManager.put(guiObjects.size() - 1, menuObjects);

	}

	public void addChildGuiObjectTo(GuiObject obj, GuiObject parent) {
		addInternal(obj);
		indexManager.put(guiObjects.size() - 1, childrenObjects);
		//Find the index of the parent in indexManager
		for(int i=0; i<guiObjects.size();i++){
			if(guiObjects.get(i) == parent){
				System.out.println("Parent found at "+i);
				childIndexManager.put(guiObjects.size()-1, i);
			}

		}
			

		
	}
	public void addGuiObject(GuiObject obj, boolean isChild) {
		
		addInternal(obj);
		indexManager.put(guiObjects.size()-1,menuObjects);
		
	

		// add the index of the new element in menuObjects to the corresponding
		// ArrayList


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
		for (int i = 0; i < menuObjects.size(); i++) {
			menuObjects.get(i).setParentPosition(x, y);
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

		//Update all guiObjects with their needed data
		for (int i = 0; i < guiObjects.size(); i++) {
			
			guiObjects.get(i).update(x, y, mouseDown);
			guiObjects.get(i).updateMembers();
			if(guiObjects.get(i) instanceof TextInput){
				TextInput t = (TextInput)guiObjects.get(i);
				t.updateKeys(input);
			}
			//If the currrent GuiObject x is of the menu itself, and is clicked, the currently selected element
			//is x
			if(indexManager.get(i) == menuObjects){
				if (guiObjects.get(i).clicked()) {
					// System.out.println(i);
					this.lastClickedElementIndex = i;
				}
			}

			// If the current GuiObject x is a child object of GuiObject y, the
			// currently selected element is y
			if (indexManager.get(i) == childrenObjects) {

				if (guiObjects.get(i).clicked()) {
					
					this.lastClickedElementIndex = this.childIndexManager.get(i);

				}
			}

		}
		//
		// for (int i = 0; i < this.textInputFieldIndexList.size(); i++) {
		// TextInputField t = (TextInputField) (this.menuObjects
		// .get(textInputFieldIndexList.get(i)));
		// t.updateKeys(input);
		//
		// }

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
	public String selectedChild(){
		return this.guiObjects.get(lastClickedElementIndex).getSelectedChild();
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
