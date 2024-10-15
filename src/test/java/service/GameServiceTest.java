package service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Game;
import model.Word;
import repository.WordsRepository;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

	private static final String TEST_WORD = "test";

	private static final String TESTING = "testing";

	private static final String TEST_WORD_REPEATING_LAST_LETTER = "banana";

	@Mock
	private HttpServletRequest request;

	@Mock
	private HttpServletResponse response;

	@Mock
	private HttpSession session;

	@Mock
	private RequestDispatcher requestDispatcher;

	@Mock
	private WordsRepository wordsRepository;

	@InjectMocks
	private GameService gameService;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testWordWithSpacesGameShouldReturnWord() {
		Word word = new Word();
		word.setWord(TEST_WORD);

		assertThat(gameService.wordWithSpaces(word)).isEqualTo("t e s t");
	}

	@Test
	void testWordWithSpacesGameShouldThrowNull() {
		Word word = new Word();
		word.setWord(null);

		assertThatThrownBy(() -> gameService.wordWithSpaces(word)).isInstanceOf(NullPointerException.class);
	}

	@Test
	void testWordToReturnCorrect() {
		assertThat(gameService.wordToReturn(TEST_WORD)).containsExactly('t', '_', '_', 't');
	}

	@Test
	void testWordToReturnWithRepeatingFirstLetter() {
		assertThat(gameService.wordToReturn(TESTING)).containsExactly('t', '_', '_', 't', '_', '_', 'g');
	}

	@Test
	void testWordToReturnWithRepeatingLastLetter() {
		assertThat(gameService.wordToReturn(TEST_WORD_REPEATING_LAST_LETTER)).containsExactly('b', 'a', '_', 'a', '_',
				'a');
	}

	@Test
	void testContainsShouldReturnTrue() {
		Word word = new Word();
		word.setWord(TEST_WORD);

		assertThat(gameService.contains(word, 'e')).isTrue();
	}

	@Test
	void testContainsShouldReturnFalse() {
		Word word = new Word();
		word.setWord(TEST_WORD);

		assertThat(gameService.contains(word, 'x')).isFalse();
	}

	@Test
	void testPutLetterOnPlaceSingleLetter() {
		Word word = new Word();
		word.setWord(TEST_WORD);
		char guess = 'e';
		char[] currentState = { 't', ' ', '_', ' ', '_', ' ', 't' };

		String res = gameService.putLetterOnPlace(word, guess, currentState);

		assertThat(res).isEqualTo("t e _ t");
	}

	@Test
	void testPutLetterOnPlaceManyLetters() {
		Word word = new Word();
		word.setWord(TEST_WORD_REPEATING_LAST_LETTER);
		char guess = 'n';
		char[] currentState = { 'b', 'a', '_', 'a', '_', 'a' };

		String res = gameService.putLetterOnPlace(word, guess, currentState);

		assertThat(res).isEqualTo("b a n a n a");
	}

	@Test
	void testPutLetterOnPlaceDoesntMatch() {
		Word word = new Word();
		word.setWord(TESTING);
		char guess = 'x';
		char[] currentState = { 't', '_', '_', 't', '_', '_', 'g' };

		String res = gameService.putLetterOnPlace(word, guess, currentState);

		assertThat(res).isEqualTo("t _ _ t _ _ g");
	}

	@Test
	void testPutLetterOnPlaceWithLetter() {
		Word word = new Word();
		word.setWord(TESTING);
		char guess = '5';
		char[] currentState = { 't', '_', '_', 't', '_', '_', 'g' };

		String res = gameService.putLetterOnPlace(word, guess, currentState);

		assertThat(res).isEqualTo("t _ _ t _ _ g");
	}

	@Test
	void testCheckFailedTries() {
		assertThat(gameService.checkFailedTries(0)).isTrue();
	}

	@Test
	void testCheckFailedTriesFail() {
		assertThat(gameService.checkFailedTries(1)).isFalse();
	}

	@Test
	void testIsWordGuessed() {
		Word word = new Word();
		word.setWord("test");

		String toCompare = gameService.wordWithSpaces(TEST_WORD);

		assertThat(gameService.isWordGuessed(word, toCompare)).isTrue();
	}

	@Test
	void testIsWordGuessedFailed() {
		Word word = new Word();
		word.setWord("t _ s t");

		String toCompare = gameService.wordWithSpaces(TEST_WORD);

		assertThat(gameService.isWordGuessed(word, toCompare)).isFalse();
	}

	@Test
	void testGetFirstLetter() {
		assertThat(gameService.getFirstLetter(TEST_WORD)).isEqualTo('t');
	}

	@Test
	void testGetLastLetter() {
		assertThat(gameService.getLastLetter(TEST_WORD)).isEqualTo('t');
	}

	@Test
	void testIsWordValid() {
		assertThat(gameService.isWordValid(TEST_WORD)).isTrue();
	}

	@Test
	void testIsWordValidFailed() {
		assertThat(gameService.isWordValid("aaa")).isFalse();
	}

	@Test
	void testIsWordValidFailedWithTwoLetters() {
		assertThat(gameService.isWordValid("aa")).isFalse();
	}

	@Test
	void testHistoryContainsWord() {
		Word word = new Word();
		word.setWord(TEST_WORD);
		Map<Word, Game> history = new HashMap<>();
		history.put(word, null);

		assertThat(gameService.historyContainsWord(history, TEST_WORD)).isTrue();
	}

	@Test
	void testHistoryContainsWordFailed() {
		Word word = new Word();
		word.setWord(TEST_WORD);
		Map<Word, Game> history = new HashMap<>();
		history.put(word, null);

		assertThat(gameService.historyContainsWord(history, TESTING)).isFalse();
	}

	@Test
	void testcontainsOtherLetters() {

		String word = TESTING;

		assertThat(gameService.containsOtherLetters(word)).isTrue();
	}

	@ParameterizedTest
	@ValueSource(strings = { "ttts", "tssss", "aa", "aab" })
	void testContainsOtherLettersWithVariousInputs(String word) {
		assertThat(gameService.containsOtherLetters(word)).isFalse();
	}

	@ParameterizedTest
	@ValueSource(strings = { "  apple", " 4a0", "  test " })
	void testContainsOnlyLettersWithSpaces(String word) {
		assertThat(gameService.containsOnlyLetters(word)).isFalse();
	}


	@Test
	void testIsWordValidWith2Symbols() {
		String word = "ok";
		assertThat(gameService.isWordValid(word)).isFalse();
	}

	@Test
	void testIsWordValidWith2SymbolsWithNumber() {
		String word = "o4";
		assertThat(gameService.isWordValid(word)).isFalse();
	}
}
