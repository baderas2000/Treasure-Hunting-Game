package communication;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collection;

import org.junit.jupiter.api.Test;

import data.logic.EPlayerGameState;
import data.logic.GameState;
import data.map.EFortState;
import data.map.EPlayerPositionState;
import data.map.ETerrain;
import data.map.ETreasureState;
import data.map.FullMap;
import data.map.MapTile;
import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromserver.PlayerState;

class ServerMessageConverterTest {
	ServerMessageConverter toClientConverter = new ServerMessageConverter();

	@Test
	void convertPlayerGameState_messagesfromserverEPlayerGameState_shouldReturnEPlayerGameState() {
		messagesbase.messagesfromserver.EPlayerGameState mustActServer = messagesbase.messagesfromserver.EPlayerGameState.MustAct;
		messagesbase.messagesfromserver.EPlayerGameState mustWaitServer = messagesbase.messagesfromserver.EPlayerGameState.MustWait;
		messagesbase.messagesfromserver.EPlayerGameState wonServer = messagesbase.messagesfromserver.EPlayerGameState.Won;
		messagesbase.messagesfromserver.EPlayerGameState lostServer = messagesbase.messagesfromserver.EPlayerGameState.Lost;
		
		EPlayerGameState mustActClientExpected = EPlayerGameState.MustAct;
		EPlayerGameState mustWaitClientExpected = EPlayerGameState.MustWait;
		EPlayerGameState wonClientExpected = EPlayerGameState.Won;
		EPlayerGameState lostClientExpected = EPlayerGameState.Lost;
		
		EPlayerGameState mustActClientResult = toClientConverter.convertPlayerGameState(mustActServer);
		EPlayerGameState mustWaitClientResult = toClientConverter.convertPlayerGameState(mustWaitServer);
		EPlayerGameState wonClientResult = toClientConverter.convertPlayerGameState(wonServer);
		EPlayerGameState lostClientResult = toClientConverter.convertPlayerGameState(lostServer);
		
		
		assertTrue(mustActClientExpected == mustActClientResult &&
				mustWaitClientExpected == mustWaitClientResult &&
						wonClientExpected == wonClientResult &&
						lostClientExpected == lostClientResult);
	}
	
	@Test
	void convertEFortState_messagesfromserverEFortState_shouldReturnEFortState() {
		messagesbase.messagesfromserver.EFortState EnemyFortPresentServer = messagesbase.messagesfromserver.EFortState.EnemyFortPresent;
		messagesbase.messagesfromserver.EFortState MyFortPresentServer = messagesbase.messagesfromserver.EFortState.MyFortPresent;
		messagesbase.messagesfromserver.EFortState NoOrUnknownFortStateServer = messagesbase.messagesfromserver.EFortState.NoOrUnknownFortState;
		
		EFortState EnemyFortPresentExpected = EFortState.EnemyFortPresent;
		EFortState MyFortPresentExpected = EFortState.MyFortPresent;
		EFortState NoOrUnknownFortStateExpected = EFortState.NoOrUnknownFortState;
		
		EFortState EnemyFortPresentResult = toClientConverter.convertEFortState(EnemyFortPresentServer);
		EFortState MyFortPresentResult = toClientConverter.convertEFortState(MyFortPresentServer);
		EFortState NoOrUnknownFortStateResult = toClientConverter.convertEFortState(NoOrUnknownFortStateServer);
		
		assertTrue(EnemyFortPresentExpected == EnemyFortPresentResult &&
				MyFortPresentExpected == MyFortPresentResult &&
						NoOrUnknownFortStateExpected == NoOrUnknownFortStateResult);
	}
	
	@Test
	void convertEPlayerPositionState_messagesfromserverEPlayerPositionState_shouldReturnEPlayerPositionState() {
		messagesbase.messagesfromserver.EPlayerPositionState BothPlayerPositionServer = messagesbase.messagesfromserver.EPlayerPositionState.BothPlayerPosition;
		messagesbase.messagesfromserver.EPlayerPositionState EnemyPlayerPositionServer = messagesbase.messagesfromserver.EPlayerPositionState.EnemyPlayerPosition;
		messagesbase.messagesfromserver.EPlayerPositionState MyPlayerPositionServer = messagesbase.messagesfromserver.EPlayerPositionState.MyPlayerPosition;
		messagesbase.messagesfromserver.EPlayerPositionState NoPlayerPresentServer = messagesbase.messagesfromserver.EPlayerPositionState.NoPlayerPresent;
				
		EPlayerPositionState BothPlayerPositionExpected = EPlayerPositionState.BothPlayerPosition;
		EPlayerPositionState EnemyPlayerPositionExpected = EPlayerPositionState.EnemyPlayerPosition;
		EPlayerPositionState MyPlayerPositionExpected = EPlayerPositionState.MyPlayerPosition;
		EPlayerPositionState NoPlayerPresentExpected = EPlayerPositionState.NoPlayerPresent;
		
		EPlayerPositionState BothPlayerPositionResult = toClientConverter.convertEPlayerPositionState(BothPlayerPositionServer);
		EPlayerPositionState EnemyPlayerPositionResult = toClientConverter.convertEPlayerPositionState(EnemyPlayerPositionServer);
		EPlayerPositionState MyPlayerPositionResult = toClientConverter.convertEPlayerPositionState(MyPlayerPositionServer);
		EPlayerPositionState NoPlayerPresentResult = toClientConverter.convertEPlayerPositionState(NoPlayerPresentServer);
		
		assertTrue(BothPlayerPositionExpected == BothPlayerPositionResult &&
				EnemyPlayerPositionExpected == EnemyPlayerPositionResult &&
						MyPlayerPositionExpected == MyPlayerPositionResult &&
						NoPlayerPresentExpected == NoPlayerPresentResult);
	}
	
