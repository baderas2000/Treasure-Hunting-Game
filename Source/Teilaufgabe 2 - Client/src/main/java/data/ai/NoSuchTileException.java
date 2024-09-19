package data.ai;

public class NoSuchTileException extends RuntimeException {

	public NoSuchTileException (String message) {
		super(message);
	}
	
	public NoSuchTileException (String message, Throwable cause) {
		super(message, cause);
	}

}

