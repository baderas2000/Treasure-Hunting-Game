package server.main.converters;

import java.util.EnumMap;
import java.util.Optional;

import messagesbase.UniqueGameIdentifier;
import messagesbase.messagesfromclient.PlayerHalfMap;
import messagesbase.messagesfromclient.PlayerHalfMapNode;
import messagesbase.messagesfromclient.PlayerRegistration;
import server.data.game.GameIdentifier;
import server.data.map.EFortStatus;
import server.data.map.ETerrainType;
import server.data.map.ETreasureStatus;
import server.data.map.FullMap;
import server.data.map.FullMapTile;
import server.data.move.EMove;
import server.data.player.PlayerIdentifier;
import server.data.player.PlayerInfo;

public class ClientMessageConverter {
	
	
	public GameIdentifier convertUniqueGameIdentifier (UniqueGameIdentifier clientGameUID) {
		return new GameIdentifier(clientGameUID.getUniqueGameID());
	}
	
	public PlayerInfo convertPlayerRegistrationFromClient(PlayerRegistration clientPlayerRegistration) {
		return new PlayerInfo(
				clientPlayerRegistration.getStudentFirstName(),
				clientPlayerRegistration.getStudentLastName(),
				clientPlayerRegistration.getStudentUAccount());
	}
	
	public PlayerIdentifier convertPlayerIdentifierFromClient (String clientPlayerID) {
		return new PlayerIdentifier(clientPlayerID);
	}

	public EMove convertEMoveFromClient(messagesbase.messagesfromclient.EMove clientMove) {
		EnumMap<messagesbase.messagesfromclient.EMove, EMove> mapEMove = new EnumMap<>(messagesbase.messagesfromclient.EMove.class);
		
		mapEMove.put(messagesbase.messagesfromclient.EMove.Left, EMove.Left);
		mapEMove.put(messagesbase.messagesfromclient.EMove.Right, EMove.Right);
		mapEMove.put(messagesbase.messagesfromclient.EMove.Up, EMove.Up);
		mapEMove.put(messagesbase.messagesfromclient.EMove.Down, EMove.Down);
		
		EMove toServerMove = mapEMove.get(clientMove);
		return toServerMove;
	}
	
	public FullMap convertPlayerHalfMapFromClient(PlayerHalfMap clientMap) {		
		FullMap serverFullMap = new FullMap();
		clientMap.getMapNodes().forEach(t ->
		serverFullMap.addTile(convertPlayerHalfMapNodeFromClient(t, clientMap.getUniquePlayerID())));
		return serverFullMap;
	}

	public FullMapTile convertPlayerHalfMapNodeFromClient(PlayerHalfMapNode clientMapTile, String UniquePlayerID) {
		PlayerIdentifier playerID = convertPlayerIdentifierFromClient(UniquePlayerID);
		PlayerIdentifier fortOwner = convertIsFortPresentFromClientToFortOwner(playerID, clientMapTile.isFortPresent());
		Optional<PlayerIdentifier> treasureOwner = null;
		EFortStatus fortState = EFortStatus.NoOrUnknownFortState;
		PlayerIdentifier playerPositionOwner = null;
		
		if(fortOwner != null && fortOwner.getPlayerIdentifier().equals(playerID.getPlayerIdentifier())) {
			playerPositionOwner = fortOwner;
			fortState = EFortStatus.MyFortPresent;
		}
		else if(fortOwner != null && !(fortOwner.getPlayerIdentifier().equals(playerID.getPlayerIdentifier()))) {
			playerPositionOwner = fortOwner;
			fortState = EFortStatus.EnemyFortPresent;
		}
			
		FullMapTile toServerFullMapTile = new FullMapTile(
			clientMapTile.getX(),
			clientMapTile.getY(),
			convertETerrainFromClient(clientMapTile.getTerrain()), 
			fortOwner,
			null,
			fortState,
			ETreasureStatus.NoOrUnknownTreasureState,
			playerPositionOwner);
		
        return toServerFullMapTile;
    }
	
	public ETerrainType convertETerrainFromClient(messagesbase.messagesfromclient.ETerrain clientETerrain) {
		EnumMap<messagesbase.messagesfromclient.ETerrain, ETerrainType> mapETerrainType = new EnumMap<>(messagesbase.messagesfromclient.ETerrain.class);
	
		mapETerrainType.put(messagesbase.messagesfromclient.ETerrain.Grass, ETerrainType.Grass);
		mapETerrainType.put(messagesbase.messagesfromclient.ETerrain.Mountain, ETerrainType.Mountain);
		mapETerrainType.put(messagesbase.messagesfromclient.ETerrain.Water, ETerrainType.Water);
		
		ETerrainType serverETerrain = mapETerrainType.get(clientETerrain);
		return serverETerrain;
	}
	
	public PlayerIdentifier convertIsFortPresentFromClientToFortOwner(PlayerIdentifier playerID, boolean isFortPresent) {
		if(isFortPresent) {
			return new PlayerIdentifier(playerID.getPlayerIdentifier());
		}
		else
			return null;
	}
}
