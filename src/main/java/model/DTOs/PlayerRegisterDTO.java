package model.DTOs;

import enums.RoleName;

public class PlayerRegisterDTO {
	private long id;
	
	private String username;
	
	private RoleName roleName;

	public PlayerRegisterDTO() {
		super();
	}



	public PlayerRegisterDTO(long id, String username, RoleName roleName) {
		super();
		this.id = id;
		this.username = username;
		this.roleName = roleName;
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



	public RoleName getRoleName() {
		return roleName;
	}



	public void setRoleName(RoleName roleName) {
		this.roleName = roleName;
	}
	
	
}
