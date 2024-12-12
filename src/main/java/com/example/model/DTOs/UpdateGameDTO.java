package com.example.model.DTOs;

public class UpdateGameDTO {

	private GuessDTO guessDTO;

	private LoginDTO playerDTO;

	public UpdateGameDTO() {
		super();
	}

	public UpdateGameDTO(GuessDTO guessDTO, LoginDTO playerDTO) {
		super();
		this.guessDTO = guessDTO;
		this.playerDTO = playerDTO;
	}

	public LoginDTO getPlayerDTO() {
		return playerDTO;
	}

	public void setPlayerDTO(LoginDTO playerDTO) {
		this.playerDTO = playerDTO;
	}

	public GuessDTO getGuessDTO() {
		return guessDTO;
	}

	public void setGuessDTO(GuessDTO guessDTO) {
		this.guessDTO = guessDTO;
	}

}
