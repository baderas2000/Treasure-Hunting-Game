package server.data.map;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;

import server.data.player.PlayerIdentifier;
import server.exceptions.NullPlayerException;
import server.exceptions.PlayerNotFoundException;

public class FullMap extends GameMap<FullMapTile> {
	
	public FullMap () {};
	
	public FullMap (Stream<? extends FullMapTile> tileStream) {
		super(tileStream);
	}
	
	public FullMapTile getMapTile (int x, int y) {
		return this.stream().filter(e -> e.getX() == x && e.getY() == y)
				.findFirst().orElse(null);
	}
	
	public Optional<FullMapTile> getPlayerPosition (PlayerIdentifier playerID) {
		return this.stream()
			.filter(t -> t.hasPlayerPresent(playerID))
			.findFirst();
	}
	
	public Optional<FullMapTile> getPlayerTreasure (PlayerIdentifier playerID) {
		if (playerID.getPlayerIdentifier() == null)
			throw new NullPlayerException("Invalid player identifier",
				"Unable to process null object");
		return this.stream()
			.filter(t -> t.getTreasureOwner()
				.orElse(new PlayerIdentifier(null))
				.equals(playerID))
			.findFirst();
	}
	
	public Optional<FullMapTile> getPlayerFort (PlayerIdentifier playerID) {
		if (playerID.getPlayerIdentifier() == null)
			throw new NullPlayerException("Invalid player identifier",
				"Unable to process null object");
		return this.stream()
			.filter(t -> t.getFortOwner()
				.orElse(new PlayerIdentifier(null))
				.equals(playerID))
			.findFirst();
	}
	
	public FullMap getPlayerVisibleArea (PlayerIdentifier playerID) {
		FullMapTile playerPosition = this.getPlayerPosition(playerID)
			.orElseThrow(() -> new PlayerNotFoundException(
				"Player not found",
				"Player " + playerID.getPlayerIdentifier() + " is not present on map"));
		int radius = (playerPosition.getTerrainType() == ETerrainType.Mountain) ? 2 : 1;
		Stream<FullMapTile> visibleAreaStream = this.stream()
			.filter(t -> {
				int dx = t.getX() - playerPosition.getX();
				int dy = t.getY() - playerPosition.getY();
				return dx * dx + dy * dy < radius;
			});
		return new FullMap(visibleAreaStream);
	}
	public FullMap getAdjacentArea (FullMapTile tile) {
		if (tile == null)
			throw new ArithmeticException("Can not find adjacent area for null.");
		int x = tile.getX();
		int y = tile.getY();
		return new FullMap(
			this.stream().filter(e -> (e.getX() - x) * (e.getX() - x) + (e.getY() - y) * (e.getY() - y) == 1)
		);
	}
	
	public void setRandomEnemyPosition(String enemy) {

		if(getPlayerPosition(new PlayerIdentifier(enemy)).isPresent()) {
			FullMapTile realPosition =  getPlayerPosition(new PlayerIdentifier(enemy)).get();
			realPosition.removePlayer(new PlayerIdentifier(enemy));
			
			List<FullMapTile> tiles = this.stream().toList();
			Random random = new Random();
			int randomIndex = random.nextInt(tiles.size());
			FullMapTile randomTile = tiles.get(randomIndex);
			int randomX = randomTile.getX();
			int randomY = randomTile.getY();
			FullMapTile fakePosition = getMapTile(randomX, randomY);
			fakePosition.addPlayer(new PlayerIdentifier(enemy));
		}
	}
}
