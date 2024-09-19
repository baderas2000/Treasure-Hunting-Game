package data.ai;

import data.map.FullMap;
import data.map.MapTile;
import data.map.EFortState;
import data.map.ETerrain;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;
import communication.Network;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class MapValidatorTest {
	private final static Logger logger = LoggerFactory.getLogger(MapValidatorTest.class);

	private FullMap defaultMap = new FullMap();
	
	@BeforeEach
	public void init () {
		defaultMap = new FullMap();
		for (int x=0; x<10; x++) {
			for (int y=0; y<5; y++) {
				this.defaultMap.addTile(new MapTile(x, y, ETerrain.Grass));
			}
		}
	}
	
	@Test
	public void defaultMap_checkIsland_shouldDetectSmallIsland () throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		this.defaultMap.replaceTile(new MapTile(1, 0, ETerrain.Water));
		this.defaultMap.replaceTile(new MapTile(0, 1, ETerrain.Water));
		MapValidator validator = new MapValidator();
		//logger.debug(this.defaultMap.visualize());
		// Obtain the private method using reflection
        Method checkIslands = MapValidator.class.getDeclaredMethod("checkIslands", FullMap.class);
        checkIslands.setAccessible(true);
        boolean result = (boolean) checkIslands.invoke(validator, this.defaultMap);
 
		assertFalse(result);
	}
	
	@Test
	public void defaultMap_checkIsland_shouldDetectMiddleIsland () throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		this.defaultMap.replaceTile(new MapTile(2, 0, ETerrain.Water));
		this.defaultMap.replaceTile(new MapTile(2, 1, ETerrain.Water));
		this.defaultMap.replaceTile(new MapTile(0, 2, ETerrain.Water));
		this.defaultMap.replaceTile(new MapTile(1, 2, ETerrain.Water));
		MapValidator validator = new MapValidator();
		//logger.debug(this.defaultMap.visualize());
		// Obtain the private method using reflection
        Method checkIslands = MapValidator.class.getDeclaredMethod("checkIslands", FullMap.class);
        checkIslands.setAccessible(true);
        boolean result = (boolean) checkIslands.invoke(validator, this.defaultMap);
		assertFalse(result);
	}
	
	@Test
	public void defaultMap_checkIsland_shouldDetectNoIslands () throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		MapValidator validator = new MapValidator();
		//logger.debug(this.defaultMap.visualize());
		
		// Obtain the private method using reflection
        Method checkIslands = MapValidator.class.getDeclaredMethod("checkIslands", FullMap.class);
        checkIslands.setAccessible(true);
        boolean result = (boolean) checkIslands.invoke(validator, this.defaultMap);
		assertTrue(result);
	}
	
	
	@Test
	public void defaultMap_checkTerrains_false () throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		this.defaultMap.replaceTile(new MapTile(2, 0, ETerrain.Water));
		this.defaultMap.replaceTile(new MapTile(2, 1, ETerrain.Water));
		this.defaultMap.replaceTile(new MapTile(0, 2, ETerrain.Water));
		this.defaultMap.replaceTile(new MapTile(1, 2, ETerrain.Water));
		this.defaultMap.replaceTile(new MapTile(1, 0, ETerrain.Mountain));
		this.defaultMap.replaceTile(new MapTile(1, 1, ETerrain.Mountain));
		this.defaultMap.replaceTile(new MapTile(0, 1, ETerrain.Mountain));
		this.defaultMap.replaceTile(new MapTile(4, 2, ETerrain.Mountain));
		this.defaultMap.replaceTile(new MapTile(4, 3, ETerrain.Mountain));
		MapValidator validator = new MapValidator();
		//logger.debug(this.defaultMap.visualize());
		// Obtain the private method using reflection
        Method checkTerrains = MapValidator.class.getDeclaredMethod("checkTerrains", FullMap.class);
        checkTerrains.setAccessible(true);
        boolean result = (boolean) checkTerrains.invoke(validator, this.defaultMap);
        assertFalse(result);
	}
	
	@Test
	public void defaultMap_checkBorders () throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		this.defaultMap.replaceTile(new MapTile(2, 2, ETerrain.Water));
		this.defaultMap.replaceTile(new MapTile(2, 1, ETerrain.Water));
		this.defaultMap.replaceTile(new MapTile(3, 2, ETerrain.Water));
		this.defaultMap.replaceTile(new MapTile(1, 2, ETerrain.Water));
		this.defaultMap.replaceTile(new MapTile(1, 3, ETerrain.Water));
		MapValidator validator = new MapValidator();
		//logger.debug(this.defaultMap.visualize());
		// Obtain the private method using reflection
        Method checkBorders = MapValidator.class.getDeclaredMethod("checkBorders", FullMap.class);
        checkBorders.setAccessible(true);
        boolean result = (boolean) checkBorders.invoke(validator, this.defaultMap);
		assertTrue(result);
	}
	
	@Test
	public void defaultMap_checkBorders_false () throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		this.defaultMap.replaceTile(new MapTile(2, 2, ETerrain.Water));
		this.defaultMap.replaceTile(new MapTile(2, 1, ETerrain.Water));
		this.defaultMap.replaceTile(new MapTile(3, 2, ETerrain.Water));
		this.defaultMap.replaceTile(new MapTile(0, 2, ETerrain.Water));
		this.defaultMap.replaceTile(new MapTile(0, 3, ETerrain.Water));
		MapValidator validator = new MapValidator();
		//logger.debug(this.defaultMap.visualize());
		// Obtain the private method using reflection
        Method checkBorders = MapValidator.class.getDeclaredMethod("checkBorders", FullMap.class);
        checkBorders.setAccessible(true);
        boolean result = (boolean) checkBorders.invoke(validator, this.defaultMap);
		assertFalse(result);
	}
	
	@Test
	public void defaultMap_checkFort () throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		this.defaultMap.getMapTile(1, 1).setFortState(EFortState.MyFortPresent);
		MapValidator validator = new MapValidator();
		//logger.debug(this.defaultMap.visualize());
		// Obtain the private method using reflection
        Method checkFort = MapValidator.class.getDeclaredMethod("checkFort", FullMap.class);
        checkFort.setAccessible(true);
        boolean result = (boolean) checkFort.invoke(validator, this.defaultMap);
		assertTrue(result);
	}
	
	@Test
	public void defaultMap_checkFort_false () throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		MapValidator validator = new MapValidator();
		//logger.debug(this.defaultMap.visualize());
		// Obtain the private method using reflection
        Method checkFort = MapValidator.class.getDeclaredMethod("checkFort", FullMap.class);
        checkFort.setAccessible(true);
        boolean result = (boolean) checkFort.invoke(validator, this.defaultMap);
		assertFalse(result);
	}
	
	@Test
	public void defaultMap_isValidMap_false () {
		this.defaultMap.replaceTile(new MapTile(2, 2, ETerrain.Water));
		this.defaultMap.replaceTile(new MapTile(2, 1, ETerrain.Water));
		this.defaultMap.replaceTile(new MapTile(3, 2, ETerrain.Water));
		this.defaultMap.replaceTile(new MapTile(1, 2, ETerrain.Water));
		this.defaultMap.replaceTile(new MapTile(2, 3, ETerrain.Water));
		
		this.defaultMap.replaceTile(new MapTile(1, 0, ETerrain.Mountain));
		this.defaultMap.replaceTile(new MapTile(1, 1, ETerrain.Mountain));
		this.defaultMap.replaceTile(new MapTile(0, 1, ETerrain.Mountain));
		this.defaultMap.replaceTile(new MapTile(2, 2, ETerrain.Mountain));
		this.defaultMap.replaceTile(new MapTile(4, 3, ETerrain.Mountain));
		this.defaultMap.replaceTile(new MapTile(5, 2, ETerrain.Mountain));
		this.defaultMap.replaceTile(new MapTile(4, 4, ETerrain.Mountain));
		
		MapValidator validator = new MapValidator();
		//logger.debug(this.defaultMap.visualize());
		assertFalse(validator.isValidMap(this.defaultMap));
	}
}
