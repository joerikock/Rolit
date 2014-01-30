package menuitems;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class MessageBox extends GuiObject{

	public MessageBox(String name, String text){
		super(name);
		super.setDimensions(275, 275, 200, 200);

	}

	@Override
	public void batchDraw(SpriteBatch batch) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void shapesDraw(ShapeRenderer shapes){
		shapes.setColor(1, 1,1, .2f);
		shapes.rect(x, y, width, height);
	}
}
