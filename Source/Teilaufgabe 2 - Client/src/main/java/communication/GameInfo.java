package communication;

public final class GameInfo {
	
	private final String serverURL;
	private final String gameID;
	
	public GameInfo(String serverURL, String gameID) {
		this.serverURL = serverURL;
		this.gameID = gameID;
	}
	public String getServerURL() {
		return serverURL;
	}

	public String getGameID() {
		return gameID;
	}


}
