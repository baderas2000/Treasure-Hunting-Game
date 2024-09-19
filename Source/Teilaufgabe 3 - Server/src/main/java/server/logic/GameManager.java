package server.logic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import server.data.game.GameEntity;
import server.data.game.GameIdentifier;
import server.exceptions.GameNotFoundException;

@Component
@Scope("singleton")
public class GameManager {
	private final static int MAX_GAME_NUMBER = 100;
	final static Logger logger = LoggerFactory.getLogger(GameManager.class);

	private List<GameEntity> games = new ArrayList<>();
	
	public GameEntity getGame (GameIdentifier gameID) {
		return games.stream()
			.filter(game -> game.getUniqueGameID().equals(gameID))
			.findAny()
			.orElseThrow(() -> new GameNotFoundException(
					"GameNotFoundException: ",
					"Game " + gameID.getGameID() + " was not found"));
	}
	
	// TODO: For Debugging
	public List<GameEntity> getAllGames () {
		return games;
	}
	
	public GameIdentifier createGame () {
		if(this.games.size() >= MAX_GAME_NUMBER) 
			this.games.remove(0);
	
		GameIdentifier gameID = new GameIdentifier();
		GameEntity newGame = new GameEntity(gameID);
		this.games.add(newGame);
		return gameID;
	}
	
	@Scheduled(fixedRate = 1000)
	public void updateGames() {
        Iterator<GameEntity> iterator = this.games.iterator();
        while (iterator.hasNext()) {
        	GameEntity game = iterator.next();
            if (game.isGameExpired()) {
            	logger.info("Game " + game.getUniqueGameID().getGameID() +  " is expired");
                iterator.remove();
            }
        }
    }
}
