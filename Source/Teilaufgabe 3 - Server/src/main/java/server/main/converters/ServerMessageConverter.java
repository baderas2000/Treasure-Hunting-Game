package server.main.converters;

import java.util.ArrayList;
import java.util.EnumMap;

import messagesbase.UniqueGameIdentifier;
import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromserver.FullMapNode;
import server.data.game.GameIdentifier;
import server.data.map.EFortStatus;
import server.data.map.EPlayerPositionStatus;
import server.data.map.ETerrainType;
import server.data.map.ETreasureStatus;
import server.data.map.FullMap;
import server.data.map.FullMapTile;
import server.data.player.EPlayerGameStatus;
import server.data.player.ERequestState;
import server.data.player.PlayerIdentifier;
import server.data.player.PlayerStatus;

public class ServerMessageConverter {
	
	public UniqueGameIdentifier convertGameIdentifier (GameIdentifier serverGameID) {
		return new UniqueGameIdentifier(serverGameID.getGameID());
	}
	
	public UniquePlayerIdentifier convertPlayerIdentifierFromServer (String serverPlayerID) {
		return new UniquePlayerIdentifier(serverPlayerID);
	}
	
	public messagesbase.messagesfromserver.FullMap convertHalfMapFromServer(FullMap serverMap, String player, String enemy) {		
		ArrayList<FullMapNode> nodes = new ArrayList<FullMapNode>();
		serverMap.setRandomEnemyPosition(enemy);
		serverMap.stream().forEach(t ->
		nodes.add(convertHalfMapTileFromServer(t, player)));
		messagesbase.messagesfromserver.FullMap clientFullMap = new messagesbase.messagesfromserver.FullMap(nodes);
		return clientFullMap;
	}
	
	public FullMapNode convertHalfMapTileFromServer(FullMapTile serverHalfMapTile, String player) {
		EFortStatus fortState = EFortStatus.NoOrUnknownFortState;
		ETreasureStatus treasureState = ETreasureStatus.NoOrUnknownTreasureState;
		EPlayerPositionStatus playerPosState = EPlayerPositionStatus.NoPlayerPresent;
		
		if(serverHalfMapTile.getFortOwner().isPresent()) {
			PlayerIdentifier tileFortOwner = serverHalfMapTile.getFortOwner().get();
			if(tileFortOwner.getPlayerIdentifier().equals(player)) {
				fortState = EFortStatus.MyFortPresent;
			}
		}
		
		if(serverHalfMapTile.getTreasureOwner().isPresent()) {
			PlayerIdentifier tileTreasureOwner = serverHalfMapTile.getTreasureOwner().get();
			if(tileTreasureOwner.getPlayerIdentifier().equals(player)) {
				treasureState = ETreasureStatus.MyTreasureIsPresent;
			}
		}
		
		if(!(serverHalfMapTile.getPresentPlayers().isEmpty())) {
			int numberOfPlayers = serverHalfMapTile.getPresentPlayers().size();
			if(numberOfPlayers == 1) {
				PlayerIdentifier positionOwner = serverHalfMapTile.getPresentPlayers().get(0);
				if(positionOwner != null) {
					if(positionOwner.getPlayerIdentifier().equals(player)) {
						playerPosState = EPlayerPositionStatus.MyPlayerPosition;
					}
					else if(!(positionOwner.getPlayerIdentifier().equals(player))) {
						playerPosState = EPlayerPositionStatus.EnemyPlayerPosition;
					}
				}
			}
			else if (numberOfPlayers == 2) {
				playerPosState = EPlayerPositionStatus.BothPlayerPosition;
			}
		}
		
		FullMapNode toClientFullMapNode = new FullMapNode(
				convertETerrainTypeFromServer(serverHalfMapTile.getTerrainType()), 
				convertEPlayerPositionStateFromServer(playerPosState), 
				convertETreasureStateFromServer(treasureState), 
				convertEFortStateFromServer(fortState), 
				serverHalfMapTile.getX(), 
				serverHalfMapTile.getY());
		
        return toClientFullMapNode;
    }

	public  messagesbase.messagesfromclient.ETerrain convertETerrainTypeFromServer(ETerrainType serverETerrainType) {
		EnumMap<ETerrainType, messagesbase.messagesfromclient.ETerrain> mapETerrainType = new EnumMap<>(ETerrainType.class);
	
		mapETerrainType.put(ETerrainType.Grass, messagesbase.messagesfromclient.ETerrain.Grass);
		mapETerrainType.put(ETerrainType.Mountain, messagesbase.messagesfromclient.ETerrain.Mountain);
		mapETerrainType.put(ETerrainType.Water, messagesbase.messagesfromclient.ETerrain.Water);
		
		messagesbase.messagesfromclient.ETerrain toClientETerrain = mapETerrainType.get(serverETerrainType);
		return toClientETerrain;
	}

