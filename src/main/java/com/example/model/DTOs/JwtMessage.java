package com.example.model.DTOs;

public class JwtMessage {

	String username;
	

	public JwtMessage() {
		super();
	}

	public JwtMessage(String username) {
		super();
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	
}
