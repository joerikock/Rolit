package game;

public abstract class Player {
	private String name;
	private int id;
	Board board;
	public Player(String name, int id){
		this.name = name;
		this.id = id;
	}
	public void setBoard(Board b){
		this.board = b;
	}
	public abstract int[] determineMove(Board b);
	public boolean hasMove(){
		return true;
	}
	public void makeMove(Board b){
		int[] move = determineMove(b);
		b.tryMove(move[0], move[1], id);
	}
}
