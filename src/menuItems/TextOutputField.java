package menuItems;

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
		this.string = text;
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
	 * Function for setting the colour of the text.
	 */
	public void setColor(float r, float g, float b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}

	/**
	 * Prevents the rendered String from being longer than the TextInputField
	 * itself.
	 */
	private void cutDisplayString() {
		// checks whether the String can be displayed in the box. if not, the
		// String is substring so it fits.
		int maxChars = RollIt.FONT.computeVisibleGlyphs(displayString, 0,
				displayString.length(), this.width);
		if (maxChars < this.displayString.length()) {
			this.displayString = displayString.substring(maxChars,
					displayString.length() - 1);
		}
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
		RollIt.FONT.setColor(r, g, b, alpha);
		if (string.length() == 0) {
			RollIt.FONT.draw(batch, empty, x,
					y + height / 2 + RollIt.FONT.getXHeight());
		} else {
			if (!this.textVisible) {
				if (this.displayString.length() != this.string.length()) {
					this.displayString = "";
					for (int i = 0; i < string.length(); i++) {
						displayString += "*";
					}
				}
			} else {
				this.displayString = string;
			}
			cutDisplayString();
			RollIt.FONT.draw(batch, displayString, x, y + height / 2
					+ RollIt.FONT.getCapHeight());
		}
	}
}
