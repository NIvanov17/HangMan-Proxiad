package repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

class WordsRepositoryTest {

	@Test
	void getWordsRepository() {

		WordsRepository wordRepository = WordsRepository.getWordRepository();
		WordsRepository wordRepository2 = WordsRepository.getWordRepository();

		assertThat(wordRepository).isNotNull();
		assertSame(wordRepository, wordRepository2);
	}
}
