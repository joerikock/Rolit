package rollitMenus;

import menuItems.*;

public class IngameMenu extends Menu {
	Button back;

//	String[] names = new String[]{"Yes", "No"};

	public IngameMenu(MenuManager m) {
		super("Ingame");
		super.setManager(m);

		this.back = new Button("Back", 10, 10);
		this.addGuiObject(back);
	}
}
