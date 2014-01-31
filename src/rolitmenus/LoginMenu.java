package rolitmenus;

import menuitems.Button;
import menuitems.Menu;
import menuitems.MenuManager;
import menuitems.TextInputField;
import menuitems.TextOutputField;

public class LoginMenu extends Menu {
	TextInputField username, portField, server;
	TextOutputField address, port, name;

	public LoginMenu(MenuManager m, int standartPort) {
		super("Login");
		super.setManager(m);
		Button b = new Button("Login", 325, 200);
		Button cancel = new Button("Cancel", 425, 200);
		username = new TextInputField("Name", 350, 350);
		server = new TextInputField("Server-IP", 350, 450);
		server.setStandartValue("localHost");
		
		this.address = new TextOutputField("address", 250, 450);
		address.setColor(1, 1, 1);
		address.setText("IP-Address:");
		
		this.port = new TextOutputField("port", 250, 400);
		port.setColor(1, 1, 1);
		port.setText("Port:");
		
		this.name = new TextOutputField("name", 250, 350);
		name.setColor(1, 1, 1);
		name.setText("Name:");

		portField = new TextInputField("Port", 350, 400);
		portField.setStandartValue(String.valueOf(standartPort));
		this.addGuiObject(b);
		this.addGuiObject(cancel);
		this.addGuiObject(username);
		this.addGuiObject(server);
		this.addGuiObject(portField);
		this.addGuiObject(address);
		this.addGuiObject(port);
		this.addGuiObject(name);
	}
	public String getServerIp() {
		return server.getInput();
	}
	public String getPort() {
		return portField.getInput();
	}

	public String getUser() {
		return username.getInput();
	}

}
