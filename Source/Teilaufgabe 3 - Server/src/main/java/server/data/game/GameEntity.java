package server.data.game;

import java.util.Set;

import server.data.player.PlayerStatus;

public class GameEntity {
	private final static int GAME_CAPACITY = 2;
	private static final int MAX_DURATION_SECONDS = 600; // 10 minutes
    private static final int MAX_ACTIONS = 320;
    
	private GameIdentifier uniqueGameID;
	private GameStatus gameState;
	private long creationTime;
	private int numberOfActions;

	public GameEntity(GameIdentifier uniqueGameID) {
		this.uniqueGameID = uniqueGameID;
		this.gameState = new GameStatus();
		this.creationTime = System.currentTimeMillis();
		this.numberOfActions = 0;
	}
	
	public GameIdentifier getUniqueGameID() {
		return this.uniqueGameID;
	}
	
	public GameStatus getGameStatus() {
		return this.gameState;
	}
	
	public Set<PlayerStatus> getPlayers() {
		return this.gameState.getPlayers();
	}
	public int getNumberOfActions() {
		return numberOfActions;
	}

	public void setNumberOfActions(int numberOfActions) {
		this.numberOfActions = numberOfActions;
	}
	
	public boolean isFull() {
		return gameState.getPlayers().size() == GAME_CAPACITY;
	}
	
	public boolean isGameExpired() {
		long currentTime = System.currentTimeMillis();
		return (currentTime - creationTime) > (MAX_DURATION_SECONDS * 1000) || numberOfActions >= MAX_ACTIONS;
	}
	
	public void addPlayer(PlayerStatus newPlayer) {
		this.gameState.addPlayer(newPlayer);
		this.gameState.changeGameStateId();
	}
}
