import static org.junit.Assert.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import server.data.map.ETerrainType;
import server.data.map.FullMap;
import server.data.map.FullMapTile;
import server.exceptions.BusinessRuleViolationException;
import server.businessRules.HalfMapHasEnoughTerrainTypes;

public class HalfMapHasEnoughTerrainTypesTest {
	
	private final static Logger logger = LoggerFactory.getLogger(HalfMapHasEnoughTerrainTypesTest.class);

	private FullMap defaultMap = new FullMap();
	private FullMap fiveWaterMap = new FullMap();
	
	@BeforeEach
	public void init () {
		defaultMap = new FullMap();
		for (int x=0; x<10; x++) {
			for (int y=0; y<5; y++) {
				this.defaultMap.addTile(new FullMapTile(x, y, ETerrainType.Grass, null, null, null, null, null));
			}
		}
		
		fiveWaterMap = new FullMap();
		for (int x=0; x<10; x++) {
			for (int y=0; y<5; y++) {
				if(x == 5) {this.fiveWaterMap.addTile(new FullMapTile(x, y, ETerrainType.Water, null, null, null, null, null));}
				else {
				this.fiveWaterMap.addTile(new FullMapTile(x, y, ETerrainType.Grass, null, null, null, null, null));
				}
			}
		}
	}
	
	@Test
	public void defaultMap_onlyGrassTerrains_false () {
		HalfMapHasEnoughTerrainTypes rule = new HalfMapHasEnoughTerrainTypes(defaultMap);
        assertThrows(BusinessRuleViolationException.class, () -> {
        	rule.enforce();
        });
	}
	
	@Test
	public void defaultMap_notEnoughWaterTerrains_false () {
		HalfMapHasEnoughTerrainTypes rule = new HalfMapHasEnoughTerrainTypes(fiveWaterMap);
		assertThrows(BusinessRuleViolationException.class, () -> {
        	rule.enforce();
        });
	}
}
