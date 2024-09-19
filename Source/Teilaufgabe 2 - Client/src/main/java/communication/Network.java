package communication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import messagesbase.ResponseEnvelope;
import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromclient.ERequestState;
import messagesbase.messagesfromclient.PlayerHalfMap;
import messagesbase.messagesfromclient.PlayerMove;
import messagesbase.messagesfromclient.PlayerRegistration;
import messagesbase.messagesfromserver.GameState;
import reactor.core.publisher.Mono;


public class Network {
	final static Logger logger = LoggerFactory.getLogger(Network.class);
	
	private String playerID;
	private GameInfo gameInfo;
	private WebClient baseWebClient;
	
	public Network(GameInfo gameInfo) {
		this.gameInfo = gameInfo;
		this.baseWebClient = WebClient.builder().baseUrl(this.gameInfo.getServerURL() + "/games")
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE)
				.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE).build();
	}
	
	public GameInfo getGameInfo() {
		return this.gameInfo;
	}
	
	public String getPlayerID() {
		return this.playerID;
	}
	
	private void setPlayerID(String playerID) {
		this.playerID = playerID;
	}
	
	public UniquePlayerIdentifier registerPlayer(PlayerRegistration playerReg) throws InvalidRequestException {
		Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.POST)
				.uri("/" + this.gameInfo.getGameID() + "/players")
				.body(BodyInserters.fromValue(playerReg))
				.retrieve().bodyToMono(ResponseEnvelope.class);
		ResponseEnvelope<UniquePlayerIdentifier> resultReg = webAccess.block();
		checkResponse(resultReg);
		UniquePlayerIdentifier uniquePlayerID = resultReg.getData().get();
		this.setPlayerID(uniquePlayerID.getUniquePlayerID());
		return uniquePlayerID;
	}
	
	public GameState requestStatus() throws InvalidRequestException {
		Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.GET)
				.uri("/" + this.gameInfo.getGameID() + "/states/" + this.playerID)
				.retrieve().bodyToMono(ResponseEnvelope.class);
		ResponseEnvelope<GameState> requestResult = webAccess.block();
		checkResponse(requestResult);
		return requestResult.getData().get();
	}
	
	public void sendMap(PlayerHalfMap halfMap) throws InvalidRequestException {
		Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.POST)
				.uri("/" + this.gameInfo.getGameID() + "/halfmaps")
				.body(BodyInserters.fromValue(halfMap))
				.retrieve().bodyToMono(ResponseEnvelope.class);
		ResponseEnvelope requestResult = webAccess.block();
		checkResponse(requestResult);
	}

	public void sendMove(PlayerMove move) throws InvalidRequestException {
		Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.POST)
				.uri("/" + this.gameInfo.getGameID() + "/moves")
				.body(BodyInserters.fromValue(move))
				.retrieve().bodyToMono(ResponseEnvelope.class);
		ResponseEnvelope requestResult = webAccess.block();
		checkResponse(requestResult);
	}
	
	private void checkResponse (ResponseEnvelope response) {
		if (response.getState() == ERequestState.Error) {
			logger.warn("Error Response recieved");
			throw new InvalidRequestException("Client error, errormessage: " + response.getExceptionMessage());			
		}
	}

	
	
	
	

}
