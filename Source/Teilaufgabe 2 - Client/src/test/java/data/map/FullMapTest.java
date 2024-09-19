package data.map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import data.ai.MapValidator;

public class FullMapTest {

	private FullMap defaultVerticalMap;
	private FullMap defaultHorizontalMap;
	private ETerrain defaultTerrainType = ETerrain.Grass;

	private FullMap getMapPart(int xStart, int yStart, int xEnd, int yEnd) {
		FullMap map = new FullMap();
		for (int x=xStart; x<xEnd; x++) {
			for (int y=yStart; y<yEnd; y++) {
				map.addTile(new MapTile(x, y, this.defaultTerrainType));
			}
		}
		return map;
	}
	
	@BeforeEach
	public void setUp () {
		this.defaultVerticalMap = new FullMap();
		this.defaultHorizontalMap = new FullMap();
		for (int i=0; i<10; i++) {
			for (int j=0; j<10; j++) {
				this.defaultVerticalMap.addTile(new MapTile(i, j, this.defaultTerrainType));
				this.defaultHorizontalMap.addTile(new MapTile(i , j, this.defaultTerrainType));
			}
		}
	}
	
	@AfterEach
	public void tearDown () {
		this.defaultVerticalMap = null;
		this.defaultHorizontalMap = null;
		
	}
	
	@Test
	public void newMap_equals_shouldEqualMapWithSameTiles () {
		MapTile[] tiles = {
			new MapTile(0, 0, this.defaultTerrainType),
			new MapTile(1, 0, this.defaultTerrainType)
		};
		FullMap firstMap = new FullMap(tiles);
		FullMap secondMap = new FullMap(tiles);
		assertTrue(firstMap.equals(secondMap));
	}
	
	@Test
	public void newMap_equals_shouldNotEqualMapWithLessButSameTiles () {
		MapTile firstTile = new MapTile(0, 0, this.defaultTerrainType);
		MapTile secondTile = new MapTile(1, 0, this.defaultTerrainType);
		MapTile[] tiles = {firstTile, secondTile};
		FullMap firstMap = new FullMap(tiles);
		FullMap secondMap = new FullMap();
		secondMap.addTile(firstTile);
		assertFalse(firstMap.equals(secondMap));
	}
	
	@Test
	public void newMap_equals_shouldNotEqualMapWithMoreButSameTiles () {
		MapTile firstTile = new MapTile(0, 0, this.defaultTerrainType );
		MapTile secondTile = new MapTile(1, 0, this.defaultTerrainType);
		MapTile[] tiles = {firstTile, secondTile};
		FullMap firstMap = new FullMap();
		firstMap.addTile(firstTile);
		FullMap secondMap = new FullMap(tiles);
		assertFalse(firstMap.equals(secondMap));
	}
	
	@Test
	public void newMap_equals_shouldNotEqualMapWithSameTileButDifferentFortState () {
		MapTile firstTile = new MapTile(0, 0, this.defaultTerrainType);
		firstTile.setFortState(EFortState.MyFortPresent);
		MapTile secondTile = new MapTile(0, 0, this.defaultTerrainType);
		FullMap firstMap = new FullMap();
		firstMap.addTile(firstTile);
		FullMap secondMap = new FullMap();
		secondMap.addTile(secondTile);
		assertFalse(firstMap.equals(secondMap));
	}
	
	@Test
	public void newMap_equals_shouldNotEqualMapWithSameTileButDifferentTerrainType () {
		MapTile firstTile = new MapTile(0, 0, ETerrain.Grass);
		MapTile secondTile = new MapTile(0, 0, ETerrain.Water);
		FullMap firstMap = new FullMap();
		firstMap.addTile(firstTile);
		FullMap secondMap = new FullMap();
		secondMap.addTile(secondTile);
		assertFalse(firstMap.equals(secondMap));
	}
	
	@Test
	public void newMap_equals_shouldNotEqualMapWithSameTileButDifferentTreasureState () {
		MapTile firstTile = new MapTile(0, 0, this.defaultTerrainType);
		firstTile.setTreasureState(ETreasureState.MyTreasureIsPresent);
		MapTile secondTile = new MapTile(0, 0, this.defaultTerrainType);
		FullMap firstMap = new FullMap();
		firstMap.addTile(firstTile);
		FullMap secondMap = new FullMap();
		secondMap.addTile(secondTile);
		assertFalse(firstMap.equals(secondMap));
	}
	
	@Test
	public void newMap_equals_shouldNotEqualMapWithSameTileButDifferentCheckState () {
		MapTile firstTile = new MapTile(0, 0, this.defaultTerrainType);
		firstTile.setChecked();
		MapTile secondTile = new MapTile(0, 0, this.defaultTerrainType);
		FullMap firstMap = new FullMap();
		firstMap.addTile(firstTile);
		FullMap secondMap = new FullMap();
		secondMap.addTile(secondTile);
		assertTrue(firstMap.equals(secondMap));
	}
	
