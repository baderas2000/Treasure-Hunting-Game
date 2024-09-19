package communication;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import data.logic.EMove;
import data.logic.EPlayerGameState;
import data.logic.GameLogic;
import data.logic.GameState;
import data.logic.PlayerState;
import data.map.EFortState;
import data.map.EPlayerPositionState;
import data.map.ETerrain;
import data.map.ETreasureState;
import data.map.FullMap;
import data.map.MapTile;
import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromclient.PlayerHalfMap;
import messagesbase.messagesfromclient.PlayerHalfMapNode;
import messagesbase.messagesfromclient.PlayerMove;
import messagesbase.messagesfromclient.PlayerRegistration;

class ClientMessageConverterTest {
	
	ClientMessageConverter toServerConverter = new ClientMessageConverter();

	@Test
	void convertPlayerRegistration_PlayerInfo_shouldReturnToServerPlayerRegistration() {
		PlayerInfo clientPlayerInfo = new PlayerInfo("Sofiia", "Badera", "baderas17");
		PlayerRegistration result = toServerConverter.convertPlayerRegistration(clientPlayerInfo);
		PlayerRegistration expectedResult = new PlayerRegistration("Sofiia", "Badera", "baderas17");
		assertTrue(expectedResult.getStudentFirstName()== result.getStudentFirstName() &&
				expectedResult.getStudentLastName()== result.getStudentLastName() &&
				expectedResult.getStudentUAccount()== result.getStudentUAccount());

	}
	
	@Test
	void convertEMove_EMove_shouldReturnToServerEMove() {
		EMove upMoveClient = EMove.Up;
		EMove downMoveClient = EMove.Down;
		EMove leftMoveClient = EMove.Left;
		EMove rightMoveClient = EMove.Right;
		
		messagesbase.messagesfromclient.EMove upMoveServerExpected = messagesbase.messagesfromclient.EMove.Up;
		messagesbase.messagesfromclient.EMove downMoveServerExpected = messagesbase.messagesfromclient.EMove.Down;
		messagesbase.messagesfromclient.EMove leftMoveServerExpected = messagesbase.messagesfromclient.EMove.Left;
		messagesbase.messagesfromclient.EMove rightMoveServerExpected = messagesbase.messagesfromclient.EMove.Right;
		
		messagesbase.messagesfromclient.EMove upMoveServerResult = toServerConverter.convertEMove(upMoveClient);
		messagesbase.messagesfromclient.EMove downMoveServerResult = toServerConverter.convertEMove(downMoveClient);
		messagesbase.messagesfromclient.EMove leftMoveServerResult = toServerConverter.convertEMove(leftMoveClient);
		messagesbase.messagesfromclient.EMove rightMoveServerResult = toServerConverter.convertEMove(rightMoveClient);
		
		assertTrue(upMoveServerExpected == upMoveServerResult &&
				downMoveServerExpected == downMoveServerResult &&
				leftMoveServerExpected == leftMoveServerResult  &&
				rightMoveServerExpected == rightMoveServerResult );
	}
	
	@Test
	void convertETerrain_ETerrain_shouldReturnToServerETerrain() {
		ETerrain grassClient = ETerrain.Grass;
		ETerrain mountainClient = ETerrain.Mountain;
		ETerrain waterClient = ETerrain.Water;
		
		messagesbase.messagesfromclient.ETerrain grassServerExpected = messagesbase.messagesfromclient.ETerrain.Grass;
		messagesbase.messagesfromclient.ETerrain mountainServerExpected = messagesbase.messagesfromclient.ETerrain.Mountain;
		messagesbase.messagesfromclient.ETerrain waterServerExpected = messagesbase.messagesfromclient.ETerrain.Water;
		
		messagesbase.messagesfromclient.ETerrain grassServerResult = toServerConverter.convertETerrain(grassClient);
		messagesbase.messagesfromclient.ETerrain mountainServerResult = toServerConverter.convertETerrain(mountainClient);
		messagesbase.messagesfromclient.ETerrain waterServerResult = toServerConverter.convertETerrain(waterClient);
		
		assertTrue(grassServerExpected == grassServerResult &&
				mountainServerExpected == mountainServerResult &&
				waterServerExpected == waterServerResult);
	}
	
	@Test
	void convertPlayerMove_EMoveAndPlayerID_shouldReturnToServerPlayerMove() {
		UniquePlayerIdentifier id = new UniquePlayerIdentifier("12345");
		PlayerMove playerMoveExpected = PlayerMove.of("12345", messagesbase.messagesfromclient.EMove.Up);
		PlayerMove playerMoveResult = toServerConverter.convertPlayerMove(id, EMove.Up);
		assertTrue(playerMoveExpected.getUniquePlayerID() == playerMoveResult.getUniquePlayerID() &&
					playerMoveExpected.getMove() == playerMoveResult.getMove());
	}
	
	@Test
	void convertMapTile_MapTile_shouldReturnPlayerHalfMapNode(){
		MapTile tileClient = new MapTile(1, 2, ETerrain.Grass,EFortState.MyFortPresent,
				ETreasureState.MyTreasureIsPresent, EPlayerPositionState.MyPlayerPosition);
		PlayerHalfMapNode playerHalfMapNodeExpected = new PlayerHalfMapNode(1, 2, true, messagesbase.messagesfromclient.ETerrain.Grass);
		PlayerHalfMapNode playerHalfMapNodeResult = toServerConverter.convertMapTile(tileClient);
		assertTrue(playerHalfMapNodeExpected.getX() == playerHalfMapNodeResult.getX() &&
				playerHalfMapNodeExpected.getY() == playerHalfMapNodeResult.getY() &&
				playerHalfMapNodeExpected.getTerrain() == playerHalfMapNodeResult.getTerrain() &&
				playerHalfMapNodeExpected.isFortPresent() == playerHalfMapNodeResult.isFortPresent());
	}
	
	@Test
	void convertFullMap_PlayerIDAndFullMap_shouldReturnPlayerHalfMap(){
		FullMap clientFullMap = new FullMap();
		
		for (int i=0; i<10; i++) {
			for (int j=0; j<5; j++) {
				clientFullMap.addTile(new MapTile(i, j, ETerrain.Grass));
			}
		}
		
		Set<PlayerHalfMapNode> nodes = new HashSet<PlayerHalfMapNode>();
		clientFullMap.stream().forEach(t ->	nodes.add(toServerConverter.convertMapTile(t)));
		
		UniquePlayerIdentifier id = new UniquePlayerIdentifier("12345");
		PlayerHalfMap playerHalfMapExpected = new PlayerHalfMap("12345", nodes);
		PlayerHalfMap playerHalfMapResult = toServerConverter.convertFullMap(id, clientFullMap);
		
		assertTrue(playerHalfMapExpected.getMapNodes().toString().equals(playerHalfMapResult.getMapNodes().toString())  
				&&	playerHalfMapExpected.getUniquePlayerID() == playerHalfMapResult.getUniquePlayerID()
				);
	}
	
	


}
