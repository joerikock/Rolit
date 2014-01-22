package rollitMenus;

import MenuItems.Button;
import MenuItems.Menu;
import MenuItems.MenuManager;
import MenuItems.MultiChoiceButton;

public class NewGameMenu extends Menu {

	public NewGameMenu(MenuManager manager) {
		super("newGameMenu");
		super.setManager(manager);
		Button start = new Button("Start", 10, 10);
		this.addGuiObject(start);
		String[] choices = { "Human Player", "Simple AI" };
		MultiChoiceButton opponentChooser = new MultiChoiceButton("Opponet",
				choices, null, this, 50, 50);
		this.addGuiObject(opponentChooser);
	}

}
