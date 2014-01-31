package menuitems;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class MessageBox extends GuiObject {

	public MessageBox(String name, String text) {
		super(name);
		super.setDimensions(280, 275, 350, 200);

	}

	@Override
	public void batchDraw(SpriteBatch batch) {
		
	}
	@Override
	public void drawOverLay(SpriteBatch batch) {
		
	}
	@Override
	public void shapesDraw(ShapeRenderer shapes) {
		shapes.setColor(1, 1, 1, alpha);
		shapes.rect(x, y, width, height);
	}
}
