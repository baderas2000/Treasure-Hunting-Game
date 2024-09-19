package data.map;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import data.map.EFortState;
import data.map.EPlayerPositionState;
import data.map.ETerrain;
import data.map.ETreasureState;
import data.map.MapTile;


class MapTileTest {
	
	private final MapTile defaultMapTile = new MapTile(0, 0, ETerrain.Grass,
			EFortState.MyFortPresent, ETreasureState.MyTreasureIsPresent, EPlayerPositionState.MyPlayerPosition);
	private final MapTile defaultMinimalMapTile = new MapTile(0, 0, ETerrain.Grass);
	
	
	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@ParameterizedTest
	@EnumSource(EFortState.class)
	void newTile_getFortState_getFortStateReturnRightValues (EFortState fortState) {
		MapTile tile = new MapTile(0, 0, ETerrain.Grass,
				fortState, ETreasureState.MyTreasureIsPresent, EPlayerPositionState.MyPlayerPosition);
		assertEquals(fortState, tile.getFortState());
	}
	
	@ParameterizedTest
	@EnumSource(ETerrain.class)
	public void newTile_getTerrainType_getTerrainTypeReturnsRightValue (ETerrain terrainType) {
		MapTile tile = new MapTile(0, 0, terrainType,
				EFortState.MyFortPresent, ETreasureState.MyTreasureIsPresent, EPlayerPositionState.MyPlayerPosition);
		assertEquals(terrainType, tile.getTerrainType());
	}

	@ParameterizedTest
	@EnumSource(ETreasureState.class)
	void newTile_getTreasureState_getFortStateReturnRightValues (ETreasureState treasureState) {
		MapTile tile = new MapTile(0, 0, ETerrain.Grass,
				EFortState.MyFortPresent, treasureState, EPlayerPositionState.MyPlayerPosition);
		assertEquals(treasureState, tile.getTreasureState());
	}
	
	@ParameterizedTest
	@EnumSource(EPlayerPositionState.class)
	void newTile_getPlayerPositionState_getPlayerPositionStateReturnRightValues (EPlayerPositionState playerPositionState) {
		MapTile tile = new MapTile(0, 0, ETerrain.Grass,
				EFortState.MyFortPresent, ETreasureState.MyTreasureIsPresent, playerPositionState);
		assertEquals(playerPositionState, tile.getPlayerPositionState());
	}
	
	@ParameterizedTest
	@ValueSource(ints = {0, 1, 2, 3, 4 ,5})
	void newTile_getX_getXReturnRightValues (int x) {
		MapTile tile = new MapTile(x, 0, ETerrain.Grass,
				EFortState.MyFortPresent, ETreasureState.MyTreasureIsPresent, EPlayerPositionState.MyPlayerPosition);
		assertEquals(x, tile.getX());
	}
	
	@ParameterizedTest
	@ValueSource(ints = {0, 1, 2, 3, 4 ,5})
	void newTile_getX_getYReturnRightValues (int y) {
		MapTile tile = new MapTile(0, y, ETerrain.Grass,
				EFortState.MyFortPresent, ETreasureState.MyTreasureIsPresent, EPlayerPositionState.MyPlayerPosition);
		assertEquals(y, tile.getY());
	}
	
	@ParameterizedTest
	@EnumSource(EFortState.class)
	public void newTile_setFortState_setFortStateShouldOverrideValue(
			EFortState fortState) {
		this.defaultMapTile.setFortState(fortState);
		assertEquals(fortState, this.defaultMapTile.getFortState());
	}
	
	@ParameterizedTest
	@EnumSource(ETreasureState.class)
	public void newTile_setTreasureState_setTreasureStateShouldOverrideValue(
			ETreasureState treasureState) {
		this.defaultMapTile.setTreasureState(treasureState);
		assertEquals(treasureState, this.defaultMapTile.getTreasureState());
	}
	
	@ParameterizedTest
	@EnumSource(EPlayerPositionState.class)
	public void newTile_setPlayerPositionState_setPlayerPositionStateShouldOverrideValue(
			EPlayerPositionState playerPositionState) {
		this.defaultMapTile.setPlayerPositionState(playerPositionState);
		assertEquals(playerPositionState, this.defaultMapTile.getPlayerPositionState());
	}
	
	@ParameterizedTest
	@EnumSource(EFortState.class)
	public void newTile_setFortState_setFortStateShouldInitializeValue(
			EFortState fortState) {
		this.defaultMinimalMapTile.setFortState(fortState);
		assertEquals(fortState, this.defaultMinimalMapTile.getFortState());
	}
	
	@ParameterizedTest
	@EnumSource(ETreasureState.class)
	public void newTile_setTreasureState_setTreasureStateShouldInitializeValue(
			ETreasureState treasureState) {
		this.defaultMinimalMapTile.setTreasureState(treasureState);
		assertEquals(treasureState, this.defaultMinimalMapTile.getTreasureState());
	}
	