	@Test
	public void newMap_equals_shouldNotEqualMapWithSameTileButDifferentPlayerPositionState () {
		MapTile firstTile = new MapTile(0, 0, this.defaultTerrainType);
		firstTile.setPlayerPositionState(EPlayerPositionState.MyPlayerPosition);
		MapTile secondTile = new MapTile(0, 0, this.defaultTerrainType);
		FullMap firstMap = new FullMap();
		firstMap.addTile(firstTile);
		FullMap secondMap = new FullMap();
		secondMap.addTile(secondTile);
		assertFalse(firstMap.equals(secondMap));
	}
	
	@Test
	public void defaultVerticalMap_equals_EqualsItself () {
		assertTrue(defaultVerticalMap.equals(defaultVerticalMap));
	}
	
	@Test
	public void defaultVerticalMap_equals_doesNotEqualEmptyMap () {
		assertFalse(defaultVerticalMap.equals(new FullMap()));
	}
	
	@Test
	public void defaultVerticalMap_equals_doesNotEqualsNull () {
		assertFalse(defaultVerticalMap.equals(null));
	}
	
	@Test
	public void defaultVerticalMap_equals_doesNotEqualsMapTile () {
		assertFalse(defaultVerticalMap.equals(new MapTile(0, 0, this.defaultTerrainType)));
	}
	
	@Test
	public void defaultVerticalMap_size_containsHundredOfTile () {
		assertEquals(100, defaultVerticalMap.size());
	}
	
	@Test
	public void emptyMap_equals_containtsZeroTiles () {
		assertEquals(0, new FullMap().size());
	}
	
	@Test
	public void defaultVerticalMap_getHalfMap_returnsUpperHalfMap () {
		FullMap actualHalfMap = this.defaultVerticalMap.getHalfMap(0, 0);
		FullMap expectedHalfMap = this.getMapPart(0, 0, 10, 5);
		assertEquals(actualHalfMap, expectedHalfMap);
	}
	
	@Test
	public void defaultVerticalMap_getHalfMap_returnLowerHalfMap () {
		FullMap actualHalfMap = this.defaultVerticalMap.getHalfMap(0, 5);
		FullMap expectedHalfMap = this.getMapPart(0, 5, 10, 10);
		assertEquals(actualHalfMap, expectedHalfMap);
	}
	
	@Test
	public void defaultHorizontalMap_getHalfMap_returnRightHalfMap () {
		FullMap actualHalfMap = this.defaultHorizontalMap.getHalfMap(5, 0);
		FullMap expectedHalfMap = this.getMapPart(5, 0, 10, 5);
		assertEquals(actualHalfMap, expectedHalfMap);
	}
	
	
	@Test
	public void defaultVerticalMap_getMapTile_shouldReturnUpperLeftTile () {
		MapTile actualMapTile = this.defaultVerticalMap.getMapTile(0, 0);
		MapTile expectedMapTile = new MapTile(0, 0, this.defaultTerrainType);
		assertEquals(actualMapTile, expectedMapTile);
	}
	
	@Test
	public void defaultVerticalMap_getMapTile_shouldReturnMiddleTile () {
		MapTile actualMapTile = this.defaultVerticalMap.getMapTile(5, 2);
		MapTile expectedMapTile = new MapTile(5, 2, this.defaultTerrainType);
		assertEquals(actualMapTile, expectedMapTile);
	}
	
	@Test
	public void defaultVerticalMap_getMapTile_shouldReturnNull () {
		MapTile actualMapTile = this.defaultVerticalMap.getMapTile(10, 5);
		MapTile expectedMapTile = null;
		assertEquals(actualMapTile, expectedMapTile);
	}
	
	@Test
	public void defaultVerticalMap_getAdjacentArea_shouldReturnAdjacentOfUpperLeftTile () {
		FullMap actualArea = this.defaultVerticalMap.getAdjacentArea(
			new MapTile(0, 0, this.defaultTerrainType));
		FullMap expectedArea = new FullMap();
		expectedArea.addTile(new MapTile(0, 1, this.defaultTerrainType));
		expectedArea.addTile(new MapTile(1, 0, this.defaultTerrainType));
		assertEquals(actualArea, expectedArea);
	}
	
