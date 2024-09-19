package server.logic;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import messagesbase.UniquePlayerIdentifier;
import server.businessRules.IPlayerRule;
import server.businessRules.PlayerMaySendMapHalf;
import server.businessRules.PlayerSentMapHalfOnce;
import server.data.game.GameEntity;
import server.data.player.EPlayerGameStatus;
import server.data.player.PlayerIdentifier;
import server.data.player.PlayerInfo;
import server.data.player.PlayerStatus;
import server.exceptions.BusinessRuleViolationException;
import server.exceptions.GameIsFullException;
import server.exceptions.PlayerNotFoundException;

public class PlayerManager {
	
	final static Logger logger = LoggerFactory.getLogger(PlayerManager.class);
	private GameEntity currentGame;
	
	public PlayerManager (GameEntity game) {
		this.currentGame = game;
	}
	
	public PlayerStatus getSinglePlayer(String playerID) {
		return currentGame.getPlayers().stream()
				.filter(player -> player.getPlayerIdentifier().equals(playerID))
				.findAny()
				.orElseThrow(() -> new PlayerNotFoundException(
						"Player not found",
						"Player " + playerID + " is not registered in the Game."));
		}
	
	public PlayerStatus getEnemyPlayer(String playerID) {
		return currentGame.getPlayers().stream()
				.filter(player -> !player.getPlayerIdentifier().equals(playerID))
				.findFirst()
				.orElseThrow(() -> new PlayerNotFoundException(
						"Player not found",
						"Enemy of Player " + playerID + " is not registered in the Game."));
		}
	
	public PlayerIdentifier registerPlayer (PlayerInfo playerInfo) {
		if (currentGame.isFull()) {
			throw new GameIsFullException("Game is full",
				"Allowed number of players are already registered");
		}
		
		String playerID = UUID.randomUUID().toString();
		PlayerStatus newPlayer = new PlayerStatus(playerID, playerInfo);
		currentGame.addPlayer(newPlayer);
		PlayerStatus currentPlayer = this.getSinglePlayer(playerID);
		currentPlayer.setPlayerGameState(EPlayerGameStatus.MustWait);

		return currentPlayer;
	}
	
	public boolean checkPlayerBusinessRulesForHalfMapPost(PlayerStatus player) {
		List<IPlayerRule> rules = Arrays.asList(new PlayerSentMapHalfOnce(player), new PlayerMaySendMapHalf(player));
		boolean result;
		try {
			result = rules.stream().allMatch(IPlayerRule::enforce);
		} catch (BusinessRuleViolationException e) {
			result = false;
			throw new BusinessRuleViolationException("Player Business Rule Violation: " , e.getMessage());
		}
		return result;
    }
	
	public void setMustSendMapStatusRandomly() {
		if(currentGame.isFull()) {
    		Set<PlayerStatus> activePlayers = currentGame.getPlayers();
    		PlayerStatus randomPlayer = activePlayers.stream().skip((int) (activePlayers.size() * Math.random())).findFirst().orElse(null);
    		randomPlayer.setPlayerGameState(EPlayerGameStatus.MustSentMap);
		}
	}
	
	public void setPlayerVictory(PlayerStatus player, PlayerStatus enemy) {
		enemy.setPlayerGameState(EPlayerGameStatus.Won);
		player.setPlayerGameState(EPlayerGameStatus.Lost);
	}
	
	public void setMustSendMoveStatus(PlayerStatus player, PlayerStatus enemy) {
		player.setPlayerGameState(EPlayerGameStatus.MustWait);
    	player.setMapHalfSent();
    	if(enemy != null && enemy.isMapHalfSent()) {
    		enemy.setPlayerGameState(EPlayerGameStatus.MustMove);
    	}
    	else if(enemy != null && !(enemy.isMapHalfSent())) {
    		enemy.setPlayerGameState(EPlayerGameStatus.MustSentMap);
    	}
	}
	
	public PlayerStatus setFakeEnemyID(String playerID) {
    	PlayerStatus enemy = getEnemyPlayer(playerID);
    	PlayerStatus fakeEnemy = new PlayerStatus(UUID.randomUUID().toString(), enemy.getPlayerInfo());
    	return fakeEnemy;

	}
}
