package service;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import enums.Category;
import enums.Commands;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpSession;
import model.Game;
import model.History;
import repository.WordsRepository;

@Service
public class GameService {
	private WordsRepository wordsRepository;
	private Game game;

	@Autowired
	public GameService(WordsRepository wordsRepository) {
		this.wordsRepository = wordsRepository;
	}

	public String wordWithSpaces(Game wordWithoutSpaces) {
		StringBuilder sb = new StringBuilder();
		String word = wordWithoutSpaces.getWord();
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

	public boolean contains(Game wordToFind, char guess) {
		String word = wordToFind.getWord();
		for (int i = 0; i < word.length(); i++) {
			if (word.charAt(i) == guess) {
				return true;
			}

		}
		return false;
	}

	public String putLetterOnPlace(Game wordToFind, char guess, char[] currentState) {
		StringBuilder sb = new StringBuilder();
		String currentstateWithoutSpaces = new String(currentState);
		currentstateWithoutSpaces = currentstateWithoutSpaces.replaceAll(" ", "");

		for (int i = 0; i < wordToFind.getWord().length(); i++) {
			if (currentstateWithoutSpaces.charAt(i) >= Commands.SMALL_A
					&& currentstateWithoutSpaces.charAt(i) <= Commands.SMALL_Z) {
				sb.append(currentstateWithoutSpaces.charAt(i));
			} else if (wordToFind.getWord().charAt(i) == guess) {
				sb.append(wordToFind.getWord().charAt(i));
			} else {
				sb.append('_');
			}
		}

		String currentWordWithSpaces = wordWithSpaces(sb.toString().trim());
		return currentWordWithSpaces;
	}

	public boolean checkFailedTries(int triesLeft) {
		return triesLeft == 0;
	}

	public boolean isWordGuessed(Game wordToFind, String currentState) {
		return currentState.equals(wordWithSpaces(wordToFind));
	}

	public char getFirstLetter(String wordWithoutSpaces) {
		return wordWithoutSpaces.charAt(0);
	}

	public char getLastLetter(String wordWithoutSpaces) {
		return wordWithoutSpaces.charAt(wordWithoutSpaces.length() - 1);
	}

	public void restartGame(HttpSession session, Set<Character> usedCharacters) {
		game = wordsRepository.getRandomGame();
		usedCharacters.removeAll(usedCharacters);
		String currentState = wordWithSpaces(new String(wordToReturn(game.getWord())));
		if (wordsRepository.getHistory().containsKey(game)) {
			game = wordsRepository.getRandomGame();
		}
		session.setAttribute("word", game.getWord());
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

	public boolean containsOtherLetters(String wordToGuess) {
		String copy = String.valueOf(wordToGuess);
		copy = copy.replaceAll(Character.toString(getFirstLetter(wordToGuess)), "");
		copy = copy.replaceAll(Character.toString(getLastLetter(wordToGuess)), "");
		if (copy.isEmpty()) {
			return false;
		}
		return true;
	}
	
	public Map<Game,History> getHistory(){
		return wordsRepository.getHistory();
	}

	public boolean isWordValid(String wordToGuess) {
		if (wordToGuess.length() < Commands.MIN_LENGHT || !containsOnlyLetters(wordToGuess)) {
			return false;
		}
		if (containsOtherLetters(wordToGuess)) {
			return true;
		}
		return false;
	}

	public boolean containsOnlyLetters(String wordToGuess) {
		for (char symbol : wordToGuess.toCharArray()) {
			if (Character.isLetter(symbol)) {
				continue;
			} else {
				return false;
			}
		}
		return true;
	}

	public boolean historyContainsWord(Map<Game, History> history, String wordToGuess) {
		for (Map.Entry<Game, History> e : history.entrySet()) {
			if (e.getKey().getWord().equals(wordToGuess)) {
				return true;
			}
		}
		return false;
	}

	public String resumeGame(String wordToSet, HttpSession session, Map<Game, History> history)
			throws ServletException, IOException {
		Game selectedgame = null;

		for (Game g : history.keySet()) {

			if (g.getWord().equals(wordToSet)) {
				selectedgame = g;
				break;
			}
		}
		History gamesHistory = history.get(selectedgame);
		session.setAttribute("word", selectedgame);
		session.setAttribute("currentState", gamesHistory.getWordState());
		session.setAttribute("triesLeft", gamesHistory.getTriesLeft());
		session.setAttribute("isFinished", gamesHistory.isFinished());
		session.setAttribute("usedCharacters", gamesHistory.getUsedChars());
		session.setAttribute("wordCategory", gamesHistory.getCategory());
		session.setAttribute("mode", gamesHistory.getMode());
		session.setAttribute("gameStatus", "");

		if (gamesHistory.getMode().equals("Single Player")) {
			return "gameStarted";

		} else {
			return "multiplayerStartedView";

		}

	}

	public String newGameStarted(HttpSession session, Map<Game, History> history) throws ServletException, IOException {
		List<Game> gameslist = wordsRepository.getGameslist();
		Game game = wordsRepository.getRandomGame();
		if (history.containsKey(game)) {
			game = wordsRepository.getRandomGame();
		}
		Category wordCategory = game.getCategory();
		String wordWithoutSpaces = new String(wordToReturn(game.getWord()));
		String wordToReturn = wordWithSpaces(wordWithoutSpaces);
		char firstLetter = getFirstLetter(wordWithoutSpaces);
		char lastLetter = getLastLetter(wordWithoutSpaces);
		Set<Character> usedCharacters = new HashSet<Character>();
		usedCharacters.add(firstLetter);
		usedCharacters.add(lastLetter);
		int triesLeft = 6;
		String mode = "Single Player";

		session.setAttribute("word", game);
		session.setAttribute("wordCategory", wordCategory);
		session.setAttribute("triesLeft", triesLeft);
		session.setAttribute("currentState", wordToReturn);
		session.setAttribute("isFinished", false);
		session.setAttribute("usedCharacters", usedCharacters);
		session.setAttribute("mode", mode);
		session.setAttribute("gameStatus", "");

		return "gameStarted";
	}

	public String tryGuess(char guess, HttpSession session, Map<Game, History> history) throws IOException {
		Game wordToFind = (Game) session.getAttribute("word");
		int triesLeft = (int) session.getAttribute("triesLeft");
		String currentStateAsStr = (String) session.getAttribute("currentState");
		boolean isFinished = (boolean) session.getAttribute("isFinished");
		Category wordCategory = (Category) session.getAttribute("wordCategory");
		Set<Character> usedCharacters = (Set<Character>) session.getAttribute("usedCharacters");
		String mode = (String) session.getAttribute("mode");

		char[] currentState = currentStateAsStr.toCharArray();
		usedCharacters.add(guess);

		if (contains(wordToFind, guess)) {
			String wordToReturn = putLetterOnPlace(wordToFind, guess, currentState);
			history.put(wordToFind,
					new History(wordToReturn, triesLeft, wordCategory, usedCharacters, isFinished, mode));

			session.setAttribute("currentState", wordToReturn);

			if (isWordGuessed(wordToFind, wordToReturn)) {

				history.get(wordToFind).setFinished(true);
				session.setAttribute("isFinished", true);
				session.setAttribute("gameStatus", Commands.CONGRATULATIONS_YOU_WON);

			}
			return "gameStarted";
		} else {
			triesLeft--;
			history.put(wordToFind,
					new History(currentStateAsStr, triesLeft, wordCategory, usedCharacters, isFinished, mode));

			session.setAttribute("triesLeft", triesLeft);
			if (checkFailedTries(triesLeft)) {
				history.get(wordToFind).setFinished(true);
				session.setAttribute("isFinished", true);
				session.setAttribute("gameStatus", Commands.GAME_STATUS_LOSS + wordToFind.getWord() + ".");

			}
			return "gameStarted";
		}
	}

	public String prepareWordToBeDisplayed(HttpSession session, String wordToGuess, Category category)
			throws ServletException, IOException {
		game = new Game();
		game = game.createNewGame(wordToGuess, category);
		game.setSessionId(session.getId());
		
		String wordWithoutSpaces = new String(wordToReturn(wordToGuess));
		String wordToReturn = wordWithSpaces(wordWithoutSpaces);
		char firstLetter = getFirstLetter(wordWithoutSpaces);
		char lastLetter = getLastLetter(wordWithoutSpaces);
		Set<Character> usedCharacters = new HashSet<Character>();
		usedCharacters.add(firstLetter);
		usedCharacters.add(lastLetter);
		int triesLeft = 6;
		String mode = "Multiplayer";

		session.setAttribute("word", game);
		session.setAttribute("category", category);
		session.setAttribute("triesLeft", triesLeft);
		session.setAttribute("currentState", wordToReturn);
		session.setAttribute("isFinished", false);
		session.setAttribute("usedCharacters", usedCharacters);
		session.setAttribute("gameStatus", "");
		session.setAttribute("mode", mode);
		session.setAttribute("isWordValid", true);


		return "multiplayerStarted";
	}

	public String tryGuessMultiplayer(char guess, HttpSession session) throws IOException {
		Set<Character> usedCharacters = (Set<Character>) session.getAttribute("usedCharacters");
		Game wordToFind = (Game) session.getAttribute("word");
		Category category = (Category) session.getAttribute("category");
		int triesLeft = (int) session.getAttribute("triesLeft");
		String currentStateAsStr = (String) session.getAttribute("currentState");
		boolean isFinished = (boolean) session.getAttribute("isFinished");
		String mode = (String) session.getAttribute("mode");

		char[] currentState = currentStateAsStr.toCharArray();
		usedCharacters.add(guess);

		if (contains(wordToFind, guess)) {
			String wordToReturn = putLetterOnPlace(wordToFind, guess, currentState);
			wordsRepository.getHistory().put(wordToFind,
					new History(wordToReturn, triesLeft, category, usedCharacters, isFinished, mode));

			session.setAttribute("currentState", wordToReturn);

			if (isWordGuessed(wordToFind, wordToReturn)) {

				wordsRepository.getHistory().get(wordToFind).setFinished(true);
				session.setAttribute("isFinished", true);
				session.setAttribute("gameStatus", Commands.CONGRATULATIONS_YOU_WON);

			}
			return "redirect:/multiplayerStarted";
		} else {
			triesLeft--;
			wordsRepository.getHistory().put(wordToFind,
					new History(currentStateAsStr, triesLeft, category, usedCharacters, isFinished, mode));

			session.setAttribute("triesLeft", triesLeft);
			if (checkFailedTries(triesLeft)) {
				wordsRepository.getHistory().get(wordToFind).setFinished(true);
				session.setAttribute("isFinished", true);
				session.setAttribute("gameStatus", Commands.GAME_STATUS_LOSS + wordToFind.getWord() + ".");

			}
			return "redirect:/multiplayerStarted";
		}
	}
	
	public static String generateRandomString(int length) {
		Random random = new Random();
        StringBuilder result = new StringBuilder(length);
        
        for (int i = 0; i < length; i++) {
            result.append(Commands.CHARACTERS.charAt(random.nextInt(Commands.CHARACTERS.length())));
        }
        
        return result.toString();
    }

}
