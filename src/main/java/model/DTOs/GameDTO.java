package model.DTOs;

import java.util.Set;

public class GameDTO {

	private Long gameId;
	
	private String wordProgress;
	
	private int triesLeft;
	
	private Set<Character> usedChars;
	
	private boolean isFinished;
	
	private String gameStatus;

	private PlayerDTO giver;

	private PlayerDTO guesser;
	
	

	public GameDTO(Long gameId, String wordProgress, int triesLeft, Set<Character> usedChars, boolean isFinished, PlayerDTO guesser) {
		super();
		this.gameId = gameId;
		this.wordProgress = wordProgress;
		this.triesLeft = triesLeft;
		this.usedChars = usedChars;
		this.isFinished = isFinished;
		this.guesser = guesser;
	}

	public Long getGameId() {
		return gameId;
	}

	public void setGameId(Long gameId) {
		this.gameId = gameId;
	}

	public String getWordProgress() {
		return wordProgress;
	}

	public void setWordProgress(String wordProgress) {
		this.wordProgress = wordProgress;
	}

	public int getTriesLeft() {
		return triesLeft;
	}

	public void setTriesLeft(int triesLeft) {
		this.triesLeft = triesLeft;
	}

	public Set<Character> getUsedChars() {
		return usedChars;
	}

	public void setUsedChars(Set<Character> usedChars) {
		this.usedChars = usedChars;
	}

	public boolean isFinished() {
		return isFinished;
	}

	public void setFinished(boolean isFinished) {
		this.isFinished = isFinished;
	}

	public String getGameStatus() {
		return gameStatus;
	}

	public void setGameStatus(String gameStatus) {
		this.gameStatus = gameStatus;
	}

	public PlayerDTO getGiver() {
		return giver;
	}

	public void setGiver(PlayerDTO giver) {
		this.giver = giver;
	}

	public PlayerDTO getGuesser() {
		return guesser;
	}

	public void setGuesser(PlayerDTO guesser) {
		this.guesser = guesser;
	}

}
