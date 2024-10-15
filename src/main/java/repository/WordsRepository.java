package repository;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;

import model.Game;
import model.Word;

@Repository
public class WordsRepository {
	private List<Word> gamesList;
	private Map<Word, Game> history;
	public static final Random RANDOM = new Random();

	public WordsRepository() {
		gamesList = new ArrayList<>();
		history = new HashMap<>();
	}

	public Map<Word, Game> getHistory() {
		return history;
	}

	public List<Word> getGameslist() {
		return gamesList;
	}

	public Word getRandomGame() {
		Word word = gamesList.get(RANDOM.nextInt(gamesList.size()));
		return word;
	}

	public List<Word> initializeWords() throws IOException {
		List<Word> list = new ArrayList<>();
		ObjectMapper objectMapper = new ObjectMapper();
		String file = "json/words.json";
		InputStream inputStream = WordsRepository.class.getClassLoader().getResourceAsStream(file);
		Word[] wordArray = objectMapper.readValue(inputStream, Word[].class);

		for (Word word : wordArray) {
			list.add(word);
		}
		return list;
	}

}
