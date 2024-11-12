package model.DTOs;

public class PlayerRankingDTO {

	private long id;

	private String username;

	private int totalWins;

	public PlayerRankingDTO() {
		super();
	}

	public PlayerRankingDTO(long id, String username, int totalWins) {
		super();
		this.id = id;
		this.username = username;
		this.totalWins = totalWins;
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

	public int getTotalWins() {
		return totalWins;
	}

	public void setTotalWins(int totalWins) {
		this.totalWins = totalWins;
	}

}
