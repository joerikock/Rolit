package rollitMenus;

import menuItems.*;

public class IngameMenu extends Menu {
	Button back;
	TextOutputField score;
	TextOutputField red;
	TextOutputField yellow;
	TextOutputField green;
	TextOutputField blue;
	TextOutputField redScore;
	TextOutputField yellowScore;
	TextOutputField greenScore;
	TextOutputField blueScore;

	public IngameMenu(MenuManager m) {
		super("Ingame");
		super.setManager(m);

		this.back = new Button("Back", 10, 10);
		this.addGuiObject(back);
		
		this.score = new TextOutputField("score", 210, 400);
		score.setText("Score");
		score.setColor(1, 1, 1);
		this.addGuiObject(score);
		
		this.red = new TextOutputField("red", 150, 350);
		red.setText("Red");
		red.setColor(1, 0, 0);
		this.addGuiObject(red);
		
		this.yellow = new TextOutputField("yellow", 150, 300);
		yellow.setText("Yellow");
		yellow.setColor(1, 1, 0);
		this.addGuiObject(yellow);
		
		this.green = new TextOutputField("green", 150, 250);
		green.setText("Green");
		green.setColor(0, 1, 0);
		this.addGuiObject(green);
		
		this.blue = new TextOutputField("blue", 150, 200);
		blue.setText("Blue");
		blue.setColor(0, 0, 1);
		this.addGuiObject(blue);
		
		this.redScore = new TextOutputField("redScore", 250, 350);
		redScore.setText("1");
		redScore.setColor(1, 1, 1);
		this.addGuiObject(redScore);
		
		this.yellowScore = new TextOutputField("yellowScore", 250, 300);
		yellowScore.setText("1");
		yellowScore.setColor(1, 1, 1);
		this.addGuiObject(yellowScore);
		
		this.greenScore = new TextOutputField("greenScore", 250, 250);
		greenScore.setText("1");
		greenScore.setColor(1, 1, 1);
		this.addGuiObject(greenScore);
		
		this.blueScore = new TextOutputField("blueScore", 250, 200);
		blueScore.setText("1");
		blueScore.setColor(1, 1, 1);
		this.addGuiObject(blueScore);
	}
}
