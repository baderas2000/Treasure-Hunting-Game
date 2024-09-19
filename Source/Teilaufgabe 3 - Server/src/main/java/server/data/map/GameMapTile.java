package server.data.map;

public abstract class GameMapTile {
	private int x;
	private int y;
	private ETerrainType terrainType;
	
	public GameMapTile (int x, int y, ETerrainType terrainType) {
		this.x = x;
		this.y = y;
		this.terrainType = terrainType;
	}
	
	public GameMapTile (GameMapTile tile) {
		this.x = tile.x;
		this.y = tile.y;
		this.terrainType = tile.terrainType;
	}
	
	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public int getX () {
		return x;
	}
	
	public int getY () {
		return y;
	}
	
	public ETerrainType getTerrainType () {
		return this.terrainType;
	}
	
	@Override
	public boolean equals (Object o) {
		if (this == o)
			return true;
		if (o == null)
			return false;
		if (!(o instanceof GameMapTile))
			return false;
		GameMapTile tile = (GameMapTile) o;
		return tile.x == this.x && 
			tile.y == this.y;
	}
	
	@Override
	public final int hashCode () {
		return y * 100 + x;
	}

}

