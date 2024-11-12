package controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;
import org.springframework.web.servlet.NoHandlerFoundException;

import exception.GameAlreadyFinishedException;
import exception.InvalidGameIDException;
import exception.InvalidUsernameException;
import exception.MultiPlayerModeException;
import exception.SinglePlayerModeException;
import model.ErrorResponse;

@ControllerAdvice
public class GlobalExHandler {

	@ExceptionHandler(NoHandlerFoundException.class)
	public ResponseEntity<ErrorResponse> handleBadRequestException(NoHandlerFoundException ex) {
		ErrorResponse error = new ErrorResponse("Bad request", ex.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}

	@ExceptionHandler(InternalServerError.class)
	public ResponseEntity<ErrorResponse> handleBadRequestException(InternalServerError ex) {
		ErrorResponse error = new ErrorResponse("Bad request", ex.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> handleBadRequestException(IllegalArgumentException ex) {
		ErrorResponse error = new ErrorResponse("Invalid or not existing!", ex.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}

	@ExceptionHandler(InvalidUsernameException.class)
	public ResponseEntity<ErrorResponse> handleBadRequestException(InvalidUsernameException ex) {
		ErrorResponse error = new ErrorResponse("Bad request", ex.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}

	@ExceptionHandler(InvalidGameIDException.class)
	public ResponseEntity<ErrorResponse> handleBadRequestException(InvalidGameIDException ex) {
		ErrorResponse error = new ErrorResponse("Bad request", ex.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}

	@ExceptionHandler(GameAlreadyFinishedException.class)
	public ResponseEntity<ErrorResponse> handleBadRequestException(GameAlreadyFinishedException ex) {
		ErrorResponse error = new ErrorResponse("Bad request", ex.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}

	@ExceptionHandler(MultiPlayerModeException.class)
	public ResponseEntity<ErrorResponse> handleBadRequestException(MultiPlayerModeException ex) {
		ErrorResponse error = new ErrorResponse("Bad request", ex.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}
	
	@ExceptionHandler(SinglePlayerModeException.class)
	public ResponseEntity<ErrorResponse> handleBadRequestException(SinglePlayerModeException ex) {
		ErrorResponse error = new ErrorResponse("Bad request", ex.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}
}
