package server.businessRules;

import server.data.map.FullMap;
import server.data.map.GameMap;
import server.exceptions.BusinessRuleViolationException;

public class HalfMapHasEnoughTiles implements IMapRule {
	
	private FullMap map;
	
	public HalfMapHasEnoughTiles(GameMap mapHalf) {
        this.map = (FullMap) mapHalf;
    }

	@Override
	public boolean enforce() {
		if (map.stream().count() == 50) {
            return true;
        }
		else 
		 throw new BusinessRuleViolationException("HalfMapHasEnoughTiles", "Sent Half Map does not have enough tiles: " + map.stream().count());
	}
}