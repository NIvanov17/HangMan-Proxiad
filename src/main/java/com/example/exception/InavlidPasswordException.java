package com.example.exception;

public class InavlidPasswordException extends RuntimeException{

	public InavlidPasswordException(String message) {
        super(message);
    }

	public InavlidPasswordException(String message, Throwable cause) {
        super(message, cause);
    }
}
