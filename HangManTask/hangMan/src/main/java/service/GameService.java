package service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.servlet.http.HttpSession;
import model.Word;
import repository.WordsRepository;

public class GameService {
	private static final int SMALL_A = 97;
	private static GameService gameService;
	private WordsRepository wordsRepository;
	private Word word;

	private GameService() {
		wordsRepository = WordsRepository.getWordRepository();
		word = WordsRepository.getRandomWord();
	}

	public static GameService getGameService() {
		if (gameService == null) {
			gameService = new GameService();
		}
		return gameService;
	}

	public String wordWithSpaces(Word wordWithoutSpaces) {
		StringBuilder sb = new StringBuilder();
		String word = wordWithoutSpaces.getName();
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

	public boolean contains(Word wordToFind, char guess) {
		String word = wordToFind.getName();
		for (int i = 0; i < word.length(); i++) {
			if (word.charAt(i) == guess) {
				return true;
			}

		}
		return false;
	}

	public String putLetterOnPlace(Word wordToFind, char guess, char[] currentState) {
		StringBuilder sb = new StringBuilder();
		String currentstateWithoutSpaces = new String(currentState);
		currentstateWithoutSpaces = currentstateWithoutSpaces.replaceAll(" ", "");

		for (int i = 0; i < wordToFind.getName().length(); i++) {
			if (currentstateWithoutSpaces.charAt(i) >= SMALL_A && currentstateWithoutSpaces.charAt(i) <= 122) {
				sb.append(currentstateWithoutSpaces.charAt(i));
			} else if (wordToFind.getName().charAt(i) == guess) {
				sb.append(wordToFind.getName().charAt(i));
			} else {
				sb.append('_');
			}
			//System.out.println(sb.toString());
		}

		String currentWordWithSpaces = wordWithSpaces(sb.toString().trim());
		return currentWordWithSpaces;
	}

	public boolean checkFailedTries(int triesLeft) {
		return triesLeft == 0;
	}

	public boolean isWordGuessed(Word wordToFind, String currentState) {
		return currentState.equals(wordWithSpaces(wordToFind));
	}

	public char getFirstLetter(String wordWithoutSpaces) {
		return wordWithoutSpaces.charAt(0);
	}

	public char getLastLetter(String wordWithoutSpaces) {
		return wordWithoutSpaces.charAt(wordWithoutSpaces.length() - 1);
	}

	public void restartGame(HttpSession session, Set<Character> usedCharacters) {
		word = WordsRepository.getRandomWord();
		usedCharacters.removeAll(usedCharacters);
		String currentState = gameService.wordWithSpaces(new String(gameService.wordToReturn(word.getName())));
		System.out.println("New word after restart: " + word.getName());
		System.out.println("Restart method called and clean the used: " + usedCharacters);
		session.setAttribute("word", word.getName());
		session.setAttribute("currentState", currentState);
		session.setAttribute("triesLeft", 6);
		session.setAttribute("gameStatus", "");
		session.setAttribute("isFinished", false);
		session.setAttribute("usedCharacters", usedCharacters);
	}

	public String wordWithSpaces(String word) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < word.length(); i++) {
			sb.append(word.charAt(i)).append(' ');
		}
		return sb.toString().trim();
	}
}
