package data.logic;

import static org.junit.jupiter.api.Assertions.*;

import java.beans.PropertyChangeSupport;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import data.map.EFortState;
import data.map.EPlayerPositionState;
import data.map.ETerrain;
import data.map.ETreasureState;
import data.map.FullMap;
import data.map.MapTile;
import visualization.CLI;

class GameLogicTest {
	private final static Logger logger = LoggerFactory.getLogger(GameLogicTest.class);
	private FullMap defaultMap;
	
	private boolean treasureCollected = false;
	private EPlayerGameState playerGameState = EPlayerGameState.MustAct;
	PlayerState playerState = new PlayerState(treasureCollected, playerGameState);
	PropertyChangeSupport pcs = new PropertyChangeSupport(new CLI());
	GameLogic gameLogic = new GameLogic(pcs);
	
	@BeforeEach
	public void setUp () {
		this.defaultMap = new FullMap();
		for (int i=0; i<10; i++) {
			for (int j=0; j<10; j++) {
				this.defaultMap.addTile(new MapTile(i, j, ETerrain.Grass));
			}
		}
	}
	
	@AfterEach
	public void tearDown () {
		this.defaultMap = null;
		
	}

	@Test
	public void UpdateGameState_withGeneratingMapAndMustActStatus_shouldGenerateMap () {
		FullMap map= this.defaultMap;
		GameState gameState = new GameState(map, playerState);
		gameLogic.updateGameState(gameState);
		FullMap resultMap = gameLogic.getMap();
		assertEquals(50, resultMap.size());
	}
	
	@Test
	public void UpdateGameState_withGeneratingMyHalfCoverageAndMustActStatus_shouldSendMove () {
		this.UpdateGameState_withGeneratingMapAndMustActStatus_shouldGenerateMap ();
		MapTile startTile = gameLogic.getMap().getMapTile(0, 0);
		startTile.setPlayerPositionState(EPlayerPositionState.MyPlayerPosition);
		startTile.setFortState(EFortState.MyFortPresent);
		
		GameState gameState = new GameState(gameLogic.getMap(), playerState);
		EActionType resultAction = gameLogic.updateGameState(gameState);
		
		assertEquals(EActionType.SendMove, resultAction);
	}
	
	@Test
	public void UpdateGameState_withGeneratingMyHalfCoverageAndMustActStatus_shouldReturnMove () {
		UpdateGameState_withGeneratingMyHalfCoverageAndMustActStatus_shouldSendMove ();
		EMove resultMove = gameLogic.getMove();
		
		assertNotEquals(null, resultMove);
	}
}
