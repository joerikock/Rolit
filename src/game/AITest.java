package game;

public class AITest {
	Board b;

	public static void main(String[] args) {
		Player p1 = new DumbPlayer(0);
		Player p2 = new DumbPlayer(1);
		Player[] players = { p1, p2 };
		Board b = new Board(players);
		b.tryMove(3, 3, 0);
		b.tryMove(3, 4, 1);
		b.tryMove(4, 4, 2);
		b.tryMove(4, 5, 3);
		b.print();
		while (!b.finished()) {
//			System.out.println("-----");
			if (b.currentPlayer() == 0) {
				p1.determineMove(b);
//				System.out.println(0);
			} else {
				if (b.currentPlayer() == 1) {
					p2.determineMove(b);
//					System.out.println(1);
				}
			}

			// b.update();
			// b.print();
		}

		b.print();
		System.out.println("WINNER IS " + b.getWinner());
	}

}
