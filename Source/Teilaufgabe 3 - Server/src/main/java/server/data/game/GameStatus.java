package server.data.game;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import server.data.map.FullMap;
import server.data.map.GameMap;
import server.data.player.PlayerIdentifier;
import server.data.player.PlayerStatus;
import server.exceptions.PlayerNotFoundException;

public class GameStatus {
	private String gameStateId;
	private FullMap map;
	private Set<PlayerStatus> players = new HashSet<PlayerStatus>(); 
	
	public GameStatus() {
		GameIdentifier randomId = new GameIdentifier();
		this.gameStateId = randomId.getGameID();
	}

	public FullMap getMap() {
		return this.map;
	}

	public String getGameStateId() {
		return this.gameStateId;
	}

	public Set<PlayerStatus> getPlayers() {
		return this.players;
	}

	public PlayerStatus getSinglePlayer(PlayerIdentifier playerID) {
		return this.players.stream()
				.filter(player -> player.getPlayerIdentifier() == playerID.getPlayerIdentifier())
				.findFirst()
				.orElseThrow(() -> new PlayerNotFoundException(
						"Player not found",
						"Player " + playerID.getPlayerIdentifier() + " is not registered in the Game."));
		}
	
	public void changeGameStateId() {
		this.gameStateId = UUID.randomUUID().toString();
	}
	
	public void setFullMap(GameMap mapHalf) {
		this.map = (FullMap) mapHalf;
	}
	
	public void addPlayer(PlayerStatus player) {
		this.players.add(player);
	}
}
