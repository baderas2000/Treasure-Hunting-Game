package server.data.map;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

public abstract class GameMap<T extends GameMapTile> {
	
	private Set<T> tiles = new HashSet<>();
	
	public GameMap () {}
	
	public GameMap (Stream<? extends T> tileStream) {
		tileStream.forEach(this::addTile);
	}
	
	public GameMap (Collection<? extends T> tileCollection) {
		tileCollection.forEach(this::addTile);
	}
	
	public boolean addTile (T tile) {
		if (this.getTile(tile.getX(), tile.getY()).isEmpty())
			return this.tiles.add(tile);
		return false;
	}
	
	public Optional<T> getTile (int x, int y) {
		return this.tiles.stream()
			.filter(t -> t.getX() == x && t.getY() == y)
			.findFirst();
	}
	
	public Stream<T> stream () {
		return this.tiles.stream();
	}
	
}