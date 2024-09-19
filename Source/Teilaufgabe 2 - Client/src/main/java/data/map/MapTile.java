package data.map;

public class MapTile {
	private int X = 0;
	private int Y = 0;
	private EFortState fortState = EFortState.NoOrUnknownFortState;
	private ETerrain terrainType = ETerrain.Grass;
	private ETreasureState treasureState = ETreasureState.NoOrUnknownTreasureState;
	private EPlayerPositionState playerPositionState = EPlayerPositionState.NoPlayerPresent;

	private boolean checked = false;
	
	public final static int MIN_X = 0;
	public final static int MIN_Y = 0;
	public final static int MAX_X = 19;
	public final static int MAX_Y = 9;
	
	public MapTile (MapTile tile) {
		this.X = tile.X;
		this.Y = tile.Y;
		this.checked = tile.checked;
		this.fortState = tile.fortState;
		this.terrainType = tile.terrainType;
		this.treasureState = tile.treasureState;
		this.playerPositionState = tile.playerPositionState;
	}
	
	public MapTile (int x, int y, ETerrain terrainType,
			EFortState fortState, ETreasureState treasureState, 
			EPlayerPositionState playerPositionState) {
		this.checkCoordinates(x, y);
		this.X = x;
		this.Y = y;
		this.fortState = fortState;
		this.terrainType = terrainType;
		this.treasureState = treasureState;
		this.playerPositionState = playerPositionState;
	}
	
	public MapTile (int x, int y, ETerrain terrainType) {
		this.checkCoordinates(x, y);
		this.X = x;
		this.Y = y;
		this.terrainType = terrainType;
	}
	
	public void checkCoordinates (int x, int y) {
		if (x < MapTile.MIN_X || y < MapTile.MIN_Y || 
			x > MapTile.MAX_X || y > MapTile.MAX_Y) {
			throw new IndexOutOfBoundsException(
				"Coordinates X and Y should be in range from" + MapTile.MAX_X + 
				" to " + MapTile.MAX_X + ".");
		}
	}
	
	public int getX() {
		return X;
	}
	
	public int getY() {
		return Y;
	}
	
	public boolean isChecked() {
		return checked;
	}
	
	public EFortState getFortState() {
		return fortState;
	}
	
	public ETerrain getTerrainType() {
		return terrainType;
	}
	
	public ETreasureState getTreasureState() {
		return treasureState;
	}
	
	public EPlayerPositionState getPlayerPositionState() {
		return playerPositionState;
	}
	
	public void setChecked() {
		this.checked = true;
	}
	
	public void setFortState(EFortState fortState) {
		this.fortState = fortState;
	}
	
	public void setTreasureState(ETreasureState treasureState) {
		this.treasureState = treasureState;
	}
	
	public void setPlayerPositionState(EPlayerPositionState playerPositionState) {
		this.playerPositionState = playerPositionState;
	}
	
	public MapTile replaceTerrainType (ETerrain terrainType) {
		MapTile newTile = new MapTile(this);
		newTile.terrainType = terrainType;
		return newTile;
	}
	
	public MapTile replaceFortState (EFortState fortState) {
		MapTile newTile = new MapTile(this);
		newTile.fortState = fortState;
		return newTile;
	}
	
	@Override
	public int hashCode () {
		String hashString = String.valueOf(this.X * 100 + this.Y);
		return hashString.hashCode();
	}
	
	@Override
	public boolean equals (Object o) {
		if (o == null)
			return false;
		if (o.getClass() != this.getClass())
			return false;
		if (o == this)
			return true;
		MapTile tile = (MapTile) o;
		if (this.X != tile.X || this.Y != tile.Y || 
			this.terrainType != tile.terrainType ||
			this.playerPositionState != tile.playerPositionState ||
			this.fortState != tile.fortState  ||
			this.treasureState != tile.treasureState)
			return false;
		return true;
	}
	
	@Override
	public String toString () {
		return new StringBuffer()
			.append("<MapTile, x: ").append(X)
			.append(", y: ").append(Y)
			.append(", type: ").append(this.terrainType)
			.append(">").toString();
	}



}
