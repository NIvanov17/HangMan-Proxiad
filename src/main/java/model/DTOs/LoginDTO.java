package model.DTOs;

public class LoginDTO {

	private String username;
	
	

	public LoginDTO() {
		super();
	}

	public LoginDTO(String username) {
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
