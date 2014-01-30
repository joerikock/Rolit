package menuitems;

import java.awt.Color;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.me.mygdxgame.RollIt;

public class TextOutputField extends GuiObject {
	String string, displayString;
	String empty;
	private boolean textVisible;
	private float r, g, b;

	/**
	 * Creates a new TextInputField that can be used to get text input from the
	 * user. A starting value can be set to describe the purpose of the
	 * TextInputField (e.g "Username")
	 * 
	 * @param start
	 * @param x
	 * @param y
	 */
	public TextOutputField(String start, float x, float y) {
		super(start);
		this.empty = start;
		this.string = "";
		this.displayString = "";
		this.textVisible = true;
		super.setDimensions(x, y, 150, 40);

	}

	public void setText(String text) {
		this.displayString = text;
	}

	/**
	 * Updates the TextInputField with characters.
	 */

	/**
	 * Returns what the user has written.
	 * 
	 * @return
	 */
	public String getInput() {
		return this.string;
	}

	/**
	 * Function for setting the color of the text.
	 */
	public void setColor(float r, float g, float b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}

	/**
	 * Draws the String.
	 */
	@Override
	public void drawOverLay(SpriteBatch batch) {
	}

	@Override
	public void shapesDraw(ShapeRenderer shapes) {
	}

	public void batchDraw(SpriteBatch batch) {
		RollIt.font.setColor(r, g, b, alpha);
			RollIt.font.draw(batch, displayString, x, y + height / 2
					+ RollIt.font.getCapHeight());
		
	}
}
