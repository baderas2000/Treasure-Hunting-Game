import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import server.businessRules.HalfMapHasValidBordersLength;
import server.data.map.ETerrainType;
import server.data.map.FullMap;
import server.data.map.FullMapTile;
import server.exceptions.BusinessRuleViolationException;

public class HalfMapHasValidBordersLengthTest {

	private final static Logger logger = LoggerFactory.getLogger(HalfMapHasValidBordersLengthTest.class);

	private FullMap correctMap = new FullMap();
	private FullMap smallMap = new FullMap();
	
	@BeforeEach
	public void init () {
		correctMap = new FullMap();
		for (int x=0; x<10; x++) {
			for (int y=0; y<5; y++) {
				this.correctMap.addTile(new FullMapTile(x, y, ETerrainType.Grass, null, null, null, null, null));
			}
		}
		
		smallMap = new FullMap();
		for (int x=0; x<1; x++) {
			for (int y=0; y<5; y++) {
				this.smallMap.addTile(new FullMapTile(x, y, ETerrainType.Grass, null, null, null, null, null));
			}
		}
	}
	
	@Test
	public void correctMap_bordersLengthIsCorrect_true () {
		HalfMapHasValidBordersLength rule = new HalfMapHasValidBordersLength(correctMap);
		boolean result = rule.enforce();
		assertTrue(result);
	}
	
	@Test
	public void smallMap_mapLengthIsTooShort_false () {
		HalfMapHasValidBordersLength rule = new HalfMapHasValidBordersLength(smallMap);
		assertThrows(BusinessRuleViolationException.class, () -> {
        	rule.enforce();
        });
	}
}
