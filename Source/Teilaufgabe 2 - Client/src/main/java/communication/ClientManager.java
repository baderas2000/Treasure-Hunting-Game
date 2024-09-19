package communication;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import messagesbase.UniquePlayerIdentifier;
import visualization.CLI;
import communication.ClientMessageConverter;
import communication.ServerMessageConverter;
import data.logic.EPlayerGameState;
import data.logic.GameState;
import data.logic.GameLogic;
import data.logic.EActionType;
import data.logic.EMove;
import data.map.FullMap;

import java.util.Optional;


public class ClientManager {
	private ClientMessageConverter clientMessageConverter;
	private ServerMessageConverter serverMessageConverter;
	private PlayerInfo playerInfo;
	private GameInfo gameInfo;
	private GameLogic gameLogic;
	private Network network;
	private CLI view;
	private Optional<UniquePlayerIdentifier> playerUID;

	public ClientManager(PlayerInfo playerInfo, GameInfo gameInfo) {
		this.playerInfo = playerInfo;
		this.gameInfo = gameInfo;
		this.network = new Network(gameInfo);
		this.view = new CLI();
		this.clientMessageConverter = new ClientMessageConverter();
		this.serverMessageConverter = new ServerMessageConverter();
		PropertyChangeSupport pcs = new PropertyChangeSupport(this.view);
		pcs.addPropertyChangeListener("lastmove", view);
		pcs.addPropertyChangeListener("map", view);
		pcs.addPropertyChangeListener("gamephase", view);
		this.gameLogic = new GameLogic(pcs);
	}

	public void startGame() {
		EActionType actionType;
		this.registerPlayer(this.playerInfo);
		while(!this.gameLogic.isGameFinished()) {
			GameState gameState = this.requestStatus();
			actionType = this.gameLogic.updateGameState(gameState);
			if (actionType == EActionType.SendMap) {
				FullMap map = this.gameLogic.getMap();
				this.sendMap(map);
			} else if (actionType == EActionType.SendMove) {
				EMove move = this.gameLogic.getMove();
				this.sendMove(move);
			}
		}
	}

	private GameState requestStatus() {
		messagesbase.messagesfromserver.GameState rawGameState = this.network.requestStatus();
		return this.serverMessageConverter.convertGameState(rawGameState, this.getPlayerID());
	}

	private boolean registerPlayer(PlayerInfo playerInfo) {
		messagesbase.messagesfromclient.PlayerRegistration playerRegistration =
				this.clientMessageConverter.convertPlayerRegistration(playerInfo);
		UniquePlayerIdentifier playerUID = this.network.registerPlayer(playerRegistration);
		this.playerUID = Optional.of(playerUID);
		return true;
	}

	private boolean sendMove(EMove move) {
		messagesbase.messagesfromclient.PlayerMove playerMove = this.clientMessageConverter.convertPlayerMove(
				this.getPlayerID(), move);
		this.network.sendMove(playerMove);
		return true;
	}

	private boolean sendMap(FullMap map) {
		messagesbase.messagesfromclient.PlayerHalfMap playerHalfMap = this.clientMessageConverter.convertFullMap(
				this.getPlayerID(), map);
		this.network.sendMap(playerHalfMap);
		return true;
	}

	public UniquePlayerIdentifier getPlayerID() {
		return this.playerUID.orElseThrow(() -> new RuntimeException("Player is not registered"));
	}

}
