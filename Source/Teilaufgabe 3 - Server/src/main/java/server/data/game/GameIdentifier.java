package server.data.game;

import java.util.Random;

public class GameIdentifier {

	private String gameID;
	
	public GameIdentifier() {
		this.gameID = getRandomGameID();	
	}
	
	public GameIdentifier (String gameID) {
		this.gameID = gameID;
	}
		
	private String getRandomGameID() {
        Random random = new Random();
		char[] id_arr = new char[5];
		char[] alphabet = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
		for (int i=0; i<5; i++) {
			id_arr[i] = alphabet[random.nextInt(26)];
		}
		return new String(id_arr);
	}
	
	public String getGameID () {
		return this.gameID;
	}
	
	@Override 
	public boolean equals (Object o) {
		if (null == o)
			return false;
		if (this == o)
			return true;
		if (!(o instanceof GameIdentifier))
			return false;
		GameIdentifier ID = (GameIdentifier) o;
		return this.gameID.equals(ID.gameID);
	}
}