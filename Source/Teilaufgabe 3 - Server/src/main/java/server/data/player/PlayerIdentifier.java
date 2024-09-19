package server.data.player;

public class PlayerIdentifier {
	
	private String playerIdentifier;
	
	public PlayerIdentifier (String playerIdentifier) {
		this.playerIdentifier = playerIdentifier;
	}
	
	public String getPlayerIdentifier () {
		return this.playerIdentifier;
	}
	
	@Override
	public boolean equals (Object o) {
		if (null == o)
			return false;
		if (this == o)
			return true;
		if (!(o instanceof PlayerIdentifier))
			return false;
		PlayerIdentifier playerID = (PlayerIdentifier) o;
		return this.playerIdentifier != null &&
			playerID.playerIdentifier != null &&
			this.playerIdentifier.equals(playerID.playerIdentifier) ||
			this.playerIdentifier == playerID.playerIdentifier;		
	}
	
	@Override
	public int hashCode () {
		return this.playerIdentifier.hashCode();
	}
}