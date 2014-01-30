package menuItems;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.me.mygdxgame.RollIt;

public class MultiChoiceButton extends GuiObject {
	private Button[] buttons;

	/**
	 * --> RadioButton.
	 * 
	 * @param text
	 *            Name of the element
	 * @param buttonNames
	 *            Array of Strings that should be added as options to the
	 *            RadioButton in form of Buttons.
	 * @param menu
	 *            Parent menu
	 * @param x
	 *            position
	 * @param y
	 *            position
	 */
	public MultiChoiceButton(String text, String[] buttonNames, Menu menu,
			float x, float y) {
		super(text);
		// Set Text;
		super.setDimensions(x, y, GuiObject.ELEMENT_WIDTH,
				GuiObject.ELEMENT_HEIGHT);
		super.setClickAble(false);
		// Add Buttons
		menu.addGuiObject(this);
		buttons = new Button[buttonNames.length];
		for (int i = 0; i < buttonNames.length; i++) {

			buttons[i] = new Button(buttonNames[i], x + width + i * width, y);
			buttons[i].setParentObject(this);
			buttons[i].setSelectAble(true);
			if (i == 0) {
				buttons[i].setSelected(true);
			}
			menu.addChildGuiObjectTo(buttons[i], this);
		}
	}

	public void batchDraw(SpriteBatch batch) {
		RollIt.FONT
				.setColor(FONT_COLOR[0], FONT_COLOR[1], FONT_COLOR[2], alpha);
		RollIt.FONT.draw(batch, this.name, x + 10, y + 30);
	}

}
