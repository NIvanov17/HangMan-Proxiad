package com.example.model.DTOs;

public class MultiPlayerTokenDTO {

	private String wordToGuess;

	private String category;
	
	private String token;

	public MultiPlayerTokenDTO() {
		super();
	}

	public MultiPlayerTokenDTO(String wordToGuess, String category) {
		super();
		this.wordToGuess = wordToGuess;
		this.category = category;
	}

	public String getWordToGuess() {
		return wordToGuess;
	}

	public void setWordToGuess(String wordToGuess) {
		this.wordToGuess = wordToGuess;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	
}
