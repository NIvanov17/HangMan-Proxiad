package com.example.model.DTOs;

public class GuessDTO {

	private char guess;
	
	

	public GuessDTO() {
		super();
	}

	public GuessDTO(char guess) {
		super();
		this.guess = guess;
	}

	public char getGuess() {
		return guess;
	}

	public void setGuess(char guess) {
		this.guess = guess;
	}
	
	
}
