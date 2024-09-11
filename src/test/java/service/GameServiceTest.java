package service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import enums.Category;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Game;
import model.History;
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
		Game game = new Game();
		game.setWord(TEST_WORD);

		assertThat(gameService.wordWithSpaces(game)).isEqualTo("t e s t");
	}

	@Test
	void testWordWithSpacesGameShouldThrowNull() {
		Game game = new Game();
		game.setWord(null);

		assertThatThrownBy(() -> gameService.wordWithSpaces(game)).isInstanceOf(NullPointerException.class);
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
		Game game = new Game();
		game.setWord(TEST_WORD);

		assertThat(gameService.contains(game, 'e')).isTrue();
	}

	@Test
	void testContainsShouldReturnFalse() {
		Game game = new Game();
		game.setWord(TEST_WORD);

		assertThat(gameService.contains(game, 'x')).isFalse();
	}

	@Test
	void testPutLetterOnPlaceSingleLetter() {
		Game game = new Game();
		game.setWord(TEST_WORD);
		char guess = 'e';
		char[] currentState = { 't', ' ', '_', ' ', '_', ' ', 't' };

		String res = gameService.putLetterOnPlace(game, guess, currentState);

		assertThat(res).isEqualTo("t e _ t");
	}

	@Test
	void testPutLetterOnPlaceManyLetters() {
		Game game = new Game();
		game.setWord(TEST_WORD_REPEATING_LAST_LETTER);
		char guess = 'n';
		char[] currentState = { 'b', 'a', '_', 'a', '_', 'a' };

		String res = gameService.putLetterOnPlace(game, guess, currentState);

		assertThat(res).isEqualTo("b a n a n a");
	}

	@Test
	void testPutLetterOnPlaceDoesntMatch() {
		Game game = new Game();
		game.setWord(TESTING);
		char guess = 'x';
		char[] currentState = { 't', '_', '_', 't', '_', '_', 'g' };

		String res = gameService.putLetterOnPlace(game, guess, currentState);

		assertThat(res).isEqualTo("t _ _ t _ _ g");
	}

	@Test
	void testPutLetterOnPlaceWithLetter() {
		Game game = new Game();
		game.setWord(TESTING);
		char guess = '5';
		char[] currentState = { 't', '_', '_', 't', '_', '_', 'g' };

		String res = gameService.putLetterOnPlace(game, guess, currentState);

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
		Game game = new Game();
		game.setWord("test");

		String toCompare = gameService.wordWithSpaces(TEST_WORD);

		assertThat(gameService.isWordGuessed(game, toCompare)).isTrue();
	}

	@Test
	void testIsWordGuessedFailed() {
		Game game = new Game();
		game.setWord("t _ s t");

		String toCompare = gameService.wordWithSpaces(TEST_WORD);

		assertThat(gameService.isWordGuessed(game, toCompare)).isFalse();
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
		Game game = new Game();
		game.setWord(TEST_WORD);
		Map<Game, History> history = new HashMap<>();
		history.put(game, null);

		assertThat(gameService.historyContainsWord(history, TEST_WORD)).isTrue();
	}

	@Test
	void testHistoryContainsWordFailed() {
		Game game = new Game();
		game.setWord(TEST_WORD);
		Map<Game, History> history = new HashMap<>();
		history.put(game, null);

		assertThat(gameService.historyContainsWord(history, TESTING)).isFalse();
	}

	@Test
	void testcontainsOtherLetters() {

		String word = TESTING;
		char firstLetter = gameService.getFirstLetter(word);
		char lastLetter = gameService.getLastLetter(word);

		assertThat(gameService.containsOtherLetters(word)).isTrue();
	}

	@Test
	void testcontainsOtherLetters2() {

		String word = "ttts";
		char firstLetter = gameService.getFirstLetter(word);
		char lastLetter = gameService.getLastLetter(word);

		assertThat(gameService.containsOtherLetters(word)).isFalse();
	}

	@Test
	void testcontainsOtherLetters3() {

		String word = "tssss";
		char firstLetter = gameService.getFirstLetter(word);
		char lastLetter = gameService.getLastLetter(word);

		assertThat(gameService.containsOtherLetters(word)).isFalse();
	}

	@Test
	void testcontainsOtherLettersTwoLetters() {
		String word = "aa";

		assertThat(gameService.containsOtherLetters(word)).isFalse();
	}

	@Test
	void testContainsOtherLettersThreeLettersFail() {
		String word = "aab";

		assertThat(gameService.containsOtherLetters(word)).isFalse();
	}


}
