package server.exceptions;


public class GameIsFullException extends GenericException {

	public GameIsFullException(String errorName, String errorMessage) {
		super(errorName, errorMessage);		
	}
	
}