package rolitmenus;

import menuitems.Button;
import menuitems.Menu;
import menuitems.MenuManager;
import menuitems.MultiChoiceButton;

public class NewGameMenu extends Menu {

	MultiChoiceButton hints;
	String[] names = new String[] {"Off", "On"};

	public NewGameMenu(MenuManager manager) {
		super("newGameMenu");

		super.setManager(manager);
		Button start = new Button("Start", 10, 10);
		Button back = new Button("Back", 200, 10);
		this.addGuiObject(start);
		this.addGuiObject(back);
		String[] choices = {"No Player", "Human Player", "Simple AI", "Smart AI"};
		MultiChoiceButton opponentChooser1 = new MultiChoiceButton("Player 1",
				choices, this, 50, 450);
		MultiChoiceButton opponentChooser2 = new MultiChoiceButton("Player 2",
				choices, this, 50, 400);
		MultiChoiceButton opponentChooser3 = new MultiChoiceButton("Player 3",
				choices, this, 50, 350);
		MultiChoiceButton opponentChooser4 = new MultiChoiceButton("Player 4",
				choices, this, 50, 300);

		this.addGuiObject(opponentChooser1);
		this.addGuiObject(opponentChooser2);
		this.addGuiObject(opponentChooser3);
		this.addGuiObject(opponentChooser4);

		this.hints = new MultiChoiceButton("Show hints", names, this, 50, 150);
		this.addGuiObject(hints);
	}
}
