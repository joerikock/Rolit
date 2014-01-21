package rollitMenus;

import MenuItems.Button;
import MenuItems.Menu;
import MenuItems.MenuManager;

public class MainMenu extends Menu {

	public MainMenu(MenuManager menu) {
		super("Main");
		super.setManager(menu);
		Button start = new Button("New Game", 0, 100, 00);
		Button settings = new Button("Settings", 1, 300, 00);
		Button profile = new Button("Profile", 2, 500, 00);
		Button exit = new Button("Exit", 3, 700, 00);
		this.addGuiObject(start);
		this.addGuiObject(settings);
		this.addGuiObject(profile);
		this.addGuiObject(exit);
	}

}
