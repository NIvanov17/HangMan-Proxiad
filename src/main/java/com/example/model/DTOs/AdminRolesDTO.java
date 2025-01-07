package com.example.model.DTOs;

import java.util.List;

public class AdminRolesDTO {
	
	private String username;
	
	private List<String> role;
	
	

	public AdminRolesDTO() {
		super();
	}

	public AdminRolesDTO(String username, List<String> role) {
		super();
		this.username = username;
		this.role = role;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<String> getRole() {
		return role;
	}

	public void setRole(List<String> role) {
		this.role = role;
	}
	
	

}
