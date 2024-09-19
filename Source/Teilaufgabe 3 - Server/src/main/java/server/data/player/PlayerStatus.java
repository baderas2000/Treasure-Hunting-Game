package server.data.player;

public class PlayerStatus  extends PlayerIdentifier{
	
	private PlayerInfo playerInfo;
	private EPlayerGameStatus playerGameState = EPlayerGameStatus.MustWait;
	private boolean visibleTreasure = false;
	private boolean collectedTreasure = false;
	private boolean mapHalfSent = false;

	public PlayerStatus (String playerID, PlayerInfo playerInfo) {
		super(playerID);
		this.playerInfo = playerInfo;
	}

	public EPlayerGameStatus getPlayerGameState () {
		return playerGameState;
	}
	
	public boolean isVisibleTreasure () {
		return visibleTreasure;
	}
	
	public boolean isCollectedTreasure () {
		return collectedTreasure;
	}
	
	public PlayerInfo getPlayerInfo () {
		return this.playerInfo;
	}

	public void setPlayerGameState (EPlayerGameStatus playerGameState) {
		this.playerGameState = playerGameState;
	}

	public void setVisibleTreasure (boolean visibleTreasure) {
		this.visibleTreasure = visibleTreasure;
	}

	public void setCollectedTreasure (boolean collectedTreasure) {
		this.collectedTreasure = collectedTreasure;
	}

	public boolean isMapHalfSent() {
		return mapHalfSent;
	}

	public void setMapHalfSent() {
		this.mapHalfSent = true;
	}

	@Override
	public boolean equals (Object o) {
		if (this == o)
			return true;
		if (o == null)
			return false;
		if (!(o instanceof PlayerStatus))
			return false;
		PlayerStatus playerState = (PlayerStatus) o;
		return super.equals(playerState) &&
			this.collectedTreasure == playerState.collectedTreasure &&
			this.visibleTreasure == playerState.visibleTreasure &&
			this.playerGameState == playerState.playerGameState;
	}
	
	@Override
	public int hashCode () {
		final int prime = 17;
		int result = 19;
		result = result * 10 + (this.collectedTreasure ? 1 : 0);
		result = result * 10 + (this.visibleTreasure ? 1 : 0);
		return result * prime + ((this.playerGameState == null) ? 0 : this.playerGameState.hashCode());
	}

}