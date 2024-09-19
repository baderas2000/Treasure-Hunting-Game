package data.ai;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import data.map.EFortState;
import data.map.ETerrain;
import data.map.FullMap;
import data.map.MapTile;

public class MapValidator {
	private final int NUMBER_OF_GRASS;
	private final int NUMBER_OF_WATER;
	private final int NUMBER_OF_MOUNTAIN;
	
	private final static int LENGHT_OF_MAP = 10;
	private final static int WIDTH_OF_MAP = 5;

	public MapValidator () {
		this.NUMBER_OF_GRASS = 38;
		this.NUMBER_OF_WATER = 7;
		this.NUMBER_OF_MOUNTAIN = 5; 
	}
	
	public MapValidator (int numberOfGrass, int numberOfMountain, int numberOfWater) {
		this.NUMBER_OF_GRASS = numberOfGrass;
		this.NUMBER_OF_WATER = numberOfWater;
		this.NUMBER_OF_MOUNTAIN = numberOfMountain;
	}
	
	private boolean checkTerrains (FullMap map) {
		long numberOfGrass = map.stream().filter(t -> t.getTerrainType() == ETerrain.Grass)
				.count();
		long numberOfMountain = map.stream().filter(t -> t.getTerrainType() == ETerrain.Mountain)
				.count();
		long numberOfWater = map.stream().filter(t -> t.getTerrainType() == ETerrain.Water)
				.count();
		return numberOfGrass == this.NUMBER_OF_GRASS && 
			numberOfMountain == this.NUMBER_OF_MOUNTAIN &&
			numberOfWater == this.NUMBER_OF_WATER;
	}
	
	private boolean checkBorders (FullMap map) {
		return map.stream()
				.filter(t -> t.getTerrainType() == ETerrain.Water)
				.filter((t) -> {
			int x = t.getX();
			int y = t.getY();
			int MAX_X = MapValidator.LENGHT_OF_MAP - 1;
			int MAX_Y = MapValidator.WIDTH_OF_MAP - 1;
			return x == 0 || x == MAX_X || y == 0 || y == MAX_Y;
			}).findFirst().isEmpty();
	}
	
	private boolean checkFort (FullMap map) {
		return map.stream().filter(t -> t.getFortState() == EFortState.MyFortPresent).count() == 1;
	}
	
	private boolean checkIslands (FullMap map) {
		Queue<MapTile> tileQueue = new LinkedList<MapTile>();
		Set<MapTile> checkedTiles = new HashSet<MapTile>();
		MapTile currentTile = map.getMapTile(0, 0);
		if (currentTile != null) {
			tileQueue.offer(currentTile);
		}
		while (!tileQueue.isEmpty()) {
			currentTile = tileQueue.poll();
			checkedTiles.add(currentTile);
			map.getAdjacentArea(currentTile).stream()
				.filter(t -> t.getTerrainType() != ETerrain.Water)
				.filter(t -> !checkedTiles.contains(t))
				.forEach(tileQueue::offer);
		}
		int expectedSize = (int) map.stream()
			.filter(t -> t.getTerrainType() != ETerrain.Water).count();
		int actualSize = checkedTiles.size();
		return actualSize == expectedSize;
	}
	
	public boolean isValidMap (FullMap map) {
		return this.checkTerrains(map) && this.checkIslands(map) && this.checkBorders(map) && this.checkFort(map);
	}

}
