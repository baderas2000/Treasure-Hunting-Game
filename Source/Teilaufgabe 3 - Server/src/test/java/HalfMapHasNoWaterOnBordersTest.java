import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import server.businessRules.HalfMapHasNoWaterOnBorders;
import server.data.map.ETerrainType;
import server.data.map.FullMap;
import server.data.map.FullMapTile;
import server.exceptions.BusinessRuleViolationException;

public class HalfMapHasNoWaterOnBordersTest {
	
	private final static Logger logger = LoggerFactory.getLogger(HalfMapHasNoWaterOnBordersTest.class);

	private FullMap noWaterOnBordersMap = new FullMap();
	private FullMap smallIslandMap = new FullMap();
	
	@BeforeEach
	public void init () {
		noWaterOnBordersMap = new FullMap();
		for (int x=0; x<10; x++) {
			for (int y=0; y<5; y++) {
				this.noWaterOnBordersMap.addTile(new FullMapTile(x, y, ETerrainType.Grass, null, null, null, null, null));
			}
		}
		
		smallIslandMap = new FullMap();
		for (int x=0; x<10; x++) {
			for (int y=0; y<5; y++) {
				if((x == 3 || x == 4 || x == 5 || x == 6) && (y == 0 || y == 1 || y == 2 || y == 3 ||y == 4)) {
					if ((x == 4 && y == 2) || (x == 5 && y == 2)) {
						this.smallIslandMap.addTile(new FullMapTile(x, y, ETerrainType.Grass, null, null, null, null, null));
					}
					else {					
						this.smallIslandMap.addTile(new FullMapTile(x, y, ETerrainType.Water, null, null, null, null, null));
						}
					}
				else {
				this.smallIslandMap.addTile(new FullMapTile(x, y, ETerrainType.Grass, null, null, null, null, null));
				}
			}
		}
	}
	
	@Test
	public void noWaterOnBordersMap_onlyGrassTerrains_true () {
		HalfMapHasNoWaterOnBorders rule = new HalfMapHasNoWaterOnBorders(noWaterOnBordersMap);
		boolean result = rule.enforce();
		assertTrue(result);
	}
	
	@Test
	public void smallIslandMap_someWaterPresent_false () {
		HalfMapHasNoWaterOnBorders rule = new HalfMapHasNoWaterOnBorders(smallIslandMap);
		assertThrows(BusinessRuleViolationException.class, () -> {
        	rule.enforce();
        });
	}

}
