package model.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;

public class MultiPlayerGameInputDTO {

	private Long giverId;

	private Long guesserId;

	private String wordToGuess;

	private String catergory;

	public MultiPlayerGameInputDTO(long giverId, long guesserId, String wordToGuess, String catergory) {
		super();
		this.giverId = giverId;
		this.guesserId = guesserId;
		this.wordToGuess = wordToGuess;
		this.catergory = catergory;
	}

	public Long getGiverId() {
		return giverId;
	}

	public void setGiverId(Long giverId) {
		this.giverId = giverId;
	}

	public Long getGuesserId() {
		return guesserId;
	}

	public void setGuesserId(Long guesserId) {
		this.guesserId = guesserId;
	}

	public String getWordToGuess() {
		return wordToGuess;
	}

	public void setWordToGuess(String wordToGuess) {
		this.wordToGuess = wordToGuess;
	}

	public String getCatergory() {
		return catergory;
	}

	public void setCatergory(String catergory) {
		this.catergory = catergory;
	}

}
