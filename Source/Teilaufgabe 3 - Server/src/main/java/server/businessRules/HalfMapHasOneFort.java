package server.businessRules;

import server.data.map.FullMap;
import server.data.map.GameMap;
import server.exceptions.BusinessRuleViolationException;

public class HalfMapHasOneFort implements IMapRule {
	
	private FullMap map;
	
	public HalfMapHasOneFort(GameMap mapHalf) {
        this.map = (FullMap) mapHalf;
    }

	@Override
	public boolean enforce() {
		if (map.stream().filter(t -> t.getFortOwner().isPresent()).count() == 1) {
            return true;
        }
		else 
		 throw new BusinessRuleViolationException("HalfMapHasOneFort", "Sent Half Map does not have one Fort" );
	}
}
