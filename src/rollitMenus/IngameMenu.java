package rollitMenus;

import MenuItems.*;

public class IngameMenu extends Menu {
	Button back;
	MultiChoiceButton hints;
	
	String[] names = new String[]{"Yes", "No"};

	public IngameMenu(MenuManager m) {
		super("Ingame");
		super.setManager(m);
		this.back = new Button("Back", 10, 10);
		this.addGuiObject(back);
		
		this.hints = new MultiChoiceButton("Show hints", names, this, 10, 150);
		this.addGuiObject(hints);
	}
}
