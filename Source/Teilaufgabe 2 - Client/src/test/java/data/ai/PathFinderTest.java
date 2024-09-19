package data.ai;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import data.ai.PathFinder;
import data.map.FullMap;
import data.map.MapTile;
import data.map.ETerrain;

import java.util.List;
import java.util.ArrayList;

public class PathFinderTest {

	private final static int MAP_LENGTH_X = 10;
	private final static int MAP_LENGTH_Y = 10;
	private FullMap defaultVerticalMap = new FullMap();
	
	@BeforeEach
	public void init () {
		this.defaultVerticalMap = new FullMap();
		for (int x=0; x<PathFinderTest.MAP_LENGTH_X; x++) {
			for (int y=0; y<PathFinderTest.MAP_LENGTH_Y; y++) {
				this.defaultVerticalMap.addTile(new MapTile(x, y, ETerrain.Grass));
			}
		}
	}
	
	@AfterEach
	public void tearDown () {
		this.defaultVerticalMap = null;
	}
	
	@Test
	public void simpleMap_findPath_shouldFindStraightPathInFiveTiles () {
		PathFinder pathFinder = new PathFinder(
				this.defaultVerticalMap, this.defaultVerticalMap.getMapTile(0, 0));
		List<MapTile> actualPath = pathFinder.findPath(this.defaultVerticalMap.getMapTile(0, 4));
		List<MapTile> expectedPath = new ArrayList<>();
		expectedPath.add(this.defaultVerticalMap.getMapTile(0, 0));
		expectedPath.add(this.defaultVerticalMap.getMapTile(0, 1));
		expectedPath.add(this.defaultVerticalMap.getMapTile(0, 2));
		expectedPath.add(this.defaultVerticalMap.getMapTile(0, 3));
		expectedPath.add(this.defaultVerticalMap.getMapTile(0, 4));
		
		assertEquals(actualPath, expectedPath);
	}
	
	@Test
	public void defaultVerticalMap_findPath_shouldFindPathAroundWaterInFiveTiles () {
		this.defaultVerticalMap.removeTile(0, 1);
		this.defaultVerticalMap.addTile(new MapTile(0, 1, ETerrain.Water));
		PathFinder pathFinder = new PathFinder(
				this.defaultVerticalMap, this.defaultVerticalMap.getMapTile(0, 0));
		List<MapTile> actualPath = pathFinder.findPath(this.defaultVerticalMap.getMapTile(0, 2));
		List<MapTile> expectedPath = new ArrayList<>();
		expectedPath.add(this.defaultVerticalMap.getMapTile(0, 0));
		expectedPath.add(this.defaultVerticalMap.getMapTile(1, 0));
		expectedPath.add(this.defaultVerticalMap.getMapTile(1, 1));
		expectedPath.add(this.defaultVerticalMap.getMapTile(1, 2));
		expectedPath.add(this.defaultVerticalMap.getMapTile(0, 2));
		assertEquals(actualPath, expectedPath);
	}
	
	@Test
	public void defaultVerticalMap_findPath_shouldFindPathAroundWaterInSevenTiles () {
		this.defaultVerticalMap.replaceTile(new MapTile(0, 1, ETerrain.Water));
		this.defaultVerticalMap.replaceTile(new MapTile(1, 1, ETerrain.Water));
		PathFinder pathFinder = new PathFinder(
				this.defaultVerticalMap, this.defaultVerticalMap.getMapTile(0, 0));
		List<MapTile> actualPath = pathFinder.findPath(this.defaultVerticalMap.getMapTile(0, 2));
		List<MapTile> expectedPath = new ArrayList<>();
		expectedPath.add(this.defaultVerticalMap.getMapTile(0, 0));
		expectedPath.add(this.defaultVerticalMap.getMapTile(1, 0));
		expectedPath.add(this.defaultVerticalMap.getMapTile(2, 0));
		expectedPath.add(this.defaultVerticalMap.getMapTile(2, 1));
		expectedPath.add(this.defaultVerticalMap.getMapTile(2, 2));
		expectedPath.add(this.defaultVerticalMap.getMapTile(1, 2));
		expectedPath.add(this.defaultVerticalMap.getMapTile(0, 2));
		assertEquals(actualPath, expectedPath);
	}

	@Test
	public void defaultVerticalMap_findPath_shouldFindPathInMaze () {
		for (int y=0; y<7; y++) {
			this.defaultVerticalMap.replaceTile(new MapTile(2, y, ETerrain.Water));
		}
		this.defaultVerticalMap.replaceTile(new MapTile(0, 1, ETerrain.Water));
		this.defaultVerticalMap.replaceTile(new MapTile(1, 3, ETerrain.Water));
		this.defaultVerticalMap.replaceTile(new MapTile(0, 5, ETerrain.Water));
		PathFinder pathFinder = new PathFinder(
				this.defaultVerticalMap, this.defaultVerticalMap.getMapTile(0, 0));
		List<MapTile> actualPath = pathFinder.findPath(this.defaultVerticalMap.getMapTile(0, 6));
		List<MapTile> expectedPath = new ArrayList<>();
		expectedPath.add(this.defaultVerticalMap.getMapTile(0, 0));
		expectedPath.add(this.defaultVerticalMap.getMapTile(1, 0));
		expectedPath.add(this.defaultVerticalMap.getMapTile(1, 1));
		expectedPath.add(this.defaultVerticalMap.getMapTile(1, 2));
		expectedPath.add(this.defaultVerticalMap.getMapTile(0, 2));
		expectedPath.add(this.defaultVerticalMap.getMapTile(0, 3));
		expectedPath.add(this.defaultVerticalMap.getMapTile(0, 4));
		expectedPath.add(this.defaultVerticalMap.getMapTile(1, 4));
		expectedPath.add(this.defaultVerticalMap.getMapTile(1, 5));
		expectedPath.add(this.defaultVerticalMap.getMapTile(1, 6));
		expectedPath.add(this.defaultVerticalMap.getMapTile(0, 6));
		assertEquals(expectedPath, actualPath);
	}
	
	@Test
	public void simpleMap_updateMap_shouldThrowPathNotFoundException () {
		PathFinder pathFinder = new PathFinder(
				this.defaultVerticalMap, this.defaultVerticalMap.getMapTile(0, 0));
		Assertions.assertThrows(PathNotFoundException.class, () -> pathFinder.updateMap(null, null));
	}
}
