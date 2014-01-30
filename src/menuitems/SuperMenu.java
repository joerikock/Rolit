package menuitems;

public class SuperMenu extends Menu{
	MessageBox box;
	TextOutputField message;
	Button okay;
	public SuperMenu() {
		super("super");
		box = new MessageBox("box", "HAllo");
		this.addGuiObject(box);
		message = new TextOutputField("text", 320,390);
		message.setColor(0, 0, 0);
		this.addGuiObject(message);
		okay = new Button("Okay", 400, 300);
		this.addGuiObject(okay);
		// TODO Auto-generated constructor stub
	}
	public void setMessage(String text){
		message.setText(text);
	}
	public boolean okayClicked(){
		return okay.clicked();
	}


}
