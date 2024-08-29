package model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import enums.Category;

class HistoryTest {

	private String wordState;

	private int triesLeft;

	private Category category;

	private Set<Character> usedCharacters;

	private boolean isFinished;

	private String mode;

	private History history;

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
		history = new History(wordState, triesLeft, category, usedCharacters, isFinished, mode);
	}

	@Test
	void testHistoryConstructor() {
		History newHistory = new History(wordState, triesLeft, category, usedCharacters, isFinished, mode);

		assertThat(newHistory.getWordState()).isEqualTo(wordState);
		assertThat(newHistory.getTriesLeft()).isEqualTo(triesLeft);
		assertThat(newHistory.getCategory()).isEqualTo(category);
		assertThat(newHistory.getUsedChars()).isEqualTo(usedCharacters);
		assertThat(newHistory.isFinished()).isFalse();
		assertThat(newHistory.getMode()).isEqualTo(mode);
	}

	@Test
	void testReset() {
		String newWordState = "a _ _ _ e";
		int newTriesLeft = 6;
		Category newCategory = Category.FRUITS;
		Set<Character> newUsedChars = new HashSet<>();
		boolean newIsFinished = false;

		history.reset(newWordState, newTriesLeft, newCategory, newUsedChars, newIsFinished);

		assertThat(history.getWordState()).isEqualTo(newWordState);
		assertThat(history.getTriesLeft()).isEqualTo(newTriesLeft);
		assertThat(history.getCategory()).isEqualTo(newCategory);
		assertThat(history.getUsedChars()).containsExactlyInAnyOrderElementsOf(newUsedChars);
		assertThat(history.isFinished()).isFalse();
	}

}
