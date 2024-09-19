package data.logic;

import data.map.FullMap;

public class GameState {

	private FullMap map;
	private PlayerState playerState;
	
	public GameState(FullMap map, PlayerState playerState) {
		this.map = map;
		this.playerState = playerState;
	}

	public FullMap getMap() {
		return map;
	}

	public PlayerState getPlayerState() {
		return playerState;
	}
	
}
