package com.example.model.DTOs;

public class CreateGameInputDTO {

	private  LoginDTO loginDTO;
	
	private MultiPlayerGameInputDTO multiPlayerGameInputDTO;

	public CreateGameInputDTO() {
		super();
	}

	public CreateGameInputDTO(LoginDTO loginDTO, MultiPlayerGameInputDTO multiPlayerGameInputDTO) {
		super();
		this.loginDTO = loginDTO;
		this.multiPlayerGameInputDTO = multiPlayerGameInputDTO;
	}

	public LoginDTO getPlayerDTO() {
		return loginDTO;
	}

	public void setPlayerDTO(LoginDTO loginDTO) {
		this.loginDTO = loginDTO;
	}

	public MultiPlayerGameInputDTO getMultiPlayerGameInputDTO() {
		return multiPlayerGameInputDTO;
	}

	public void setMultiPlayerGameInputDTO(MultiPlayerGameInputDTO multiPlayerGameInputDTO) {
		this.multiPlayerGameInputDTO = multiPlayerGameInputDTO;
	}
	
	
}
