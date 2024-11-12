package exception;

public class GameAlreadyFinishedException extends RuntimeException {
	public GameAlreadyFinishedException(String message) {
		super(message);
	}

	public GameAlreadyFinishedException(String message, Throwable cause) {
		super(message, cause);
	}
}
