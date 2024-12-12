package com.example.model.DTOs;

import java.util.Objects;
import java.util.Set;

public class GameDTO {

	private Long gameId;

	private String wordProgress;

	private int triesLeft;

	private Set<Character> usedChars;

	private boolean isFinished;

	private String gameStatus;

	private String gameMode;

	private PlayerDTO giver;

	private PlayerDTO guesser;
	
	private String category;
	
	private String word;

	public GameDTO(Long gameId, String wordProgress, int triesLeft, Set<Character> usedChars, boolean isFinished,
			PlayerDTO guesser) {
		super();
		this.gameId = gameId;
		this.wordProgress = wordProgress;
		this.triesLeft = triesLeft;
		this.usedChars = usedChars;
		this.isFinished = isFinished;
		this.guesser = guesser;
	}
	

	public GameDTO() {
		super();
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

	public String getGameMode() {
		return gameMode;
	}

	public void setGameMode(String gameMode) {
		this.gameMode = gameMode;
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

	public String getCategory() {
		return category;
	}


	public void setCategory(String category) {
		this.category = category;
	}


	public String getWord() {
		return word;
	}


	public void setWord(String word) {
		this.word = word;
	}


	@Override
	public int hashCode() {
		return Objects.hash(gameId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GameDTO other = (GameDTO) obj;
		return Objects.equals(gameId, other.gameId);
	}

}
