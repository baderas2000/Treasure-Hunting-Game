package server.businessRules;

import server.data.map.ETerrainType;
import server.data.map.FullMap;
import server.data.map.GameMap;
import server.exceptions.BusinessRuleViolationException;

public class HalfMapHasEnoughTerrainTypes implements IMapRule {
	
	private FullMap map;
	private final int NUMBER_OF_GRASS = 38;
	private final int NUMBER_OF_WATER = 7;
	private final int NUMBER_OF_MOUNTAIN = 5;
	
	
	public HalfMapHasEnoughTerrainTypes(GameMap mapHalf) {
        this.map = (FullMap) mapHalf;
    }

	@Override
	public boolean enforce() {
		long numberOfGrass = map.stream().filter(t -> 
			t.getTerrainType() == ETerrainType.Grass).count();
		long numberOfMountain = map.stream().filter(t ->
			t.getTerrainType() == ETerrainType.Mountain).count();
		long numberOfWater = map.stream().filter(t -> 
			t.getTerrainType() == ETerrainType.Water).count();
		
		if (numberOfGrass == this.NUMBER_OF_GRASS && 
			numberOfMountain == this.NUMBER_OF_MOUNTAIN &&
			numberOfWater == this.NUMBER_OF_WATER) {
            return true;
        }
		else 
		 throw new BusinessRuleViolationException("HalfMapHasEnoughTerrainTypes", 
				 "Sent Half Map does not have correct amount of different terrains." );
	}
}