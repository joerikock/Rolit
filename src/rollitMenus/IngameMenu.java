package rollitMenus;

import MenuItems.Button;
import MenuItems.Menu;
import MenuItems.MenuManager;

public class IngameMenu extends Menu {
	Button back;

	public IngameMenu(MenuManager m) {
		super("Ingame");
		super.setManager(m);
		this.back = new Button("Back", 0, 10, 10);
		this.addGuiObject(back);
	}
}
