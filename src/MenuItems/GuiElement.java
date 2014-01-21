package MenuItems;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public interface GuiElement {
	public void update(float x, float y, boolean mouseDown);

	// public void draw(ShapeRenderer shapes, SpriteBatch batch);
	public void shapesDraw(ShapeRenderer shapes);

	public void batchDraw(SpriteBatch batch);

	public void drawExtras(ShapeRenderer shapes, SpriteBatch batch);

	public boolean focused();

	public boolean selected();

	public boolean clicked();
}
