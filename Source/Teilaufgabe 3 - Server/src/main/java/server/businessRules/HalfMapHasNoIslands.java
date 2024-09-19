package server.businessRules;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import server.data.map.ETerrainType;
import server.data.map.FullMap;
import server.data.map.FullMapTile;
import server.data.map.GameMap;
import server.exceptions.BusinessRuleViolationException;

public class HalfMapHasNoIslands implements IMapRule {
	
	private FullMap map;
	
	public HalfMapHasNoIslands(GameMap mapHalf) {
        this.map = (FullMap) mapHalf;
    }

	@Override
	public boolean enforce() {
		Queue<FullMapTile> tileQueue = new LinkedList<FullMapTile>();
		Set<FullMapTile> checkedTiles = new HashSet<FullMapTile>();
		FullMapTile currentTile = map.getMapTile(0, 0);
		if (currentTile != null) {
			tileQueue.offer(currentTile);
		}
		while (!tileQueue.isEmpty()) {
			currentTile = tileQueue.poll();
			checkedTiles.add(currentTile);
			map.getAdjacentArea(currentTile).stream()
				.filter(t -> t.getTerrainType() != ETerrainType.Water)
				.filter(t -> !checkedTiles.contains(t))
				.forEach(tileQueue::offer);
		}
		int expectedSize = (int) map.stream()
			.filter(t -> t.getTerrainType() != ETerrainType.Water)
			.count();
		int actualSize = checkedTiles.size();
		
		if (actualSize == expectedSize) {
            return true;
        }
		else 
		 throw new BusinessRuleViolationException("HalfMapHasNoIslands", "Sent Half Map has islands." );
	}
}