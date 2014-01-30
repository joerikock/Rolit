package menuitems;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.me.mygdxgame.RollIt;

/**
 * Class for setting a button.
 * 
 * @author Max Messerich en Joeri Kock
 */

public class Button extends GuiObject {

	// Instance variables
	// -------------------------------------------------------

	/**
	 * The text to be displayed on the button.
	 */
	private String text;

	/**
	 * Create a button.
	 * 
	 * @param text
	 *            the text to be displayed on the button.
	 * @param x
	 * 
	 * @param y
	 */
	public Button(String text, float x, float y) {
		super(text);
		this.text = text;
		this.heightProgress = 0;

		RollIt.font.getBounds(text);
		super.setDimensions(x, y, GuiObject.ELEMENT_WIDTH,
				GuiObject.ELEMENT_HEIGHT);
		super.setSelectAble(false);
	}

	@Override
	public void batchDraw(SpriteBatch batch) {

		RollIt.font.setColor(GuiObject.FONT_COLOR[0], GuiObject.FONT_COLOR[1],
				GuiObject.FONT_COLOR[2], alpha);
		RollIt.font.draw(batch, text, x + 10, y + 30);
	}

	@Override
	public String toString() {
		return new String("Button: " + text + "- - alpha: " + alpha);
	}

}
