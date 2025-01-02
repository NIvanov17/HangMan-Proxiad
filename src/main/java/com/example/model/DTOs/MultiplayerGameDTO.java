package com.example.model.DTOs;

import java.util.Objects;
import java.util.Set;

public class MultiplayerGameDTO {

	private Long gameId;

	private String wordProgress;

	private int triesLeft;

	private Set<Character> usedChars;

	private boolean isFinished;

	private String gameStatus;

	private String gameMode;
	
	private String category;
	
	private String word;

	public MultiplayerGameDTO(Long gameId, String wordProgress, int triesLeft, Set<Character> usedChars, boolean isFinished) {
		super();
		this.gameId = gameId;
		this.wordProgress = wordProgress;
		this.triesLeft = triesLeft;
		this.usedChars = usedChars;
		this.isFinished = isFinished;
	}
	

	public MultiplayerGameDTO() {
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
		MultiplayerGameDTO other = (MultiplayerGameDTO) obj;
		return Objects.equals(gameId, other.gameId);
	}
}
