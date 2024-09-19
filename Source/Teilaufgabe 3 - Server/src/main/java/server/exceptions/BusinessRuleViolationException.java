package server.exceptions;

public class BusinessRuleViolationException  extends GenericException {

	public BusinessRuleViolationException(String errorName, String errorMessage) {
		super(errorName, errorMessage);		
	}
}
