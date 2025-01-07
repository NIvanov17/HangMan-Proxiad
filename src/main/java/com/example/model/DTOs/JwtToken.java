package com.example.model.DTOs;

import java.util.List;

import org.apache.shiro.authc.AuthenticationToken;

public class JwtToken implements AuthenticationToken{
	
    private final String username;
    private final String token;
	private List<String> roles;

	public JwtToken(String username, String token, List<String> roles) {
		super();
		this.username = username;
		this.token = token;
		this.roles = roles;
	}

	@Override
	public Object getPrincipal() {
		return username;
	}

	@Override
	public Object getCredentials() {
		return token;
	}
	
	public List<String> getRoles() {
		return roles;
	}


}
