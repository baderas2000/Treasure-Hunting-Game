package communication;

import java.util.EnumMap;
import java.util.Optional;
import java.util.Set;

import data.logic.EPlayerGameState;
import data.logic.GameState;
import data.logic.PlayerState;
import data.map.EFortState;
import data.map.EPlayerPositionState;
import data.map.ETerrain;
import data.map.ETreasureState;
import data.map.FullMap;
import data.map.MapTile;
import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromserver.FullMapNode;

public class ServerMessageConverter {

	public GameState convertGameState(messagesbase.messagesfromserver.GameState serverGameState, UniquePlayerIdentifier playerIdentifier) {
		Set<messagesbase.messagesfromserver.PlayerState> players = serverGameState.getPlayers();
		messagesbase.messagesfromserver.PlayerState myPlayer = players
			.stream()
			.filter(p -> p.getUniquePlayerID().equals(playerIdentifier.getUniquePlayerID()))
			.findFirst()
			.orElseThrow(() -> new PlayerNotFoundException("My player is absent in server response."));
		EPlayerGameState playerGameState = this.convertPlayerGameState(myPlayer.getState());
		FullMap fullMap = this.convertFullMap(serverGameState.getMap()).get();
		PlayerState playerState = new PlayerState(myPlayer.hasCollectedTreasure(), playerGameState);
		GameState gameStatus = new GameState(fullMap, playerState);
		return gameStatus;
	}
	
	public Optional<FullMap> convertFullMap(messagesbase.messagesfromserver.FullMap serverFullMap) {		
		if (serverFullMap == null) {
			return Optional.empty();
		}
		FullMap clientFullMap = new FullMap();
		serverFullMap.getMapNodes().forEach(t ->
			clientFullMap.addTile(convertFullMapNode(t)));
		return Optional.ofNullable(clientFullMap);
	}
	
	public ETerrain convertETerrain(messagesbase.messagesfromclient.ETerrain serverETerrain) {
		EnumMap<messagesbase.messagesfromclient.ETerrain, ETerrain> mapETerrainType = new EnumMap<>(messagesbase.messagesfromclient.ETerrain.class);
		mapETerrainType.put(messagesbase.messagesfromclient.ETerrain.Grass, ETerrain.Grass);
		mapETerrainType.put(messagesbase.messagesfromclient.ETerrain.Mountain, ETerrain.Mountain);
		mapETerrainType.put(messagesbase.messagesfromclient.ETerrain.Water, ETerrain.Water);
		ETerrain clientETerrain = mapETerrainType.get(serverETerrain);
		return clientETerrain;
	}
	
	public ETreasureState convertETreasureState(messagesbase.messagesfromserver.ETreasureState serverETreasureState) {
		EnumMap<messagesbase.messagesfromserver.ETreasureState, ETreasureState> mapETreasureState = new EnumMap<>(messagesbase.messagesfromserver.ETreasureState.class);
		mapETreasureState.put(messagesbase.messagesfromserver.ETreasureState.NoOrUnknownTreasureState, ETreasureState.NoOrUnknownTreasureState);
		mapETreasureState.put(messagesbase.messagesfromserver.ETreasureState.MyTreasureIsPresent, ETreasureState.MyTreasureIsPresent);	
		ETreasureState clientETreasureState = mapETreasureState.get(serverETreasureState);
		return clientETreasureState;
	}
	
	public MapTile convertFullMapNode(FullMapNode serverFullMapNode) {
		MapTile clientMapTile = new MapTile(serverFullMapNode.getX(), serverFullMapNode.getY(), 
				convertETerrain(serverFullMapNode.getTerrain()),
				convertEFortState(serverFullMapNode.getFortState()),
				convertETreasureState(serverFullMapNode.getTreasureState()), 
				convertEPlayerPositionState(serverFullMapNode.getPlayerPositionState()));
		return clientMapTile;
	}
	
	public String convertUniquePlayerIdentifier (UniquePlayerIdentifier serverPlayerID) {
		return serverPlayerID.getUniquePlayerID();
	}
	
	public EPlayerPositionState convertEPlayerPositionState(messagesbase.messagesfromserver.EPlayerPositionState serverEPlayerPositionState) {	
		switch (serverEPlayerPositionState) {
		case BothPlayerPosition:
			return EPlayerPositionState.BothPlayerPosition;
		case EnemyPlayerPosition:
			return EPlayerPositionState.EnemyPlayerPosition;
		case MyPlayerPosition:
			return EPlayerPositionState.MyPlayerPosition;
		case NoPlayerPresent:
			return EPlayerPositionState.NoPlayerPresent;
		default:
			throw new PlayerNotFoundException("Can not determine player position state");
		}
	}
	
	public EFortState convertEFortState(messagesbase.messagesfromserver.EFortState serverEFortState) {
		EnumMap<messagesbase.messagesfromserver.EFortState, EFortState> mapEFortState = new EnumMap<>(messagesbase.messagesfromserver.EFortState.class);
		mapEFortState.put(messagesbase.messagesfromserver.EFortState.NoOrUnknownFortState, EFortState.NoOrUnknownFortState);
		mapEFortState.put(messagesbase.messagesfromserver.EFortState.MyFortPresent, EFortState.MyFortPresent);
		mapEFortState.put(messagesbase.messagesfromserver.EFortState.EnemyFortPresent, EFortState.EnemyFortPresent);
		EFortState clientEFortState = mapEFortState.get(serverEFortState);
		return clientEFortState;
	}
	
	public EPlayerGameState convertPlayerGameState(messagesbase.messagesfromserver.EPlayerGameState serverState) {
		EnumMap<messagesbase.messagesfromserver.EPlayerGameState, EPlayerGameState> mapPlayerGameState = new EnumMap<>(messagesbase.messagesfromserver.EPlayerGameState.class);
		mapPlayerGameState.put(messagesbase.messagesfromserver.EPlayerGameState.Lost, EPlayerGameState.Lost);
		mapPlayerGameState.put(messagesbase.messagesfromserver.EPlayerGameState.Won, EPlayerGameState.Won);
		mapPlayerGameState.put(messagesbase.messagesfromserver.EPlayerGameState.MustAct, EPlayerGameState.MustAct);
		mapPlayerGameState.put(messagesbase.messagesfromserver.EPlayerGameState.MustWait, EPlayerGameState.MustWait);
		EPlayerGameState clientState = mapPlayerGameState.get(serverState);
		return clientState;
	}
	
}
