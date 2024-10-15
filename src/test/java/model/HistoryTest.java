package model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import enums.Category;

class gameTest {

	private String wordState;

	private int triesLeft;

	private Category category;

	private Set<Character> usedCharacters;

	private boolean isFinished;

	private String mode;

	private Game game;

	@BeforeEach
	public void setUp() {
		wordState = "p _ _ _ h";
		triesLeft = 6;
		category = Category.FRUITS;
		usedCharacters = new HashSet<>();
		usedCharacters.add('p');
		usedCharacters.add('h');
		isFinished = false;
		mode = "Multiplayer";
		game = new Game(wordState, triesLeft, category, usedCharacters, isFinished, mode);
	}

	@Test
	void testgameConstructor() {
		Game newGame = new Game(wordState, triesLeft, category, usedCharacters, isFinished, mode);

		assertThat(newGame.getWordState()).isEqualTo(wordState);
		assertThat(newGame.getTriesLeft()).isEqualTo(triesLeft);
		assertThat(newGame.getCategory()).isEqualTo(category);
		assertThat(newGame.getUsedChars()).isEqualTo(usedCharacters);
		assertThat(newGame.isFinished()).isFalse();
		assertThat(newGame.getMode()).isEqualTo(mode);
	}

	@Test
	void testReset() {
		String newWordState = "a _ _ _ e";
		int newTriesLeft = 6;
		Category newCategory = Category.FRUITS;
		Set<Character> newUsedChars = new HashSet<>();
		boolean newIsFinished = false;

		game.reset(newWordState, newTriesLeft, newCategory, newUsedChars, newIsFinished);

		assertThat(game.getWordState()).isEqualTo(newWordState);
		assertThat(game.getTriesLeft()).isEqualTo(newTriesLeft);
		assertThat(game.getCategory()).isEqualTo(newCategory);
		assertThat(game.getUsedChars()).containsExactlyInAnyOrderElementsOf(newUsedChars);
		assertThat(game.isFinished()).isFalse();
	}

	@Test
	void testSetUsedChars() {
		char toSet = 'e';
		Set<Character> charToSet = new HashSet<>();
		charToSet.add(toSet);

		Game newgame = new Game(wordState, triesLeft, category, new HashSet<>(), isFinished, mode);

		newgame.setUsedChars(charToSet);

		assertThat(newgame.getUsedChars()).contains(toSet);
	}

	@Test
	void testSetWordState() {
		String wordToSet = "p e _ _ h";
		game.setWordState(wordToSet);

		assertThat(game.getWordState()).isEqualTo(wordToSet);
	}
	
	@Test
	void testSetTriesLeft() {
		int triesToSet = 3;
		game.setTriesLeft(triesToSet);

		assertThat(game.getTriesLeft()).isEqualTo(triesToSet);
	}
	
	@Test
	void testSetCategory() {
		Category categoryToSet = Category.ANIMALS;
		game.setCategory(categoryToSet);
		

		assertThat(game.getCategory()).isEqualTo(categoryToSet);
	}
	
	@Test
	void testSetMode() {
		String modeToSet = "Single player";
		game.setMode(modeToSet);
		

		assertThat(game.getMode()).isEqualTo(modeToSet);
	}

}
