package rollitMenus;

import menuItems.Button;
import menuItems.Menu;
import menuItems.MenuManager;
import menuItems.TextInputField;

public class LoginMenu extends Menu {
	TextInputField username, portField, server;

	public LoginMenu(MenuManager m, int standartPort) {
		super("Login");
		super.setManager(m);
		Button b = new Button("Login", 325, 200);
		Button cancel = new Button("Cancel", 425, 200);
		username = new TextInputField("Name", 350, 350);
		server = new TextInputField("Server-IP", 350, 400);
		server.setStandartValue("localHost");

		portField = new TextInputField("Port", 500, 200);
		portField.setStandartValue(String.valueOf(standartPort));
		this.addGuiObject(b);
		this.addGuiObject(cancel);
		this.addGuiObject(username);
		this.addGuiObject(server);
		this.addGuiObject(portField);

		
	}
	public String getServerIp(){
		return server.getInput();
	}
	public String getPort() {
		return portField.getInput();
	}

	public String getUser() {
		return username.getInput();
	}

}
