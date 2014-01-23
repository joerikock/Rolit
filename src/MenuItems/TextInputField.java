package MenuItems;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.me.mygdxgame.RollIt;

public class TextInputField extends GuiObject {
	String string, displayString;
	String empty;
	private boolean textVisible;

	/**
	 * Creates a new TextInputField that can be used to get text input from the
	 * user. A starting value can be set to describe the purpose of the
	 * TextInputField (e.g "Username")
	 * 
	 * @param start
	 * @param x
	 * @param y
	 */
	public TextInputField(String start, float x, float y) {
		super(start);
		this.empty = start;
		this.string = "";
		this.displayString = "";
		this.textVisible = true;
		super.setDimensions(x, y, 150, 40);
	}

	public void setTextVisibility(boolean bool) {
		this.textVisible = bool;
	}

	public void updateKeys(char input) {
		// \0 = empty character
		// Only add keys if selected element
		if (input != '\0') {
			if (selected) {

				// remove the last char from the string if backspace is pressed

				if (input == '\b') {
					if (string.length() > 1) {
						string = string.substring(0, string.length() - 1);
					} else {
						string = "";
					}

				} else {
					string += input;
				}

			}
		}
	}

	public String getInput() {
		return this.string;
	}

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

	@Override
	public void batchDraw(SpriteBatch batch) {
		// TODO Auto-generated method stub
		// System.out.println("sad");

		// batch.begin();
		// RollIt.FONT.setScale(.8f);
		RollIt.FONT.setColor(1, 0, 0, alpha);
		// If the string is empty display standart text e.g "name" or "password"
		if (string.length() == 0) {
			RollIt.FONT.draw(batch, empty, x,
					y + height / 2 + RollIt.FONT.getXHeight());
		} else {
			// If it is not empty render the string itself or a string
			// representing the length of the string
			// if textVisible is set to false
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

			// AWESOME!!!!!
			// System.out.println(RollIt.FONT.computeVisibleGlyphs(displayString,
			// 0, displayString.length(), this.width));

			cutDisplayString();
			RollIt.FONT.draw(batch, displayString, x, y + height / 2
					+ RollIt.FONT.getCapHeight());
		}

		// batch.end();

	}

	@Override
	public void updateMembers() {
		// TODO Auto-generated method stub
		
	}

	

	@Override
	public boolean newSelectedObject() {
		// TODO Auto-generated method stub
		return false;
	}

}