	@ParameterizedTest
	@EnumSource(EPlayerPositionState.class)
	public void newTile_setPlayerPositionState_setPlayerPositionStateShouldInitializeValue(
			EPlayerPositionState playerPositionState) {
		this.defaultMinimalMapTile.setPlayerPositionState(playerPositionState);
		assertEquals(playerPositionState, this.defaultMinimalMapTile.getPlayerPositionState());
	}
	
	@Test
	public void newTile_equals_shouldNotEqualToTileWithDifferentXCoordinate () {
		MapTile firstTile = new MapTile(0, 0, ETerrain.Grass,
				EFortState.MyFortPresent, ETreasureState.MyTreasureIsPresent, EPlayerPositionState.MyPlayerPosition);
		MapTile secondTile = new MapTile(1, 0, ETerrain.Grass,
				EFortState.MyFortPresent, ETreasureState.MyTreasureIsPresent, EPlayerPositionState.MyPlayerPosition);
		assertFalse(firstTile.equals(secondTile));
	}
	
	@Test
	public void newTile_equals_shouldNotEqualToTileWithDifferentYCoordinate () {
		MapTile firstTile = new MapTile(0, 0, ETerrain.Grass,
				EFortState.MyFortPresent, ETreasureState.MyTreasureIsPresent, EPlayerPositionState.MyPlayerPosition);
		MapTile secondTile = new MapTile(0, 1, ETerrain.Grass,
				EFortState.MyFortPresent, ETreasureState.MyTreasureIsPresent, EPlayerPositionState.MyPlayerPosition);
		assertFalse(firstTile.equals(secondTile));
	}
	
	@Test
	public void newTile_equals_shouldEqualToTileWithDifferentCheckedValue () {
		MapTile firstTile = new MapTile(0, 0, ETerrain.Grass,
				EFortState.MyFortPresent, ETreasureState.MyTreasureIsPresent, EPlayerPositionState.MyPlayerPosition);
		MapTile secondTile = new MapTile(0, 0, ETerrain.Grass,
				EFortState.MyFortPresent, ETreasureState.MyTreasureIsPresent, EPlayerPositionState.MyPlayerPosition);
		assertTrue(firstTile.equals(secondTile));
	}
	
	@Test
	public void newTile_equals_shouldNotEqualToTileWithDifferentTerrainType () {
		MapTile firstTile = new MapTile(0, 0, ETerrain.Grass,
				EFortState.MyFortPresent, ETreasureState.MyTreasureIsPresent, EPlayerPositionState.MyPlayerPosition);
		MapTile secondTile = new MapTile(0, 0, ETerrain.Mountain,
				EFortState.MyFortPresent, ETreasureState.MyTreasureIsPresent, EPlayerPositionState.MyPlayerPosition);
		assertFalse(firstTile.equals(secondTile));
	}
	
	@Test
	public void newTile_equals_shouldNotEqualToTileWithDifferentFortState () {
		MapTile firstTile = new MapTile(0, 0, ETerrain.Grass,
				EFortState.MyFortPresent, ETreasureState.MyTreasureIsPresent, EPlayerPositionState.MyPlayerPosition);
		MapTile secondTile = new MapTile(0, 0, ETerrain.Grass,
				EFortState.EnemyFortPresent, ETreasureState.MyTreasureIsPresent, EPlayerPositionState.MyPlayerPosition);
		assertFalse(firstTile.equals(secondTile));
	}
	
	@Test
	public void newTile_equals_shouldNotEqualToTileWithDifferentTreasureState () {
		MapTile firstTile = new MapTile(0, 0, ETerrain.Grass,
				EFortState.MyFortPresent, ETreasureState.MyTreasureIsPresent, EPlayerPositionState.MyPlayerPosition);
		MapTile secondTile = new MapTile(0, 0, ETerrain.Grass,
				EFortState.MyFortPresent, ETreasureState.NoOrUnknownTreasureState, EPlayerPositionState.MyPlayerPosition);
		assertFalse(firstTile.equals(secondTile));
	}
	
	@Test
	public void newTile_equals_shouldNotEqualToTileWithDifferentEPlayerPosition () {
		MapTile firstTile = new MapTile(0, 0, ETerrain.Grass,
				EFortState.MyFortPresent, ETreasureState.MyTreasureIsPresent, EPlayerPositionState.MyPlayerPosition);
		MapTile secondTile = new MapTile(0, 0, ETerrain.Grass,
				EFortState.MyFortPresent, ETreasureState.MyTreasureIsPresent, EPlayerPositionState.EnemyPlayerPosition);
		assertFalse(firstTile.equals(secondTile));
	}
	
	@Test
	public void newPartiallyDefineTile_equals_shouldEqualsToAntherPartiallyDefinedTile () {
		MapTile firstTile = new MapTile(0, 0, ETerrain.Grass);
		MapTile secondTile = new MapTile(0, 0, ETerrain.Grass);
		assertTrue(firstTile.equals(secondTile));
	}
}
