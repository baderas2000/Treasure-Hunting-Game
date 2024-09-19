package server.main;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import messagesbase.ResponseEnvelope;
import messagesbase.UniqueGameIdentifier;
import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromclient.ERequestState;
import messagesbase.messagesfromclient.PlayerHalfMap;
import messagesbase.messagesfromclient.PlayerRegistration;
import messagesbase.messagesfromserver.GameState;
import messagesbase.messagesfromserver.PlayerState;
import server.data.game.GameEntity;
import server.data.game.GameIdentifier;
import server.data.game.GameStatus;
import server.data.map.FullMap;
import server.data.map.GameMap;
import server.data.player.PlayerIdentifier;
import server.data.player.PlayerInfo;
import server.data.player.PlayerStatus;
import server.exceptions.BusinessRuleViolationException;
import server.exceptions.GenericException;
import server.logic.GameManager;
import server.logic.MapManager;
import server.logic.PlayerManager;
import server.main.converters.ClientMessageConverter;
import server.main.converters.ServerMessageConverter;


@RestController
@RequestMapping(value = "/games")
@EnableScheduling
@ComponentScan(basePackages = "server.logic")
public class ServerEndpoints {
	
	final static Logger logger = LoggerFactory.getLogger(ServerEndpoints.class);

	private ServerMessageConverter serverConverter = new ServerMessageConverter();
	private ClientMessageConverter clientConverter = new ClientMessageConverter();
	
	@Autowired
	private GameManager gameManager;
	
    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody UniqueGameIdentifier newGame(
			@RequestParam(required = false, defaultValue = "false", value = "enableDebugMode") boolean enableDebugMode,
			@RequestParam(required = false, defaultValue = "false", value = "enableDummyCompetition") boolean enableDummyCompetition) {
    	
    	GameIdentifier localGameID = gameManager.createGame();
    	logger.info("Create game " + localGameID.getGameID()); 
    	
		return new UniqueGameIdentifier(localGameID.getGameID());
	}

    @RequestMapping(value = "/{gameID}/players", method = RequestMethod.POST, consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody ResponseEnvelope<UniquePlayerIdentifier> registerPlayer(
			@Validated @PathVariable UniqueGameIdentifier gameID,
			@Validated @RequestBody PlayerRegistration playerRegistration) {
   	
    	logger.info("Request to register player in game " + gameID.getUniqueGameID());   	
    	GameIdentifier gameUID = clientConverter.convertUniqueGameIdentifier(gameID);
    	GameEntity currentGame = gameManager.getGame(gameUID);
    	PlayerInfo playerInfo = clientConverter.convertPlayerRegistrationFromClient(playerRegistration);
    	PlayerManager playerManager = new PlayerManager(currentGame);
    	PlayerIdentifier newPlayer = playerManager.registerPlayer(playerInfo);
    	UniquePlayerIdentifier playerID = serverConverter.convertPlayerIdentifierFromServer(newPlayer.getPlayerIdentifier());
    	
    	playerManager.setMustSendMapStatusRandomly();
    	
    	ResponseEnvelope<UniquePlayerIdentifier> playerIDMessage = new ResponseEnvelope<>(playerID);
		return playerIDMessage;
	}
    
    @RequestMapping(value = "/{gameID}/halfmaps", method = RequestMethod.POST, consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody ResponseEnvelope<ERequestState> supplementMap (
			@Validated @PathVariable UniqueGameIdentifier gameID,
			@Validated @RequestBody PlayerHalfMap playerHalfMap) {
    	
    	logger.info("Request to send half map in game " + gameID.getUniqueGameID() + " by Player: " + playerHalfMap.getUniquePlayerID());
    	GameIdentifier gameUID = clientConverter.convertUniqueGameIdentifier(gameID);
    	GameEntity currentGame = gameManager.getGame(gameUID);
    	String recievedPlayerID = playerHalfMap.getUniquePlayerID();
    	PlayerManager playerManager = new PlayerManager(currentGame);  	
    	PlayerStatus player = playerManager.getSinglePlayer(recievedPlayerID);
    	PlayerStatus enemy = playerManager.getEnemyPlayer(recievedPlayerID);
    	MapManager mapManager = new MapManager(currentGame);	
    	GameMap mapHalf = clientConverter.convertPlayerHalfMapFromClient(playerHalfMap);

    	boolean playerMayAct = playerManager.checkPlayerBusinessRulesForHalfMapPost(player);
    	server.data.player.ERequestState requestState = server.data.player.ERequestState.Error;
    	
    	try {
    		mapManager.validateMapHalf(mapHalf);
    		requestState = server.data.player.ERequestState.Okay;
			mapManager.setFullMap(mapHalf);
			
    	}catch (BusinessRuleViolationException e) {
    		playerManager.setPlayerVictory(player, enemy);
			throw new BusinessRuleViolationException("Map Business Rule Violation: " , e.getMessage());
		}
        if(playerMayAct && requestState == server.data.player.ERequestState.Okay) {
        	playerManager.setMustSendMoveStatus(player, enemy);
        }
        ERequestState requestStateToClient = serverConverter.convertERequestStateFromServer(requestState);
    	return new ResponseEnvelope<ERequestState>(requestStateToClient);
	}
	
    @RequestMapping(value = "/{gameID}/states/{playerID}", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody ResponseEnvelope<GameState> sendGameState(
			@Validated @PathVariable UniqueGameIdentifier gameID, 
			@Validated @PathVariable UniquePlayerIdentifier playerID) {
    	logger.info("Request status in game " + gameID.getUniqueGameID() + " player " + playerID.getUniquePlayerID());

    	GameIdentifier gameUID = clientConverter.convertUniqueGameIdentifier(gameID);
    	GameEntity currentGame = gameManager.getGame(gameUID);
    	GameStatus serverGameStatus = currentGame.getGameStatus();
    	PlayerManager playerManager = new PlayerManager(currentGame);  	
    	PlayerStatus player = playerManager.getSinglePlayer(playerID.getUniquePlayerID());
    	Collection<PlayerState> players = new ArrayList<>();
    	players.add(serverConverter.convertPlayerStateFromServer(player));
    	
    	if(currentGame.isFull()) {
    		PlayerStatus fakeEnemy = playerManager.setFakeEnemyID(playerID.getUniquePlayerID());
    		players.add(serverConverter.convertPlayerStateFromServer(fakeEnemy));
    	}
    	if(serverGameStatus.getMap() == null) {
    		messagesbase.messagesfromserver.FullMap map = new messagesbase.messagesfromserver.FullMap();
    		return new ResponseEnvelope<>(new GameState(map, players, serverGameStatus.getGameStateId()));
    	}
    	PlayerStatus enemy = playerManager.getEnemyPlayer(playerID.getUniquePlayerID());
    	messagesbase.messagesfromserver.FullMap map = serverConverter.convertHalfMapFromServer(serverGameStatus.getMap(), playerID.getUniquePlayerID(), enemy.getPlayerIdentifier());
		return new ResponseEnvelope<>(new GameState(map, players, serverGameStatus.getGameStateId()));
	}
  
    @ExceptionHandler({ GenericException.class })
	public @ResponseBody ResponseEnvelope<?> handleException(GenericException ex, HttpServletResponse response) {
		ResponseEnvelope<?> result = new ResponseEnvelope<>(ex.getErrorName(), ex.getMessage());
		response.setStatus(HttpServletResponse.SC_OK);
		return result;
	}
}
