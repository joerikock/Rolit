package menuitems;

public class SuperMenu extends Menu{
	MessageBox box;
	TextOutputField message;
	Button okay, decline;
	public SuperMenu() {
		super("super");
		box = new MessageBox("box", "HAllo");
		this.addGuiObject(box);
		message = new TextOutputField("text", 320,390);
		message.setColor(0, 0, 0);
		this.addGuiObject(message);
		okay = new Button("Okay", 400, 300);
		this.addGuiObject(okay);
		decline = new Button("Decline", 500,300);
		this.addGuiObject(decline);
		// TODO Auto-generated constructor stub
	}
	public void setMessage(String text, boolean showDecline){
		this.activate();
		message.setText(text);
		decline.setRender(showDecline);
		}
	public boolean okayClicked(){
		return (this.lastClickedElement()!=null&&this.lastClickedElement().equals("Okay"));	}
	public boolean declineClicked(){
		
		return (this.lastClickedElement()!=null&&this.lastClickedElement().equals("Decline"));
	}

}
