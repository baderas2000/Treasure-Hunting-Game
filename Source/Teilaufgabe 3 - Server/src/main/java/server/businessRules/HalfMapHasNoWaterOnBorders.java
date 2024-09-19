package server.businessRules;

import server.data.map.ETerrainType;
import server.data.map.FullMap;
import server.data.map.GameMap;
import server.exceptions.BusinessRuleViolationException;

public class HalfMapHasNoWaterOnBorders implements IMapRule {
	private final static int LENGHT_OF_MAP = 10;
	private final static int WIDTH_OF_MAP = 5;
	private FullMap map;
	
	public HalfMapHasNoWaterOnBorders(GameMap mapHalf) {
        this.map = (FullMap) mapHalf;
    }

	@Override
	public boolean enforce() {
		if (map.stream()
				.filter(t -> t.getTerrainType() == ETerrainType.Water)
				.filter((t) -> {
			int x = t.getX();
			int y = t.getY();
			int MAX_X = HalfMapHasNoWaterOnBorders.LENGHT_OF_MAP - 1;
			int MAX_Y = HalfMapHasNoWaterOnBorders.WIDTH_OF_MAP - 1;
			
			return x == 0 || x == MAX_X || y == 0 || y == MAX_Y;
			}).findFirst().isEmpty()) {
            return true;
        }
		else 
		 throw new BusinessRuleViolationException("HalfMapHasNoWaterOnBorders", "Sent Half Map has water on borders" );
	}
}