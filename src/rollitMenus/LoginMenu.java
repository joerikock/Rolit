package rollitMenus;

import menuItems.Button;
import menuItems.Menu;
import menuItems.MenuManager;
import menuItems.TextInputField;

public class LoginMenu extends Menu {
	TextInputField password, username;

	public LoginMenu(MenuManager m) {
		super("Login");
		super.setManager(m);
		Button b = new Button("Login", 325, 200);
		Button cancel = new Button("Cancel", 425, 200);
		password = new TextInputField("Password", 350, 300);
		password.setTextVisibility(false);
		username = new TextInputField("Name", 350, 350);

		this.addGuiObject(b);
		this.addGuiObject(cancel);
		this.addGuiObject(username);
		this.addGuiObject(password);

	}

	public String getPassword() {
		return password.getInput();
	}

	public String getUser() {
		return username.getInput();
	}

}
