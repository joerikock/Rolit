package menuitems;

public class SuperMenu extends Menu{
	MessageBox box;
	TextOutputField message;
	Button okay;
	public SuperMenu() {
		super("super");
		box = new MessageBox("box", "HAllo");
		this.addGuiObject(box);
		message = new TextOutputField("text", 100,300);
		this.addGuiObject(message);
		okay = new Button("Okay", 350, 350);
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
