package data.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyChangeSupport;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Optional;

import data.ai.NoSuchTileException;
import data.ai.PathFinder;
import data.ai.MapGenerator;
import data.ai.MapCoverageFinder;
import data.ai.PathNotFoundException;
import data.ai.MapGenerationException;
import data.map.ETerrain;
import data.map.FullMap;
import data.map.MapTile;


public class GameLogic {
	final static Logger logger = LoggerFactory.getLogger(GameLogic.class);

	private EGamePhase gamePhase = EGamePhase.GeneratingMap;
	private Queue<EMove> moveQueue = new LinkedList<EMove>();
	private PropertyChangeSupport pcs;
	private Optional<FullMap> generatedHalfMap = Optional.empty();
	private Optional<PlayerState> playerState = Optional.empty();;
	private Optional<FullMap> map = Optional.empty();;
	
	public GameLogic(PropertyChangeSupport pcs) {
		this.pcs = pcs;
	}
	
	public EActionType updateGameState(GameState gameState) {
		logger.debug("Fire property change for map");
		this.pcs.firePropertyChange("map", this.map, gameState.getMap());
		this.map = Optional.of(gameState.getMap());
		this.playerState = Optional.of(gameState.getPlayerState());
		FullMap map = this.map.orElseThrow(
				() -> new InvalidGameStateException("Invalid gameState is supplemented, map is null"));
		PlayerState playerState = this.playerState.orElseThrow(
				() -> new InvalidGameStateException("Invalid gameState is supplemented, map is null"));
		EActionType actionType = playerState.getPlayerGameState() == EPlayerGameState.MustAct ?
				EActionType.SendMove : EActionType.Wait;
		boolean mustAct = playerState.getPlayerGameState() == EPlayerGameState.MustAct;
		boolean treasureCollected = playerState.isTreasureCollected();
		if (this.gamePhase == EGamePhase.GeneratingMap && mustAct) {
			logger.info("Generating my half map.");
			try {
				MapGenerator mapGenerator = new MapGenerator();
				FullMap generatedMap = mapGenerator.generateMap();
				this.pcs.firePropertyChange("map", null, generatedMap);
				this.generatedHalfMap = Optional.of(generatedMap);
			} catch (MapGenerationException mge) {
				logger.info("Unable to generate valid map.");
			}
			actionType = EActionType.SendMap;
			this.setGamePhase(EGamePhase.GeneratingMyHalfCoverage);
		} else if (this.gamePhase == EGamePhase.GeneratingMyHalfCoverage && mustAct) {
			logger.info("Calculating minimal coverage for my half of map.");
			MapCoverageFinder mapCoverageFinder = new MapCoverageFinder();
			MapTile playerTile = map.getMyPlayer().orElseThrow(
					() -> new NoSuchTileException("Own player is not found on map"));
			List<MapTile> mapCoverageTiles = mapCoverageFinder.coverMyHalf(playerTile, map);
			List<MapTile> mapCoveragePath = mapCoverageFinder.convertToTileSequence(mapCoverageTiles, map);
			Queue<EMove>  mapCoverage = this.convertToMoveQueue(mapCoveragePath); 
			this.moveQueue.clear();
			this.moveQueue.addAll(mapCoverage);
			this.setGamePhase(EGamePhase.LookingForTreasure);
		} else if (this.gamePhase == EGamePhase.LookingForTreasure && map.isNearbyTreasure()) {
			logger.info("Treasure is found, calculating path to treasure from player current position.");
			MapTile treasureTile = map.getMyTreasure().orElseThrow(
					() -> new NoSuchTileException("Treasure not found on map"));
			MapTile playerTile = map.getMyPlayer().orElseThrow(
					() -> new NoSuchTileException("No player found on map"));
			PathFinder pathFinder = new PathFinder(map, playerTile);
			List<MapTile> pathToTreasure = pathFinder.findPath(treasureTile);
			Queue<EMove> movesToTreasure = this.convertToMoveQueue(pathToTreasure);
			this.moveQueue.clear();
			this.moveQueue.addAll(movesToTreasure);
			this.setGamePhase(EGamePhase.GoingToTreasure);
		} else if (this.gamePhase == EGamePhase.LookingForTreasure && treasureCollected ||
				this.gamePhase == EGamePhase.GoingToTreasure && treasureCollected) {
			logger.info("Treasure is collected, calculating minimal coverage of enemy`s half of map.");
			MapTile playerTile = map.getMyPlayer().orElseThrow(
					() -> new NoSuchTileException("No player found on map"));
			logger.debug("Current player tile: " + playerTile.toString());
			MapCoverageFinder mapCoverageFinder = new MapCoverageFinder();
			List<MapTile> enemyMapCoverageTiles = mapCoverageFinder.coverEnemyHalf(playerTile, map);
			List<MapTile> enemyMapCoveragePath = mapCoverageFinder.convertToTileSequence(enemyMapCoverageTiles, map);
			Queue<EMove> enemyMapCoverage = this.convertToMoveQueue(enemyMapCoveragePath);
			this.moveQueue.clear();
			this.moveQueue.addAll(enemyMapCoverage);
			this.setGamePhase(EGamePhase.LookingForCastle);
		} else if (this.gamePhase == EGamePhase.LookingForCastle && map.isNearbyCastle() && !(this.isGameFinished())) {
			logger.info("Enemy`s fort is found, calculating path to enemy`s fort.");
			MapTile enemyFortTile = map.getEnemyFort().orElseThrow(
					() -> new NoSuchTileException("Treasure not found on map"));
			MapTile playerTile = map.getMyPlayer().orElseThrow(
					() -> new NoSuchTileException("No player found on map"));
			PathFinder pathFinder = new PathFinder(map, playerTile);
			List<MapTile> pathToEnemyFort = pathFinder.findPath(enemyFortTile);
			Queue<EMove> movesToEnemyFort = this.convertToMoveQueue(pathToEnemyFort);
			this.moveQueue.clear();
			this.moveQueue.addAll(movesToEnemyFort);
			this.setGamePhase(EGamePhase.GoingToCastle);
		} else if (this.isGameFinished()) {
			logger.info("Game is finished with state: " + playerState.getPlayerGameState().toString());
			this.setGamePhase(EGamePhase.GameFinished);
		}	
		logger.info("Return action type " + actionType.toString());
		return actionType;
	}

