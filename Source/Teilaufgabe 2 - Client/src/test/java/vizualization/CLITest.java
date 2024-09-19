package vizualization;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.beans.PropertyChangeEvent;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import data.logic.GameState;
import data.logic.PlayerState;
import data.ai.MapGenerator;
import data.logic.EGamePhase;
import data.map.FullMap;
import data.map.MapTile;
import visualization.CLI;
import data.logic.EPlayerGameState;
import data.logic.EMove;

public class CLITest {
	
	private CLI cli;
	private Random random = new Random();
	private EPlayerGameState[] playerGameStates = EPlayerGameState.values();
	
	@BeforeEach
	public void setUp () {
		this.cli = new CLI();
	}
	
	private GameState getRandomGameState () {
		FullMap map = null;
		FullMap secondMap = null;
		PlayerState playerState = new PlayerState(true,
				this.playerGameStates[this.random.nextInt(this.playerGameStates.length)]);
		try {
			MapGenerator mapGenerator = new MapGenerator();
			map = mapGenerator.generateMap();
			secondMap = mapGenerator.generateMap();
			this.supplementMap(map, secondMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(map.size());
		return new GameState(map, playerState);
	}
	
	private void supplementMap(FullMap mainMap, FullMap map) {
		System.out.println("Supplementing existing Map");
		for (MapTile tile: map.stream().collect(Collectors.toList())) {
			MapTile newTile = new MapTile(
					tile.getX(),
					tile.getY() + 5,
					tile.getTerrainType(),
					tile.getFortState(),
					tile.getTreasureState(),
					tile.getPlayerPositionState());
			System.out.println(newTile);
			mainMap.addTile(newTile);
		}
	}

	@Test
	public void depictChangingGameState () {
		GameState randomGameState = this.getRandomGameState();
		PropertyChangeEvent pce = new PropertyChangeEvent(this, "map", randomGameState.getMap(), randomGameState.getMap());
		this.cli.propertyChange(pce);
		assertTrue(true);
	}
	
}