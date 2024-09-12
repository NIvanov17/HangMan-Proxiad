package model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import enums.Category;

public class History {
	private String wordState;
	private int triesLeft;
	private Category category;
	private Set<Character> usedChars;
	private boolean isFinished;
	private String mode;

	public History(String wordState, int triesLeft, Category category, Set<Character> usedCharacters,
			boolean isFinished, String mode) {
		this.wordState = wordState;
		this.triesLeft = triesLeft;
		this.category = category;
		this.usedChars = new HashSet<>(usedCharacters);
		this.setFinished(false);
		this.mode = mode;
	}

	public History() {
	}

	public void reset(String wordState, int triesLeft, Category category, Set<Character> usedCharacters,
			boolean isFinished) {
		this.wordState = wordState;
		this.triesLeft = triesLeft;
		this.category = category;
		this.usedChars.clear();
		this.setFinished(false);
	}

	public String getWordState() {
		return wordState;
	}

	public void setWordState(String wordState) {
		this.wordState = wordState;
	}

	public int getTriesLeft() {
		return triesLeft;
	}

	public void setTriesLeft(int triesLeft) {
		this.triesLeft = triesLeft;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
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

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

}