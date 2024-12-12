package com.example.model.DTOs;

public class MultiPlayerGameInputDTO {

	private String giverUsername;

	private String guesserUsername;

	private String wordToGuess;

	private String category;
	
	

	public MultiPlayerGameInputDTO() {
		super();
	}

	public MultiPlayerGameInputDTO(String giverUsername, String guesserUsername, String wordToGuess, String category) {
		super();
		this.giverUsername = giverUsername;
		this.guesserUsername = guesserUsername;
		this.wordToGuess = wordToGuess;
		this.category = category;
	}


	public String getGiverUsername() {
		return giverUsername;
	}

	public void setGiverUsername(String giverUsername) {
		this.giverUsername = giverUsername;
	}

	public String getGuesserUsername() {
		return guesserUsername;
	}

	public void setGuesserUsername(String guesserUsername) {
		this.guesserUsername = guesserUsername;
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

	public void setCatergory(String category) {
		this.category = category;
	}

}
