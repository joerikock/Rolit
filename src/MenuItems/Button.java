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
		super.setDimensions(x, y, 100, 44);
		super.setSelectAble(false);
	}

	@Override
	public void batchDraw(SpriteBatch batch) {

		RollIt.FONT.setColor(GuiObject.FONT_COLOR[0],GuiObject.FONT_COLOR[1],GuiObject.FONT_COLOR[2],alpha);
		RollIt.FONT.draw(batch, text, x, y + 20);
	}


	@Override
	public String toString() {
		return new String("Button: " + text + "- - alpha: " + alpha);
	}

}
