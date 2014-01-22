package rollitMenus;

import MenuItems.Button;
import MenuItems.Menu;
import MenuItems.MenuManager;
import MenuItems.MultiChoiceButton;
import MenuItems.TextInputField;

public class LoginMenu extends Menu {
	TextInputField password, username;

	public LoginMenu(MenuManager m) {
		super("Login");
		super.setManager(m);
		Button b = new Button("Login", 200, 300);
		password = new TextInputField("Password", 200, 200);
		password.setTextVisibility(false);
		username = new TextInputField("Name", 200, 100);
		String[] buttonNames = {"Test", "Cool", "DADUM"};
		
		
		MultiChoiceButton test= new MultiChoiceButton("Test", buttonNames, "Cool", this, 300, 200);
		this.addGuiObject(b, false);
		this.addGuiObject(username, false);
		this.addGuiObject(password, false);
		this.addGuiObject(test, false);

	}

	public String getPassword() {
		return password.getInput();
	}

	public String getUser() {
		return username.getInput();
	}

}
