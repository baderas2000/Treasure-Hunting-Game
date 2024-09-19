package data.map;

public enum ETerrain {
	Grass,
	Mountain,
	Water;
	
	//TODO:  Add appropriate exception
	public int getWeight() {
		switch (this) {
			case Mountain: return 2;
			case Grass: return 1;
			default: throw new RuntimeException("Water tiles have no weight");
		}
	}
}
