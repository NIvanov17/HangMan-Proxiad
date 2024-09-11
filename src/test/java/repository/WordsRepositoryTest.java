package repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import model.Game;
import model.History;
import service.GameService;

class WordsRepositoryTest {

	private WordsRepository wordsRepository;

	private List<Game> gamesList;

	private Map<Game, History> history;

	@BeforeEach
	public void setUp() {
		wordsRepository = new WordsRepository();
		gamesList = wordsRepository.getGameslist();
		history = new HashMap<>();

		Game newGame = new Game();
		newGame.setWord("test");
		gamesList.add(newGame);

		Game newGame2 = new Game();
		newGame2.setWord("test2");
		gamesList.add(newGame2);

		Game newGame3 = new Game();
		newGame3.setWord("test3");
		gamesList.add(newGame3);
	}

	@Test
	void getRandomSingleWord() {
		gamesList.clear();
		Game newGame4 = new Game();
		newGame4.setWord("test4");
		gamesList.add(newGame4);

		Game randomGame = wordsRepository.getRandomGame();

		assertThat(randomGame).isNotNull();
		assertThat(randomGame).isEqualTo(newGame4);

	}

	@Test
	void getRandomWord() {

		Game randomGame = wordsRepository.getRandomGame();

		assertThat(randomGame).isNotNull();
		assertThat(gamesList).contains(randomGame);

	}

	@Test
	void testGetHistory() {
		Map<Game, History> getHistory = wordsRepository.getHistory();

		assertThat(getHistory).isEqualTo(history);

	}

	@Test
	void testInitializeWords() throws IOException {

		List<Game> initializeWords = wordsRepository.initializeWords();

		assertThat(initializeWords).isNotEmpty();
		assertThat(initializeWords.get(0).getWord()).isEqualTo("apple");
	}
}
