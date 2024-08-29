package model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import enums.Category;

class GameTest {

	private static long counter = 1;

	private long id;

	private String word;

	private Category category;

	private Game game;

	@BeforeEach
	void setUp() {
		game = new Game();
	}

	@Test
	void testCreateGame() {
		word = "dog";
		category = Category.ANIMALS;

		Game newGame = game.createNewGame(word, category);

		assertThat(newGame).isNotNull();
		assertThat(newGame.getWord()).isEqualTo(word);
		assertThat(newGame.getCategory()).isEqualTo(category);
		assertThat(newGame.getId()).isNotNull();

	}

	@Test
	void testGenerateNextId() {
		word = "dog";
		category = Category.ANIMALS;
		Game newGame = game.createNewGame(word, category);
		long id = newGame.getId();

		Game newGame2 = game.createNewGame(word, category);
		long id2 = newGame2.getId();

		assertThat(id).isEqualTo(id2 - 1);
	}

}
