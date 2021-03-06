package rolitmenus;

import menuitems.Button;
import menuitems.Menu;
import menuitems.MenuManager;
import menuitems.MultiChoiceButton;
import menuitems.TextInputField;

public class OnlineGameMenu extends Menu {
	TextInputField portField;

	public OnlineGameMenu(MenuManager manager) {
		super("OnlineGameMenu");
		super.setManager(manager);

		
		String[] choices = {"1", "2", "3"};
		MultiChoiceButton opponentChooser = new MultiChoiceButton(
				"Opponents", choices, this, 50, 450);
		
		Button b = new Button("Connect", 200, 200);
		
		Button a = new Button("Back", 100, 100);
		this.addGuiObject(a);
		this.addGuiObject(opponentChooser);
		this.addGuiObject(b);
	}

}
