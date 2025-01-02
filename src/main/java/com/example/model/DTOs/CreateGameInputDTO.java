package com.example.model.DTOs;

public class CreateGameInputDTO {

	private  LoginDTO loginDTO;
	
	private MultiPlayerTokenDTO multiPlayerTokenDTO;

	public CreateGameInputDTO() {
		super();
	}

	public CreateGameInputDTO(LoginDTO loginDTO, MultiPlayerTokenDTO multiPlayerTokenDTO) {
		super();
		this.loginDTO = loginDTO;
		this.multiPlayerTokenDTO = multiPlayerTokenDTO;
	}

	public LoginDTO getPlayerDTO() {
		return loginDTO;
	}

	public void setPlayerDTO(LoginDTO loginDTO) {
		this.loginDTO = loginDTO;
	}

	public MultiPlayerTokenDTO getMultiPlayerTokenDTO() {
		return multiPlayerTokenDTO;
	}

	public void setMultiPlayerTokenDTO(MultiPlayerTokenDTO multiPlayerTokenDTO) {
		this.multiPlayerTokenDTO = multiPlayerTokenDTO;
	}


	
	
}
