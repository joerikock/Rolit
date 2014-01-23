package MenuItems;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.me.mygdxgame.RollIt;

public class MultiChoiceButton extends GuiObject implements ParentObject{
	private Button[] buttons;
	private Button selectedButton;
	private boolean newItem;
	public MultiChoiceButton(String text, String[] buttonNames, String selectedButtonName, Menu menu, float x, float y){
		super(text);
		//Set Text;
		super.setDimensions(x, y, 100, 30);
		//Add Buttons

		menu.addGuiObject(this);
		buttons = new Button[buttonNames.length];
		for(int i=0; i<buttonNames.length; i++){

			buttons[i] = new Button(buttonNames[i], x+100+i*100, y);
			if(i==0){
				buttons[i].setSelected(true);
			}
			menu.addChildGuiObjectTo(buttons[i], this);
			super.addChildren(buttons[i]);
			
		}
		this.selectedButton = buttons[1];
//		buttons[0].setSelected(true);
		
	
	}
	@Override
	public String getSelectedChild(){
		return this.selectedButton.getName();
	}
	/**
	 * Updated the Button of the "Choicebox".
	 */
	@Override
	public void updateMembers() {
//		GuiObject[] children = this.getChildren();
		this.newItem = false;
		for(int ind=0; ind<this.getChildren().size();ind++){
			if(this.getChildren().get(ind).clicked()){
				newItem = true;
				for(int i=0; i<this.getChildren().size(); i++){
					this.getChildren().get(i).setSelected(false);
				}
				this.selectedButton = (Button)(this.getChildren().get(ind));
				
			}
		}
		if(this.selectedButton!=null){
			selectedButton.setSelected(true);
		}
		
		
	}
	@Override
	public boolean newSelectedObject() {
		return newItem;
	}
	@Override
	public void batchDraw(SpriteBatch batch) {
		RollIt.FONT.setColor(FONT_COLOR[0], FONT_COLOR[1], FONT_COLOR[2], alpha);
		RollIt.FONT.draw(batch, this.name, realX(), realY() + 20);
	}

}
