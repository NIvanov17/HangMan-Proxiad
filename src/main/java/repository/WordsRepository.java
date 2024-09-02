package repository;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.fasterxml.jackson.databind.ObjectMapper;

import model.Game;
import model.History;

public class WordsRepository {
	private static List<Game> gamesList;
	private static Map<Game, History> history;
	private static WordsRepository wordsRepository;

	private WordsRepository() {
		gamesList = new ArrayList<>();
		history = new HashMap<>();
	}
	

	public static WordsRepository getWordRepository() {
		if (wordsRepository == null) {
			 wordsRepository = new WordsRepository();
		} 
			return wordsRepository;
	}

	public Map<Game, History> getHistory() {
		return history;
	}

	public List<Game> getGameslist() {
		return gamesList;
	}

	public  Game getRandomGame() {
		Random random = new Random();
		Game game = gamesList.get(random.nextInt(gamesList.size()));
		return game;
	}


	public List<Game> initializeWords() throws IOException {
		List<Game> list = new ArrayList<>();
		ObjectMapper objectMapper = new ObjectMapper();
		String file = "json/words.json";
		//inputStream because of the objectMapper
		InputStream inputStream = WordsRepository.class.getClassLoader().getResourceAsStream(file);
		Game[] gameArray = objectMapper.readValue(inputStream, Game[].class);

		for (Game game : gameArray) {
			list.add(game);
		}
		return list;
	}

}
