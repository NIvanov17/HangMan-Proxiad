package com.example.model.DTOs;

public class PlayerDTO {

	private long id;

	private String username;

	public PlayerDTO() {
		super();
	}
	
	

	public PlayerDTO(String username) {
		super();
		this.username = username;
	}



	public PlayerDTO(long id, String username) {
		super();
		this.id = id;
		this.username = username;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}


}
