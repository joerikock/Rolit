package rollitMenus;

import menuItems.Button;
import menuItems.Menu;
import menuItems.MenuManager;
import menuItems.TextInputField;

public class OnlineGameMenu extends Menu{
	TextInputField portField;
	public OnlineGameMenu(MenuManager manager){
		super("OnlineGameMenu");
		super.setManager(manager);
		TextInputField portField = new TextInputField("Port", 500,200);
		this.addGuiObject(portField);
		Button b = new Button("Connect", 200 ,200);
		this.addGuiObject(b);
		Button a = new Button("Back", 100,100);
		this.addGuiObject(a);
	}

}
