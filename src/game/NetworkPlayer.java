package game;

public class NetworkPlayer extends Player {
	private int moveX, moveY;
	private boolean newMove;

	public NetworkPlayer(String theName, int theID) {
		super(theName);

	}

	public void setMove(int x, int y) {
		moveX = x;
		moveY = y;
		newMove = true;
	}

	@Override
	public int[] determineMove(Board b) {
		if (newMove) {
			if (board.tryMove(moveX, moveY, getID())) {
				int[] move = {moveX, moveY};
				return move;
			}
		}

		return null;
	}

	@Override
	public boolean hasMove() {
		// TODO Auto-generated method stub
		return newMove;
	}

}
