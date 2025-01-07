package com.example.model.DTOs;

public class RoleDTO {

	private String username;
	
	private String role;
	
	

	public RoleDTO() {
		super();
	}



	public RoleDTO(String username, String role) {
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



	public String getRole() {
		return role;
	}



	public void setRole(String role) {
		this.role = role;
	} 
	
	
}
