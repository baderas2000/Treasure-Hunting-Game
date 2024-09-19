package data.ai;

import java.util.HashSet;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import data.map.EFortState;
import data.map.ETerrain;
import data.map.FullMap;
import data.map.MapTile;

public class MapGenerator {
	

	private final int NUMBER_OF_GRASS = 38;
	private final int NUMBER_OF_MOUNTAIN = 5;
	private final int NUMBER_OF_WATER = 7;
	private final int NUMBER_OF_RETRIES = 30;
	
	private final int MIN_X = 0;
	private final int MAX_X = 9;
	private final int MIN_Y = 0;
	private final int MAX_Y = 4;
	
	final static Logger logger = LoggerFactory.getLogger(MapGenerator.class);
	
	private MapTile getRandomTile(Stream<MapTile> stream, int size) {
	    return stream.skip(new Random().nextInt(size)).findFirst().orElse(null);
	}
	
	private void addFort (FullMap map) {
		Optional<MapTile> optionalRandomTile = map.stream()
			.filter(t -> t.getTerrainType() == ETerrain.Grass)
			.skip(new Random().nextInt(map.size()))
			.findFirst();
		if (optionalRandomTile.isEmpty())
			return;
		map.replaceTile(optionalRandomTile.get().replaceFortState(EFortState.MyFortPresent));
	}
	
	private void changeTileType (FullMap map, Set<MapTile> emptyTiles, int NUMBER, ETerrain terrain) {
		logger.debug("Add " + NUMBER + " tiles of type " + terrain.toString());
		for (int i=0; i< NUMBER; i++) {
			MapTile tile = this.getRandomTile(emptyTiles.stream(), emptyTiles.size());
			map.addTile(tile.replaceTerrainType(terrain));
			emptyTiles.remove(tile);
		}
	}
	
	private void addGrass (FullMap map, Set<MapTile> emptyTiles) {
		this.changeTileType(map, emptyTiles, NUMBER_OF_GRASS, ETerrain.Grass);
	}
	
	private void addMountain (FullMap map, Set<MapTile> emptyTiles) {
		this.changeTileType(map, emptyTiles, NUMBER_OF_MOUNTAIN, ETerrain.Mountain);
	}
	
	private void addWater (FullMap map, Set<MapTile> emptyTiles) {
		logger.debug("Add " + NUMBER_OF_WATER + " tiles of type " + ETerrain.Water.toString());
		int mapLength = this.MAX_X - this.MIN_X + 1;
		int mapWidth = this.MAX_Y - this.MIN_Y + 1;
		int missingTiles = mapLength * 2 + mapWidth * 2 - 4;
		for (int i=0; i<this.NUMBER_OF_WATER; i++) {
			 MapTile tile = this.getRandomTile(
				emptyTiles.stream()
					.filter(t -> t.getX() > this.MIN_X && t.getX() < this.MAX_X)
					.filter(t -> t.getY() > this.MIN_Y && t.getY() < this.MAX_Y),
				emptyTiles.size() - missingTiles);
			 logger.trace("Added Water Tile: " + tile.toString());
			 map.addTile(tile.replaceTerrainType(ETerrain.Water));
			 emptyTiles.remove(tile);
		}
	}
	
	private FullMap getMap () {
		FullMap map = new FullMap();
		Set<MapTile> emptyTiles = new HashSet<MapTile>();
		for (int x = this.MIN_X; x <= this.MAX_X; x++) {
			for (int y = this.MIN_Y; y <= this.MAX_Y; y++) {
				emptyTiles.add(new MapTile(x, y, ETerrain.Grass));
			}
		}
		this.addWater(map, emptyTiles);
		this.addMountain(map, emptyTiles);
		this.addGrass(map, emptyTiles);
		this.addFort(map);
		return map;
	}
	
	public FullMap generateMap () throws MapGenerationException {
		MapValidator mapValidator = new MapValidator();
		for (int i=0; i<this.NUMBER_OF_RETRIES; i++) {
			logger.info("Map generation attempt: " + i);
			FullMap map = this.getMap();
			if (mapValidator.isValidMap(map)) {
				logger.info("Map generation attempt " + i + " was successful");
				return map;
			}
		}
		throw new MapGenerationException("Can not generate valid map.");
		
	}


}
