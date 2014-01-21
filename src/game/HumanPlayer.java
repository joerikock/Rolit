package game;

public class HumanPlayer extends Player{
	BoardGUI gui;
	public HumanPlayer(String name, int color, BoardGUI gui){
		super(name, color);
		this.gui = gui;
	}
	@Override
	public boolean hasMove(){
		return gui.hasSelectedField();
	}
	@Override
	public int[] determineMove(Board b) {

		if(gui.hasSelectedField()){
//			System.out.println("HUMANPLAYER has selected field");
			return gui.getSelectedField();
		}
		// TODO Auto-generated method stub
		return null;
	}

}
