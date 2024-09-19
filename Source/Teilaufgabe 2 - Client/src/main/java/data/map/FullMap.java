package data.map;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FullMap {
	public final static int HALF_MAP_X_SIZE = 10;
	public final static int HALF_MAP_Y_SIZE = 5;
	
	private Set<MapTile> tiles = new HashSet<MapTile>();
	
	public FullMap () {
		
	}
	
	public FullMap (MapTile[] tiles) {
		for (MapTile tile : tiles)
			this.addTile(tile);
	}
	
	public FullMap (FullMap map) {
		map.stream().forEach(e -> this.addTile(e));
	}
	
	public FullMap (Stream<MapTile> tileStream) {
		this.tiles = tileStream.collect(Collectors.toSet());
	}
	
	public FullMap getHalfMap (int x, int y) {
		FullMap newMap = new FullMap(this.stream()
				.filter(e -> e.getX() >= x && e.getX() < x + FullMap.HALF_MAP_X_SIZE)
				.filter(e -> e.getY() >= y && e.getY() < y + FullMap.HALF_MAP_Y_SIZE));
		return newMap;
	}
	
	public MapTile getMapTile (int x, int y) {
		return this.stream().filter(e -> e.getX() == x && e.getY() == y)
				.findFirst().orElse(null);
	}
	
	public void addTile (MapTile tile) {
		if (tile == null)
			throw new ArithmeticException("Can not add null.");
		int x = tile.getX();
		int y = tile.getY();
		if (this.getMapTile(tile.getX(), tile.getY()) == null) {
			this.tiles.add(tile);
		} else {
			throw new ArithmeticException(
				"Tile with coordinates (" + x + ", " + y + ") already exists.");
		}
	}
	
	public void removeTile (int x, int y) {
		this.tiles.removeIf(t -> t.getX() == x && t.getY() == y);
	}
	
	public void replaceTile (MapTile tile) {
		if (tile == null) {
			return;
		}
		this.removeTile(tile.getX(), tile.getY());
		this.addTile(tile);
	}
	
	public boolean hasTile (MapTile tile) {
		return this.tiles.contains(tile);
	}

	public FullMap getAdjacentArea (MapTile tile) {
		if (tile == null)
			throw new ArithmeticException("Can not find adjacent area for null.");
		int x = tile.getX();
		int y = tile.getY();
		return new FullMap(
			this.stream().filter(e -> (e.getX() - x) * (e.getX() - x) + (e.getY() - y) * (e.getY() - y) == 1)
		);
	}
	
	public FullMap getVisibleArea (MapTile tile) {
		FullMap map = new FullMap();
		if (tile == null)
			throw new ArithmeticException("Can not calculate visible area for null.");
		int x = tile.getX();
		int y = tile.getY();
		if (tile.getTerrainType() == ETerrain.Mountain)
			this.stream().filter(t -> (t.getX() - x) * (t.getX() - x) + (t.getY() - y) * (t.getY() - y) <= 2)
			.forEach(map::addTile);
		else
			map.addTile(tile);
		return map;
	}
	
	public FullMap getVisibleArea (int x, int y) {
		return this.getVisibleArea(this.getMapTile(x, y));
	}
	
	public FullMap getPlayerVisibleArea () {
		return this.getVisibleArea(this.getPlayerPosition());
	}
	
	public FullMap getUncheckedArea () {
		return new FullMap(
			this.stream().filter(e -> !e.isChecked())
		);
	}
	
	public MapTile getPlayerPosition () {
		return this.stream()
			.filter(e -> e.getPlayerPositionState() == EPlayerPositionState.MyPlayerPosition)
			.findFirst().orElse(null);
	}
	
	public Optional<MapTile> getMyTreasure () {
		return this.stream()
				.filter(e -> e.getTreasureState() == ETreasureState.MyTreasureIsPresent)
				.findFirst();
	}
	
	public Optional<MapTile> getEnemyFort () {
		return this.stream()
				.filter(e -> e.getFortState() == EFortState.EnemyFortPresent)
				.findFirst();
	}
	
	public MapTile getMyFort () {
		return this.stream()
				.filter(e -> e.getFortState() == EFortState.MyFortPresent)
				.findFirst().orElse(null);
	}
	
	public Optional<MapTile> getEnemyPlayer () {
		return this.stream()
			.filter(t -> t.getPlayerPositionState() == EPlayerPositionState.EnemyPlayerPosition)
			.findFirst();
	}
	
	public Optional<MapTile> getMyPlayer () {
		return this.stream()
			.filter(t -> t.getPlayerPositionState() == EPlayerPositionState.MyPlayerPosition ||
					t.getPlayerPositionState() == EPlayerPositionState.BothPlayerPosition)
			.findFirst();
	}

	public FullMap getPassableArea () {
		return new FullMap(
			this.stream()
				.filter(t -> t.getTerrainType() != ETerrain.Water)
		);
	}

	public boolean isNearbyTreasure() {
		return this.stream().anyMatch((t) -> t.getTreasureState() == ETreasureState.MyTreasureIsPresent);
	}
	
	//TODO: Return something meaningful
	public boolean isNearbyCastle() {
		return this.stream().anyMatch((t) -> t.getFortState() == EFortState.EnemyFortPresent);
	}

	public void checkArea () {
		this.stream().forEach(MapTile::setChecked);
	}
	
	public Stream<MapTile> stream () {
		return this.tiles.stream();
	}
	
	public Iterator<MapTile> iterator () {
		return this.tiles.iterator();
	}
	
	public int size () {
		return this.tiles.size();
	}
	
	@Override
	public boolean equals (Object o) {
		if (o == null)
			return false;
		if (o.getClass() != this.getClass())
			return false;
		if (o == this)
			return true;
		FullMap map = (FullMap) o;
		return this.tiles.equals(map.tiles);
	}
	
	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append(super.toString())
			.append("(size: ").append(this.size()).append(")");
		return buffer.toString();
	}

	
	public String visualize () {
		StringBuilder buffer = new StringBuilder();
		int minX = this.stream()
				.mapToInt(t -> t.getX())
				.min().orElse(-1);
		int maxX = this.stream()
				.mapToInt(t -> t.getX())
				.max().orElse(-1);
		int minY = this.stream()
				.mapToInt(t -> t.getY())
				.min().orElse(-1);
		int maxY = this.stream()
				.mapToInt(t -> t.getY())
				.max().orElse(-1);
		
		buffer.append("X: (").append(minX).append(", ").append(maxX).append(")\n")
			.append("Y: (").append(minY).append(", ").append(maxY).append(")\n");
		
		for (int y=minY; y<=maxY; y++) {
			for (int x=minX; x<=maxX; x++) {
				MapTile tile = this.getMapTile(x, y);
				if (tile == null) {
					buffer.append("* ");
					continue;
				}
				ETerrain terrainType = tile.getTerrainType();
				if (terrainType == null) {
					buffer.append("? ");
					continue;
				}
				if (tile.getTreasureState() == ETreasureState.MyTreasureIsPresent) {
					buffer.append("+ ");
					continue;						
				}
				switch (terrainType) {
				case Grass:
					buffer.append(tile.getPlayerPositionState() == EPlayerPositionState.MyPlayerPosition ? "G " : "g ");
					break;
				case Mountain:
					buffer.append(tile.getPlayerPositionState() == EPlayerPositionState.MyPlayerPosition ? "M " : "m ");
					break;
				case Water:
					buffer.append(tile.getPlayerPositionState() == EPlayerPositionState.MyPlayerPosition ? "W " : "w ");
					break;
				default:
					buffer.append(tile.getPlayerPositionState() == EPlayerPositionState.MyPlayerPosition ? "? " : "? ");
					break;
				} 
			}
			buffer.append("\n");
		}
		return buffer.toString();
	}

}
