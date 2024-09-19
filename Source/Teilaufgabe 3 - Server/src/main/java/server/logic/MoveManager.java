package server.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import server.data.game.GameEntity;
import server.data.move.EMove;

public class MoveManager {
	
	final static Logger logger = LoggerFactory.getLogger(MoveManager.class);
	private GameEntity currentGame;
	
	public MoveManager (GameEntity game) {
		this.currentGame = game;
	}

	public boolean validateMove(EMove move) {
		//TODO: Implement function
		return false;
	}
	
	public void incrementNumberOfActions() {
		int numberOfActions = currentGame.getNumberOfActions();
		currentGame.setNumberOfActions(numberOfActions++);
	}


}
