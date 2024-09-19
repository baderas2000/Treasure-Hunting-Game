package communication;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Set;

import data.logic.EMove;
import data.map.EFortState;
import data.map.ETerrain;
import data.map.FullMap;
import data.map.MapTile;
import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromclient.PlayerHalfMap;
import messagesbase.messagesfromclient.PlayerHalfMapNode;
import messagesbase.messagesfromclient.PlayerMove;
import messagesbase.messagesfromclient.PlayerRegistration;

public class ClientMessageConverter {
	
	public messagesbase.messagesfromclient.EMove convertEMove(EMove clientMove) {
		EnumMap<EMove, messagesbase.messagesfromclient.EMove> mapEMove = new EnumMap<>(EMove.class);
		mapEMove.put(EMove.Left, messagesbase.messagesfromclient.EMove.Left);
		mapEMove.put(EMove.Right, messagesbase.messagesfromclient.EMove.Right);
		mapEMove.put(EMove.Up, messagesbase.messagesfromclient.EMove.Up);
		mapEMove.put(EMove.Down, messagesbase.messagesfromclient.EMove.Down);
		messagesbase.messagesfromclient.EMove toServerMove = mapEMove.get(clientMove);
		return toServerMove;
	}

	public PlayerHalfMap convertFullMap(UniquePlayerIdentifier uniquePlayerID, FullMap clientFullMap) {
		Set<PlayerHalfMapNode> nodes = new HashSet<PlayerHalfMapNode>();
		clientFullMap.stream().forEach(t ->	nodes.add(convertMapTile(t)));
		PlayerHalfMap toServerHalfMap = new PlayerHalfMap(uniquePlayerID, nodes);
		return toServerHalfMap;
	}

	public PlayerRegistration convertPlayerRegistration(PlayerInfo clientPlayerInfo) {
		PlayerRegistration toServerPlayerRegistration = new PlayerRegistration(
				clientPlayerInfo.getStudentFirstName(),
				clientPlayerInfo.getStudentLastName(),
				clientPlayerInfo.getStudentUAccount());
		return toServerPlayerRegistration;
	}

	public messagesbase.messagesfromclient.ETerrain convertETerrain(ETerrain clientETerrain) {
		EnumMap<ETerrain, messagesbase.messagesfromclient.ETerrain> mapETerrainType = new EnumMap<>(ETerrain.class);
		mapETerrainType.put(ETerrain.Grass, messagesbase.messagesfromclient.ETerrain.Grass);
		mapETerrainType.put(ETerrain.Mountain, messagesbase.messagesfromclient.ETerrain.Mountain);
		mapETerrainType.put(ETerrain.Water, messagesbase.messagesfromclient.ETerrain.Water);
		messagesbase.messagesfromclient.ETerrain toServerETerrain = mapETerrainType.get(clientETerrain);
		return toServerETerrain;
	}

	public PlayerHalfMapNode convertMapTile(MapTile clientMapTile) {
		PlayerHalfMapNode toServerHalfMapNode = new PlayerHalfMapNode(
				clientMapTile.getX(),
				clientMapTile.getY(),
				//TODO: Check if getFortState does not cause the errors
				clientMapTile.getFortState() == EFortState.MyFortPresent,
				convertETerrain(clientMapTile.getTerrainType()));
        return toServerHalfMapNode;
    }
	
	public PlayerMove convertPlayerMove(UniquePlayerIdentifier uniquePlayerID, EMove move) {
		PlayerMove toServerPlayerMove = PlayerMove.of(uniquePlayerID, convertEMove(move));
		return toServerPlayerMove;
	}
	
}