	public messagesbase.messagesfromserver.EPlayerPositionState convertEPlayerPositionStateFromServer(EPlayerPositionStatus serverEPlayerPositionState) {
		EnumMap<EPlayerPositionStatus, messagesbase.messagesfromserver.EPlayerPositionState> mapEPlayerPositionState = new EnumMap<>(EPlayerPositionStatus.class);
	
		mapEPlayerPositionState.put(EPlayerPositionStatus.BothPlayerPosition, messagesbase.messagesfromserver.EPlayerPositionState.BothPlayerPosition);
		mapEPlayerPositionState.put(EPlayerPositionStatus.EnemyPlayerPosition, messagesbase.messagesfromserver.EPlayerPositionState.EnemyPlayerPosition);
		mapEPlayerPositionState.put(EPlayerPositionStatus.MyPlayerPosition, messagesbase.messagesfromserver.EPlayerPositionState.MyPlayerPosition);
		mapEPlayerPositionState.put(EPlayerPositionStatus.NoPlayerPresent, messagesbase.messagesfromserver.EPlayerPositionState.NoPlayerPresent);
		
		messagesbase.messagesfromserver.EPlayerPositionState toClientEPlayerPositionState = mapEPlayerPositionState.get(serverEPlayerPositionState);
		return toClientEPlayerPositionState;
	}

	public messagesbase.messagesfromserver.ETreasureState convertETreasureStateFromServer(ETreasureStatus serverETreasureState) {
		EnumMap<ETreasureStatus, messagesbase.messagesfromserver.ETreasureState> mapETreasureState = new EnumMap<>(ETreasureStatus.class);
	
		mapETreasureState.put(ETreasureStatus.MyTreasureIsPresent, messagesbase.messagesfromserver.ETreasureState.MyTreasureIsPresent);
		mapETreasureState.put(ETreasureStatus.NoOrUnknownTreasureState, messagesbase.messagesfromserver.ETreasureState.NoOrUnknownTreasureState);
		
		messagesbase.messagesfromserver.ETreasureState toClientETreasureState = mapETreasureState.get(serverETreasureState);
		return toClientETreasureState;
	}

	public messagesbase.messagesfromserver.EFortState convertEFortStateFromServer(EFortStatus serverEFortState) {
		EnumMap<EFortStatus, messagesbase.messagesfromserver.EFortState> mapEFortState = new EnumMap<>(EFortStatus.class);
	
		mapEFortState.put(EFortStatus.EnemyFortPresent, messagesbase.messagesfromserver.EFortState.EnemyFortPresent);
		mapEFortState.put(EFortStatus.MyFortPresent, messagesbase.messagesfromserver.EFortState.MyFortPresent);
		mapEFortState.put(EFortStatus.NoOrUnknownFortState, messagesbase.messagesfromserver.EFortState.NoOrUnknownFortState);
		
		messagesbase.messagesfromserver.EFortState toClientEFortState = mapEFortState.get(serverEFortState);
		return toClientEFortState;
	}

	public messagesbase.messagesfromserver.PlayerState convertPlayerStateFromServer(PlayerStatus serverPlayerState) {
		
		messagesbase.messagesfromserver.PlayerState toClientPlayerState = new messagesbase.messagesfromserver.PlayerState(
				serverPlayerState.getPlayerInfo().getStudentFirstName(), 
				serverPlayerState.getPlayerInfo().getStudentLastName(), 
				serverPlayerState.getPlayerInfo().getStudentAccount(), 
				convertPlayerGameStateFromServer(serverPlayerState.getPlayerGameState()), 
				convertPlayerIdentifierFromServer(serverPlayerState.getPlayerIdentifier()), 
				serverPlayerState.isCollectedTreasure());
		
        return toClientPlayerState;
    }
	
	public messagesbase.messagesfromserver.EPlayerGameState convertPlayerGameStateFromServer(EPlayerGameStatus serverState) {
		EnumMap<EPlayerGameStatus, messagesbase.messagesfromserver.EPlayerGameState> mapEPlayerGameState = new EnumMap<>(EPlayerGameStatus.class);
		
		mapEPlayerGameState.put(EPlayerGameStatus.Lost, messagesbase.messagesfromserver.EPlayerGameState.Lost);
		mapEPlayerGameState.put(EPlayerGameStatus.Won, messagesbase.messagesfromserver.EPlayerGameState.Won);
		mapEPlayerGameState.put(EPlayerGameStatus.MustMove, messagesbase.messagesfromserver.EPlayerGameState.MustAct);
		mapEPlayerGameState.put(EPlayerGameStatus.MustSentMap, messagesbase.messagesfromserver.EPlayerGameState.MustAct);
		mapEPlayerGameState.put(EPlayerGameStatus.MustWait, messagesbase.messagesfromserver.EPlayerGameState.MustWait);
		
		messagesbase.messagesfromserver.EPlayerGameState clientState = mapEPlayerGameState.get(serverState);
		return clientState;
	}
	
	public messagesbase.messagesfromclient.ERequestState convertERequestStateFromServer(ERequestState serverRequestState) {
		EnumMap<ERequestState, messagesbase.messagesfromclient.ERequestState> mapERequestState = new EnumMap<>(ERequestState.class);
		
		mapERequestState.put(ERequestState.Okay, messagesbase.messagesfromclient.ERequestState.Okay);
		mapERequestState.put(ERequestState.Error, messagesbase.messagesfromclient.ERequestState.Error);
		
		messagesbase.messagesfromclient.ERequestState clientRequestState = mapERequestState.get(serverRequestState);
		return clientRequestState;
	}
}