	@Test
	void convertETreasureState_messagesfromserverETreasureState_shouldReturnETreasureState() {
		messagesbase.messagesfromserver.ETreasureState MyTreasureIsPresentServer = messagesbase.messagesfromserver.ETreasureState.MyTreasureIsPresent;
		messagesbase.messagesfromserver.ETreasureState NoOrUnknownTreasureStateServer = messagesbase.messagesfromserver.ETreasureState.NoOrUnknownTreasureState;
		
		ETreasureState MyTreasureIsPresentExpected = ETreasureState.MyTreasureIsPresent;
		ETreasureState NoOrUnknownTreasureStateExpected = ETreasureState.NoOrUnknownTreasureState;
		
		ETreasureState MyTreasureIsPresentResult = toClientConverter.convertETreasureState(MyTreasureIsPresentServer);
		ETreasureState NoOrUnknownTreasureStateResult = toClientConverter.convertETreasureState(NoOrUnknownTreasureStateServer);
		
		assertTrue(MyTreasureIsPresentExpected == MyTreasureIsPresentResult &&
				NoOrUnknownTreasureStateExpected == NoOrUnknownTreasureStateResult);
	}
	
	@Test
	void convertGameState_messagesfromserverGameState_shouldReturnGameState() {
		UniquePlayerIdentifier player1ID = new UniquePlayerIdentifier("09809");
		messagesbase.messagesfromserver.PlayerState player1 = new messagesbase.messagesfromserver.PlayerState("Sofiia", "Badera", "baderas17", messagesbase.messagesfromserver.EPlayerGameState.MustAct, player1ID, false);
		messagesbase.messagesfromserver.PlayerState player2 = new messagesbase.messagesfromserver.PlayerState("Max", "Musterman", "max17", messagesbase.messagesfromserver.EPlayerGameState.MustWait, new UniquePlayerIdentifier("07807"), false);
		Collection<messagesbase.messagesfromserver.PlayerState> players = Arrays.asList(player1, player2);
		
		messagesbase.messagesfromserver.FullMapNode node1 = new messagesbase.messagesfromserver.FullMapNode(messagesbase.messagesfromclient.ETerrain.Grass, messagesbase.messagesfromserver.EPlayerPositionState.MyPlayerPosition, messagesbase.messagesfromserver.ETreasureState.NoOrUnknownTreasureState, messagesbase.messagesfromserver.EFortState.MyFortPresent, 1, 2);
		messagesbase.messagesfromserver.FullMapNode node2 = new messagesbase.messagesfromserver.FullMapNode(messagesbase.messagesfromclient.ETerrain.Mountain, messagesbase.messagesfromserver.EPlayerPositionState.NoPlayerPresent,messagesbase.messagesfromserver.ETreasureState.MyTreasureIsPresent, messagesbase.messagesfromserver.EFortState.NoOrUnknownFortState,3, 4);
		Collection<messagesbase.messagesfromserver.FullMapNode> nodes = Arrays.asList(node1, node2);
		messagesbase.messagesfromserver.FullMap map = new messagesbase.messagesfromserver.FullMap(nodes);
		
		messagesbase.messagesfromserver.GameState GameStateServer =  new messagesbase.messagesfromserver.GameState(map, players, "12345");
		UniquePlayerIdentifier IDResult = new UniquePlayerIdentifier("09809");
		GameState GameStateResult =  toClientConverter.convertGameState(GameStateServer, IDResult);
		
		data.logic.PlayerState playerStateExpected = new data.logic.PlayerState(false, EPlayerGameState.MustAct);
		FullMap mapExpected = new FullMap();
		MapTile tile1 = new MapTile (1, 2, ETerrain.Grass, EFortState.MyFortPresent, ETreasureState.NoOrUnknownTreasureState, 
							EPlayerPositionState.MyPlayerPosition);
		MapTile tile2 = new MapTile (3, 4, ETerrain.Mountain, EFortState.NoOrUnknownFortState, ETreasureState.MyTreasureIsPresent, 
							EPlayerPositionState.NoPlayerPresent);
		mapExpected.addTile(tile1);
		mapExpected.addTile(tile2);
		
		GameState GameStateExpected = new GameState(mapExpected, playerStateExpected);
		
		
		assertTrue(GameStateExpected.getPlayerState().getPlayerGameState() == GameStateResult.getPlayerState().getPlayerGameState()
		&& GameStateExpected.getMap().equals(GameStateResult.getMap()));
	}

}
