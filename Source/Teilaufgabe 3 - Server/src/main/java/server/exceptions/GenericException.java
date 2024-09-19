package server.exceptions;

/* Note, this is just a quick minimal example to show the generic exception handling
 * functionality in spring. Typically I would recommend that you create individual exception types
 * for each unique kind of exception (e.g., individual exceptions for different kinds of rule errors)
 * 
 * While doing so, follow the tips given in the Error Handling Tutorial.
 * 
 * For simplicity reasons, your own exceptions could, e.g., inherit from the one given below
 */
public class GenericException extends RuntimeException {

	private final String errorName;

	public GenericException(String errorName, String errorMessage) {
		super(errorMessage);
		this.errorName = errorName;		
	}

	public String getErrorName() {
		return errorName;
	}
}
