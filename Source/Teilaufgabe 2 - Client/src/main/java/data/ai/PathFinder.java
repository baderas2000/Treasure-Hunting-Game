package data.ai;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import data.map.ETerrain;
import data.map.FullMap;
import data.map.MapTile;


/**
 * Class is dedicated for searching for the shortest path form Tile A to Tile B.
 * For the sake of optimisation tree of ancestors derived from provided map and start tile.
 * Start tile will be save as well to validate if path can be found.
 * It will allow quickly find paths from provided start tile to any other tile present on the map.
 * Since tree of ancestors is derived from map and start tile is calculated upon instantiation, 
 * only paths from provided in constructor start tile can be found.
 * 
 * @author Sofiia Badera
*/

public class PathFinder {
	
	private FullMap map;
	private MapTile startTile;
	private Map<MapTile, Integer> distances = new HashMap<MapTile, Integer>();
	private Set<MapTile> checkedTiles = new HashSet<MapTile>();
	private Map<MapTile, MapTile> ancestors = new HashMap<MapTile, MapTile>();
	
	public PathFinder (FullMap map, MapTile startTile) {
		this.updateMap(map, startTile);
	}
	
	public void updateMap (FullMap map, MapTile startTile) {
		if (map == null)
			throw new PathNotFoundException("Can not find path on null map.");
		if (startTile == null)
			throw new PathNotFoundException("Can not find path from null tile.");
		if (startTile.getTerrainType() == ETerrain.Water)
			throw new PathNotFoundException("Can not find path from water tile.");
		this.map = map;
		this.startTile = startTile;
		map.stream().forEach(t -> this.distances.put(t, Integer.MAX_VALUE));
		this.calculateTileTree(startTile);
	}
	
	public List<MapTile> findPath(MapTile endTile) {
		if (endTile == null)
			throw new PathNotFoundException("Can not calculate path to null.");
		if (this.startTile.equals(endTile))
			return new ArrayList<>();
		List<MapTile> path = new ArrayList<>();
		MapTile tile = endTile;
		while (tile != null) {
			path.add(0, tile);
			tile = this.ancestors.get(tile);
		}
		if (path.isEmpty() && this.startTile.equals(path.get(0)))
			throw new PathNotFoundException("Path not found between " +
				this.startTile.toString() + endTile.toString());
		return path;
	}
	
	private void calculateTileTree (MapTile startTile) {
		this.distances.put(startTile, 0);
		MapTile currentTile = startTile;
		while (currentTile != null) {
			Set<MapTile> adjacentTiles = this.map
					.getAdjacentArea(currentTile)
					.stream()
					.filter(t -> !this.checkedTiles.contains(t))
					.filter(t -> t.getTerrainType() != ETerrain.Water)
					.collect(Collectors.toSet());
			for (MapTile tile: adjacentTiles) {
				int currentDistance = this.distances.get(tile);
				int newDistance = this.distances.get(currentTile) +
						this.getDistance(currentTile, tile);
				if (newDistance < currentDistance) {
					this.distances.put(tile, newDistance);
					this.ancestors.put(tile, currentTile);
				}
			}
			this.checkedTiles.add(currentTile);
			currentTile = this.getMinimalDistance();
		}
	}
	
	private int getDistance(MapTile startTile, MapTile endTile) {
		EnumMap<ETerrain, Integer> distances = new EnumMap<>(ETerrain.class);
		distances.put(ETerrain.Grass, 1);
		distances.put(ETerrain.Mountain, 2);
		if (startTile == null || endTile == null)
			throw new PathNotFoundException("Can not calculate distance for null.");
		if (startTile.getTerrainType() == ETerrain.Water || 
			endTile.getTerrainType() == ETerrain.Water)
			throw new PathNotFoundException("Can not calculate distance for water tile.");
		return distances.get(startTile.getTerrainType()) + distances.get(endTile.getTerrainType());
	}
	
	public MapTile getMinimalDistance () {
		return this.distances.keySet().stream()
			.filter(t -> t.getTerrainType() != ETerrain.Water)
			.filter(t -> !this.checkedTiles.contains(t))
			.min(Comparator.comparing(k -> this.distances.get(k)))
			.orElse(null);
	}
	
	public int getPathWeigth (MapTile endTile) {
		return this.distances.get(endTile);
	}


}
