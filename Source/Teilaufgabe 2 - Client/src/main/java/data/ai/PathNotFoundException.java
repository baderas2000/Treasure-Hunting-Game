package data.ai;


public class PathNotFoundException extends RuntimeException {

	public PathNotFoundException (String message) {
		super(message);
	}

	public PathNotFoundException (String message, Throwable cause) {
		super(message, cause);
	}

	public PathNotFoundException (Throwable cause) {
		super(cause);
	}

}
