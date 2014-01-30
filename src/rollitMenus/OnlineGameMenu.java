package rollitMenus;

import menuItems.Button;
import menuItems.Menu;
import menuItems.MenuManager;
import menuItems.MultiChoiceButton;
import menuItems.TextInputField;

public class OnlineGameMenu extends Menu {
	TextInputField portField;

	public OnlineGameMenu(MenuManager manager, int standartPort) {
		super("OnlineGameMenu");
		super.setManager(manager);
		TextInputField portField = new TextInputField("Port", 500, 200);
		portField.setStandartValue(String.valueOf(standartPort));
		this.addGuiObject(portField);
		String[] choices = { "1", "2", "3"};
		MultiChoiceButton opponentChooser1 = new MultiChoiceButton("Opponents",choices, this, 50, 450);
		Button b = new Button("Connect", 200, 200);
		this.addGuiObject(b);
		Button a = new Button("Back", 100, 100);
		this.addGuiObject(a);
	}

}
