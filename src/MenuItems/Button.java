package MenuItems;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.me.mygdxgame.RollIt;

/**
 * Class for setting a button.
 * 
 * @author Max Messerich en Joeri Kock
 */

public class Button extends GuiObject {

	// Instance variables -------------------------------------------------------
	
	/**
	 * The text to be displayed on the button.
	 */
	private String text;

	/**
	 * Create a button.
	 * 
	 * @param text
	 * 			the text to be displayed on the button.
	 * @param x
	 * 			
	 * @param y
	 */
	public Button(String text,float x, float y) {
		super(text);
		this.text = text;
		this.heightProgress = 0;

		RollIt.FONT.getBounds(text);
		super.setDimensions(x, y, 100, 30);
	}

	public void batchDraw(SpriteBatch batch) {
		RollIt.FONT.setColor(1, 0, 0, alpha);
		RollIt.FONT.draw(batch, text, realX(), realY() + 20);
	}

	public boolean selected() {
		return false;
	}

	public String toString() {
		return new String("Button: " + text + "- - alpha: " + alpha);
	}

	@Override
	public void updateMembers() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getSelectedChild() {
		// TODO Auto-generated method stub
		return null;
	}

}
