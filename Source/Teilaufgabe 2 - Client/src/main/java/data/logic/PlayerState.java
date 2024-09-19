package data.logic;

public class PlayerState {

	private boolean treasureCollected;
	private EPlayerGameState playerGameState;
	
	public PlayerState(boolean treasureCollected, EPlayerGameState playerGameState) {
		this.treasureCollected = treasureCollected;
		this.playerGameState = playerGameState;
	}
	
	public boolean isTreasureCollected() {
		return treasureCollected;
	}
	
	public EPlayerGameState getPlayerGameState() {
		return playerGameState;
	}
		
	
}
