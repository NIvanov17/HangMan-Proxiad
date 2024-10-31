package service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import enums.CategoryName;
import enums.Commands;
import enums.ErrorMessages;
import model.Category;
import model.Game;
import model.Word;
import model.DTOs.WordDTO;
import repository.CategoryRepository;
import repository.WordRepository;

@Service
public class WordService {

	private WordRepository wordRepository;

	private CategoryRepository categoryRepository;

	private ObjectMapper objectMapper;

	public static final Random RANDOM = new Random();

	@Autowired
	public WordService(WordRepository wordRepository, CategoryRepository categoryRepository,
			ObjectMapper objectMapper) {
		this.wordRepository = wordRepository;
		this.categoryRepository = categoryRepository;
		this.objectMapper = objectMapper;
	}

	public void initWords() throws IOException {

		ClassLoader classLoader = getClass().getClassLoader();
		InputStream resourceAsStream = classLoader.getResourceAsStream("words.json");

		List<WordDTO> wordDTOs = objectMapper.readValue(resourceAsStream, new TypeReference<List<WordDTO>>() {
		});

		for (WordDTO wordDTO : wordDTOs) {
			Category category = categoryRepository
					.findByCategoryName(CategoryName.valueOf(wordDTO.getCategory().toUpperCase())).orElseGet(() -> {
						Category newCategory = new Category();
						CategoryName categoryName = CategoryName.valueOf(wordDTO.getCategory().toUpperCase());
						newCategory.setCategoryName(categoryName);
						return categoryRepository.save(newCategory);
					});

			Word word = new Word();

			word.setName(wordDTO.getWord());
			word.setCategory(category);
			wordRepository.save(word);

		}
	}

	public String wordWithSpaces(String word) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < word.length(); i++) {
			sb.append(word.charAt(i)).append(' ');
		}
		return sb.toString().trim();
	}

	public char[] wordToReturn(String word) {
		char[] currentState = new char[word.length()];
		currentState[0] = word.charAt(0);
		currentState[word.length() - 1] = word.charAt(word.length() - 1);

		for (int i = 1; i < word.length() - 1; i++) {
			if (word.charAt(i) == currentState[0]) {
				currentState[i] = word.charAt(i);
				continue;
			} else if (word.charAt(i) == currentState[word.length() - 1]) {
				currentState[i] = word.charAt(i);
				continue;
			} else {
				currentState[i] = '_';
			}

		}

		return currentState;
	}

	public Word getRandomGame() {
		List<Word> wordList = wordRepository.findAll();
		return wordList.get(RANDOM.nextInt(wordList.size()));

	}

	public String putLetterOnPlace(Game game, char guess) {
		StringBuilder sb = new StringBuilder();
		String currentstateWithoutSpaces = new String(game.getCurrentState());
		currentstateWithoutSpaces = currentstateWithoutSpaces.replaceAll(" ", "");

		for (int i = 0; i < game.getWord().getName().length(); i++) {
			if (currentstateWithoutSpaces.charAt(i) >= Commands.SMALL_A
					&& currentstateWithoutSpaces.charAt(i) <= Commands.SMALL_Z) {
				sb.append(currentstateWithoutSpaces.charAt(i));
			} else if (game.getWord().getName().charAt(i) == guess) {
				sb.append(game.getWord().getName().charAt(i));
			} else {
				sb.append('_');
			}
		}

		String currentWordWithSpaces = wordWithSpaces(sb.toString().trim());
		return currentWordWithSpaces;

	}

	public Word createWord(String wordToSet, Category category) {
		Word word = null;
		if (!contains(wordToSet)) {
			word = new Word();
			word.setName(wordToSet);
			word.setCategory(category);
			String wordWithoutSpaces = new String(wordToReturn(wordToSet));
			String wordToReturn = wordWithSpaces(wordWithoutSpaces);
			wordRepository.save(word);
		}else {
			word = getWordByName(wordToSet);
		}

		return word;
	}

	private boolean contains(String wordToSet) {
		return wordRepository.findByName(wordToSet).isPresent();
	}

	private Word getWordByName(String wordToSet) {
		return wordRepository.findByName(wordToSet).orElseThrow(()->new IllegalArgumentException(ErrorMessages.WORD_NOT_EXISTING));
	}

	public boolean contains(Word wordToFind, char guess) {
		String word = wordToFind.getName();
		for (int i = 0; i < word.length(); i++) {
			if (word.charAt(i) == guess) {
				return true;
			}
		}
		return false;
	}

}
