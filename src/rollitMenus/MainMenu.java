package rollitMenus;

import menuItems.Button;
import menuItems.Menu;
import menuItems.MenuManager;

public class MainMenu extends Menu {

	public MainMenu(MenuManager menu) {
		super("Main");
		super.setManager(menu);
		Button start = new Button("New Game", 0, 00);
		Button onlineGame = new Button("Play Online", 150, 00);
		Button settings = new Button("Settings", 300, 00);
		Button profile = new Button("Profile", 450, 00);
		Button exit = new Button("Exit", 600, 00);
		this.addGuiObject(start);
		this.addGuiObject(settings);
		this.addGuiObject(profile);
		this.addGuiObject(exit);
		this.addGuiObject(onlineGame);
	}
}
