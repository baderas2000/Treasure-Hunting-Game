package data.ai;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import data.map.FullMap;
import data.map.MapTile;
import data.logic.EMove;
import data.map.EPlayerPositionState;
import data.map.ETerrain;

import java.util.List;
import java.util.Queue;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;

public class MapCoverageFinderTest {

	private FullMap defaultHalfMap = new FullMap();
	private final ETerrain defaultTerrainType = ETerrain.Grass;
	
	@BeforeEach
	public void setUp () {
		this.defaultHalfMap = new FullMap();
		for (int i=0; i<10; i++) {
			for (int j=0; j<5; j++) {
				this.defaultHalfMap.addTile(new MapTile(i, j, this.defaultTerrainType));
			}
		}
	}
	
	@Test
	public void smallMap_findMinimalCoverage () {
		FullMap map = new FullMap();
		List<MapTile> expectedTilePath = new ArrayList<>();
		MapTile[] tiles = {
			new MapTile(0, 0, ETerrain.Grass),
			new MapTile(0, 1, ETerrain.Grass),
			new MapTile(0, 2, ETerrain.Grass),
			new MapTile(1, 2, ETerrain.Grass),
		};
		for(MapTile tile: tiles) {
			expectedTilePath.add(tile);
			map.addTile(tile);
		}
		
		MapCoverageFinder targetFinder = new MapCoverageFinder();
		List<MapTile> actualTilePath = targetFinder.findMinimalCoverage(
			new MapTile(0, 0, ETerrain.Grass), map, map);
		assertEquals(expectedTilePath, actualTilePath);
	}
	
	@Test
	public void smallMountainMap_findMinimalCoverage () {
		FullMap map = new FullMap();
		for (int x=0; x<3; x++) {
			for (int y=0; y<6; y++) {
					map.addTile(new MapTile(x, y, ETerrain.Mountain));
			}
		}
		MapCoverageFinder targetFinder = new MapCoverageFinder();
		List<MapTile> expectedTilePath = new ArrayList<>();
		expectedTilePath.add(new MapTile(1, 1, ETerrain.Mountain));
		expectedTilePath.add(new MapTile(1, 3, ETerrain.Mountain));
		expectedTilePath.add(new MapTile(1, 5, ETerrain.Mountain));
		List<MapTile> actualTilePath = targetFinder.findMinimalCoverage(
			new MapTile(1, 1, ETerrain.Mountain), map, map);
		assertEquals(expectedTilePath, actualTilePath);
	}
	
	@Test
	public void smallMountainMap_convertToTileSequence () {
		FullMap map = new FullMap();
		for (int x=0; x<3; x++) {
			for (int y=0; y<6; y++) {
					map.addTile(new MapTile(x, y, ETerrain.Mountain));
			}
		}
		MapCoverageFinder targetFinder = new MapCoverageFinder();
		List<MapTile> tilePath =  new ArrayList<>();
		tilePath.add(new MapTile(1, 1, ETerrain.Mountain));
		tilePath.add(new MapTile(1, 3, ETerrain.Mountain));
		tilePath.add(new MapTile(1, 5, ETerrain.Mountain));
		List<MapTile> expectedTileSequence = new ArrayList<>();
		expectedTileSequence.add(new MapTile(1, 1, ETerrain.Mountain));
		expectedTileSequence.add(new MapTile(1, 2, ETerrain.Mountain));
		expectedTileSequence.add(new MapTile(1, 3, ETerrain.Mountain));
		expectedTileSequence.add(new MapTile(1, 3, ETerrain.Mountain));
		expectedTileSequence.add(new MapTile(1, 4, ETerrain.Mountain));
		expectedTileSequence.add(new MapTile(1, 5, ETerrain.Mountain));
		List<MapTile> actualTileSequence = targetFinder.convertToTileSequence(tilePath, map);
		assertEquals(expectedTileSequence, actualTileSequence);
	}
	
	@Test
	public void smallTrickyMap_findMinimalCoverage () {
		FullMap map = new FullMap();
		for (int x=0; x<3; x++) {
			for (int y=0; y<4; y++) {
					map.addTile(new MapTile(x, y, ETerrain.Grass));
			}
		}
		map.replaceTile(new MapTile(1, 0, ETerrain.Mountain));
		map.replaceTile(new MapTile(0, 1, ETerrain.Water));
		map.replaceTile(new MapTile(1, 1, ETerrain.Mountain));
		map.replaceTile(new MapTile(2, 1, ETerrain.Water));
		map.replaceTile(new MapTile(2, 2, ETerrain.Water));
		map.replaceTile(new MapTile(0, 3, ETerrain.Water));
		map.replaceTile(new MapTile(2, 3, ETerrain.Mountain));
		MapCoverageFinder targetFinder = new MapCoverageFinder();
		List<MapTile> expectedTilePath = new ArrayList<>();
		expectedTilePath.add(new MapTile(1, 1, ETerrain.Mountain));
		expectedTilePath.add(new MapTile(2, 3, ETerrain.Mountain));
		List<MapTile> actualTilePath = targetFinder.findMinimalCoverage(
			new MapTile(1, 1, ETerrain.Mountain), map, map);
		assertEquals(expectedTilePath, actualTilePath);
	}
	
	@Test
	public void bigMap_findMinimalCoverage () {
		FullMap map = new FullMap();
		for (int x=0; x<6; x++) {
			for (int y=0; y<6; y++) {
					map.addTile(new MapTile(x, y, ETerrain.Grass));
			}
		}
		map.replaceTile(new MapTile(1, 1, ETerrain.Mountain));
		map.replaceTile(new MapTile(4, 1, ETerrain.Mountain));
		map.replaceTile(new MapTile(4, 4, ETerrain.Mountain));
		for (int x=0; x<3; x++) {
			for (int y=3; y<6; y++) {
					map.removeTile(x, y);
			}
		}
		MapCoverageFinder targetFinder = new MapCoverageFinder();
		List<MapTile> expectedTilePath = new ArrayList<>();
		expectedTilePath.add(new MapTile(0, 1, ETerrain.Grass));
		expectedTilePath.add(new MapTile(1, 1, ETerrain.Mountain));
		expectedTilePath.add(new MapTile(4, 1, ETerrain.Mountain));
		expectedTilePath.add(new MapTile(4, 4, ETerrain.Mountain));
		List<MapTile> actualTilePath = targetFinder.findMinimalCoverage(
			new MapTile(0, 1, ETerrain.Grass), map, map);
		assertEquals(expectedTilePath, actualTilePath);
	}
	
	@Test
	public void simpleMap_convertToTileSequence_shouldThrowPathNotFoundException () {
		FullMap map = new FullMap();
		List<MapTile> expectedTilePath = new ArrayList<>();
		MapTile[] tiles = {
			new MapTile(0, 0, ETerrain.Grass),
			new MapTile(0, 1, ETerrain.Grass),
			new MapTile(0, 2, ETerrain.Grass),
			new MapTile(1, 2, ETerrain.Grass),
		};
		for(MapTile tile: tiles) {
			expectedTilePath.add(tile);
			map.addTile(tile);
		}
		MapCoverageFinder coverageFinder = new MapCoverageFinder();
		Assertions.assertThrows(PathNotFoundException.class, () -> coverageFinder.convertToTileSequence (null, map));
	}
}
