package com.example.model.DTOs;

import org.apache.shiro.authc.AuthenticationToken;

public class JwtToken implements AuthenticationToken{
	
    private final String username;
    private final String token;

    
    
	public JwtToken(String username, String token) {
		super();
		this.username = username;
		this.token = token;
	}

	@Override
	public Object getPrincipal() {
		return username;
	}

	@Override
	public Object getCredentials() {
		return token;
	}

}
