package game;

import java.util.ArrayList;

public class DumbPlayer extends Player{
	public DumbPlayer(int color){
		super("DumbPlayer", color);
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	@Override
	public int[] determineMove(Board b) {
		ArrayList<int[]> possibleMoves = b.getValidMoveList();
		int choice = Tools.randomInt(0, possibleMoves.size()-1);
		if(choice==possibleMoves.size()){
			return possibleMoves.get(choice-1);
		}
		return possibleMoves.get(choice);
//		this.makeMove(possibleMoves.get(choice)[0],possibleMoves.get(choice)[1], color);
		
	}

}
