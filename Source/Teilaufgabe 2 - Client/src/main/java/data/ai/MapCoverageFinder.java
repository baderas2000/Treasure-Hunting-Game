package data.ai;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import data.map.FullMap;
import data.map.MapTile;

public class MapCoverageFinder {
	final static Logger logger = LoggerFactory.getLogger(MapCoverageFinder.class);
	
	private final int MAP_LENGHT = 10;
	private final int MAP_WIDTH = 5;
	
	
	private FullMap getEnemyHalf (FullMap map) {
		FullMap myHalfMap = this.getMyHalf(map);
		return new FullMap(map.stream().filter(t -> !myHalfMap.hasTile(t)));
	}
	
	private FullMap getMyHalf (FullMap map) {
		MapTile myFort = map.getMyFort();
		int x = myFort.getX() / this.MAP_LENGHT;
		int y = myFort.getY() / this.MAP_WIDTH;
		return map.getHalfMap(x * this.MAP_LENGHT, y * this.MAP_WIDTH);
	}
	
	public List<MapTile> findMinimalCoverage (MapTile startTile, FullMap coverageArea, FullMap fullMap) {
		List<MapTile> minimalCoverage = new ArrayList<MapTile>();
		Optional<MapTile> optionalCurrentTile = Optional.of(startTile);
		while (optionalCurrentTile.isPresent()) {
			MapTile currentTile = optionalCurrentTile.get();
			logger.debug("Adding current tile: " + currentTile.toString());
			minimalCoverage.add(currentTile);
			coverageArea.getVisibleArea(currentTile).checkArea();
			optionalCurrentTile = this.findNextTile(currentTile, coverageArea, fullMap);
		}
		return minimalCoverage;
	}

	private Optional<MapTile> findNextTile (MapTile currentTile, FullMap coverageArea, FullMap fullMap) {
		PathFinder pathFinder = new PathFinder(fullMap, currentTile);
		return coverageArea.getUncheckedArea().getPassableArea().stream()
				.max(Comparator.comparing(t -> getPathPriority(t, pathFinder, fullMap)));
	}
	
	private double getPathPriority(MapTile endTile, PathFinder pathFinder, FullMap map) {
		int pathWeight = pathFinder.getPathWeigth(endTile);
		int coveredArea = map.getVisibleArea(endTile).size();
		return (double) coveredArea / pathWeight;
	}
	
	public List<MapTile> convertToTileSequence (List<MapTile> tiles, FullMap map) {
		if (tiles == null)
			throw new PathNotFoundException("Can not convert null to tile sequence.");
		List<MapTile> tileSequence = new ArrayList<>();
		for (int i = 0; i < tiles.size() - 1; i++) {
			MapTile currTile = tiles.get(i);
			MapTile nextTile = tiles.get(i + 1);
			PathFinder pathFinder = new PathFinder(map, currTile);
			tileSequence.addAll(pathFinder.findPath(nextTile));
		}
		return tileSequence;
	}
	
	public List<MapTile> coverMap (MapTile startTile, FullMap coverageArea, FullMap fullMap) {
		List<MapTile> minimalCoverage = this.findMinimalCoverage(startTile, coverageArea, fullMap);
		logger.debug("Raw minimal coverage is found: " + minimalCoverage.toString());
		List<MapTile> tileSequence = this.convertToTileSequence(minimalCoverage, fullMap);
		logger.trace("Coverage of map:" + tileSequence.toString());
		return tileSequence;
	}
	
	public List<MapTile> coverMyHalf (MapTile startTile, FullMap map) {
		FullMap myHalfMap = this.getMyHalf(map);
		return this.coverMap(startTile, myHalfMap, map);
	}
	
	public List<MapTile> coverEnemyHalf (MapTile startTile, FullMap map) {
		logger.debug("Calculating coverage for enemy map from tile: " + startTile.toString());
		FullMap enemyHalfMap = this.getEnemyHalf(map);
		List<MapTile> mapCoverage = new ArrayList<MapTile>();
//		MapTile firstEnemyTile = enemyHalfMap
//				.stream().skip(new Random().nextInt(enemyHalfMap.size()))
//				.findFirst().orElseThrow(() -> new NoSuchTileException("No enemy tile is detected"));
//		logger.debug("Found first enemy tile: " + firstEnemyTile.toString());
//		PathFinder pathFinder = new PathFinder(map, startTile);
//		List<MapTile> firstEnemyTilePath = pathFinder.findPath(firstEnemyTile);
//		logger.trace("Path to first enemy tile is calculated: " + firstEnemyTilePath.toString());
//		mapCoverage.addAll(firstEnemyTilePath);
		List<MapTile> enemyMapCoverage = this.coverMap(startTile, enemyHalfMap, map);
		logger.trace("Found minimal coverage of enemy map: " + enemyMapCoverage.toString());
		mapCoverage.addAll(enemyMapCoverage);
		return mapCoverage;
	}

}
