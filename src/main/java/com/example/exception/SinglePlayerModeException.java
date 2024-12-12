package com.example.exception;

public class SinglePlayerModeException extends RuntimeException{
	public SinglePlayerModeException(String message) {
		super(message);
	}

	public SinglePlayerModeException(String message, Throwable cause) {
		super(message, cause);
	}
}
