package model;

import enums.Category;

public class Game {
	
	private static long counter = 1;

	private long id;

	private String word;

	private Category category;

	public Game() {
	
	}

	 public Game createNewGame(String word, Category category) {
	        Game game = new Game();
	        game.setId(generateNextId());
	        game.setWord(word);
	        game.setCategory(category);
	        return game;
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
		return word;
	}

	public void setWord(String name) {
		this.word = name;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

}
