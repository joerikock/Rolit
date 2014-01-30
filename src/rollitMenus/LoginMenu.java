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
		Button b = new Button("Login", 200, 300);
		Button cancel = new Button("Cancel", 300,300);
		password = new TextInputField("Password", 200, 200);
		password.setTextVisibility(false);
		username = new TextInputField("Name", 200, 100);

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
