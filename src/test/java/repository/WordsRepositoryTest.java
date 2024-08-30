package repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Game;
import service.GameService;

class WordsRepositoryTest {
	
	private WordsRepository wordsRepository;
	
	private  List<Game> gamesList;
	
	@BeforeEach
	public void setUp() {
		wordsRepository = WordsRepository.getWordRepository();
		gamesList = wordsRepository.getGameslist();
		
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
	void getWordsRepository() {

		WordsRepository wordRepository = WordsRepository.getWordRepository();
		WordsRepository wordRepository2 = WordsRepository.getWordRepository();

		assertThat(wordRepository).isNotNull();
		assertSame(wordRepository, wordRepository2);
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
}
