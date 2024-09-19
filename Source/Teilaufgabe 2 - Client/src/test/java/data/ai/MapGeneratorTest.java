package data.ai;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import data.map.FullMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MapGeneratorTest {
	private final static Logger logger = LoggerFactory.getLogger(MapGeneratorTest.class);
	
	@Test
	public void newMap_generateMap_shouldGenerateMap () {
		MapGenerator mapGenerator = new MapGenerator();
		FullMap map = null;
		try {
			map = mapGenerator.generateMap();
		} catch (MapGenerationException e) {
			logger.error("MapGenerationException log message: ", e);
		}
		assertEquals(50, map.size());
	}
	
}

