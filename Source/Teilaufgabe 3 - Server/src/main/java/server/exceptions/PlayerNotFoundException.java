package server.exceptions;

public class PlayerNotFoundException extends GenericException {
	
	public PlayerNotFoundException(String errorName, String errorMessage) {
		super(errorName, errorMessage);		
	}
	
}
