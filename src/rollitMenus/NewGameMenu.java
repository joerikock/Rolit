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
		Button back = new Button("Back", 200, 10);
		this.addGuiObject(start);
		this.addGuiObject(back);
		String[] choices = { "No Player", "Human Player", "Simple AI", "Smart AI"};
		MultiChoiceButton opponentChooser1 = new MultiChoiceButton("Red",
				choices,this, 50, 250);
		MultiChoiceButton opponentChooser2 = new MultiChoiceButton("Blue",
				choices,this, 50, 200);
		MultiChoiceButton opponentChooser3 = new MultiChoiceButton("Green",
				choices, this, 50, 150);
		MultiChoiceButton opponentChooser4 = new MultiChoiceButton("Yellow",
				choices, this, 50, 100);
		
		this.addGuiObject(opponentChooser1);
		this.addGuiObject(opponentChooser2);
		this.addGuiObject(opponentChooser3);
		this.addGuiObject(opponentChooser4);


	}

}
