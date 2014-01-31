package menuitems;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class MenuManager {
	/**
	 * Defines the speed of the animation when switching between menus.
	 */
	public static final float ANIMATION_SPEED = .05f;
	/**
	 * Defines the maximum alpha value for all menus and their GuiObjects.
	 */
	public static final float MAX_ALPHA = .8f;
	/**
	 * ArrayList for menus managed by this manager.
	 */
	ArrayList<Menu> menus;
	/**
	 * Menu instance for MessageBox.
	 */
	private SuperMenu superMenu;
	/**
	 * Boolean deciding whether the messageBox is active or not-
	 */
	private boolean messageBoxActive;
	/**
	 * Index of the currently active menu in the ArrayList menus.
	 */
	private int currentMenuIndex;
	/**
	 * Index of the previously active menu. Used to blend the old menu out.
	 */
	private int lastMenuIndex;
	/**
	 * Alpha value for rendering.
	 */
	float alpha;
	/**
	 * Boolean preventing an update of the menus during animation.
	 */
	boolean animationDone;

	/**
	 * Manages multiple Menus. Responsible for switching between menus and
	 * updating the selected one.
	 */
	public MenuManager() {
		this.currentMenuIndex = -1;
		this.lastMenuIndex = -1;
		this.superMenu = new SuperMenu();	
		superMenu.setAlpha(.8f);
		this.menus = new ArrayList<Menu>();
	}

	/**
	 * Adds a menu to the manager and marks it as active.
	 * 
	 * @param menu
	 */
	public void addMenu(Menu menu) {
		this.menus.add(menu);
		this.currentMenuIndex = menus.size() - 1;
	}

	/**
	 * Switches to the given menu.
	 * 
	 * @param m
	 */
	public void setActiveMenu(Menu m) {
		for (int i = 0; i < this.menus.size(); i++) {
			if (menus.get(i) == m) {
				this.menus.get(currentMenuIndex).activate();
				lastMenuIndex = currentMenuIndex;
				this.currentMenuIndex = i;
				alpha = 0;
			}
		}
	}

	/**
	 * Updated the menus and their GuiObject with user input
	 * 
	 * @param x
	 *            mouse x
	 * @param y
	 *            mouse y
	 * @param mouseDown
	 * @param input
	 *            Last released key from keyboard
	 */
	public void closeMesssage(){
		this.messageBoxActive = false;
	}
	public void update(float x, float y, boolean mouseDown, char input) {
		// TODO Auto-generated method stub
		if (this.messageBoxActive) {
			superMenu.update(x, y, mouseDown, input);
			//Reset the currently active menu to prevent.
			//Otherwise the lastClicked element will still be set and
			//report it has been clicked.
			if(currentMenuIndex >= 0){
				menus.get(currentMenuIndex).activate();
			}
			
			
		} else {
			if (menus.size() > 0 && currentMenuIndex >= 0) {
				menus.get(currentMenuIndex).update(x, y, mouseDown, input);
			}
			this.updateAnimation();
		}

	}

	/**
	 * Returns true if the manager is not in a transition between menus.
	 * 
	 * @return
	 */
	public boolean animationCompleted() {
		return animationDone;
	}

	/**
	 * Calls the shapesDraw function for the menus.
	 * 
	 * @param shapes
	 */
	private void updateAnimation() {
		animationDone = true;
		if (menus.size() > 0 && currentMenuIndex >= 0) {

			if (alpha < MAX_ALPHA) {
				animationDone = false;
				alpha += ANIMATION_SPEED;
			} else {
				alpha = MAX_ALPHA;
			}

			if (lastMenuIndex != -1) {

				menus.get(lastMenuIndex).setAlpha(MAX_ALPHA - alpha);

			}
		}
		menus.get(currentMenuIndex).setAlpha(alpha);
	}

	public void openMessageBox(String message, boolean showDecline) {
		this.messageBoxActive = true;
		superMenu.setMessage(message,showDecline);
	}
	public boolean messageBoxOpen(){
		return this.messageBoxActive;
	}
	public int messageBoxChoice(){
		if(messageBoxActive){
			if(superMenu.declineClicked()){
	
				return -1;
			}
			if(superMenu.okayClicked()){
	
				return 1;
			}
		}
		return 0;
	}
	public void shapesDraw(ShapeRenderer shapes) {
		if (this.messageBoxActive) {
			superMenu.shapesDraw(shapes);
			
		} else {
			if (!animationDone && lastMenuIndex != -1) {
				menus.get(lastMenuIndex).shapesDraw(shapes);
			}
			menus.get(currentMenuIndex).setAlpha(alpha);
			menus.get(currentMenuIndex).shapesDraw(shapes);
		}
	}

	/**
	 * Draw the fonts of the menus.
	 * 
	 * @param batch
	 */
	public void batchDraw(SpriteBatch batch) {
		batch.begin();
		if (this.messageBoxActive) {
			superMenu.batchDraw(batch);
		} else {
			if (!animationDone) {
				if (lastMenuIndex != -1) {
					menus.get(lastMenuIndex).batchDraw(batch);
				}
			}
			menus.get(currentMenuIndex).batchDraw(batch);
			
		}
		batch.end();
	}

	/**
	 * Returns the instance of the currently active menu.
	 * 
	 * @return
	 */
	public Menu getActiveMenu() {
		return menus.get(currentMenuIndex);
	}

}
