package server.businessRules;

import server.data.map.FullMap;
import server.data.map.GameMap;
import server.exceptions.BusinessRuleViolationException;

public class HalfMapHasValidBordersLength implements IMapRule {
	private final static int LENGHT_OF_MAP = 10;
	private final static int WIDTH_OF_MAP = 5;
	private FullMap map;
	
	public HalfMapHasValidBordersLength(GameMap mapHalf) {
        this.map = (FullMap) mapHalf;
    }

	@Override
	public boolean enforce() {
		for(int x = 0; x < LENGHT_OF_MAP; x++) {
			for(int y = 0; y < WIDTH_OF_MAP; y++) {
				final int X = x;
				final int Y = y;
				boolean isTilePresent = map.stream().anyMatch(tile -> tile.getX()== X && tile.getY() == Y);
				if (!isTilePresent) {
					throw new BusinessRuleViolationException("HalfMapHasValidBordersLength", 
							"Sent Half Map does not have valid borders length");
		        }
			}	
		}
		return true;
	}
}