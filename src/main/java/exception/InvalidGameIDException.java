package exception;

public class InvalidGameIDException extends RuntimeException {
	public InvalidGameIDException(String message) {
		super(message);
	}

	public InvalidGameIDException(String message, Throwable cause) {
		super(message, cause);
	}
}
