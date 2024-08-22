package repository;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.fasterxml.jackson.databind.ObjectMapper;

import model.OngoingGame;
import model.Word;

public class WordsRepository {
	private static List<Word> wordsList;
	private static Map<Word, OngoingGame> onGoingWords;
	private static WordsRepository wordsRepository;

	private WordsRepository() {
		wordsList = new ArrayList<>();
		onGoingWords = new HashMap<>();
	}

	public static WordsRepository getWordRepository() {
		if (wordsRepository == null) {
			 wordsRepository = new WordsRepository();
		} 
			return wordsRepository;
	}

	public static Map<Word, OngoingGame> getOnGoingWordslist() {
		return onGoingWords;
	}

	public static List<Word> getWordslist() {
		return wordsList;
	}

	public static Word getRandomWord() {
		Random random = new Random();
		Word word = wordsList.get(random.nextInt(wordsList.size()));
		return word;
	}

//	public static String getRandomWord() {
//		Random rand = new Random();
//		String str = wordsList.get(rand.nextInt(10));
//		return str;
//	}

	public static List<Word> initializeWords() throws IOException {
		List<Word> list = new ArrayList<>();
		ObjectMapper objectMapper = new ObjectMapper();
		String file = "json/words.json";
		//inputStream because of the objectMapper
		InputStream inputStream = WordsRepository.class.getClassLoader().getResourceAsStream(file);
		Word[] wordArray = objectMapper.readValue(inputStream, Word[].class);

		for (Word word : wordArray) {
			list.add(word);
		}
		return list;
	}

}
