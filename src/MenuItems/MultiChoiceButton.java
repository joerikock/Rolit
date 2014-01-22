package MenuItems;

public class MultiChoiceButton extends GuiObject implements ParentObject{
	private Button[] buttons;
	private Button selectedButton;
	public MultiChoiceButton(String text, String[] buttonNames, String selectedButtonName, Menu menu, float x, float y){
		super(text);
		//Set Text;
		super.setDimensions(x, y, 100, 30);
		//Add Buttons

		menu.addGuiObject(this);
		buttons = new Button[buttonNames.length];
		for(int i=0; i<buttonNames.length; i++){
			buttons[i] = new Button(buttonNames[i], x+100+i*100, y);
			menu.addChildGuiObjectTo(buttons[i], this);
			super.addChildren(buttons[i]);
		}
		this.selectedButton = buttons[0];
		buttons[0].setSelected(true);
		
	
	}
	public String selectedButton(){
		return this.selectedButton.getName();
	}
	@Override
	public void updateMembers() {
//		GuiObject[] children = this.getChildren();
		for(int i=0; i<this.getChildren().size(); i++){
			this.getChildren().get(i).setSelected(false);
		}
		for(int i=0; i<this.getChildren().size();i++){
			if(this.getChildren().get(i).clicked()){
				this.selectedButton = (Button)(this.getChildren().get(i));
			}
		}
		selectedButton.setSelected(true);
		
	}

}
