package rollitMenus;

import menuItems.Button;
import menuItems.Menu;
import menuItems.MenuManager;

public class MainMenu extends Menu {

	public MainMenu(MenuManager menu) {
		super("Main");
		super.setManager(menu);
		Button start = new Button("New Game", 100, 300);
		Button onlineGame = new Button("Play Online", 400, 300);
		Button exit = new Button("Exit", 700, 300);
		this.addGuiObject(start);
		this.addGuiObject(exit);
		this.addGuiObject(onlineGame);
	}
}
