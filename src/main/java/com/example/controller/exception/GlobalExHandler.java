package com.example.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.example.exception.GameAlreadyFinishedException;
import com.example.exception.InvalidGameIDException;
import com.example.exception.InvalidUsernameException;
import com.example.exception.MultiPlayerModeException;
import com.example.exception.SinglePlayerModeException;
import com.example.model.ErrorResponse;

@ControllerAdvice
public class GlobalExHandler {

	@ExceptionHandler(NoHandlerFoundException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ErrorResponse> handleBadRequestException(NoHandlerFoundException ex) {
		ErrorResponse error = new ErrorResponse("Bad request", ex.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}

	@ExceptionHandler(InternalServerError.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<ErrorResponse> handleBadRequestException(InternalServerError ex) {
		ErrorResponse error = new ErrorResponse("Bad request", ex.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<ErrorResponse> handleBadRequestException(IllegalArgumentException ex) {
		ErrorResponse error = new ErrorResponse("Invalid or not existing!", ex.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}

	@ExceptionHandler(InvalidUsernameException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ErrorResponse> handleBadRequestException(InvalidUsernameException ex) {
		ErrorResponse error = new ErrorResponse("Bad request", ex.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}

	@ExceptionHandler(InvalidGameIDException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ErrorResponse> handleBadRequestException(InvalidGameIDException ex) {
		ErrorResponse error = new ErrorResponse("Bad request", ex.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}

	@ExceptionHandler(GameAlreadyFinishedException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ErrorResponse> handleBadRequestException(GameAlreadyFinishedException ex) {
		ErrorResponse error = new ErrorResponse("Bad request", ex.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}

	@ExceptionHandler(MultiPlayerModeException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ErrorResponse> handleBadRequestException(MultiPlayerModeException ex) {
		ErrorResponse error = new ErrorResponse("Bad request", ex.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}
	
	@ExceptionHandler(SinglePlayerModeException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ErrorResponse> handleBadRequestException(SinglePlayerModeException ex) {
		ErrorResponse error = new ErrorResponse("Bad request", ex.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}
}
