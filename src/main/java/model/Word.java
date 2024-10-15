package model;

import enums.Category;

public class Word {

	private static long counter = 1;

	private long id;

	private String name;

	private Category category;


	public Word createNewGame(String wordToSet, Category category) {
		Word word = new Word();
		word.setId(generateNextId());
		word.setWord(wordToSet);
		word.setCategory(category);
		return word;
	}

	private synchronized long generateNextId() {
		return counter++;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getWord() {
		return name;
	}

	public void setWord(String name) {
		this.name = name;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}


}
