package repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Game;
import model.Word;

class WordsRepositoryTest {

	private WordsRepository wordsRepository;

	private List<Word> gamesList;

	private Map<Word, Game> history;

	@BeforeEach
	public void setUp() {
		wordsRepository = new WordsRepository();
		gamesList = wordsRepository.getGameslist();
		history = new HashMap<>();

		Word newGame = new Word();
		newGame.setWord("test");
		gamesList.add(newGame);

		Word newGame2 = new Word();
		newGame2.setWord("test2");
		gamesList.add(newGame2);

		Word newGame3 = new Word();
		newGame3.setWord("test3");
		gamesList.add(newGame3);
	}

	@Test
	void getRandomSingleWord() {
		gamesList.clear();
		Word newGame4 = new Word();
		newGame4.setWord("test4");
		gamesList.add(newGame4);

		Word randomGame = wordsRepository.getRandomGame();

		assertThat(randomGame).isNotNull();
		assertThat(randomGame).isEqualTo(newGame4);

	}

	@Test
	void getRandomWord() {

		Word randomGame = wordsRepository.getRandomGame();

		assertThat(randomGame).isNotNull();
		assertThat(gamesList).contains(randomGame);

	}

	@Test
	void testGetHistory() {
		Map<Word,Game> getHistory = wordsRepository.getHistory();

		assertThat(getHistory).isEqualTo(history);

	}

	@Test
	void testInitializeWords() throws IOException {

		List<Word> initializeWords = wordsRepository.initializeWords();

		assertThat(initializeWords).isNotEmpty();
		assertThat(initializeWords.get(0).getWord()).isEqualTo("apple");
	}
}