	@Test
	public void defaultVerticalMap_getAdjacentArea_shouldReturnAdjacentOfLowerLeftTile () {
		FullMap actualArea = this.defaultVerticalMap.getAdjacentArea(
			new MapTile(0, 9, this.defaultTerrainType));
		FullMap expectedArea = new FullMap();
		expectedArea.addTile(new MapTile(0, 8, this.defaultTerrainType));
		expectedArea.addTile(new MapTile(1, 9, this.defaultTerrainType));
		assertEquals(actualArea, expectedArea);
	}
	
	@Test
	public void defaultVerticalMap_getAdjacentArea_shouldReturnAdjacentOfLowerRightTile () {
		FullMap actualArea = this.defaultVerticalMap.getAdjacentArea(
			new MapTile(9, 9, this.defaultTerrainType));
		FullMap expectedArea = new FullMap();
		expectedArea.addTile(new MapTile(8, 9, this.defaultTerrainType));
		expectedArea.addTile(new MapTile(9, 8, this.defaultTerrainType));
		assertEquals(actualArea, expectedArea);
	}
	
	@Test
	public void defaultVerticalMap_getAdjacentArea_shouldReturnAdjacentOfUpperRightTile () {
		FullMap actualArea = this.defaultVerticalMap.getAdjacentArea(
			new MapTile(9, 0, this.defaultTerrainType));
		FullMap expectedArea = new FullMap();
		expectedArea.addTile(new MapTile(9, 1, this.defaultTerrainType));
		expectedArea.addTile(new MapTile(8, 0, this.defaultTerrainType));
		assertEquals(actualArea, expectedArea);
	}
	
	@Test
	public void defaultVerticalMap_getAdjacentArea_shouldReturnAdjacentOfLeftTile () {
		FullMap actualArea = this.defaultVerticalMap.getAdjacentArea(
			new MapTile(0, 5, this.defaultTerrainType));
		FullMap expectedArea = new FullMap();
		expectedArea.addTile(new MapTile(0, 4, this.defaultTerrainType));
		expectedArea.addTile(new MapTile(1, 5, this.defaultTerrainType));
		expectedArea.addTile(new MapTile(0, 6, this.defaultTerrainType));
		assertEquals(actualArea, expectedArea);
	}
	
	@Test
	public void defaultVerticalMap_getAdjacentArea_shouldReturnAdjacentOfLowerTile () {
		FullMap actualArea = this.defaultVerticalMap.getAdjacentArea(
			new MapTile(5, 9, this.defaultTerrainType));
		FullMap expectedArea = new FullMap();
		expectedArea.addTile(new MapTile(4, 9, this.defaultTerrainType));
		expectedArea.addTile(new MapTile(5, 8, this.defaultTerrainType));
		expectedArea.addTile(new MapTile(6, 9, this.defaultTerrainType));
		assertEquals(actualArea, expectedArea);
	}
	
	@Test
	public void defaultVerticalMap_getAdjacentArea_shouldReturnAdjacentOfRightTile () {
		FullMap actualArea = this.defaultVerticalMap.getAdjacentArea(
			new MapTile(9, 5, this.defaultTerrainType));
		FullMap expectedArea = new FullMap();
		expectedArea.addTile(new MapTile(9, 6, this.defaultTerrainType));
		expectedArea.addTile(new MapTile(8, 5, this.defaultTerrainType));
		expectedArea.addTile(new MapTile(9, 4, this.defaultTerrainType));
		assertEquals(actualArea, expectedArea);
	}
	
	@Test
	public void defaultVerticalMap_getAdjacentArea_shouldReturnAdjacentOfUpperTile () {
		FullMap actualArea = this.defaultVerticalMap.getAdjacentArea(
			new MapTile(5, 0, this.defaultTerrainType));
		FullMap expectedArea = new FullMap();
		expectedArea.addTile(new MapTile(6, 0, this.defaultTerrainType));
		expectedArea.addTile(new MapTile(5, 1, this.defaultTerrainType));
		expectedArea.addTile(new MapTile(4, 0, this.defaultTerrainType));
		assertEquals(actualArea, expectedArea);
	}
	
	@Test
	public void defaultVerticalMap_getAdjacentArea_shouldReturnAdjacentOfMiddleTile () {
		FullMap actualArea = this.defaultVerticalMap.getAdjacentArea(
			new MapTile(5, 5, this.defaultTerrainType));
		FullMap expectedArea = new FullMap();
		expectedArea.addTile(new MapTile(5, 4, this.defaultTerrainType));
		expectedArea.addTile(new MapTile(4, 5, this.defaultTerrainType));
		expectedArea.addTile(new MapTile(5, 6, this.defaultTerrainType));
		expectedArea.addTile(new MapTile(6, 5, this.defaultTerrainType));
		assertEquals(actualArea, expectedArea);
	}
	
