package rolitmenus;

import menuItems.*;

public class IngameMenu extends Menu {
	Button back;
	TextOutputField score, p1, p2, p3, p4, p1Score, p2Score, p3Score, p4Score, winner;

	public IngameMenu(MenuManager m) {
		super("Ingame");
		super.setManager(m);

		this.back = new Button("Back", 10, 10);
		this.addGuiObject(back);

		this.score = new TextOutputField("score", 210, 400);
		score.setText("Score");
		score.setColor(1, 1, 1);
		this.addGuiObject(score);

		this.p1 = new TextOutputField("p1", 150, 350);
		p1.setText("");
		p1.setColor(1, 0, 0);
		this.addGuiObject(p1);

		this.p2 = new TextOutputField("p2", 150, 300);
		p2.setText("");
		p2.setColor(0, 1, 0);
		this.addGuiObject(p2);

		this.p3 = new TextOutputField("p3", 150, 250);
		p3.setText("");
		p3.setColor(0, 0, 1);
		this.addGuiObject(p3);

		this.p4 = new TextOutputField("p4", 150, 200);
		p4.setText("");
		p4.setColor(1, 1, 0);
		this.addGuiObject(p4);

		this.p1Score = new TextOutputField("p1Score", 250, 350);
		p1Score.setText("");
		p1Score.setColor(1, 1, 1);
		this.addGuiObject(p1Score);

		this.p2Score = new TextOutputField("p2Score", 250, 300);
		p2Score.setText("");
		p2Score.setColor(1, 1, 1);
		this.addGuiObject(p2Score);

		this.p3Score = new TextOutputField("p3Score", 250, 250);
		p3Score.setText("");
		p3Score.setColor(1, 1, 1);
		this.addGuiObject(p3Score);

		this.p4Score = new TextOutputField("p4Score", 250, 200);
		p4Score.setText("");
		p4Score.setColor(1, 1, 1);
		this.addGuiObject(p4Score);

		this.winner = new TextOutputField("winner", 150, 100);
		winner.setText("");
		winner.setColor(1, 1, 1);
		this.addGuiObject(winner);
	}
	public void setPlayerName(String name, int i) {
		if (i == 0) {
			p1.setText(name);
		}
		if (i == 1) {
			p2.setText(name);
		}
		if (i == 2) {
			p3.setText(name);
		}
		if (i == 3) {
			p4.setText(name);
		}
	}
	public void setPlayerScore(int score, int i){
		if (i == 0) {
			p1Score.setText(score + "");
		}
		if (i == 1) {
			p2Score.setText(score + "");
		}
		if (i == 2) {
			p3Score.setText(score + "");
		}
		if (i == 3) {
			p4Score.setText(score + "");
		}
	}
}
