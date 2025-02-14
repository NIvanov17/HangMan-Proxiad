package com.example.model.DTOs;

import java.util.List;

public class JwtResponse {
	private String token;
	private String username;
	private List<String> roles;

	public JwtResponse(String token, String username, List<String> roles) {
		super();
		this.token = token;
		this.username = username;
		this.roles = roles;
	}

	public String getToken() {
		return token;
	}

	public String getUsername() {
		return username;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	
}
