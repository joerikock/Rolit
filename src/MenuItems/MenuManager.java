package MenuItems;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class MenuManager {
	public static final int animationType = 1;
	public static final float ANIMATION_SPEED = 10f;
	public static final float MAX_ALPHA = .4f;
	ArrayList<Menu> menus;
	private int currentMenuIndex, lastMenuIndex;

	float animationX, alpha;
	int animationDir;
	boolean animationDone;

	/**
	 * Manages multiple Menus. Responsible for switching between menus and
	 * updating the selected one.
	 */
	public MenuManager() {
		this.currentMenuIndex = -1;
		this.lastMenuIndex = -1;
		this.menus = new ArrayList<Menu>();
	}

	public void addMenu(Menu menu) {
		this.menus.add(menu);
		this.currentMenuIndex = menus.size() - 1;
	}

	/**
	 * sets the current menu to the previous one (atm: "previous": added to
	 * menus before this one) If the current menu is the first in the list, the
	 * new menu will be the last in the list 0-------- -> --------0 (0 = current
	 * menu)
	 */
	public void previousMenu() {
		this.lastMenuIndex = currentMenuIndex;
		this.currentMenuIndex--;
		alpha = 0;
		animationX = -Gdx.graphics.getWidth();
		animationDir = 1;
		if (this.currentMenuIndex < 0) {
			this.currentMenuIndex = menus.size() - 1;
		}
		alpha = 0;
	}

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
	 * sets the current menu to the previous one (atm: "next": added to menus
	 * after this one) If the current menu is the last in the list, the new menu
	 * will be the first in the list --------0 -> 0-------- (0 = current menu)
	 */
	public void nextMenu() {
		this.lastMenuIndex = currentMenuIndex;
		this.currentMenuIndex++;
		animationX = Gdx.graphics.getWidth();
		animationDir = -1;
		if (this.currentMenuIndex > menus.size() - 1) {
			this.currentMenuIndex = 0;
		}
		alpha = 0;
	}

	public void update(float x, float y, boolean mouseDown, char input) {
		// TODO Auto-generated method stub

		if (menus.size() > 0 && currentMenuIndex >= 0) {
			menus.get(currentMenuIndex).update(x, y, mouseDown, input);
			// menus.get(currentMenuIndex).action();
		}

	}

	public boolean animationCompleted() {
		return animationDone;
	}

	public void shapesDraw(ShapeRenderer shapes) {
		
		if (menus.size() > 0 && currentMenuIndex >= 0) {
			animationDone = true;
			if (MenuManager.animationType == 0) {
				
				if (animationX != 0) {
					animationDone = false;
					if (animationDir > 0) {
						animationX += animationDir
								* MenuManager.ANIMATION_SPEED;
						if (animationX > 0) {
							animationX = 0;
						}
					} else {
						animationX += animationDir
								* MenuManager.ANIMATION_SPEED;
						if (animationX < 0) {
							animationX = 0;
						}
					}

				}
				menus.get(currentMenuIndex).setPosition(animationX, 0);
			}
			if (MenuManager.animationType == 1) {
				if (alpha < MAX_ALPHA) {
					animationDone = false;
					alpha += 0.03f;
					menus.get(currentMenuIndex).setAlpha(alpha);

					if (lastMenuIndex != -1) {

						menus.get(lastMenuIndex).setAlpha(MAX_ALPHA - alpha);
						menus.get(lastMenuIndex).shapesDraw(shapes);
					}

				}
			}
			// System.out.println(lastMenuIndex+", "+currentMenuIndex);
			menus.get(currentMenuIndex).setAlpha(alpha);
			menus.get(currentMenuIndex).shapesDraw(shapes);
		}

	}

	public void batchDraw(SpriteBatch batch) {
		batch.begin();
		menus.get(currentMenuIndex).batchDraw(batch);
		batch.end();
	}

	public Menu getActiveMenu() {
		return menus.get(currentMenuIndex);
	}

	public void draw(ShapeRenderer shapes, SpriteBatch batch) {
		// TODO Auto-generated method stub
		if (menus.size() > 0 && currentMenuIndex >= 0) {
			animationDone = true;
			// if (MenuManager.animationType == 0) {
			// if (animationX != 0) {
			// animationDone = false;
			// if (animationDir > 0) {
			// animationX += animationDir
			// * MenuManager.ANIMATION_SPEED;
			// if (animationX > 0) {
			// animationX = 0;
			// }
			// } else {
			// animationX += animationDir
			// * MenuManager.ANIMATION_SPEED;
			// if (animationX < 0) {
			// animationX = 0;
			// }
			// }
			//
			// }
			// menus.get(currentMenuIndex).setPosition(animationX, 0);
			// }
			// if (MenuManager.animationType == 1) {
			// if (alpha < MAX_ALPHA) {
			// animationDone = false;
			// alpha += 0.01f;
			// menus.get(currentMenuIndex).setAlpha(alpha);
			// }
			// }
			// System.out.println("::::");
			menus.get(currentMenuIndex).setAlpha(.2f);
			menus.get(currentMenuIndex).draw(shapes, batch);
		}
	}

	public void print() {
		System.out
				.println("##################### PRINTING MENU MANANGER #######################");
		for (int i = 0; i < this.menus.size(); i++) {
			System.out.println();
			System.out.println("MENU " + i);
			menus.get(i).print();
		}
		System.out.println("############################################");

	}

}
