package model.DTOs;

public class PlayersDTO {
	
	private PlayerDTO guesser;
	
	private PlayerDTO giver;

	public PlayersDTO(PlayerDTO guesser, PlayerDTO giver) {
		super();
		this.guesser = guesser;
		this.giver = giver;
	}

	public PlayerDTO getGuesser() {
		return guesser;
	}

	public void setGuesser(PlayerDTO guesser) {
		this.guesser = guesser;
	}

	public PlayerDTO getGiver() {
		return giver;
	}

	public void setGiver(PlayerDTO giver) {
		this.giver = giver;
	}
	
	
	

}