	@Test
	public void defaultVerticalMap_getVisibleArea_shouldReturnExistingTile () {
		FullMap actualArea = this.defaultVerticalMap.getVisibleArea(0, 0);
		FullMap expectedArea = new FullMap();
		expectedArea.addTile(new MapTile(0, 0, this.defaultTerrainType));
		assertEquals(actualArea, expectedArea);
	}
	
	@Test
	public void defaultVerticalMap_getVisibleArea_shouldReturnAreaOfUpperLeftTile () {
		this.defaultVerticalMap.removeTile(0, 0);
		this.defaultVerticalMap.addTile(new MapTile(0, 0, ETerrain.Mountain));
		FullMap actualArea = this.defaultVerticalMap.getVisibleArea(0, 0);
		MapTile[] expectedTiles = {new MapTile(0, 0, ETerrain.Mountain),
			new MapTile(1, 0, this.defaultTerrainType),
			new MapTile(0, 1, this.defaultTerrainType),
			new MapTile(1, 1, this.defaultTerrainType)
		};
		FullMap expectedArea = new FullMap(expectedTiles);
		assertEquals(actualArea, expectedArea);
	}
	
	@ParameterizedTest
	@ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8})
	public void defaultVerticalMap_getVisibleArea_shouldReturnAreaOfMiddleTile (int coordinate) {
		int x = coordinate;
		int y = coordinate;
		this.defaultVerticalMap.removeTile(x, y);
		this.defaultVerticalMap.addTile(new MapTile(x, y, ETerrain.Mountain));
		FullMap actualArea = this.defaultVerticalMap.getVisibleArea(x, y);
		FullMap expectedArea = new FullMap();
		expectedArea.addTile(new MapTile(x, y, ETerrain.Mountain));
		for (int i=x-1; i<=x+1; i++) {
			for (int j=y-1; j<=y+1; j++) {
				if (i != x || j != y)
					expectedArea.addTile(new MapTile(i, j, this.defaultTerrainType));
			}
		}
		assertEquals(actualArea, expectedArea);
	}
	
	@ParameterizedTest
	@ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8})
	public void defaultHorizontalMap_getVisibleArea_shouldReturnAreaOfUpperTile (int coordinate) {
		int x = coordinate;
		int y = 0;
		this.defaultHorizontalMap.removeTile(x, y);
		this.defaultHorizontalMap.addTile(new MapTile(x, y, ETerrain.Mountain));
		FullMap actualArea = this.defaultHorizontalMap.getVisibleArea(x, y);
		FullMap expectedArea = new FullMap();
		expectedArea.addTile(new MapTile(x, y, ETerrain.Mountain));
		for (int i=x-1; i<=x+1; i++) {
			for (int j = y; j<=y+1; j++) {
				if (i != x || j != y) {
					expectedArea.addTile(new MapTile(i, j, this.defaultTerrainType));
				}
			}
		}
		assertEquals(actualArea, expectedArea);
	}
	
	@Test
	public void defaultHorizontalMap_replaceTile_shouldNotReplaceNullValue () {
		MapTile defaultTile = this.defaultHorizontalMap.getMapTile(0, 0);
		this.defaultHorizontalMap.replaceTile(null);
		MapTile updatedTile = this.defaultHorizontalMap.getMapTile(0, 0);
		assertEquals(updatedTile, defaultTile);
	}

	@Test
	public void defaultHorizontalMap_replaceTile_shouldReplaceTile () {
		MapTile newTile = new MapTile(0, 0, ETerrain.Mountain);
		this.defaultHorizontalMap.replaceTile(newTile);
		MapTile updatedTile = this.defaultHorizontalMap.getMapTile(0, 0);
		assertEquals(updatedTile, newTile);
	}

	@Test
	public void defaultHorizontalMap_replaceTile_shouldAddTile () {
		MapTile newTile = new MapTile(10, 0, ETerrain.Mountain);
		this.defaultHorizontalMap.replaceTile(newTile);
		MapTile updatedTile = this.defaultHorizontalMap.getMapTile(10, 0);
		assertEquals(updatedTile, newTile);
	}
	
	@Test
	public void mockedMap_getMapTile_shouldReturnFirstTile () {
		FullMap mockedMap = Mockito.mock(FullMap.class);
		MapTile tile = new MapTile(0, 0, ETerrain.Grass,
				EFortState.MyFortPresent, ETreasureState.MyTreasureIsPresent, EPlayerPositionState.MyPlayerPosition);
		Mockito.when(mockedMap.getMapTile(anyInt(), anyInt())).thenReturn(tile);
		
		MapTile mockedGetMapTile = mockedMap.getMapTile(1, 2);
		
		assertEquals(tile, mockedGetMapTile, "Expected to be mocked to tile 0,0");	
		
	}
	
}
