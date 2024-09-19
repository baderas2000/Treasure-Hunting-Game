package server.exceptions;

public class GameNotFoundException extends GenericException {
	
	public GameNotFoundException(String errorName, String errorMessage) {
		super(errorName, errorMessage);		
	}
	
}
