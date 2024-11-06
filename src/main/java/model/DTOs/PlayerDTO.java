package model.DTOs;

public class PlayerDTO {
	
	private long id;
	
	private String username;

	public PlayerDTO() {
		super();
	}

	public PlayerDTO(long id, String username) {
		super();
		this.setId(id);
		this.setUsername(username);
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
