package rollitMenus;

import MenuItems.*;

public class IngameMenu extends Menu {
	Button back;
	Button hints;
	Button noHints;

//	String[] names = new String[]{"Yes", "No"};

	public IngameMenu(MenuManager m) {
		super("Ingame");
		super.setManager(m);

		this.back = new Button("Back", 10, 10);
		this.addGuiObject(back);

		this.hints = new Button("Show hints", 10, 150);
		this.addGuiObject(hints);
		
		this.noHints = new Button("No hints", 120, 150);
		this.addGuiObject(noHints);
	}
}
