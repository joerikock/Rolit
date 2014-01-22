package rollitMenus;

import MenuItems.Button;
import MenuItems.Menu;

public class NewGameMenu extends Menu{
	
	public NewGameMenu() {
		super("newGameMenu");
		Button start = new Button("Start", 10,10);
		this.addGuiObject(start);
	}

}
