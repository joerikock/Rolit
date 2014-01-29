package multiplayer;

import game.Board;
import game.Player;

import java.util.ArrayList;
import java.util.Set;

public class RollitServer implements Runnable{
	private class Session implements Runnable{
		private ArrayList<String> playerNames;
		private ArrayList<Player> players;
		private Board board;
		boolean gameRunning;
		public int playerCount;
		public Session(int playerCount, String clientName){
			
			players = new ArrayList<Player>();
			playerNames = new ArrayList<String>();
			board = new Board();
			this.playerCount = playerCount;
			System.out.println("New session created by"+ clientName);
		}
		public int getPlayerCount(){
			return playerCount;
		}
		private ArrayList<String> getPlayers(){
			return playerNames;
		}
		public boolean isInGame(){
			return gameRunning;
		}
		public boolean join(String name){
			if(!gameRunning&&playerNames.size()<playerCount){
				playerNames.add(name);
				System.out.println(name+" joined session. " +playerNames.size()+" | "+playerCount);
				return true;
			}
			return false;
		}
		public void makeMove(int x, int y, String player){
			for(int i=0; i<playerNames.size();i++){
				if(player.equals(playerNames.get(i))){
					if(board.currentPlayer().getName().equals(player)){
						if(!board.tryMove(x, y, board.currentPlayerColor())){
							//kick player
						}
					}
				}
			}
		}
		public void addPlayer(Player player){
			players.add(player);

			
		}
		public void startGame(){
			board.newGame(players);
			gameRunning = true;
		}
		@Override
		public void run() {
			
			if(gameRunning){
//				if(board.currentPlayer().hasMove())
			}
			
		}
		public Player currentPlayer(){
			return board.currentPlayer();
		}
	}
	private class SessionHandler{
		ArrayList<Session> sessions = new ArrayList<Session>();
		public SessionHandler(){
			this.sessions = new ArrayList<Session>();
		}
		private Session getPlayerSession(String name){
			for(int i=0; i<sessions.size();i++){
				for(int a=0; a<sessions.get(i).getPlayers().size();a++){
					if(sessions.get(i).getPlayers().get(a).equals(name)){
						return sessions.get(i);
					}
				}
			}
			return null;
		}
		/**
		 * Creates or finds a new game session for the client.
		 * @param playerCount
		 */
		public Session requestSession(int playerCount, String playerName){
			
			if(sessions.size()==0){
				Session session = new Session(playerCount, playerName);
				sessions.add(session);
				return session;
			}else{
				for(int i=0; i<sessions.size();i++){
					if(!sessions.get(i).isInGame()&&sessions.get(i).getPlayerCount()==playerCount){
						sessions.get(i).join(playerName);
						return sessions.get(i);
					}
				}
				Session session = new Session(playerCount, playerName);
				sessions.add(session);
				return session;
			}
		}
	}
	/*
	 * ArrayList containing active game sessions.
	 */
	private SessionHandler sessionHandler;
	private MultiSocket socketManager;
	public RollitServer(){
//		this.sessions = new ArrayList<Session>();
		this.sessionHandler = new SessionHandler();
		MultiSocket.init();
	}
	public void newSession(int playerCount){
		
	}
	@Override
	public void run() {
		while(true){
			Set<String> clients = MultiSocket.getClients();
			for(String c : clients){
				String[] s = MultiSocket.getClientMessage(c);
				Session session = sessionHandler.getPlayerSession(c);
				if(s[0].equals("join")){
					System.out.println("JOIN REQUEST");
					sessionHandler.requestSession(Integer.parseInt(s[1]), c);
				}
				if(s[0].equals("move")){
					session.makeMove(Integer.parseInt(s[1]), Integer.parseInt(s[2]), c);
				}
				
			}
		}

		
	}
	public static void main(String[] args){
		RollitServer server = new RollitServer();
		Thread serverThread = new Thread(server);
		serverThread.start();
		
	}

}
