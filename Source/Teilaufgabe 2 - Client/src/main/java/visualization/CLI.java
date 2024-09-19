package visualization;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.util.Arrays;
import java.util.Optional;

import data.map.MapTile;
import data.map.FullMap;
import data.map.EFortState;
import data.map.ETreasureState;
import data.map.EPlayerPositionState;
import data.logic.EMove;
import data.logic.GameLogic;
import data.logic.EGamePhase;

public class CLI implements PropertyChangeListener {
	final static Logger logger = LoggerFactory.getLogger(CLI.class);

	private Optional<FullMap> map = Optional.empty();
	private Optional<EMove> lastMove = Optional.empty();;
	private Optional<EGamePhase> gamePhase = Optional.empty();;
	static private String grassCharacter =          "🍀";
	static private String waterCharacter =          "🌊";
	static private String mountainCharacter =       "🗻";
	static private String playerCharacter =         "🦸";
	static private String enemyCharacter =          "🦹";
	static private String playerFortCharacter =     "🏰";
	static private String enemyFortCharacter =      "🏯";
	static private String playerTreasureCharacter = "💰";

	/*
	 * Uncomment these symbols, if your console does not support UTF-8.
	 * 
	static private String grassCharacter =          "#";
	static private String waterCharacter =          "~";
	static private String mountainCharacter =       "^";
	static private String playerCharacter =         "&";
	static private String enemyCharacter =          "?";
	static private String playerFortCharacter =     "+";
	static private String enemyFortCharacter =      "!";
	static private String playerTreasureCharacter = "$";
	*/

	public void propertyChange(PropertyChangeEvent event) {
		String propertyName = event.getPropertyName();
		logger.trace("Received update for property " + propertyName);
		switch (propertyName) {
		case "map":
			this.map = Optional.of((FullMap) event.getNewValue());
			break;
		case "gamephase":
			this.gamePhase = Optional.of((EGamePhase) event.getNewValue());
			break;
		case "lastmove":
			this.lastMove = Optional.of((EMove) event.getNewValue());
		}
		this.update();
	}

	private void update() {
		StringBuffer buffer = new StringBuffer();
		String lastMoveRepresentation = this.lastMove.isPresent() ? String.valueOf(this.lastMove.get()) : "No move was sent";
		buffer.append("Last Move: ").append(lastMoveRepresentation).append("\n");
		String gamePhaseRepresentation = this.gamePhase.isPresent() ? String.valueOf(this.gamePhase.get()) : "No GamePhase declared";
		buffer.append("Game phase: ").append(gamePhaseRepresentation).append("\n");
		String mapRepresentation = this.map.isPresent() ? this.representMap(this.map.get()) : "No map is present";
		buffer.append(mapRepresentation);
		System.out.print(buffer.toString());
	}

	private String representMap(FullMap map) {
		String[][] mapRepresentation = new String[20][20];
		for (String[] row: mapRepresentation)
			Arrays.fill(row, "");
		map.stream().forEach((t) -> this.representTile(t, mapRepresentation));
		StringBuffer mapRepresentationBuffer = new StringBuffer();
		for (int i=0; i<20; i++) {
			for (int j=0; j<20; j++) {
				String tileRepresentation = mapRepresentation[i][j];
				if (!tileRepresentation.equals(""))
					mapRepresentationBuffer.append(mapRepresentation[i][j]).append(" ");
			}
			if (mapRepresentationBuffer.lastIndexOf("\n") != mapRepresentationBuffer.length() - 1) {
				mapRepresentationBuffer.append("\n");
			}
		}
		return mapRepresentationBuffer.toString();
	}

	private void representTile(MapTile tile, String[][] mapRepresentation) {
		String tileCharacter;
		int tileX = tile.getY();
		int tileY = tile.getX();
		switch(tile.getTerrainType()) {
		case Grass:
			tileCharacter = CLI.grassCharacter; break;
		case Water:
			tileCharacter = CLI.waterCharacter; break;
		default:
			tileCharacter = CLI.mountainCharacter; break;
		}
		if (tile.getFortState() == EFortState.MyFortPresent) {
			tileCharacter = CLI.playerFortCharacter;
		}
		if (tile.getFortState() == EFortState.EnemyFortPresent)
			tileCharacter = CLI.enemyFortCharacter;
		if (tile.getTreasureState() == ETreasureState.MyTreasureIsPresent)
			tileCharacter = CLI.playerTreasureCharacter;
		if (tile.getPlayerPositionState() == EPlayerPositionState.EnemyPlayerPosition)
			tileCharacter = CLI.enemyCharacter;
		if (tile.getPlayerPositionState() == EPlayerPositionState.MyPlayerPosition ||
				tile.getPlayerPositionState() == EPlayerPositionState.BothPlayerPosition)
			tileCharacter = CLI.playerCharacter;
		mapRepresentation[tileX][tileY] = tileCharacter;
	}

}