	private void setGamePhase(EGamePhase gamePhase) {
		logger.info("Change game phase from " + this.gamePhase.toString() + " to " + gamePhase.toString());
		this.pcs.firePropertyChange("gamephase", null, gamePhase);
		this.gamePhase = gamePhase;
	}
	
	public EMove getMove() {
		EMove move = this.moveQueue.remove();
		logger.trace("Fire property change for move: " + move.toString());
		this.pcs.firePropertyChange("lastmove", null, move);
		return move;
	}
	
	public FullMap getMap() {
		return this.generatedHalfMap.orElseThrow(
				() -> new RuntimeException("Half map is not generated yet."));
	}

	public boolean isGameFinished() {
		if (this.playerState.isEmpty())
			return false;
		return this.playerState.get().getPlayerGameState() == EPlayerGameState.Lost ||
				this.playerState.get().getPlayerGameState() == EPlayerGameState.Won;
	}
	
	private Queue<EMove> convertToMoveQueue (List<MapTile> tiles) {
		Queue<EMove> moveQueue = new LinkedList<EMove>();
		for (int i = 0; i < tiles.size() - 1; i++) {
			logger.trace("Calculate moves required to shift from " +
					tiles.get(i).toString() + " to " + tiles.get(i+1).toString() + ".");
			List<EMove> moves = this.getMoveDirection(tiles.get(i), tiles.get(i + 1));
			logger.trace("Required moves: " + moves.toString());
			moveQueue.addAll(moves);
		}
		return moveQueue;
	}
	
	private List<EMove> getMoveDirection (MapTile startTile, MapTile endTile) {
		if (startTile == null && endTile == null)
			throw new PathNotFoundException("Can not find path for null tile.");
		if (startTile.getTerrainType() == ETerrain.Water || endTile.getTerrainType() == ETerrain.Water)
			throw new PathNotFoundException("Can not find path for water tile.");
		if (startTile.equals(endTile))
			return new ArrayList<>();
		int xDiff = endTile.getX() - startTile.getX();
		int yDiff = endTile.getY() - startTile.getY();
		if (xDiff * xDiff + yDiff * yDiff > 1)
			throw new PathNotFoundException("Can not find path for not adjacent tiles");
		int numberOfMoves = startTile.getTerrainType().getWeight() + endTile.getTerrainType().getWeight();
		EMove desiredMove;
		if (xDiff == 1)
			desiredMove = EMove.Right;
		else if (xDiff == -1)
			desiredMove = EMove.Left;
		else if (yDiff == 1)
			desiredMove = EMove.Down;
		else if (yDiff == -1)
			desiredMove = EMove.Up;
		else
			throw new PathNotFoundException("Can not determine move direction.");
		List<EMove> moves = new ArrayList<EMove>();
		for (int i=0; i<numberOfMoves; i++) {
			moves.add(desiredMove);
		}
		return moves;
	}
	
}
