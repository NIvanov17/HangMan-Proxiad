package exception;

public class MultiPlayerModeException extends RuntimeException{
	public MultiPlayerModeException(String message) {
		super(message);
	}

	public MultiPlayerModeException(String message, Throwable cause) {
		super(message, cause);
	}
}
